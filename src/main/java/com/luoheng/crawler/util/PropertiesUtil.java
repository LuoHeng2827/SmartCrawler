package com.luoheng.crawler.util;

import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

public class PropertiesUtil {
    private static Properties properties =new Properties();
    public PropertiesUtil(String path) {
        load(path);
    }

    public static void load(String path) {
        try {
            InputStream is= PropertiesUtil.class.getResourceAsStream(path);
            properties.load(is);
            is.close();
        }catch (IOException e){
            e.printStackTrace();
        }
    }

    public static String getValue(String key) {
        return properties.getProperty(key);
    }

    public static String getValue(String key, String defaultValue){
        return properties.getProperty(key,defaultValue);
    }

    public static int getIntValue(String key){
        String value = getValue(key);
        return value == null ? null : Integer.valueOf(value);
    }

    public static int getIntValue(String key, int defaultValue){
        String value = getValue(key);
        return value == null ? defaultValue : Integer.valueOf(value);
    }

    public static long getLongValue(String key) {
        String value = getValue(key);
        return value == null ? null : Long.valueOf(value);
    }

    public static long getLongValue(String key, long defaultValue) {
        String value = getValue(key);
        return value == null ? defaultValue : Long.valueOf(value);
    }

    public static float getFloatValue(String key) {
        String value = getValue(key);
        return value == null ? null : Float.valueOf(value);
    }

    public static float getFloatValue(String key, float defaultValue) {
        String value = getValue(key);
        return value == null ? defaultValue : Float.valueOf(value);
    }

    public static double getDoubleValue(String key) {
        String value = getValue(key);
        return value == null ? null : Double.valueOf(value);
    }

    public static double getDoubleValue(String key, double defaultValue) {
        String value = getValue(key);
        return value == null ? defaultValue : Double.valueOf(value);
    }

    public static void main(String[] args){
    }
}
