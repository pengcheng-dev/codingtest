package com.bgl.backend.model;

import jakarta.persistence.*;
import lombok.Data;

/**
 * @author Pengcheng Xiao
 *
 * Account entity
 */
@Data
@Entity
@Table (name = "TAccount")
public class Account {

    @Id
    @GeneratedValue (strategy = GenerationType.IDENTITY)
    @Column (name = "id")
    private Long id;

    @Column (name = "accountId", nullable = false)
    private Long accountID;

    @Column (name = "code")
    private String code;

    @Column (name = "name")
    private String name;

    @Column (name = "accountClass")
    private String accountClass;

    @Column (name = "accountType")
    private String accountType;

    @ManyToOne
    @JoinColumn(name = "pid", nullable = true)
    private Account parentAccount;
}
