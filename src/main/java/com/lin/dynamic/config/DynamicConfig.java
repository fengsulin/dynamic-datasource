//package com.lin.dynamic.config;
//
//import com.alibaba.druid.spring.boot.autoconfigure.DruidDataSourceBuilder;
//import org.apache.ibatis.session.SqlSessionFactory;
//import org.mybatis.spring.SqlSessionFactoryBean;
//import org.mybatis.spring.annotation.MapperScan;
//import org.springframework.boot.context.properties.ConfigurationProperties;
//import org.springframework.context.annotation.Bean;
//import org.springframework.context.annotation.Configuration;
//import org.springframework.context.annotation.Primary;
//import org.springframework.core.io.support.PathMatchingResourcePatternResolver;
//import org.springframework.jdbc.datasource.DataSourceTransactionManager;
//import org.springframework.transaction.support.TransactionTemplate;
//
//import javax.sql.DataSource;
//
///**
// * 这里指定mapper接口和引用的sqlSessionFactory名称
// */
//@Configuration
//@MapperScan(
//        basePackages = "com.lin.dynamic.mapper.r",
//        sqlSessionFactoryRef = "rSqlSessionFactory"
//)
//public class DynamicConfig {
//
//    @Bean
//    @ConfigurationProperties(prefix = "spring.datasource.datasource1")
//    public DataSource dataSource1(){
//        return DruidDataSourceBuilder.create().build();
//    }
//
//    @Bean
//    @Primary
//    public SqlSessionFactory rSqlSessionFactory() throws Exception {
//        final SqlSessionFactoryBean sessionFactoryBean = new SqlSessionFactoryBean();
//        // 指定主库
//        sessionFactoryBean.setDataSource(dataSource1());
//        // 指定主库对应的mapper.xml文件
//        sessionFactoryBean.setMapperLocations(new PathMatchingResourcePatternResolver()
//        .getResource("classpath:mapper/r/*.xml"));
//        return sessionFactoryBean.getObject();
//    }
//
//    @Bean
////    @Primary
//    public DataSourceTransactionManager wTransactionManager(){
//        // spring一次只能使用一个事务管理器，所有这里用primary注解，也在注解Transactional("事务名称")
//        DataSourceTransactionManager dataSourceTransactionManager = new DataSourceTransactionManager();
//        dataSourceTransactionManager.setDataSource(dataSource1());
//        return dataSourceTransactionManager;
//    }
//
//    @Bean
//    public TransactionTemplate transactionTemplate(){
//        return new TransactionTemplate(wTransactionManager());
//    }
//}
