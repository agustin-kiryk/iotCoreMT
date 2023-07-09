package com.madreTierra.enumeration;

public enum ValveFill {
    ON ("ON"), OFF ("OFF");
    private final String valveFill ;
    ValveFill(String valveFill) {
        this.valveFill = valveFill;
    }
    public ValveFill getValveFill() {
        return ValveFill.valueOf(valveFill);
    }
}
