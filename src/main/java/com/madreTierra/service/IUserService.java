package com.madreTierra.service;

import com.madreTierra.dto.UserDTO;

import java.util.List;

public interface IUserService {

    UserDTO getUserId(Long id);

    List<UserDTO> listAllUsers();
}

