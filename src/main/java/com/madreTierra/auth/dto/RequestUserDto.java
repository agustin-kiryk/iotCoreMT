package com.madreTierra.auth.dto;

import com.madreTierra.dto.MachinDTO;
import com.madreTierra.dto.MachineDTO;
import com.madreTierra.enumeration.RoleName;
import lombok.Data;

import java.util.Date;
import java.util.List;

@Data
public class RequestUserDto {
    private Boolean preCheck;
    private Long id;
    private String firstName;
    private String lastName;
    private String email;
    private String password;
    private RoleName role;
    private Date creationDate;
    private String jwt;
    private Date updateDate;
}
