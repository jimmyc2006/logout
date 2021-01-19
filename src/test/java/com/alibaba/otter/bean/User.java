package com.alibaba.otter.bean;

import lombok.Data;

@Data
public class User {
    private Integer id;
    private String name;
    private Address address;
}
