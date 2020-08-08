package com.programme.Fortress.Function.Business.service;

import com.alibaba.fastjson.JSONObject;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.programme.Fortress.Function.Business.dao.PayInforMapper;
import com.programme.Fortress.Function.Business.dao.PayInterposeMapper;
import com.programme.Fortress.Function.Business.entity.PayInfor;
import com.programme.Fortress.Function.Business.entity.PayInterpose;
import com.programme.Fortress.Function.Business.entity.ReturnData;
import com.programme.Fortress.Function.Business.service.PayInterpose.PayInterposeContext;
import com.programme.Fortress.Function.ToolCase.dao.SysMessageMapper;
import com.programme.Fortress.Function.ToolCase.entity.ResultBean;
import com.programme.Fortress.Function.ToolCase.service.FormatConService;
import com.programme.Fortress.Other.Exception.ResultUtil;
import com.programme.Fortress.Other.Feign.ToNBKOSB;
import lombok.extern.slf4j.Slf4j;
import org.dom4j.DocumentException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

@Slf4j
@Service
public class PayInterposeServiceImpl implements PayInterposeService {

    @Autowired
    private PayInterposeMapper payInterposeMapper;
    @Autowired
    private PayInforMapper payInforMapper;
    @Autowired
    private SysMessageMapper sysMessageMapper;
    @Resource
    private PayInterposeContext payInterposeContext;
    @Autowired
    private FormatConService formatConService;
    @Autowired
    private ToNBKOSB nbkosb;

    @Override
    public ReturnData<PayInterpose> getPayInterpose(PayInfor payInfor){
        ReturnData<PayInterpose> returnData=new ReturnData<PayInterpose>();
        try {
            if (!StringUtils.isEmpty(payInfor.getAgentids())){
                List<String> agentList = new ArrayList<>(Arrays.asList(payInfor.getAgentids()));
                if(agentList.contains("Z01")){
                    agentList.add("W01");
                    agentList.add("907");
                    String[] strings = agentList.stream().toArray(String[]::new);
                    log.info("优化Agentids查询条件对核心的处理[{}]",agentList.toString());
                    payInfor.setAgentids(strings);
                }
            }
            int tatlo=payInterposeMapper.getPayInforCount(payInfor);
            if(tatlo==0) return returnData;
            returnData.setTotal(tatlo);
            List<PayInterpose> payInforList = payInterposeMapper.getPayInfor(payInfor);
            returnData.setRows(payInforList);
        }catch (Exception e){
            log.error("查询付款交易干预数据异常",e);
        }
        return returnData;
    }

    @Override
    public ResultBean addPayInterpose(PayInfor payInfor){
        JSONObject json = new JSONObject();
        String ordernum = payInfor.getOrdernum();
        Integer count = payInterposeMapper.selectCount(new QueryWrapper<PayInterpose>().eq("ordernum", ordernum));
        if(count==0){
           try {
               PayInterpose payInterpose = new PayInterpose();
               payInterpose.setOrdernum(ordernum);
               payInterpose.setVersion(payInfor.getVersion());
               payInterpose.setTxcode(payInfor.getTxcode());
               payInterpose.setAgentid(payInfor.getAgentid());
               payInterpose.setSubbranchid(payInfor.getSubbranchid());
               payInterpose.setTxamount(payInfor.getTxamount());
               payInterpose.setSubmitId(123);
               payInterpose.setSubmitName("某某");
               int insert = payInterposeMapper.insert(payInterpose);
               return ResultUtil.success(null,"维护成功");
           }catch (Exception e){
               log.error("添加交易干预信息报错",e);
               return ResultUtil.fail(e.getMessage());
           }
        }else {
            return ResultUtil.fail("新增失败,该笔交易已存在!");
        }
    }

    @Override
    public ResultBean upDatePayInterpose(PayInterpose payInterpose){
        try {
            PayInterpose interpose = payInterposeMapper.selectById(payInterpose.getOrdernum());
            interpose.setBankFinalCode(payInterpose.getBankFinalCode());
            interpose.setBankFinalMsg(payInterpose.getBankFinalMsg());
            int updateById = payInterposeMapper.updateById(interpose);
            if(updateById == 0) return ResultUtil.fail("修改失敗");
            String asXML = formatConService.makeMessage1031(interpose.getVersion(), interpose.getSubbranchid(), interpose.getAgentid(),
                    interpose.getBankFinalCode(), interpose.getBankFinalMsg(), interpose.getOrdernum());
            //向OSB推送1031报文
            String resultMessage = nbkosb.sendOSBJMS(asXML);
            return ResultUtil.success("",resultMessage);
        }catch (DocumentException e){
            log.error("拼装1031报文异常",e);
            return ResultUtil.fail("拼装1031报文异常");
        } catch (Exception e) {
            log.error("在途处理发生异常",e);
            return ResultUtil.fail(e.getMessage());
        }
    }

    @Override
    public ResultBean getPayResult(String ordernum) {
        PayInfor payInfor = payInforMapper.selectById(ordernum);
        if ("1".equals(payInfor.getPayprocess())) {
            if ("2000".equals(payInfor.getTxcode()) && StringUtils.isEmpty(payInfor.getNbkcode())) {
                return payInterposeContext.confirmIntermediateState(1, payInfor);
            } else if ("Z01".equals(payInfor.getAgentid()) || "W01".equals(payInfor.getAgentid())) {
                return payInterposeContext.confirmIntermediateState(2, payInfor);
            } else {
                return ResultUtil.fail("状态停滞在核心OSB!");
            }
        } else {
            return ResultUtil.fail("交易已经处理完成，无需干预!");
        }
    }
}
