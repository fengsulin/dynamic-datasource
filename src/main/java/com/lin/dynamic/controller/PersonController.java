package com.lin.dynamic.controller;

import com.lin.dynamic.datasource.DynamicDataSource;
import com.lin.dynamic.dto.DataSourceFlagEnum;
import com.lin.dynamic.entity.Person;
import com.lin.dynamic.service.PersonService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
public class PersonController {

    @Autowired
    private PersonService personService;

    @GetMapping("/person/list")
    public String list(){
        // 设置选择数据源的标识
//        DynamicDataSource.flag.set(DataSourceFlagEnum.R);
        List<Person> list = personService.list();
        return list.toString();
    }

    @GetMapping("/person/save")
    public String save(){
//        DynamicDataSource.flag.set(DataSourceFlagEnum.W);
        Person person = new Person();
        person.setName("特朗普");
        personService.save(person);
        return "插入成功";
    }
}
