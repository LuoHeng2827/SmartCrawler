package com.luoheng.crawler.selector;

import java.util.List;

/**
 * @author ：LuoHeng
 * @date ：Created in 2019/10/7 21:23
 * @description：
 */
public interface TextSelector {
    String get();

    List<String> all();

    TextSelector regex(String regex);

    TextSelector regex(String regex, int group);

    TextSelector replace(String oldChar, String replacement);

    TextSelector replaceAll(String regex, String replacement);
}
