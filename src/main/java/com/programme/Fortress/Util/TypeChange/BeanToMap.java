package com.programme.Fortress.Util.TypeChange;

import org.springframework.cglib.beans.BeanMap;

import java.util.HashMap;
import java.util.Map;

public class BeanToMap {

    /**
     * Map 转 Bean
     * @param map
     * @param bean
     * @param <T>
     * @return
     */
    public static <T> T mapToBean(Map<String, Object> map, T bean) {
        BeanMap beanMap = BeanMap.create(bean);
        beanMap.putAll(map);
        return bean;
    }

    /**
     * Bean 转 Map
     * @param bean
     * @param <T>
     * @return
     */
    public static <T> Map<String,Object> beanToMap(T bean){
        Map<String,Object> map = new HashMap<String,Object>();
        try{
            if (bean != null){
                BeanMap beanMap = BeanMap.create(bean);
                map.putAll(beanMap);
            }
        }catch (Exception e){
            e.printStackTrace();
        }
        return map;
    }
}
