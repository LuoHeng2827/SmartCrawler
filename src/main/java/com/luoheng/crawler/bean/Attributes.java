package com.luoheng.crawler.bean;

import com.luoheng.crawler.smart.exception.NoSuchAttributeException;

import java.util.ArrayList;
import java.util.List;

/**
 * @description:
 * @author: lzh
 * @create: 2019-09-23 15:24
 **/
public class Attributes {
    private List<Attribute> attributeList = new ArrayList<>();

    public boolean contains(String name){
        for (Attribute attribute : attributeList){
            if (attribute.getName().equals(name))
                return true;
        }
        return false;
    }

    public Attribute getAttribute(String name) throws NoSuchAttributeException{
        if (!contains(name))
            throw new NoSuchAttributeException();
        for (Attribute attribute : attributeList){
            if (attribute.getName().equals(name))
                return attribute;
        }
        return null;
    }

    public void setValue(String name, Object value) throws NoSuchAttributeException {
        if (!contains(name))
            throw new NoSuchAttributeException();
        Attribute attribute = getAttribute(name);
        attribute.setValue(value);
    }

    public void setValue(int attributeIndex, Object value) throws NoSuchAttributeException {
        if (attributeIndex > attributeList.size() - 1 || attributeIndex < 0)
            throw new NoSuchAttributeException();
        attributeList.get(attributeIndex).setValue(value);
    }

    public void add(Attribute attribute){
        attributeList.add(attribute);
    }

    public void clearValue(){
        for (Attribute attribute : attributeList){
            attribute.setValue(null);
        }
    }

    public List<Attribute> getAttributeList() {
        return attributeList;
    }

    public void setAttributeList(List<Attribute> attributeList) {
        this.attributeList = attributeList;
    }
}
