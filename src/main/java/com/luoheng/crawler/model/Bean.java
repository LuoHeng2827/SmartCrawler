package com.luoheng.crawler.model;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.luoheng.crawler.smart.exception.NoSuchAttributeException;

import java.util.HashMap;
import java.util.Map;

/**
 * @description:
 * @author: lzh
 * @create: 2019-09-17 16:34
 **/
public class Bean {
    private Map<String, Object> fields = new HashMap<>();

    public Object get(String key, Object defaultValue){
        Object value = fields.get(key);
        if (value == null)
            return defaultValue;
        return value;
    }

    public Object get(String key){
        return get(key, null);
    }

    public void put(String key, Object value){
        fields.put(key, value);
    }
}
