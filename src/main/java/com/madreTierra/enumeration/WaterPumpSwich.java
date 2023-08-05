package com.madreTierra.enumeration;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Setter;



public enum WaterPumpSwich {
    ON ("ON"), OFF ("OFF");
    private final String pumpSwitch ;
    WaterPumpSwich(String pumpSwitch) {
        this.pumpSwitch = pumpSwitch;
    }
    public WaterPumpSwich getPumpSwitch() {
        return WaterPumpSwich.valueOf(pumpSwitch);
    }

}
