package com.luoheng.crawler.smart.resource;

/**
 * @description:
 * @author: lzh
 * @create: 2019-09-17 16:45
 **/
public interface Resource<T> {
    T getResource() throws Exception;

    void close();
}
