package com.programme.Fortress.Util.Remind;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.programme.Fortress.Function.ToolCase.dao.SysMessageMapper;
import com.programme.Fortress.Function.ToolCase.entity.SysMessage;
import com.programme.Fortress.Function.ToolCase.service.SysUserService;
import com.programme.Fortress.Other.Feign.ToNBKOSB;
import com.programme.Fortress.Util.Postman.HttpUtil;
import com.programme.Fortress.Util.Redis.RedisUtil;
import com.programme.Fortress.Util.StringUtil;
import lombok.extern.slf4j.Slf4j;
import org.dom4j.Document;
import org.dom4j.DocumentHelper;
import org.dom4j.Element;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;
import java.util.Map;

@Slf4j
@Component
public class SMSAlert {
    @Autowired
    private SysMessageMapper sysMessageMapper;
    @Autowired
    private SysUserService sysUserService;
    @Autowired
    private RedisUtil redisUtil;
    @Autowired
    private ToNBKOSB nbkosb;

    /**
     * 拼装短信报文(传值)
     * @param alert
     * @param phones
     * @param count
     * @return
     */
    public String AssemblySMS(String alert,String phones ,int count){
        try {
            SysMessage sysMessage = sysMessageMapper.selectOne(new QueryWrapper<SysMessage>().eq("type", "1051"));
            Document document = DocumentHelper.parseText(sysMessage.getMessage());
            Element rootElement = document.getRootElement();
            rootElement.element("Body").element("PhoneNum").setText(phones);
            rootElement.element("Body").element("PhoneMsg").setText(alert);
            rootElement.element("Body").element("PhoneCount").setText(String.valueOf(count));
            String asXML = document.asXML();
            log.info("拼装短信报文[{}]",asXML);
            return asXML;
        }catch (Exception e){
            log.error("拼装短信报文发生异常",e);
            return null;
        }
    }

    @Async(value="myAsyncPoolTaskExecutor")
    public void AssemblyAlertSMS(String alertType,String alertInfo,Boolean alert){
        try {
            Map alertInfor = sysUserService.alertPhoneInfor(alertType);
            if (StringUtil.checkEmpty(alertInfor)) {
                log.info("获取手机号异常");
                return;
            }
            String assemblySMS = AssemblySMS(alertInfo, alertInfor.get("phone").toString(), (int) alertInfor.get("num"));
            if(StringUtil.checkEmpty(assemblySMS)){
                log.error("未生成短信报文");
            }else {
                String sms = nbkosb.sendSMS(assemblySMS);
                if(alert) redisUtil.hset("COM:RADAR",alertType, LocalDateTime.now().toString());//加入提醒
            }
        }catch (Exception e){
            log.error("发送短信预警异常",e);
        }
    }
}
