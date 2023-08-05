package com.madreTierra.dto;

import com.madreTierra.entity.TransactionEntity;
import lombok.Data;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.Date;
import java.util.List;

@Data
public class TransactionDto {

    private Double amount;
    private String currency;
    private LocalDateTime date;
    private Double dispensedWater;
    private Long transactionId;
    private Long id;
    private String machineId;
    private Long machineIdBack;
    private int month;
    private int year;
    private List<TransactionDto> transactions;
    private LocalDate transactionDate;


}
