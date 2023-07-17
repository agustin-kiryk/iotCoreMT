package com.madreTierra.service.Impl;

import com.madreTierra.dto.UserDTO;
import com.madreTierra.entity.UserEntity;
import com.madreTierra.exception.IdNotFound;
import com.madreTierra.mapper.UserMap;
import com.madreTierra.repository.UserRepository;
import com.madreTierra.service.IUserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.GetMapping;

import java.util.List;

@Service
public class UserService implements IUserService {
    @Autowired
    UserRepository userRepository;
    @Autowired
    UserMap userMap;

    @Autowired
    UserService userService;
    @Override
    public UserDTO getUserId(Long id) {
        UserEntity user = userRepository.findById(id).orElseThrow(
                ()-> new IdNotFound("El id seleccionado no existe en la base de datos"));
        UserDTO response = userMap.userEntity2DTO(user);
        return response;
    }

    @Override
    public List<UserDTO> listAllUsers() {
        return userMap.userEntityList2DtoList(userRepository.findAll());
    }

    @GetMapping()
    public ResponseEntity<List<UserDTO>> getAll()
    {
        List<UserDTO> users= userService.listAllUsers();
        return ResponseEntity.ok().body(users);
    }



}
