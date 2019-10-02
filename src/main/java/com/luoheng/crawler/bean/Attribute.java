package com.luoheng.crawler.bean;

import com.luoheng.crawler.smart.exception.NoSuchAttributeTypeException;

/**
 * @description: 用来描述定义的bean的属性
 * @author: lzh
 * @create: 2019-09-23 15:07
 **/
public class Attribute {
    private String name;
    private String typeName;
    private Object value;

    public Attribute(String name, String typeName) {
        this.name = name;
        this.typeName = typeName;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getTypeName() {
        return typeName;
    }

    public void setTypeName(String typeName) {
        this.typeName = typeName;
    }

    public Object getValue() {
        return value;
    }

    public Class getType() throws NoSuchAttributeTypeException {
        return AttributeType.getTypeClass(typeName);
    }

    public void setValue(Object value) {
        this.value = value;
    }
}
