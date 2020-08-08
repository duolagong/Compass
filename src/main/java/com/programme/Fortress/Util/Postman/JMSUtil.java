package com.programme.Fortress.Util.Postman;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import lombok.extern.slf4j.Slf4j;

import javax.jms.*;
import javax.naming.Context;
import javax.naming.InitialContext;
import java.util.Properties;

@Slf4j
public class JMSUtil {

    public  static String SendMessage(String message, String targetUrl, String targetFactoryName, String targetName,int time) {
        JSONObject json = new JSONObject();
        boolean transacted = false;
        int acknowledgementMode = Session.AUTO_ACKNOWLEDGE;
        Properties properties = new Properties();
        properties.put(Context.INITIAL_CONTEXT_FACTORY, "weblogic.jndi.WLInitialContextFactory");
        properties.put(Context.PROVIDER_URL, targetUrl);
        try {
            Context context = new InitialContext(properties);
            Object obj = context.lookup(targetFactoryName);
            QueueConnectionFactory queueConnectionFactory = (QueueConnectionFactory) obj;
            obj = context.lookup(targetName);
            Queue queue = (Queue) obj;
             QueueConnection queueConnection = queueConnectionFactory.createQueueConnection();// 产生连接
            queueConnection.start();
            QueueSession queueSession = queueConnection.createQueueSession(transacted, acknowledgementMode);
            TextMessage textMessage = queueSession.createTextMessage();
            textMessage.clearBody();
            textMessage.setText(message);
            QueueSender queueSender = queueSession.createSender(queue);
            queueSender.setTimeToLive(time*1000);
            queueSender.send(textMessage);
            if (transacted) queueSession.commit();
            if (queueSender != null) queueSender.close();
            if (queueSession != null) queueSession.close();
            if (queueConnection != null) queueConnection.close();
            json.put("textStatus", "M0001");
            json.put("errorThrown", "维护成功");
            return json.toJSONString();
        } catch (Exception ex) {
            json.put("textStatus", "E9999");
            json.put("errorThrown", ex.getMessage());
            log.error("发送JMS消息异常",ex);
            return json.toJSONString();
        }
    }
}
