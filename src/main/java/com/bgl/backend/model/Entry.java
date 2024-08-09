package com.bgl.backend.model;

import lombok.Data;

import java.math.BigDecimal;

@Data
public class Entry {
    private Long id;
    private BigDecimal amount;
    private BigDecimal gstAmount;
}