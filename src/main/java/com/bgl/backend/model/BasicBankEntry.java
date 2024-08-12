package com.bgl.backend.model;

import jakarta.persistence.*;
import lombok.Data;

import java.math.BigDecimal;

/**
 * @author Pengcheng Xiao
 *
 * BasicBank entity, a subtype of Entry entity, has own specific additional fields
 */
@Data
@Entity
@Table (name = "TBasicBankEntry")
@PrimaryKeyJoinColumn (name = "id")
public class BasicBankEntry extends Entry{

        public BasicBankEntry(){
                this.setEntryType("BasicBank");
        }

        @Column(name = "field1")
        private String field1;

        @Column (name = "field2")
        private BigDecimal field2;
}