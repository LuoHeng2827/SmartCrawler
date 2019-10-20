package com.luoheng.crawler.downloader;

import com.luoheng.crawler.model.Page;
import com.luoheng.crawler.model.Site;
import com.luoheng.crawler.model.Task;
import org.apache.http.HttpHost;
import org.apache.http.HttpResponse;
import org.apache.http.client.config.RequestConfig;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.util.Map;

/**
 * @author ：LuoHeng
 * @date ：Created in 2019/10/20 16:14
 * @description：
 */
public class PageDownloader {
    private int retryTime;
    private Logger logger = LoggerFactory.getLogger(PageDownloader.class);

    public Page doGet(Task task, Map<String, String> requestParams, Map<String, String> requestHeaders,
                      RequestConfig requestConfig){
        Site site = task.getSite();
        HttpHost proxy = null;
        if (site.isAllowProxy()){
            if (site.getProxyProvider() != null)
                proxy = site.getProxyProvider().getProxy();
        }
        try {
            HttpResponse httpResponse = HttpClientUtils.doGet(site.getUrl(), requestParams,
                    requestHeaders, proxy, requestConfig);
        }catch (IOException e){
            task.failed();
            if (task.getFailedTime() <= retryTime){
                return doGet(task, requestParams, requestHeaders, requestConfig);
            }
            else{

            }
        }
        return null;
    }
}
