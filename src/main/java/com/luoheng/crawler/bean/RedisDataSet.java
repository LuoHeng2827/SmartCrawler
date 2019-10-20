package com.luoheng.crawler.bean;

import com.luoheng.crawler.model.Bean;
import com.luoheng.crawler.smart.resource.RedisResource;
import com.luoheng.crawler.util.redis.JedisUtil;

/**
 * @description:
 * @author: lzh
 * @create: 2019-09-24 17:23
 **/
public class RedisDataSet implements DataSet {
    private RedisResource redisResource;
    private String queueName;
    @Override
    public Bean popData() {
        JedisUtil jedisUtil = redisResource.getResource();
        jedisUtil.rpop(queueName);
        return null;
    }

    @Override
    public void pushData() {

    }
}
