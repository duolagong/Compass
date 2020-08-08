package com.programme.Fortress.Staircase.Server;

import Protobuf.BankMessagePOJO;

public interface SystemLogic {
    /**
     * 层级业务分类
     * @param myMessage
     * @return
     */
    Object DistinguishType(BankMessagePOJO.MyMessage myMessage);
}
