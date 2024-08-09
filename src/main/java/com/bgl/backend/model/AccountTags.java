package com.bgl.backend.model;

import lombok.Data;

@Data
public class AccountTags {
    private Long id;
    private String name;
    private String color;
    private String type;
    private Boolean isSystemLabel;
    private String code;
    private Integer itemOrder;
}
