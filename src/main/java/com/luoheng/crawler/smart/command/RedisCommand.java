package com.luoheng.crawler.smart.command;

import com.luoheng.crawler.smart.resource.RedisResource;

/**
 * @description:
 * @author: lzh
 * @create: 2019-09-11 14:26
 **/
public class RedisCommand extends Command{
    RedisResource redisResource;
    RedisCommand(RedisResource redisResource){
        this.redisResource = redisResource;
    }

    public void lpush(Object... args){
        String queueName = (String)args[0];
        String[] strings = new String[args.length - 1];
        for (int i = 1; i < args.length; i++) {
            strings[i - 1] = (String) args[i];
        }
        redisResource.getResource().lpush(queueName, strings);
    }

    public void rpush(Object... args){
        String queueName = (String)args[0];
        String[] strings = new String[args.length - 1];
        for (int i = 1; i < args.length; i++) {
            strings[i - 1] = (String) args[i];
        }
        redisResource.getResource().rpush(queueName, strings);
    }

    public String lpop(String queueName){
        return redisResource.getResource().lpop(queueName);
    }

    public String rpop(String queueName){
        return redisResource.getResource().rpop(queueName);
    }

    public void close(){
        if (redisResource != null)
            redisResource.close();
    }

    public static void main(String[] args) throws Exception{
        RedisCommand redisCommand = new RedisCommand(null);
        redisCommand.execute(redisCommand, "lpush", "list", "s", "s", "a");
    }
}
