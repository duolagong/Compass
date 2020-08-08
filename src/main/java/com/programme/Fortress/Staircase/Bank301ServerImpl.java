package com.programme.Fortress.Staircase;

import Interface.Bank.BankServer;

public class Bank301ServerImpl {

    public static String ContentType="application/x-www-form-urlencoded; charset=GBK";

    public String SendMessage(String message, int overTime, Object... objects) {
        String url="http://10.10.10.43:8899";
        String returnMsg = "";
        java.net.HttpURLConnection urlConnection = null;
        int setOverTime=overTime*1000;
        try {
            java.net.URL aURL = new java.net.URL(url);
            urlConnection = (java.net.HttpURLConnection) aURL.openConnection();
            urlConnection.setRequestMethod("POST");
            urlConnection.setDoInput(true);
            urlConnection.setDoOutput(true);
            urlConnection.setUseCaches(false);
            urlConnection.setConnectTimeout(setOverTime);
            urlConnection.setReadTimeout(setOverTime);
            urlConnection.setRequestProperty("Content-Type", ContentType);
            urlConnection.connect();
            if (message != null && message.trim().length() != 0) {
                if (urlConnection.getOutputStream() == null) {
                    throw new Exception("HTTP无法打开!");		}
                urlConnection.getOutputStream().write(message.getBytes());
            }
            int resCode = urlConnection.getResponseCode();
            int contentLen = urlConnection.getContentLength();
            java.io.DataInputStream in = new java.io.DataInputStream( urlConnection.getInputStream());
            byte buffer[] = new byte[contentLen];

            int len = 0;

            while (len < contentLen) {
                int remainedLen = contentLen - len;
                if (remainedLen > 1024)
                    remainedLen = 1024;
                int readLen = in.read(buffer, len, remainedLen);
                if (readLen == -1 || readLen == 0) {
                    break;
                }
                len = len + readLen;
            }
            urlConnection.disconnect();
            urlConnection = null;
            returnMsg = new String(buffer, 0, contentLen);
        } catch (Exception e) {
            e.printStackTrace();
            returnMsg= "P0001与银行前置机连接异常";
        } finally {
            if (urlConnection != null) {
                try {
                    urlConnection.disconnect();
                    urlConnection = null;
                } catch (Exception ee) {
                    throw ee;
                }
            }
        }
        return returnMsg;
    }
}
