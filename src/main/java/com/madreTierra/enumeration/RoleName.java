package com.madreTierra.enumeration;

public enum RoleName {
    ADMIN("ADMIN"), USER("USER");
    private final String name ;
    RoleName(String name) {
        this.name = name;
    }
    public RoleName getName() {
        return RoleName.valueOf(name);
    }
}
