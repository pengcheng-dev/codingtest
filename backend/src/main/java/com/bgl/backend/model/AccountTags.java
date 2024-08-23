package com.bgl.backend.model;

import jakarta.persistence.*;
import lombok.Data;

/**
 * @author Pengcheng Xiao
 *
 * AccountTags entity, not used
 */
@Data
@Entity
@Table (name = "TAccountTags")
public class AccountTags {

    @Id
    @GeneratedValue (strategy = GenerationType.IDENTITY)
    @Column (name = "id")
    private Long id;

    @Column (name = "name")
    private String name;

    @Column (name = "color")
    private String color;

    @Column (name = "type")
    private String type;

    @Column (name = "isSystemLabel")
    private Boolean isSystemLabel;

    @Column (name = "code")
    private String code;

    @Column (name = "item_order")
    private Integer itemOrder;
}
