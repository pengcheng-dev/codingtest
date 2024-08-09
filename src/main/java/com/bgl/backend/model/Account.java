package com.bgl.backend.model;

import lombok.Data;

@Data
public class Account {
    private Long id;
    private Long accountID;
    private String code;
    private String name;
    private String accountClass;
    private String accountType;
    private Account parentAccount;
}
