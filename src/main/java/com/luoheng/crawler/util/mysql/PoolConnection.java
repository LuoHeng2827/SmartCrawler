package com.luoheng.crawler.util.mysql;

import java.sql.Connection;

/**
 * @description:
 * @author: lzh
 * @create: 2019-09-11 10:43
 **/
public class PoolConnection {
    private Connection connection;
    private boolean busy;
    PoolConnection(Connection connection){
        this.connection = connection;
        busy = false;
    }

    public Connection getConnection() {
        return connection;
    }

    public void setConnection(Connection connection) {
        this.connection = connection;
    }

    public boolean isBusy() {
        return busy;
    }

    public void setBusy(boolean busy) {
        this.busy = busy;
    }
}
