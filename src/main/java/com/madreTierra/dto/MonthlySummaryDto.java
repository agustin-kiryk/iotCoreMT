package com.madreTierra.dto;

import lombok.Data;

@Data
public class MonthlySummaryDto {
    private Long id;
    private int month;
    private int year;
    private double totalAmount;
    private double totalWaterDispensed;

}
