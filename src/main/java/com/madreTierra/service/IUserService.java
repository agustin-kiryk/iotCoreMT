package com.madreTierra.service;

import com.madreTierra.dto.UserDTO;
import com.madreTierra.dto.UserRequestDto;

import java.util.List;

public interface IUserService {

    UserDTO getUserId(Long id);

    List<UserDTO> listAllUsers();

    UserDTO updateUserLoged(UserRequestDto updatedDto);

    UserDTO updateUser(Long id, UserRequestDto updatedDto);
}

