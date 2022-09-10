package com.lin.dynamic.entity;

import lombok.Data;

import java.io.Serializable;

@Data
public class Person implements Serializable {
    private static final long serialVersionUID = 1L;
    private int id;
    private String name;
}
