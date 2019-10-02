package com.luoheng.crawler.util.mysql;

import com.luoheng.crawler.util.ThreadUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.sql.Connection;
import java.sql.DatabaseMetaData;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.*;
import java.util.concurrent.atomic.AtomicInteger;

public class SimpleDBPool {
    private static final int DEFAULT_MAX_CONNECTION_COUNT = 5;
    private static final long DEFAULT_MAX_WAIT_TIME = 5000L;
    private int maxConnectionCount;
    private long waitTime;
    private DBConfig dbConfig;
    private Vector<PoolConnection> connections;
    private AtomicInteger busyConnectionCount = new AtomicInteger(0);
    private Logger logger = LoggerFactory.getLogger(SimpleDBPool.class);

    static {
        try{
            Class.forName("com.mysql.cj.jdbc.Driver");
        }catch (ClassNotFoundException e){
            e.printStackTrace();
        }
    }
    public SimpleDBPool(DBConfig dbConfig){
        this.dbConfig = dbConfig;
        maxConnectionCount = DEFAULT_MAX_CONNECTION_COUNT;
    }

    public SimpleDBPool(DBConfig dbConfig, int maxConnectionCount){
        this(dbConfig);
        this.maxConnectionCount = maxConnectionCount;
        try {
            createPool(this.maxConnectionCount);
        }catch (SQLException e){
            e.printStackTrace();
        }
    }


    public Connection newConnection() throws SQLException{
        Connection connection = DriverManager.getConnection(dbConfig.getUrl(), dbConfig.getUser(), dbConfig.getPasswords());
        if (connections.size() == 0){
            DatabaseMetaData databaseMetaData = connection.getMetaData();
            int maxConnections = databaseMetaData.getMaxConnections();
            if (maxConnections > 0 && maxConnections < maxConnectionCount){
                maxConnectionCount = maxConnections;
            }
        }
        return connection;
    }

    private void createPool(int count) throws SQLException{
        connections = new Vector<>(count);
        for (int i = 0; i < count; i++){
            if (i >= maxConnectionCount)
                break;
            Connection connection = newConnection();
            connections.add(new PoolConnection(connection));
        }
    }

    public void createPool() throws SQLException{
        createPool(DEFAULT_MAX_CONNECTION_COUNT);
    }

    public synchronized Connection getConnection() throws WaitTimeOutException{
        if (connections == null){
            try {
                createPool();
            }catch (SQLException e){
                e.printStackTrace();
                return null;
            }
        }
        long startMills = System.currentTimeMillis();
        while (true){
            for (PoolConnection poolConnection : connections){
                if (!poolConnection.isBusy()){
                    poolConnection.setBusy(true);
                    Connection connection = poolConnection.getConnection();
                    try{
                        if (!connection.isValid(1500)){
                            connection = newConnection();
                            poolConnection.setConnection(connection);
                        }
                    }catch (SQLException e){
                        e.printStackTrace();
                        return null;
                    }
                    busyConnectionCount.incrementAndGet();
                    return connection;
                }
            }
            long currentMills = System.currentTimeMillis();
            if (currentMills - startMills >= DEFAULT_MAX_CONNECTION_COUNT)
                throw new WaitTimeOutException("wait connection time out!");
        }
    }


    public void returnConnection(Connection connection){
        if (connections == null)
            return;
        for (PoolConnection poolConnection : connections){
            if (connection == poolConnection.getConnection()){
                poolConnection.setBusy(false);
                busyConnectionCount.decrementAndGet();
                break;
            }
        }
    }

    public void close(){
        logger.info("{} is closing", getClass().getName());
        for (PoolConnection poolConnection : connections){
            while (poolConnection.isBusy()){
                ThreadUtil.waitMillis(100);
            }
            try {
                poolConnection.getConnection().close();
            }catch (SQLException e){
                e.printStackTrace();
            }
        }
        logger.info("{} is closed", getClass().getName());
        connections = null;
    }

    public int getBusyConnectionCount() {
        return busyConnectionCount.get();
    }

    @Override
    public String toString() {
        if (connections == null)
            return "";
        StringBuilder builder = new StringBuilder();
        builder.append("SimpleDBPool info\r\n")
                .append("db url: ")
                .append(dbConfig.getUrl())
                .append("\r\n")
                .append("current connection size: ")
                .append(connections.size())
                .append("\r\n")
                .append("busy connection count: ")
                .append(getBusyConnectionCount())
                .append("\r\n");
        return builder.toString();
    }

    public static void main(String[] args) throws Exception{
        String url = "jdbc:mysql://bj-cdb-efnq4klm.sql.tencentcdb.com:63070/luoziheng?serverTimezone=Asia/Shanghai";
        String user = "root";
        String passwords = "zjtzjt1069478446";
        SimpleDBPool simpleDBPool = new SimpleDBPool(new DBConfig(url, user, passwords));
        simpleDBPool.createPool();
        System.out.println(simpleDBPool.toString());
        Connection con1 = simpleDBPool.getConnection();
        Connection con2 = simpleDBPool.getConnection();
        System.out.println(simpleDBPool.toString());
        simpleDBPool.returnConnection(con1);
        simpleDBPool.returnConnection(con2);
        System.out.println(simpleDBPool.toString());
        simpleDBPool.close();
    }
}
