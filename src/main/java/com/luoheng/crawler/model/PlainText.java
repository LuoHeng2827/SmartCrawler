package com.luoheng.crawler.model;

import com.luoheng.crawler.selector.TextSelector;

import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * @author ：LuoHeng
 * @date ：Created in 2019/10/7 22:14
 * @description：
 */
public class PlainText implements TextSelector {
    private List<String> textList;

    public PlainText(List<String> textList) {
        this.textList = textList;
    }

    @Override
    public String get() {
        return textList.get(0);
    }

    @Override
    public List<String> all() {
        return textList;
    }

    @Override
    public TextSelector regex(String regex) {
        List<String> textResult = new ArrayList<>();
        Pattern pattern = Pattern.compile(regex);
        Matcher matcher;
        for (String text : textList){
            matcher = pattern.matcher(text);
            if (matcher.matches())
                textResult.add(matcher.group());
        }
        return new PlainText(textResult);
    }

    @Override
    public TextSelector regex(String regex, int group) {
        List<String> textResult = new ArrayList<>();
        Pattern pattern = Pattern.compile(regex);
        Matcher matcher;
        for (String text : textList){
            matcher = pattern.matcher(text);
            if (matcher.find(group))
                textResult.add(matcher.group(group));
        }
        return new PlainText(textResult);
    }

    @Override
    public TextSelector replace(String oldChar, String replacement) {
        List<String> textResult = new ArrayList<>();
        for (String text : textList){
            textResult.add(text.replace(oldChar, replacement));
        }
        return new PlainText(textResult);
    }

    @Override
    public TextSelector replaceAll(String regex, String replacement) {
        List<String> textResult = new ArrayList<>();
        for (String text : textList){
            textResult.add(text.replaceAll(regex, replacement));
        }
        return new PlainText(textResult);
    }
}
