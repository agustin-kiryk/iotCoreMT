package com.madreTierra.dto;

import com.madreTierra.enumeration.RoleName;
import lombok.Data;

import java.util.Date;
@Data
public class UserRequestDto {

    private Long id;
    private String firstName;
    private String lastName;
    private String email;
    private RoleName role;
    private Date creationDate;
    private Date updateDate;
    private String machineId;
    private String image;
    private String password;
    private String adress;
    private Double cost;
    private String district;
    private String idientifier;
    private String phone;
    private String status;


}
