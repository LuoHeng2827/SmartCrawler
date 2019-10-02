package com.luoheng.crawler.smart.resource;

import com.luoheng.crawler.util.mysql.DBConfig;
import com.luoheng.crawler.util.mysql.SimpleDBPool;
import com.luoheng.crawler.util.mysql.WaitTimeOutException;

/**
 * @description:
 * @author: lzh
 * @create: 2019-09-18 9:58
 **/
public class MysqlResource implements Resource<SimpleDBPool> {
    private String id;
    private DBConfig dbConfig;
    private SimpleDBPool simpleDBPool;
    private int count;

    public MysqlResource(String id, DBConfig dbConfig) {
        this(id, dbConfig, 5);
    }

    public MysqlResource(String id, DBConfig dbConfig, int count) {
        this.id = id;
        this.dbConfig = dbConfig;
        this.count = count;
    }

    @Override
    public SimpleDBPool getResource() throws WaitTimeOutException {
        if (simpleDBPool == null){
            synchronized (this){
                if (simpleDBPool == null){
                    simpleDBPool = new SimpleDBPool(dbConfig, count);
                }
            }
        }
        return simpleDBPool;
    }

    public String getId() {
        return id;
    }

    @Override
    public void close() {
        if (simpleDBPool != null)
            simpleDBPool.close();
    }
}
