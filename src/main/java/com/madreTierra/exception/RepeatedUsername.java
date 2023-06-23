package com.madreTierra.exception;

public class RepeatedUsername extends RuntimeException{
    public RepeatedUsername(String error) {
        super(error);
    }
}

