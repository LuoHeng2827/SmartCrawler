package com.luoheng.crawler.downloader;

import org.apache.http.HttpHost;

/**
 * @author ：LuoHeng
 * @date ：Created in 2019/10/20 22:22
 * @description：
 */
public interface ProxyProvider {
    HttpHost getProxy();

    void returnProxy(HttpHost proxy);
}
