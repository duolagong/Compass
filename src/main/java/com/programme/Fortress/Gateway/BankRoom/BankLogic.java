package com.programme.Fortress.Gateway.BankRoom;

import Protobuf.BankMessagePOJO;
import Protobuf.Util.LoadUtil;
import com.programme.Fortress.Staircase.Server.SystemLogic;
import org.springframework.stereotype.Component;

@Component
public class BankLogic implements SystemLogic {

    /**
     * 层级业务分类
     * @param myMessage
     * @return
     */
    @Override
    public Object DistinguishType(BankMessagePOJO.MyMessage myMessage) {
        BankMessagePOJO.BankMessage bankMessage = myMessage.getBankMessage();
        int bankNum=bankMessage.getBankId();
        BankMessagePOJO.MyMessage responseMsg = null;
        if(bankNum==102){
//            responseMsg = new Bank301ServerImpl().SendMessage(bankMessage.getBankXml(), 20,null);
        }else if(bankNum==103){
//            responseMsg = new Bank301ServerImpl().SendMessage(bankMessage.getBankXml(), 20,null);
        }else if(bankNum==104){
//            responseMsg = new Bank301ServerImpl().SendMessage(bankMessage.getBankXml(), 20,null);
        }else if(bankNum==105){
//            responseMsg = new Bank301ServerImpl().SendMessage(bankMessage.getBankXml(), 20,null);
        }else if(bankNum==301){
//            responseMsg = new Bank301ServerImpl().SendMessage(bankMessage.getBankXml(), 20,null);
        }else {
            try {
                System.out.println("等待五秒"+Thread.currentThread().getName());
                System.out.println("线程对应："+bankMessage.getBankXml()+"-------->"+Thread.currentThread().getName());
                responseMsg =
                        new LoadUtil().LoadResponseMessage(bankMessage.getMessageId(), "M0001", bankMessage.getBankXml());
                Thread.sleep(5*1000);
            }catch (Exception e){}
        }
        return responseMsg;
    }
}
