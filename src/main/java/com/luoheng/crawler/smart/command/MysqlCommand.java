package com.luoheng.crawler.smart.command;

import com.luoheng.crawler.model.Bean;
import com.luoheng.crawler.smart.resource.MysqlResource;
import com.luoheng.crawler.smart.exception.NoSuchAttributeException;
import com.luoheng.crawler.util.mysql.DBConfig;
import com.luoheng.crawler.util.mysql.SimpleDBPool;
import com.luoheng.crawler.util.mysql.WaitTimeOutException;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

/**
 * @description:
 * @author: lzh
 * @create: 2019-09-09 16:24
 **/
public class MysqlCommand extends Command<MysqlCommand> {
    private MysqlResource mysqlResource;

    public MysqlCommand(MysqlResource mysqlResource) {
        this.mysqlResource = mysqlResource;
    }

    public long getCount(Object... args) throws SQLException, WaitTimeOutException {
        SimpleDBPool simpleDBPool = mysqlResource.getResource();
        int count = -1;
        String sql = (String)args[0];
        Connection connection = simpleDBPool.getConnection();
        PreparedStatement statement = connection.prepareStatement(sql);
        for (int i = 1; i < args.length; i++){
            statement.setObject(i, args[i]);
        }
        ResultSet resultSet = statement.executeQuery();
        if (resultSet.next()){
            count = resultSet.getInt(1);
        }
        simpleDBPool.returnConnection(connection);
        return count;
    }

    public List<Bean> query(Object... args) throws SQLException, WaitTimeOutException{
        SimpleDBPool simpleDBPool = mysqlResource.getResource();
        List<Bean> result = new ArrayList<>();
        String sql = (String)args[0];
        Connection connection = simpleDBPool.getConnection();
        PreparedStatement statement = connection.prepareStatement(sql);
        for (int i = 1; i < args.length; i++){
            statement.setObject(i, args[i]);
        }
        ResultSet resultSet = statement.executeQuery();
        int index = 0;
        while (resultSet.next()){
            Bean bean = new Bean();
            try {
                //bean.setAttributeValue(index, resultSet.getObject(index + 1));
            }catch (NoSuchAttributeException e){
                e.printStackTrace();
                System.exit(-1);
            }
            result.add(bean);
        }
        simpleDBPool.returnConnection(connection);
        return result;
    }

    public boolean execute(Object[] args) throws SQLException, WaitTimeOutException{
        SimpleDBPool simpleDBPool = mysqlResource.getResource();
        String sql = (String)args[0];
        Connection connection = simpleDBPool.getConnection();
        PreparedStatement statement = connection.prepareStatement(sql);
        for (int i = 1; i < args.length; i++){
            statement.setObject(i, args[i]);
        }
        boolean b = statement.execute();
        simpleDBPool.returnConnection(connection);
        return b;
    }

    public void close(){
        if (mysqlResource != null)
            mysqlResource.close();
    }

    public static void main(String[] args) throws Exception{
        String url = "jdbc:mysql://bj-cdb-efnq4klm.sql.tencentcdb.com:63070/luoziheng?serverTimezone=Asia/Shanghai";
        String user = "root";
        String passwords = "zjtzjt1069478446";
        DBConfig dbConfig = new DBConfig(url, user, passwords);
        MysqlCommand mysqlCommand = new MysqlCommand(new MysqlResource("res_mysql", dbConfig));
        System.out.println(mysqlCommand.execute(mysqlCommand, "getCount", "SELECT COUNT(*) FROM article WHERE author=?;", "关伟"));
    }

}
