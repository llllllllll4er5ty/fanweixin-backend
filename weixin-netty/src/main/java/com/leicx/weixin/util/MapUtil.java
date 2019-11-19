package com.leicx.weixin.util;

import java.util.Map;

/**
 * map工具类
 * @author daxiong
 * @date 2019-07-19 09:25
 **/
public class MapUtil {

    /**
     * 判断存入map的值是否为空，不为空则存入，为空则不添加
     * @author daxiong
     * @date 2019-07-19 09:25
     * @param map
     * @param key
     * @param value
     * @return void
     **/
    public static void addToMap(Map<String, Object> map, String key, Object value) {
        if (map == null || key == null) {
            return;
        }
        if (value != null) {
            map.put(key, value);
        }
    }

    /**
     * 判断存入map的值是否为空，不为空则存入，为空则存入默认值
     * @author daxiong
     * @date 2019-07-19 09:31
     * @param map
     * @param key
     * @param value
     * @param defaultValue
     * @return void
     **/
    public static void addToMap(Map<String, Object> map, String key, Object value, Object defaultValue) {
        if (map == null || key == null) {
            return;
        }
        if (value != null) {
            map.put(key, value);
        } else {
            map.put(key, defaultValue);
        }
    }
}
