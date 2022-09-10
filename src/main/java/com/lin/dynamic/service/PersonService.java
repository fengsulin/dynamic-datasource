package com.lin.dynamic.service;

import com.lin.dynamic.entity.Person;

import java.util.List;

public interface PersonService {
    List<Person> list();
    void save(Person person);
}
