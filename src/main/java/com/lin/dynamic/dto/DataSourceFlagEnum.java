package com.lin.dynamic.dto;

public enum DataSourceFlagEnum {
    W("W"),
    R("R");
    private String name;

    public String getName() {
        return name;
    }

    DataSourceFlagEnum(String name) {
        this.name = name;
    }
}
