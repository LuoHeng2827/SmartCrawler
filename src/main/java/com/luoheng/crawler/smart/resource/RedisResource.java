package com.luoheng.crawler.smart.resource;

import com.luoheng.crawler.util.redis.JedisConfig;
import com.luoheng.crawler.util.redis.JedisUtil;

/**
 * @description:
 * @author: lzh
 * @create: 2019-09-17 17:03
 **/
public class RedisResource implements Resource<JedisUtil> {
    private String id;
    private JedisConfig jedisConfig;
    private JedisUtil jedisUtil;

    public RedisResource(String id, JedisConfig jedisConfig) {
        this.id = id;
        this.jedisConfig = jedisConfig;
    }

    @Override
    public JedisUtil getResource() {
        if (jedisUtil == null){
            synchronized (this){
                if (jedisUtil == null){
                    jedisUtil = new JedisUtil(jedisConfig);
                }
            }
        }
        return jedisUtil;
    }

    public String getId() {
        return id;
    }

    @Override
    public void close() {
        if (jedisUtil != null)
            jedisUtil.close();
    }
}
