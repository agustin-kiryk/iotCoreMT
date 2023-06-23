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

}
