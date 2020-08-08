package com.programme.Fortress.Gateway;

import Protobuf.BankMessagePOJO;
import com.programme.Fortress.Gateway.BankRoom.BankLogic;
import com.programme.Fortress.Staircase.Server.SystemLogic;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class GatewayLogic implements SystemLogic {
    @Autowired
    BankLogic bankLogic;

    @Override
    public Object DistinguishType(BankMessagePOJO.MyMessage myMessage){
        BankMessagePOJO.MyMessage.DataType dataType = myMessage.getDataType();
        if(dataType == BankMessagePOJO.MyMessage.DataType.BankMessageType){
            return bankLogic.DistinguishType(myMessage);
        }else if(dataType == BankMessagePOJO.MyMessage.DataType.ResponseMessageType){
            System.out.println("数据错误-暂时允许测试");
            return bankLogic.DistinguishType(myMessage);
        }else {
            System.out.println("数据错误");
            return "数据错误";
        }
    }
}
