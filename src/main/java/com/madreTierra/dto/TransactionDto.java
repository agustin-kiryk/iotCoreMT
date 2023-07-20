package com.madreTierra.dto;

import lombok.Data;

import java.time.LocalDate;

@Data
public class TransactionDto {

    private Double amount;
    private String currency;
    private LocalDate date;
    private Double dispensedWater;
    private Long transactionId;
    private Long id;
    private String machineId;
    private Long machineIdBack;


}
