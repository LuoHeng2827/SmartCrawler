package com.luoheng.crawler.bean;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.luoheng.crawler.smart.exception.NoSuchAttributeException;

/**
 * @description:
 * @author: lzh
 * @create: 2019-09-17 16:34
 **/
public class Bean {
    private static final String DEFAULT_DATE_FORMAT = "yyyy-MM-dd hh:mm";
    private String id;
    private Attributes attributes = new Attributes();
    private String dateFormat;

    public Bean() {
        this.dateFormat = DEFAULT_DATE_FORMAT;
    }

    public static Bean fromJson(String json, String dateFormat){
        Gson gson = new GsonBuilder().setDateFormat(dateFormat).create();
        return gson.fromJson(json, Bean.class);
    }

    public static Bean fromJson(String json){
        return fromJson(json, DEFAULT_DATE_FORMAT);
    }

    public String toJson(){
        Gson gson = new GsonBuilder().setDateFormat(dateFormat).create();
        return gson.toJson(this);
    }

    public void addAttribute(Attribute attribute){
        attributes.add(attribute);
    }

    public void setAttributeValue(String name, Object value) throws NoSuchAttributeException{
        try {
            attributes.setValue(name, value);
        }catch (NoSuchAttributeException e){
            throw new NoSuchAttributeException("the bean id is " + id + " not have attribute named " + name);
        }
    }

    public void setAttributeValue(int attributeIndex, Object value) throws NoSuchAttributeException{
        try {
            attributes.setValue(attributeIndex, value);
        }catch (NoSuchAttributeException e){
            throw new NoSuchAttributeException("the bean id is " + id + " not have attribute at index " + attributeIndex);
        }
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public Attributes getAttributes() {
        return attributes;
    }

    public void setAttributes(Attributes attributes) {
        this.attributes = attributes;
    }

    public String getDateFormat() {
        return dateFormat;
    }

    public void setDateFormat(String dateFormat) {
        this.dateFormat = dateFormat;
    }
}
