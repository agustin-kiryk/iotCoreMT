package com.madreTierra.dto;

import com.madreTierra.enumeration.LightSwitch;
import com.madreTierra.enumeration.ValveFill;
import com.madreTierra.enumeration.ValveWash;
import com.madreTierra.enumeration.WaterPumpSwich;
import lombok.Data;

@Data
public class MachineRequestDTO {
    private Long id;
    private String currency;
    private LightSwitch light;
    private String machineId;
    private Double price;
    private String status;
    private ValveFill valveFill;
    private ValveWash valveWash;
    private WaterPumpSwich waterPumpSwich;
    private Long userId;
}
