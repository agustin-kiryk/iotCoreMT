package com.madreTierra.dto;

import lombok.Data;

@Data
public class MonthlyMachineSummaryDto {
    private String machineId;
    private int month;
    private int year;
    private double totalAmount;
    private double totalWaterDispensed;
    private Double cost;
    private Double revenue;
    private String Status;
}
