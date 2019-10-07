package com.luoheng.crawler.model;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * @description:
 * @author: lzh
 * @create: 2019-10-07 16:34
 **/
public class Html {
    private Logger logger = LoggerFactory.getLogger(Html.class);
    private Document document;
    private String url;
    private String charset;

    public Html(String text){
        document = Jsoup.parse(text);
    }

    public Html(String text, String url){
        this.url = url;
        document = Jsoup.parse(text, url);
    }
}
