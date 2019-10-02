package com.luoheng.crawler.smart.node;

import com.luoheng.crawler.smart.resource.Resource;

import java.util.HashMap;
import java.util.Map;

/**
 * @description:
 * @author: lzh
 * @create: 2019-09-10 10:37
 **/
public class Node {
    private String id;
    private Map<String, String> attributes = new HashMap<>();
    private Resource fromResource;
    private Resource toResource;
    private int count;

    public Node() {
        
    }
}
