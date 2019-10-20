package com.luoheng.crawler.downloader;


import org.apache.http.HttpEntity;
import org.apache.http.HttpHost;
import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.client.HttpClient;
import org.apache.http.client.config.RequestConfig;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.client.methods.HttpRequestBase;
import org.apache.http.entity.ContentType;
import org.apache.http.entity.StringEntity;
import org.apache.http.entity.mime.MultipartEntityBuilder;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.message.BasicNameValuePair;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.File;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.nio.charset.Charset;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

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
    private static HttpClient httpClient;
    private static Logger logger = LoggerFactory.getLogger(HttpClientUtils.class);

    private static void buildDefaultHeader(HttpRequestBase base){
        base.setHeader("User-Agent","Mozilla/5.0 (Windows NT 6.1; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/75.0.3770.142 Safari/537.36");
        base.setHeader("Accept","*/*");
        base.setHeader("Cache-Control","no-cache");
        base.setHeader("Connection","keep-alive");
    }

    public static HttpResponse file(String url, Map<String, File> fileMap) throws IOException{
        return file(url, null, null, fileMap);
    }

    public static HttpResponse file(String url, Map<String, String> requestParams,
                                    Map<String, String> requestHeaders, Map<String, File> fileMap) throws IOException{
        return file(url, requestParams, requestHeaders, fileMap, null);
    }

    public static HttpResponse file(String url, Map<String, String> requestParams,
                                    Map<String, String> requestHeaders, Map<String, File> fileMap, HttpHost proxy) throws IOException{
        return file(url, requestParams, requestHeaders, fileMap, proxy, null);
    }

    public static HttpResponse file(String url, Map<String, String> requestParams,
                                    Map<String, String> requestHeaders,Map<String, File> fileMap, HttpHost proxy,
                                    RequestConfig requestConfig) throws IOException{
        return multipartForm(url, requestParams, requestHeaders, null, fileMap, proxy, requestConfig);
    }

    public static HttpResponse multipartForm(String url,Map<String, String> forms,
                                             Map<String, File> fileMap) throws IOException{
        return multipartForm(url, null, null, forms, fileMap);
    }

    public static HttpResponse multipartForm(String url, Map<String, String> requestParams,
                                             Map<String, String> requestHeaders, Map<String, String> forms,
                                             Map<String, File> fileMap) throws IOException{
        return multipartForm(url, requestParams, requestHeaders, forms, fileMap, null);
    }
    
    public static HttpResponse multipartForm(String url, Map<String, String> requestParams,
                                             Map<String, String> requestHeaders, Map<String, String> forms,
                                             Map<String, File> fileMap, HttpHost proxy) throws IOException{
        return multipartForm(url, requestParams, requestHeaders, forms, fileMap, proxy, null);
    }

    public static HttpResponse multipartForm(String url, Map<String, String> requestParams,
                                             Map<String, String> requestHeaders, Map<String, String> forms,
                                             Map<String, File> fileMap, HttpHost proxy,
                                             RequestConfig requestConfig) throws IOException{
        if (forms == null)
            forms = new HashMap<>();
        MultipartEntityBuilder multipartEntityBuilder = MultipartEntityBuilder.create();
        multipartEntityBuilder.setContentType(ContentType.MULTIPART_FORM_DATA);
        for (Map.Entry<String, File> entry : fileMap.entrySet()){
            multipartEntityBuilder.addBinaryBody(entry.getKey(), entry.getValue(),
                    ContentType.DEFAULT_BINARY, entry.getValue().getName());
        }
        for (Map.Entry<String, String> entry : forms.entrySet()){
            multipartEntityBuilder.addTextBody(entry.getKey(), entry.getValue());
        }
        return doPost(url, requestParams, requestHeaders, multipartEntityBuilder.build(), proxy, requestConfig);
    }

    public static HttpResponse urlEncodedForm(String url, Map<String, String> forms) throws IOException {
        return urlEncodedForm(url, null, null, forms);
    }

    public static HttpResponse urlEncodedForm(String url, Map<String, String> requestParams,
                                              Map<String, String> requestHeaders,
                                              Map<String, String> forms) throws IOException {
        return urlEncodedForm(url, requestParams, requestHeaders, forms, null);
    }

    public static HttpResponse urlEncodedForm(String url, Map<String, String> requestParams,
                                              Map<String, String> requestHeaders, Map<String, String> forms,
                                              HttpHost proxy) throws IOException {
        return urlEncodedForm(url, requestParams, requestHeaders, forms, proxy, null);
    }

    public static HttpResponse urlEncodedForm(String url, Map<String, String> requestParams,
                                    Map<String, String> requestHeaders, Map<String, String> forms,
                                    HttpHost proxy, RequestConfig requestConfig) throws IOException {
        if (forms == null)
            forms = new HashMap<>();
        List<NameValuePair> formsParamsList = new ArrayList<>();
        for (Map.Entry<String, String> entry : forms.entrySet()){
            formsParamsList.add(new BasicNameValuePair(entry.getKey(), entry.getValue()));
        }
        UrlEncodedFormEntity urlEncodedFormEntity = new UrlEncodedFormEntity(formsParamsList);
        urlEncodedFormEntity.setContentType(ContentType.APPLICATION_FORM_URLENCODED.getMimeType());
        return doPost(url, requestParams, requestHeaders, urlEncodedFormEntity, proxy, requestConfig);
    }

    public static HttpResponse json(String url, String json) throws IOException {
        return json(url, null, null, json);
    }

    public static HttpResponse json(String url, Map<String, String> requestParams,
                                    Map<String, String> requestHeaders, String json) throws IOException {
        return json(url, requestParams, requestHeaders, json, null);
    }

    public static HttpResponse json(String url, Map<String, String> requestParams,
                                    Map<String, String> requestHeaders, String json,
                                    HttpHost proxy) throws IOException {
        return json(url, requestParams, requestHeaders, json, proxy, null);
    }

    public static HttpResponse json(String url, Map<String, String> requestParams,
                                    Map<String, String> requestHeaders, String json,
                                    HttpHost proxy, RequestConfig requestConfig) throws IOException {
        StringEntity stringEntity = new StringEntity(json, Charset.forName("utf-8"));
        stringEntity.setContentType(ContentType.APPLICATION_JSON.getMimeType());
        return doPost(url, requestParams, requestHeaders, stringEntity, proxy, requestConfig);
    }

    public static HttpResponse xml(String url, String xml) throws IOException {
        return xml(url, null, null, xml);
    }

    public static HttpResponse xml(String url, Map<String, String> requestParams,
                                   Map<String, String> requestHeaders, String xml) throws IOException {
        return xml(url, requestParams, requestHeaders, xml, null);
    }

    public static HttpResponse xml(String url, Map<String, String> requestParams,
                                   Map<String, String> requestHeaders, String xml,
                                   HttpHost proxy) throws IOException {
        return xml(url, requestParams, requestHeaders, xml, proxy, null);
    }

    public static HttpResponse xml(String url, Map<String, String> requestParams,
                                    Map<String, String> requestHeaders, String xml,
                                    HttpHost proxy, RequestConfig requestConfig) throws IOException {
        StringEntity stringEntity = new StringEntity(xml, Charset.forName("utf-8"));
        stringEntity.setContentType(ContentType.APPLICATION_XML.getMimeType());
        return doPost(url, requestParams, requestHeaders, stringEntity, proxy, requestConfig);
    }

    public static HttpResponse doGet(String url) throws IOException{
        return doGet(url, null);
    }

    public static HttpResponse doGet(String url, Map<String, String> requestParams) throws IOException{
        return doGet(url, requestParams, null);
    }

    public static HttpResponse doGet(String url, Map<String, String> requestParams, Map<String, String> requestHeaders) throws IOException {
        return doGet(url, requestParams, requestHeaders, null);
    }

    public static HttpResponse doGet(String url, Map<String, String> requestParams, Map<String, String> requestHeaders,
                                     HttpHost proxy) throws IOException {
        return doGet(url, requestParams, requestHeaders, proxy, null);
    }

    public static HttpResponse doGet(String url, Map<String, String> requestParams, Map<String, String> requestHeaders,
                                     HttpHost proxy, RequestConfig requestConfig) throws IOException {
        if (requestParams == null)
            requestParams = new HashMap<>();
        if (requestHeaders == null)
            requestHeaders = new HashMap<>();
        url = generateGetParams(url, requestParams);
        HttpGet httpGet = new HttpGet(url);
        buildDefaultHeader(httpGet);
        for (Map.Entry<String, String> entry : requestHeaders.entrySet()){
            httpGet.addHeader(entry.getKey(), entry.getValue());
        }
        if (proxy == null){
            if (requestConfig == null)
                requestConfig = buildDefaultNormalRequestCongif();
        }
        else{
            if (requestConfig == null)
                requestConfig = buildDefaultProxyRequestCongif(proxy);
        }
        httpGet.setConfig(requestConfig);
        return getInstance().execute(httpGet);
    }

    public static HttpResponse doPost(String url, HttpEntity httpEntity) throws IOException {
        return doPost(url, null, null, httpEntity);
    }

    public static HttpResponse doPost(String url, Map<String, String> requestParams, Map<String, String> requestHeaders,
                                      HttpEntity httpEntity) throws IOException {
        return doPost(url, requestParams, requestHeaders, httpEntity, null);
    }

    public static HttpResponse doPost(String url, Map<String, String> requestParams, Map<String, String> requestHeaders,
                                      HttpEntity httpEntity, HttpHost proxy) throws IOException {
        return doPost(url, requestParams, requestHeaders, httpEntity, proxy, null);
    }

    public static HttpResponse doPost(String url, Map<String, String> requestParams, Map<String, String> requestHeaders,
                                      HttpEntity httpEntity, HttpHost proxy, RequestConfig requestConfig) throws IOException {
        if (requestParams == null)
            requestParams = new HashMap<>();
        if (requestHeaders == null)
            requestHeaders = new HashMap<>();
        url = generateGetParams(url, requestParams);
        HttpPost httpPost = new HttpPost(url);
        buildDefaultHeader(httpPost);
        for (Map.Entry<String, String> entry : requestHeaders.entrySet()){
            httpPost.addHeader(entry.getKey(), entry.getValue());
        }
        if (proxy == null){
            if (requestConfig == null)
                requestConfig = buildDefaultNormalRequestCongif();
        }
        else{
            if (requestConfig == null)
                requestConfig = buildDefaultProxyRequestCongif(proxy);
        }
        httpPost.setConfig(requestConfig);
        if (httpEntity != null){
            httpPost.setEntity(httpEntity);
        }
        return getInstance().execute(httpPost);
    }

    public static String generateGetParams(String url, Map<String, String> requestParams) {
        if (requestParams.size() == 0) {
            return url;
        } else {
            StringBuilder stringBuilder = new StringBuilder(url + "?");
            for (Map.Entry<String, String> entry : requestParams.entrySet()){
                stringBuilder.append(entry.getKey());
                stringBuilder.append("=");
                try {
                    stringBuilder.append(URLEncoder.encode(entry.getValue(), "utf-8"));
                }catch (UnsupportedEncodingException e){
                    e.printStackTrace();
                }
                stringBuilder.append("&");
            }
            stringBuilder.delete(stringBuilder.lastIndexOf("&"), stringBuilder.length());
            return stringBuilder.toString();
        }
    }

    public static HttpClient getInstance(){
        if (httpClient == null){
            synchronized (HttpClientUtils.class){
                if (httpClient == null){
                    httpClient = HttpClients.custom().disableAutomaticRetries().build();
                }
            }
        }
        return httpClient;
    }

    private static void addRequestConfig(HttpRequestBase httpRequestBase){

    }

    private static RequestConfig buildDefaultNormalRequestCongif(){
        return RequestConfig.custom()
                .setConnectTimeout(DEFAULT_CONNECT_TIMEOUT)
                .setConnectionRequestTimeout(DEFAULT_CONNECT_REQUEST_TIMEOUT)
                .setSocketTimeout(DEFAULT_SOCKET_TIMEOUT)
                .build();
    }
    private static RequestConfig buildDefaultProxyRequestCongif(HttpHost proxy){
        return RequestConfig.custom()
                .setConnectTimeout(DEFAULT_PROXY_CONNECT_TIMEOUT)
                .setConnectionRequestTimeout(DEFAULT_PROXY_CONNECT_REQUEST_TIMEOUT)
                .setSocketTimeout(DEFAULT_PROXY_SOCKET_TIMEOUT)
                .setProxy(proxy)
                .build();
    }
    public static void main(String[] args){
        Map<String, String> httpParams = new HashMap<>();
        httpParams.put("a", "http://www.def456.com?id=5&userName=admin");
        httpParams.put("b", "2");
        System.out.println(ContentType.APPLICATION_JSON.getMimeType());
    }
}
