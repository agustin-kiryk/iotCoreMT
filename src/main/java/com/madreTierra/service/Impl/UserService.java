package com.madreTierra.service.Impl;

import com.madreTierra.dto.UserDTO;
import com.madreTierra.dto.UserRequestDto;
import com.madreTierra.entity.UserEntity;
import com.madreTierra.exception.IdNotFound;
import com.madreTierra.exception.ParamNotFound;
import com.madreTierra.mapper.UserMap;
import com.madreTierra.repository.UserRepository;
import com.madreTierra.service.IUserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.GetMapping;

import java.util.Date;
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

    @Override
    public UserDTO updateUserLoged(UserRequestDto updatedDto) {
        String email = SecurityContextHolder.getContext().getAuthentication().getName();
        UserEntity user = this.userRepository.findByEmail(email);
        Long id = user.getUserId();
        UserEntity userEntity= userRepository.findById(id).orElseThrow(
                ()-> new ParamNotFound("User ID Invalid"));
        if(!userEntity.equals(user))
            throw new ParamNotFound("User logged doesn't match with user being updated");
        userEntity.setFirstName(updatedDto.getFirstName());
        userEntity.setLastName(updatedDto.getLastName());
        userEntity.setUpdateDateTime(new Date());
        userEntity.setImage(updatedDto.getImage());
        UserEntity entitySaved= userRepository.save(userEntity);
        UserDTO result=userMap.userEntity2DTO(entitySaved);
        return result;
    }

    @Override
    public UserDTO updateUser(Long id, UserRequestDto updatedDto) {
        UserEntity userEntity= userRepository.findById(id).orElseThrow(
                ()-> new ParamNotFound("User ID Invalid"));
        userEntity.setFirstName(updatedDto.getFirstName());
        userEntity.setLastName(updatedDto.getLastName());
        userEntity.setUpdateDateTime(new Date());
        userEntity.setImage(updatedDto.getImage());
        UserEntity entitySaved= userRepository.save(userEntity);
        UserDTO result=userMap.userEntity2DTO(entitySaved);
        return result;
    }

    @Override
    public UserDTO getUserLoged() {
        String email = SecurityContextHolder.getContext().getAuthentication().getName();
        UserEntity user = this.userRepository.findByEmail(email);
        UserDTO response = userMap.userEntity2DTO(user);

        return response;
    }

    @GetMapping()
    public ResponseEntity<List<UserDTO>> getAll()
    {
        List<UserDTO> users= userService.listAllUsers();
        return ResponseEntity.ok().body(users);
    }



}
