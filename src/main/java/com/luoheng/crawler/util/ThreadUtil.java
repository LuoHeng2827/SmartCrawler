package com.luoheng.crawler.util;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.UUID;
import java.util.concurrent.TimeUnit;

public class ThreadUtil {
    private Logger logger = LoggerFactory.getLogger(ThreadUtil.class);
    public static void waitMillis(long millis){
        try{
            TimeUnit.MILLISECONDS.sleep(millis);
        }catch(InterruptedException e){
            e.printStackTrace();
        }
    }
    public static void waitSecond(long second){
        try{
            TimeUnit.SECONDS.sleep(second);
        }catch(InterruptedException e){
            e.printStackTrace();
        }
    }
    public static String getUUID(){
        return UUID.randomUUID().toString().replace("-","");
    }
    public static void main(String[] args){
        String uuid=getUUID();
    }
}
