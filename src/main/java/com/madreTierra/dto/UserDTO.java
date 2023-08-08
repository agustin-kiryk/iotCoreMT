package com.madreTierra.dto;

import com.madreTierra.enumeration.RoleName;
import lombok.Data;

import java.util.Date;

@Data
public class UserDTO {

    private Long id;
    private String firstName;
    private String lastName;
    private String email;
    private RoleName role;
    private Date creationDate;
    private Date updateDate;
    private String machineId;
    private String phone;
    private Double dispensedWater;
    private Double cost;
    private String status;
    private String adress;
    private long machinesTotals;
    private String district;
    private String document;
    private String image;

}
