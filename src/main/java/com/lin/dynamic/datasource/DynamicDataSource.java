package com.lin.dynamic.datasource;

import com.lin.dynamic.dto.DataSourceFlagEnum;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Primary;
import org.springframework.jdbc.datasource.lookup.AbstractRoutingDataSource;
import org.springframework.stereotype.Component;

import javax.sql.DataSource;
import java.util.HashMap;
import java.util.Map;

@Component
@Primary    // 将该bean设置为主要注入bean，防止容器中存在多个相同类型的bean时spring不知道取那个而报错。
public class DynamicDataSource extends AbstractRoutingDataSource {

    //  当前使用的数据源标识，为了动态判断使用那个数据源
    public static ThreadLocal<Object> flag = new ThreadLocal<>();

    @Autowired
    private DataSource dataSource1;
    @Autowired
    private DataSource dataSource2;


    @Override
    public void afterPropertiesSet() {
        // 初始化bean，bean注入到容器时的初始化操作
        // 为targetDataSources初始化所有数据源
        Map<Object,Object> targetDataSource = new HashMap<>();
        targetDataSource.put(DataSourceFlagEnum.W,dataSource1);
        targetDataSource.put(DataSourceFlagEnum.R,dataSource2);
        super.setTargetDataSources(targetDataSource);

        // 为defaultTargetDataSource 设置默认的数据源
        super.setDefaultTargetDataSource(dataSource1);
        super.afterPropertiesSet();
    }

    @Override
    protected Object determineCurrentLookupKey() {  // 获取数据源的标识
        return flag.get();
    }


}
