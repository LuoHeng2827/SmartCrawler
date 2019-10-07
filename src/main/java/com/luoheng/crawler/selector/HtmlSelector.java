package com.luoheng.crawler.selector;

/**
 * @author ：LuoHeng
 * @date ：Created in 2019/10/7 21:43
 * @description：
 */
public interface HtmlSelector{
    HtmlSelector css(String cssQuery);

    HtmlSelector xpath(String xpathQuery);

    HtmlSelector attr(String attrKey, String attrValue);

    HtmlSelector attr(String attrKey, boolean attrValue);

    TextSelector text();

    TextSelector ownText();

    TextSelector links();

    TextSelector attr(String attrKey);
}
