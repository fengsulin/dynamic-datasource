//package com.lin.dynamic.config;
//
//import com.lin.dynamic.datasource.DynamicDataSourcePlugin;
//import org.apache.ibatis.plugin.Interceptor;
//import org.apache.ibatis.session.SqlSessionFactory;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.context.annotation.Bean;
//import org.springframework.context.annotation.Configuration;
//
//import javax.annotation.PostConstruct;
//import java.util.List;
//
//@Configuration
//public class DynamicPluginConfig {
//
////    @Autowired
////    private List<SqlSessionFactory> sqlSessionFactoryList;
////
////    @PostConstruct
////    public void addDataSourcePlugin(){
////        DynamicDataSourcePlugin dynamicDataSourcePlugin = new DynamicDataSourcePlugin();
////        for (SqlSessionFactory sqlSessionFactory : sqlSessionFactoryList){
////            sqlSessionFactory.getConfiguration().addInterceptor(dynamicDataSourcePlugin);
////        }
////    }
//
//    /**
//     * 将自定义的Interceptor注入到spring容器，MybatisPlusAutoConfiguration 插件自定配置类在程序启动时会到容器中找到注入的拦截器
//     */
//    @Bean
//    public Interceptor dynamicDataSourcePlugin(){
//        return new DynamicDataSourcePlugin();
//    }
//}
