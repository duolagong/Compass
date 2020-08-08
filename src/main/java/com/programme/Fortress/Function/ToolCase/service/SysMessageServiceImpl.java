package com.programme.Fortress.Function.ToolCase.service;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.alibaba.fastjson.TypeReference;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.programme.Fortress.Function.ToolCase.dao.SysMessageMapper;
import com.programme.Fortress.Function.ToolCase.entity.ResultBean;
import com.programme.Fortress.Function.ToolCase.entity.SysMessage;
import com.programme.Fortress.Other.Exception.ResultUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Map;

@Slf4j
@Service
@Transactional
public class SysMessageServiceImpl extends ServiceImpl<SysMessageMapper,SysMessage> implements SysMessageService{

    @Autowired
    private SysMessageMapper sysMessageMapper;

    @Override
    public List<SysMessage> getSysMessage(){
        List<SysMessage> sysMessages = this.list();
        return sysMessages;
    }

    @Override
    public ResultBean addSysMessage(SysMessage sysMessage){
        try {
            sysMessage.setId(sysMessageMapper.getDual());
            int byId = sysMessageMapper.insert(sysMessage);
            return ResultUtil.success(null,"新增报文模板成功");
        } catch (Exception e) {
            log.error("新增报文模板异常",e);
            return ResultUtil.fail(e.getMessage());
        }
    }

    @Override
    public ResultBean upDateSysMessage(SysMessage sysMessage){
        JSONObject json = new JSONObject();
        try {
            //updateById莫名其妙的全部不校驗null值了，先这样补值
            SysMessage sysMessageLast = sysMessageMapper.selectById(sysMessage.getId());
            sysMessage.setCreatedTime(sysMessageLast.getCreatedTime());

            int byId = sysMessageMapper.updateById(sysMessage);
            return ResultUtil.success(null,"修改报文模板成功");
        } catch (Exception e) {
            log.error("修改报文模板异常",e);
            return ResultUtil.fail(e.getMessage());
        }
    }

    @Override
    public ResultBean deleteSysMessage(long id){
        try {
            int i = sysMessageMapper.deleteById(id);
            return ResultUtil.success(null,"删除报文模板成功");
        } catch (Exception e) {
            log.error("删除报文模板异常",e);
            return ResultUtil.fail(e.getMessage());
        }
    }

}
