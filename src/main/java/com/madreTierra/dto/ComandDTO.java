package com.madreTierra.dto;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.Data;

import java.util.List;
import java.util.Map;

@Data
@JsonIgnoreProperties(ignoreUnknown = true)
public class ComandDTO   {

    private String comand;
    private Map<String, Object> payload;


}
