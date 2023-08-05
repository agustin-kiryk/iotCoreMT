package com.madreTierra.enumeration;

public enum LightSwitch {
    ON ("ON"), OFF ("OFF");
    private final String lightSwitch ;
    LightSwitch(String lightSwitch) {
        this.lightSwitch = lightSwitch;
    }
    public LightSwitch getLightSwitch() {
        return LightSwitch.valueOf(lightSwitch);
    }
}
