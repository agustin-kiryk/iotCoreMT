package com.madreTierra.dto;

import lombok.Data;

@Data
public class TransactionSummaryDto {
    private int month;
    private int year;
    private double totalAmount;
    private double totalWaterDispensed;
    private double revenue;
    private double pending;
}
