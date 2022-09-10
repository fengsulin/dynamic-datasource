//package com.lin.dynamic.datasource;
//
//import org.springframework.beans.factory.InitializingBean;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.context.annotation.Primary;
//import org.springframework.stereotype.Component;
//
//import javax.sql.DataSource;
//import java.io.PrintWriter;
//import java.sql.Connection;
//import java.sql.SQLException;
//import java.sql.SQLFeatureNotSupportedException;
//import java.util.logging.Logger;
//
//@Component
//@Primary    // 将该bean设置为主要注入bean，防止容器中存在多个相同类型的bean时spring不知道取那个而报错。
//public class DynamicDataSourceBak1 implements DataSource, InitializingBean {
//
//    //  当前使用的数据源标识，为了动态判断使用那个数据源
//    public static ThreadLocal<String> flag = new ThreadLocal<>();
//
//    @Autowired
//    private DataSource dataSource1;
//    @Autowired
//    private DataSource dataSource2;
//
//    @Override
//    public Connection getConnection() throws SQLException {
//        if (flag.get().equals("W")){
//            return dataSource1.getConnection();
//        }else {
//            return dataSource2.getConnection();
//        }
//    }
//
//    @Override
//    public Connection getConnection(String username, String password) throws SQLException {
//        return null;
//    }
//
//    @Override
//    public PrintWriter getLogWriter() throws SQLException {
//        return null;
//    }
//
//    @Override
//    public void setLogWriter(PrintWriter out) throws SQLException {
//
//    }
//
//    @Override
//    public void setLoginTimeout(int seconds) throws SQLException {
//
//    }
//
//    @Override
//    public int getLoginTimeout() throws SQLException {
//        return 0;
//    }
//
//    @Override
//    public Logger getParentLogger() throws SQLFeatureNotSupportedException {
//        return null;
//    }
//
//    @Override
//    public <T> T unwrap(Class<T> iface) throws SQLException {
//        return null;
//    }
//
//    @Override
//    public boolean isWrapperFor(Class<?> iface) throws SQLException {
//        return false;
//    }
//
//    @Override
//    public void afterPropertiesSet() throws Exception {
//        // 初始化bean，bean注入到容器时的初始化操作
//        flag.set("W");  //  这里默认设置标识为W
//    }
//}
