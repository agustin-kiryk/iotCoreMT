package com.madreTierra.enumeration;

public enum ValveWash {
    ON ("ON"), OFF ("OFF");
    private final String valveWash ;
    ValveWash(String valveWash) {
        this.valveWash = valveWash;
    }
    public ValveWash getValveWash() {
        return ValveWash.valueOf(valveWash);
    }
}
