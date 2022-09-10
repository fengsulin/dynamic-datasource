package com.lin.dynamic.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.lin.dynamic.entity.Person;
import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;

import java.util.List;

@Mapper
public interface PersonMapper extends BaseMapper<Person> {

    @Select({"SELECT * FROM person"})
    List<Person> list();

    @Insert({"INSERT INTO person(name) VALUES (#{name})"})
    void save( Person person);
}
