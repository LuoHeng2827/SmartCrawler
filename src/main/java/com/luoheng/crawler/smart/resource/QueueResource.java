package com.luoheng.crawler.smart.resource;

import java.util.concurrent.ConcurrentLinkedQueue;

/**
 * @description:
 * @author: lzh
 * @create: 2019-09-24 17:42
 **/
public class QueueResource implements Resource<ConcurrentLinkedQueue> {
    @Override
    public ConcurrentLinkedQueue getResource() throws Exception {
        return null;
    }

    @Override
    public void close() {

    }
}
