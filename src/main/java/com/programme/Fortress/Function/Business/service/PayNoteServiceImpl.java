package com.programme.Fortress.Function.Business.service;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.programme.Fortress.Function.Business.dao.BankIdMapper;
import com.programme.Fortress.Function.Business.dao.CityMapper;
import com.programme.Fortress.Function.Business.dao.PayNoteMapper;
import com.programme.Fortress.Function.Business.entity.BankId;
import com.programme.Fortress.Function.Business.entity.City;
import com.programme.Fortress.Function.Business.entity.PayNote;
import com.programme.Fortress.Util.MyDateUtil;
import com.programme.Fortress.Util.Redis.RedisUtil;
import com.programme.Fortress.Util.StringUtil;
import lombok.extern.slf4j.Slf4j;
import org.dom4j.Document;
import org.dom4j.DocumentHelper;
import org.dom4j.Node;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Slf4j
@Service
public class PayNoteServiceImpl implements PayNoteService {

    @Autowired
    private PayNoteMapper payNoteMapper;
    @Autowired
    private CityMapper cityMapper;
    @Autowired
    private BankIdMapper bankIdMapper;
    @Autowired
    private RedisUtil redisUtil;

    @Override
    public PayNote getOrdernum(String ordernum){
        return payNoteMapper.selectById(ordernum);
    }

    /**
     * 获取指定时间的付款笔数
     * @return
     */
    @Override
    public Integer getPayNoteCount(String queryDate){
        return payNoteMapper.getPayNoteCount(queryDate);
    }

    /**
     * 增加支付单
     * @param tranxml
     * @param ordernum
     * @return
     */
    @Override
    public JSON insertPayNote(String tranxml, String ordernum){
        JSONObject json = new JSONObject();
        try {
            PayNote payNote = getOrdernumPayNote(tranxml,ordernum);
            Integer count = payNoteMapper.selectCount(new QueryWrapper<PayNote>().eq("ordernum", payNote.getOrderNum()));
            if (count==0) payNoteMapper.insert(payNote);
            json.put("textStatus", "M0001");
            json.put("errorThrown", "新增成功");
        }catch (Exception e){
            json.put("textStatus", "E9999");
            json.put("errorThrown", e.getMessage());
            log.error("添加支付单失败,Ordernum:"+ordernum,e);
        }finally {
            return json;
        }
    }

    /**
     * 增加支付单
     * @param payNote
     */
    @Override
    @Async(value="myAsyncPoolTaskExecutor")
    public void insertPayNote(PayNote payNote){
        try {
            Integer count = payNoteMapper.selectCount(new QueryWrapper<PayNote>().eq("ordernum", payNote.getOrderNum()));
            if (count>0) return;
            payNoteMapper.insert(payNote);
            String recCityName = payNote.getRecCityName();
            List<City> cityList = cityMapper.selectList(new QueryWrapper<City>().like("name", recCityName)
                    .notLike("name","省")
                    .notLike("name","自治州")
                    .notLike("name","自治区")
                    .notLike("name","盟"));
            //对于查询不到的安装'局部地区'处理
            String code = !StringUtils.isEmpty(cityList) ? cityList.get(0).getCode() : "000000";
            String cityName="局部地区";
            if(!"000000".equals(code)){
                List<City> regionList = cityMapper.selectList(new QueryWrapper<City>().eq("code", code.substring(0, 4) + "00"));
                cityName = regionList.get(0).getName();
                //更新地址坐标(CityPoint)
                /*redisUtil.hset("CityPoint", cityName, regionList.get(0).getPoint());*/
            }
//            Object payResult = redisUtil.hget("COM:PNUM:" + );
            //添加今日的城市交易量(CityPayNum)
            if (StringUtils.isEmpty(redisUtil.zItemGet("COM:CITY:PNUM", cityName))) {
                redisUtil.zSet("COM:CITY:PNUM", cityName, 1);
            } else {
                redisUtil.zAdd("COM:CITY:PNUM", cityName, 1);
                long timeInterval = MyDateUtil.getTimeInterval(MyDateUtil.getMaxDate("yyyyMMdd")+ " " + "23:59:59","yyyyMMdd hh:mm:ss");
                redisUtil.expire("COM:CITY:PNUM",timeInterval);
            }
            //添加今日的汇款金额(CityPayAmount)
            BigDecimal bigDecimal = new BigDecimal(payNote.getTxAmount());
            if (StringUtils.isEmpty(redisUtil.zItemGet("COM:CITY:PAMOUNT", cityName))) {
                redisUtil.zSet("COM:CITY:PAMOUNT", cityName, bigDecimal.doubleValue());
            } else {
                redisUtil.zAdd("COM:CITY:PAMOUNT", cityName, bigDecimal.doubleValue());
                long timeInterval = MyDateUtil.getTimeInterval(MyDateUtil.getMaxDate("yyyyMMdd")+ " " + "23:59:59","yyyyMMdd hh:mm:ss");
                redisUtil.expire("COM:CITY:PAMOUNT",timeInterval);
            }
            //添加Ordernum到(City_地区码)
            redisUtil.lSet("COM:CITY:" + code, payNote.getOrderNum(),true);
            long timeInterval = MyDateUtil.getTimeInterval(MyDateUtil.getMaxDate("yyyyMMdd")+ " " + "23:59:59","yyyyMMdd hh:mm:ss");
            redisUtil.expire("COM:CITY:" + code,timeInterval);
        }catch (Exception e){
            log.error("添加支付单失败,Ordernum:"+payNote.getOrderNum(),e);
        }
    }

    /**
     * 解析付款报文
     * @param tranxml
     * @param ordernum
     * @return
     */
    @Override
    public PayNote getOrdernumPayNote(String tranxml,String ordernum) {
        PayNote payNote = new PayNote();
        try {
            Document document = DocumentHelper.parseText(tranxml);
            //获取报文头里的内容
            String version = document.selectSingleNode("//Root/Head/Version").getText();
            payNote.setVersion(version);
            String txCode = document.selectSingleNode("//Root/Head/TxCode").getText();
            payNote.setTxCode(txCode);
            payNote.setSubBranchId(document.selectSingleNode("//Root/Head/SubBranchId").getText());
            payNote.setTxSerial(document.selectSingleNode("//Root/Head/TxSerial").getText());
            payNote.setTxMoment(document.selectSingleNode("//Root/Head/TxMoment").getText());
            payNote.setAgentId(document.selectSingleNode("//Root/Head/AgentId").getText());
            //获取报文体里的内容
            payNote.setOrderNum(document.selectSingleNode("//Root/Body/Record/OrderNum").getText());
            payNote.setEspDate(document.selectSingleNode("//Root/Body/Record/EspDate").getText());
            payNote.setCurCode(document.selectSingleNode("//Root/Body/Record/CurCode").getText());
            payNote.setHurryFlag(document.selectSingleNode("//Root/Body/Record/HurryFlag").getText());
            payNote.setPersonFlag(document.selectSingleNode("//Root/Body/Record/PersonFlag").getText());
            payNote.setTxAmount(document.selectSingleNode("//Root/Body/Record/TxAmount").getText());
            payNote.setOutBranchId(document.selectSingleNode("//Root/Body/Record/OutBranchId").getText());
            payNote.setOutBranchName(document.selectSingleNode("//Root/Body/Record/OutBranchName").getText());
            payNote.setOutAcctId(document.selectSingleNode("//Root/Body/Record/OutAcctId").getText());
            payNote.setOutAcctName(document.selectSingleNode("//Root/Body/Record/OutAcctName").getText());
            payNote.setInBranchId(document.selectSingleNode("//Root/Body/Record/InBranchId").getText());
            payNote.setInBranchName(document.selectSingleNode("//Root/Body/Record/InBranchName").getText());
            payNote.setInAcctId(document.selectSingleNode("//Root/Body/Record/InAcctId").getText());
            payNote.setInAcctName(document.selectSingleNode("//Root/Body/Record/InAcctName").getText());
            payNote.setPurpose(document.selectSingleNode("//Root/Body/Record/Purpose").getText());
            //判断项
            Node skipFlagNode = document.selectSingleNode("//Root/Body/Record/SkipFlag");
            if (skipFlagNode != null) payNote.setSkipFlag(skipFlagNode.getText());
            Node areaFlagNode = document.selectSingleNode("//Root/Body/Record/AreaFlag");
            if (areaFlagNode != null) payNote.setAreaFlag(areaFlagNode.getText());
            Node absNode = document.selectSingleNode("//Root/Body/Record/AbstractStr");
            if (absNode != null) payNote.setAbstractStr(absNode.getText());
            Node memoNode=document.selectSingleNode("//Root/Body/Record/Memo");
            if (memoNode != null) payNote.setMemo(memoNode.getText());
            Node recCityNode=document.selectSingleNode("//Root/Body/Record/RecCityName");
            if (recCityNode != null) payNote.setRecCityName(recCityNode.getText());
            //资管项内容
            Node reasonNoUsedNode=document.selectSingleNode("//Root/Body/Record/ReasonNoUsed");
            if (reasonNoUsedNode != null) payNote.setReasonNoUsed(reasonNoUsedNode.getText());
            Node budgetNumNode=document.selectSingleNode("//Root/Body/Record/BudgetNum");
            if (budgetNumNode != null) payNote.setBudgetNum(budgetNumNode.getText());
            Node contractNumNode=document.selectSingleNode("//Root/Body/Record/ContractNum");
            if (contractNumNode != null) payNote.setContractNum(contractNumNode.getText());
            //核心OSB内容  BankAccountType
            Node bankAccountTypeNode=document.selectSingleNode("//Root/Body/Record/BankAccountType");
            if (bankAccountTypeNode != null) payNote.setBankaccounttype(bankAccountTypeNode.getText());
        } catch (Exception e) {
            log.error("拆分"+ordernum+"交易报文时发生错误",e);
        }
        return payNote;
    }

    @Override
    public List<Map<String,String>> getPersonflag(String nowDate){
        List<Map<String,String>> personflagList = payNoteMapper.getPersonflag(nowDate);
        return personflagList;
    }

    /**
     * 获取提交日期的周交易信息
     * @param payDate
     * @return
     */
    @Override
    public HashMap<String, Object> getWeekTrad(String payDate) {
        HashMap<String, Object> hashMap = new HashMap<>();
        try {
            ArrayList<String> dateList = new ArrayList<String>(){{
                add(MyDateUtil.getHistoryDate(payDate, "yyyy-MM-dd", -6));
                add(MyDateUtil.getHistoryDate(payDate, "yyyy-MM-dd", -5));
                add(MyDateUtil.getHistoryDate(payDate, "yyyy-MM-dd", -4));
                add(MyDateUtil.getHistoryDate(payDate, "yyyy-MM-dd", -3));
                add(MyDateUtil.getHistoryDate(payDate, "yyyy-MM-dd", -2));
                add(MyDateUtil.getHistoryDate(payDate, "yyyy-MM-dd", -1));
                add(payDate);
            }};
            List<BankId> bankIds = bankIdMapper.selectList(null);
            String[] bankStrings = new String[bankIds.size()];
            for (int i = 0; i < bankIds.size(); i++) {
                String agentid=bankIds.get(i).getId();
                List<HashMap<String, String>> agentidPayList = payNoteMapper.getDayTrad(dateList.get(0), dateList.get(6),
                        "Z01".equals(agentid) ? new ArrayList<String>() {{ add(agentid); add("W01"); add("907");}} : new ArrayList<String>() {{ add(agentid);}});
                if(agentidPayList.size()==0) continue;
                bankStrings[i] = bankIds.get(i).getBank();
                int[] ints = new int[7];
                int counter=0;
                for (int x = 0; x < dateList.size(); x++) {
                    if(agentidPayList.size()<=counter) continue;
                    if(dateList.get(x).equals(agentidPayList.get(counter).get("PAYDATE"))){
                        ints[x]=Integer.parseInt(String.valueOf(agentidPayList.get(counter).get("PAYNUM")));
                        counter++;
                    }else {
                        ints[x]=0;
                    }
                }
                hashMap.put(bankIds.get(i).getBank(),ints);
            }
            hashMap.put("legend",bankStrings);
        }catch (Exception e){
            log.error("获取周交易信息异常",e);
        }
        return hashMap;
    }
}
