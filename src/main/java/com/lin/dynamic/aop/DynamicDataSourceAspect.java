package com.lin.dynamic.aop;

import com.lin.dynamic.annotation.DataSourceAnnotation;
import com.lin.dynamic.datasource.DynamicDataSource;
import com.lin.dynamic.dto.DataSourceFlagEnum;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import org.springframework.stereotype.Component;

@Component
@Aspect
public class DynamicDataSourceAspect {

    //前置或环绕
    @Before("within(com.lin.dynamic.service.imp.*) && @annotation(dataSourceAnnotation)")
    public void before(JoinPoint joinPoint, DataSourceAnnotation dataSourceAnnotation){
        DataSourceFlagEnum value = dataSourceAnnotation.value();
        DynamicDataSource.flag.set(value);
    }
}
