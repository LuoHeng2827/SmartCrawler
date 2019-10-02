package com.luoheng.crawler.bean;

import com.luoheng.crawler.smart.exception.NoSuchAttributeTypeException;

import java.util.Date;

/**
 * @description:
 * @author: lzh
 * @create: 2019-09-23 15:08
 **/
public class AttributeType {
    public static final String TYPE_STRING = "string";
    public static final String TYPE_INT = "int";
    public static final String TYPE_LONG = "long";
    public static final String TYPE_FLOAT = "float";
    public static final String TYPE_DOUBLE = "double";
    public static final String TYPE_BOOLEAN = "boolean";
    public static final String TYPE_DATE = "date";
    public static final String TYPE_BEAN = "bean";
    
    public static Class getTypeClass(String typeName) throws NoSuchAttributeTypeException {
        switch (typeName){
            case TYPE_STRING:
                return String.class;
            case TYPE_INT:
                return Integer.class;
            case TYPE_LONG:
                return Long.class;
            case TYPE_FLOAT:
                return Float.class;
            case TYPE_DOUBLE:
                return double.class;
            case TYPE_BOOLEAN:
                return Boolean.class;
            case TYPE_DATE:
                return Date.class;
            case TYPE_BEAN:
                return Bean.class;
            default:
                throw new NoSuchAttributeTypeException();
        }
    }

    public static boolean hasType(String typeName){
        switch (typeName){
            case TYPE_STRING:
                return true;
            case TYPE_INT:
                return true;
            case TYPE_LONG:
                return true;
            case TYPE_FLOAT:
                return true;
            case TYPE_DOUBLE:
                return true;
            case TYPE_BOOLEAN:
                return true;
            case TYPE_DATE:
                return true;
            default:
                return false;
        }
    }
}
