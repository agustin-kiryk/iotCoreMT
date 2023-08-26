package com.madreTierra.auth.dto;

import com.madreTierra.dto.MachinDTO;
import com.madreTierra.enumeration.RoleName;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import java.util.Date;
import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ResponseUserDto {

    private Long id;
    private String firstName;
    private String lastName;
    private String email;
    private String password;
    private RoleName role;
    private List<MachinDTO> machines;
    private Date creationDate;
    private String jwt;
    private Date updateDate;
    private String machineId;
    private String phone;
    private Double cost;
    private String status;
    private String adress;
    private String district;
    private String document;
    private String image;

}
