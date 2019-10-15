package com.luoheng.crawler.util;


import org.apache.http.client.methods.HttpRequestBase;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * @description:
 * @author:
 * @create: 2019-10-15 16:33
 **/
public class HttpClientUtils {
    private static final int DEFAULT_CONNECT_TIMEOUT=10000;
    private static final int DEFAULT_CONNECT_REQUEST_TIMEOUT=10000;
    private static final int DEFAULT_SOCKET_TIMEOUT=10000;
    private static final int DEFAULT_PROXY_CONNECT_TIMEOUT=15000;
    private static final int DEFAULT_PROXY_CONNECT_REQUEST_TIMEOUT=15000;
    private static final int DEFAULT_PROXY_SOCKET_TIMEOUT=15000;
    private static boolean autoRedirect=true;

    private static Logger logger= LoggerFactory.getLogger(HttpClientUtils.class);

    private static void buildDefaultHeader(HttpRequestBase base){
        base.setHeader("User-Agent","Mozilla/5.0 (Windows NT 6.1; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/75.0.3770.142 Safari/537.36");
        base.setHeader("Accept","*/*");
        base.setHeader("Cache-Control","no-cache");
        base.setHeader("Connection","keep-alive");
    }
}
