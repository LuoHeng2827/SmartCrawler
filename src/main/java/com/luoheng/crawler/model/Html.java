package com.luoheng.crawler.model;

import com.luoheng.crawler.selector.HtmlSelector;
import com.luoheng.crawler.selector.TextSelector;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;


/**
 * @description:
 * @author: lzh
 * @create: 2019-10-07 16:34
 **/
public class Html implements HtmlSelector {
    private Logger logger = LoggerFactory.getLogger(Html.class);
    private HtmlNode htmlNode;
    private String url;
    private String charset;


    public Html(String text){
        Document document = Jsoup.parse(text);
        htmlNode = new HtmlNode(document.getAllElements());
    }

    public Html(String text, String url){
        this(text);
        this.url = url;
    }

    public Html(String text, String url, String charset){
        this(text, url);
        this.charset = charset;
    }

    @Override
    public HtmlSelector css(String cssQuery) {
        return htmlNode.css(cssQuery);
    }

    @Override
    public HtmlSelector xpath(String xpathQuery) {
        return htmlNode.xpath(xpathQuery);
    }

    @Override
    public HtmlSelector attr(String attrKey, String attrValue) {
        return htmlNode.attr(attrKey, attrValue);
    }

    @Override
    public HtmlSelector attr(String attrKey, boolean attrValue) {
        return attr(attrKey, attrValue);
    }

    @Override
    public TextSelector text() {
        return htmlNode.text();
    }

    @Override
    public TextSelector ownText() {
        return htmlNode.ownText();
    }

    @Override
    public TextSelector links() {
        return htmlNode.links();
    }

    @Override
    public TextSelector attr(String attrKey) {
        return htmlNode.attr(attrKey);
    }

    public HtmlNode getHtmlNode() {
        return htmlNode;
    }

    public void setHtmlNode(HtmlNode htmlNode) {
        this.htmlNode = htmlNode;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public String getCharset() {
        return charset;
    }

    public void setCharset(String charset) {
        this.charset = charset;
    }
}
