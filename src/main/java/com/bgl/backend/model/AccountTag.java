package com.bgl.backend.model;

import lombok.Data;

import java.io.Serializable;

@Data
public class AccountTag {
    private AccountTagId id;
    private Account account;
    private AccountTags tag;

    @Data
    private class AccountTagId implements Serializable {
        private Long accountID;
        private Long tagID;
    }
}
