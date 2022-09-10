package com.lin.dynamic.datasource;

import com.lin.dynamic.dto.DataSourceFlagEnum;
import org.apache.ibatis.cache.CacheKey;
import org.apache.ibatis.executor.Executor;
import org.apache.ibatis.mapping.BoundSql;
import org.apache.ibatis.mapping.MappedStatement;
import org.apache.ibatis.mapping.SqlCommandType;
import org.apache.ibatis.plugin.Interceptor;
import org.apache.ibatis.plugin.Intercepts;
import org.apache.ibatis.plugin.Invocation;
import org.apache.ibatis.plugin.Signature;
import org.apache.ibatis.session.ResultHandler;
import org.apache.ibatis.session.RowBounds;

@Intercepts({
        @Signature(type = Executor.class,method = "update",args = {MappedStatement.class,Object.class}),
        @Signature(type = Executor.class,method = "query",args = {MappedStatement.class,Object.class, RowBounds.class, ResultHandler.class}),
        @Signature(type = Executor.class,method = "query",args = {MappedStatement.class,Object.class,RowBounds.class,ResultHandler.class, CacheKey.class, BoundSql.class})
})
public class DynamicDataSourcePlugin implements Interceptor {

    @Override
    public Object intercept(Invocation invocation) throws Throwable {
        // 获取当前方法（update，query）所有参数
        Object[] objects = invocation.getArgs();
        // MappedStatement封装了SQL和SQL属性
        MappedStatement ms = (MappedStatement) objects[0];
        if (ms.getSqlCommandType().equals(SqlCommandType.SELECT)){
            DynamicDataSource.flag.set(DataSourceFlagEnum.R);
        }else {
            DynamicDataSource.flag.set(DataSourceFlagEnum.W);
        }
        return invocation.proceed();
    }

}
