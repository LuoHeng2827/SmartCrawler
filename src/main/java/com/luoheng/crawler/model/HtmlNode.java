package com.luoheng.crawler.model;

import com.luoheng.crawler.selector.HtmlSelector;
import com.luoheng.crawler.selector.TextSelector;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.ArrayList;
import java.util.List;

/**
 * @author ：LuoHeng
 * @date ：Created in 2019/10/7 21:13
 * @description：
 */
public class HtmlNode implements HtmlSelector {
    private Logger logger = LoggerFactory.getLogger(HtmlNode.class);
    private Elements data;

    public HtmlNode(Elements data){
        this.data = data;
    }

    @Override
    public HtmlSelector css(String cssQuery) {
        return new HtmlNode(data.select(cssQuery));
    }

    @Override
    public HtmlSelector xpath(String xpathQuery) {
        return null;
    }

    @Override
    public HtmlSelector attr(String attrKey, String attrValue) {
        Elements elementsResult = new Elements();
        for (Element element : data){
            elementsResult.add(element.attr(attrKey, attrValue));
        }
        return new HtmlNode(elementsResult);
    }

    @Override
    public HtmlSelector attr(String attrKey, boolean attrValue) {
        Elements elementsResult = new Elements();
        for (Element element : data){
            elementsResult.add(element.attr(attrKey, attrValue));
        }
        return new HtmlNode(elementsResult);
    }

    @Override
    public TextSelector links() {
        List<String> linkList = new ArrayList<>();
        for (Element element : data){
            Elements aTags = element.getElementsByTag("a");
            for (Element aTag : aTags){
                linkList.add(aTag.absUrl("href"));
            }
        }
        return new PlainText(linkList);
    }

    @Override
    public TextSelector text() {
        List<String> textResult = new ArrayList<>();
        for (Element element : data){
            textResult.add(element.text());
        }
        return new PlainText(textResult);
    }

    @Override
    public TextSelector ownText() {
        List<String> textResult = new ArrayList<>();
        for (Element element : data){
            textResult.add(element.ownText());
        }
        return new PlainText(textResult);
    }

    @Override
    public TextSelector attr(String attrKey) {
        List<String> textResult = new ArrayList<>();
        for (Element element : data){
            textResult.add(element.attr(attrKey));
        }
        return new PlainText(textResult);
    }
}
