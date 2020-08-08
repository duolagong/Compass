package com.programme.Fortress.Util.Eureka;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.cloud.client.ServiceInstance;
import org.springframework.cloud.client.discovery.DiscoveryClient;
import org.springframework.stereotype.Component;

import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
@Deprecated
public class EurekaUtil {

    @Value("${eureka.client.serviceUrl.defaultZone}")
    private String EurekaURL;
    @Autowired
    private DiscoveryClient discoveryClient;

    /**
     * 抓取Eureka在线服务名单（标准型）
     * @return
     */
    public HashMap getEurekList(){
        HashMap<String, String> hashMap = new HashMap<>();
        List<String> serviceNames = discoveryClient.getServices();
        for (String serviceName:serviceNames){
            List<ServiceInstance> instances = discoveryClient.getInstances(serviceName);
            for (ServiceInstance serviceInstance:instances){
                hashMap.put(serviceName,serviceInstance.getUri().toString());
                /*hashMap.add(String.format("%s:%s",serviceName,serviceInstance.getUri()));*/
            }
        }
        return hashMap;
    }

    /**
     * 抓取Eureka在线服务名单（特殊型）
     * @param overTime
     * @return
     */
    @Deprecated
    public Map<String, String> SendMessage(int overTime) {
        HashMap<String, String> hashMap = new HashMap();
        HttpURLConnection urlConnection = null;
        int setOverTime = overTime * 1000;
        try {
            URL aURL = new URL(EurekaURL+"/apps");
            urlConnection = (HttpURLConnection)aURL.openConnection();
            urlConnection.setRequestMethod("GET");
            urlConnection.setDoInput(true);
            urlConnection.setDoOutput(true);
            urlConnection.setUseCaches(false);
            urlConnection.setConnectTimeout(setOverTime);
            urlConnection.setReadTimeout(setOverTime);
            urlConnection.setRequestProperty("Content-Type", "application/json");
            urlConnection.setRequestProperty("Accept", "application/xml");
            urlConnection.connect();
            if (urlConnection.getResponseCode() == 200) {
                InputStream inputStream = urlConnection.getInputStream();
                byte[] data = new byte[1024];
                StringBuffer stringBuffer = new StringBuffer();
                boolean var9 = false;

                String returnMsg;
                int length;
                while((length = inputStream.read(data)) != -1) {
                    returnMsg = new String(data, 0, length);
                    stringBuffer.append(returnMsg);
                }

                returnMsg = stringBuffer.toString();
                inputStream.close();
                Matcher serverName = Pattern.compile("<instanceId>(.+?)</instanceId>").matcher(returnMsg);
                ArrayList list = new ArrayList();

                while(serverName.find()) {
                    list.add(serverName.group(1).trim().toLowerCase());
                }

                Matcher serverAddress = Pattern.compile("<homePageUrl>(.+?)</homePageUrl>").matcher(returnMsg);
                int var14 = 0;

                while(serverAddress.find()) {
                    hashMap.put(list.get(var14++).toString(), serverAddress.group(1).trim());
                }
            }
        } catch (Exception var18) {
            var18.printStackTrace();
        } finally {
            if (urlConnection != null) {
                urlConnection.disconnect();
                urlConnection = null;
            }

        }

        return hashMap;
    }
}
