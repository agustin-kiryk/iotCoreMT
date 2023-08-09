package com.madreTierra.dto;

import lombok.Data;

@Data
public class StatsWidgetUserDTO {
    private String machineId;
    private int daysDif;
    private int daysPercen;
    private  int percentageToFilterChange;
    private  int filterChangeMonth;

}
