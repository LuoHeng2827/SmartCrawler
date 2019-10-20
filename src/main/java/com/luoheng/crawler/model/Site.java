package com.luoheng.crawler.model;

import com.luoheng.crawler.downloader.ProxyProvider;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @author ：LuoHeng
 * @date ：Created in 2019/10/19 11:13
 * @description：
 */
public class Site {
    private String url;

    private String useAgent;

    private boolean allowProxy = false;

    private Map<String, String> cookies = new HashMap<>();

    private Map<String, String> headers = new HashMap<>();

    private String charset;

    private String siteName;

    private String pageName;

    private ProxyProvider proxyProvider;

    public Site() {
    }

    public String getUrl() {
        return url;
    }

    public Site setUrl(String url) {
        this.url = url;
        return this;
    }

    public String getUseAgent() {
        return useAgent;
    }

    public Site setUseAgent(String useAgent) {
        this.useAgent = useAgent;
        return this;
    }

    public boolean isAllowProxy() {
        return allowProxy;
    }

    public Site setAllowProxy(boolean allowProxy) {
        this.allowProxy = allowProxy;
        return this;
    }

    public Map<String, String> getCookies() {
        return cookies;
    }

    public void setCookies(Map<String, String> cookies) {
        this.cookies = cookies;
    }

    public Map<String, String> getHeaders() {
        return headers;
    }

    public Site setHeaders(Map<String, String> headers) {
        this.headers = headers;
        return this;
    }

    public String getCharset() {
        return charset;
    }

    public Site setCharset(String charset) {
        this.charset = charset;
        return this;
    }

    public String getSiteName() {
        return siteName;
    }

    public void setSiteName(String siteName) {
        this.siteName = siteName;
    }

    public String getPageName() {
        return pageName;
    }

    public void setPageName(String pageName) {
        this.pageName = pageName;
    }

    public ProxyProvider getProxyProvider() {
        return proxyProvider;
    }

    public Site setProxyProvider(ProxyProvider proxyProvider) {
        this.proxyProvider = proxyProvider;
        return this;
    }
}
