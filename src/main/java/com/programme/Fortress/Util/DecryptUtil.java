package com.programme.Fortress.Util;

import cn.com.infosec.netsign.agent.NetSignAgent;
import cn.com.infosec.netsign.agent.NetSignResult;
import cn.com.infosec.netsign.agent.exception.NetSignAgentException;
import cn.com.infosec.netsign.agent.exception.ServerProcessException;
import lombok.extern.slf4j.Slf4j;

@Slf4j
public class DecryptUtil {

    public static String  signVerifyText(String signedText) throws NetSignAgentException {
        NetSignAgent.initialize() ;
        String signVerifyText="";
        try {
            NetSignResult result = NetSignAgent.attachedVerify( signedText , "" ,false );
            byte[] bytexml=result.getByteArrayResult(NetSignResult.PLAIN_TEXT);
            signVerifyText=new StringUtil().filterMessage(new String(bytexml,"UTF-8"));
            log.info("解密后数据"+signVerifyText);
            return signVerifyText;
        } catch ( NetSignAgentException e ) {
            e.printStackTrace();
            signVerifyText=e.getErrorCode()+e.getMessage();
            log.error(e.getErrorCode()+e.getMessage());
        } catch ( ServerProcessException e ) {
            e.printStackTrace();
            signVerifyText=e.getErrorCode()+e.getMessage();
            log.error(e.getErrorCode()+e.getMessage());
        } catch (Exception e) {
            e.printStackTrace();
            signVerifyText=e.getMessage();
        }
        return signVerifyText;
    }

}
