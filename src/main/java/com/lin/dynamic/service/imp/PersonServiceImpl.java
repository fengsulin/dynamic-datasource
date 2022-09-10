package com.lin.dynamic.service.imp;

import com.lin.dynamic.annotation.DataSourceAnnotation;
import com.lin.dynamic.dto.DataSourceFlagEnum;
import com.lin.dynamic.entity.Person;
import com.lin.dynamic.mapper.PersonMapper;
import com.lin.dynamic.service.PersonService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class PersonServiceImpl implements PersonService {

    @Autowired
    private PersonMapper personMapper;

    @Override
    @DataSourceAnnotation(DataSourceFlagEnum.R)
    public List<Person> list() {
        return personMapper.list();
    }

    @Override
    @DataSourceAnnotation(DataSourceFlagEnum.W)
    public void save(Person person) {
        personMapper.save(person);
    }
}
