package com.lin.dynamic.annotation;

import com.lin.dynamic.dto.DataSourceFlagEnum;

import java.lang.annotation.*;

@Target({ElementType.METHOD,ElementType.TYPE})
@Retention(RetentionPolicy.RUNTIME)
@Documented
public @interface DataSourceAnnotation {
    DataSourceFlagEnum value() default DataSourceFlagEnum.R;
}
