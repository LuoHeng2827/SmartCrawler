package com.luoheng.crawler.smart.exception;

/**
 * @description:
 * @author: lzh
 * @create: 2019-09-17 16:38
 **/
public class NoSuchAttributeException extends RuntimeException {

    public NoSuchAttributeException() {
    }

    public NoSuchAttributeException(String message) {
        super(message);
    }
}
