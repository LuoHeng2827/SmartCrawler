package com.luoheng.crawler.util.redis;


import redis.clients.jedis.Jedis;
import redis.clients.jedis.JedisPool;
import redis.clients.jedis.JedisPoolConfig;

import java.util.List;


public class JedisUtil {
    private JedisPool jedisPool;
    private JedisPoolConfig jedisPoolConfig;
    private JedisConfig jedisConfig;

    public JedisUtil(JedisConfig jedisConfig){
        this.jedisConfig = jedisConfig;
        initJedisPool();
    }

    public JedisUtil(JedisConfig jedisConfig, JedisPoolConfig jedisPoolConfig){
        this.jedisConfig = jedisConfig;
        this.jedisPoolConfig = jedisPoolConfig;
        initJedisPool();
    }

    public Jedis getResource() {
        return getInstance().getResource();
    }

    public void returnResource(Jedis jedis) {
        if (jedis != null)
            jedis.close();

    }

    private void initJedisPool() {
        if (jedisPoolConfig == null){
            jedisPoolConfig = new JedisPoolConfig();
            jedisPoolConfig.setMaxWaitMillis(10000L);
            jedisPoolConfig.setMaxIdle(64);
            jedisPoolConfig.setMaxTotal(128);
            jedisPoolConfig.setMinIdle(32);
            jedisPoolConfig.setMinEvictableIdleTimeMillis(1800000L);
            jedisPoolConfig.setTimeBetweenEvictionRunsMillis(180000L);
            jedisPoolConfig.setNumTestsPerEvictionRun(-1);
        }
        String passwords = jedisConfig.getPasswords();
        String host = jedisConfig.getHost();
        int port = jedisConfig.getPort();
        if (passwords != null)
            jedisPool = new JedisPool(jedisPoolConfig, host, port, 60000, passwords);
        else
            jedisPool = new JedisPool(jedisPoolConfig, host, port);
    }

    private JedisPool getInstance() {
        if (jedisPool == null) {
            synchronized (JedisUtil.class) {
                initJedisPool();
            }
        }
        return jedisPool;
    }

    public void lpush(String queueName, String... strings) {
        Jedis jedis = getResource();
        jedis.lpush(queueName, strings);
        jedis.close();
    }

    public void rpush(String queueName, String... strings) {
        Jedis jedis = getResource();
        jedis.rpush(queueName, strings);
        jedis.close();
    }

    public String lpop(String queueName) {
        Jedis jedis = getResource();
        String data = jedis.lpop(queueName);
        jedis.close();
        return data;
    }

    public String rpop(String queueName) {
        Jedis jedis = getResource();
        String data = jedis.rpop(queueName);
        jedis.close();
        return data;
    }

    public long llen(String queueName) {
        Jedis jedis = getResource();
        long len = jedis.llen(queueName);
        jedis.close();
        return len;
    }

    public List<String> lrange(String queueName, long start, long end) {
        Jedis jedis = getResource();
        List<String> list = jedis.lrange(queueName, start, end);
        jedis.close();
        return list;
    }

    public String get(String key) {
        Jedis jedis = getResource();
        String data = jedis.get(key);
        jedis.close();
        return data;
    }

    public long del(String... key) {
        Jedis jedis = getResource();
        long num = jedis.del(key);
        jedis.close();
        return num;
    }

    public boolean exists(String key) {
        Jedis jedis = getResource();
        boolean exists = jedis.exists(key);
        jedis.close();
        return exists;
    }

    public void setbit(String key, long offset, boolean value) {
        Jedis jedis = getResource();
        jedis.setbit(key, offset, value);
        jedis.close();
    }

    public boolean getbit(String key, long offset) {
        Jedis jedis = getResource();
        boolean value = jedis.getbit(key, offset);
        jedis.close();
        return value;
    }

    public void close() {
        jedisPool.close();
    }

    public JedisPool getJedisPool() {
        return getInstance();
    }

    public void setJedisConfig(JedisConfig jedisConfig) {
        this.jedisConfig = jedisConfig;
    }
}
