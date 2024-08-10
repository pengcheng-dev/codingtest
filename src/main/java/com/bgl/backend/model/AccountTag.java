package com.bgl.backend.model;

import jakarta.persistence.*;
import lombok.Data;

import java.io.Serializable;

@Data
@Entity
@Table (name = "TAccountTag")
public class AccountTag {

    @EmbeddedId
    private AccountTagId id;

    @ManyToOne
    @MapsId("accountID")
    @JoinColumn (name = "accountId")
    private Account account;

    @ManyToOne
    @MapsId("tagID")
    @JoinColumn (name = "tagId")
    private AccountTags tag;

    @Data
    @Embeddable
    private class AccountTagId implements Serializable {

        @Column (name = "accountId")
        private Long accountID;

        @Column (name = "tagId")
        private Long tagID;
    }
}
