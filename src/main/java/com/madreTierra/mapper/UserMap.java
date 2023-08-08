package com.madreTierra.mapper;

import com.madreTierra.auth.dto.RequestUserDto;
import com.madreTierra.auth.dto.ResponseUserDto;
import com.madreTierra.dto.UserDTO;
import com.madreTierra.entity.UserEntity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;

@Component
public class UserMap {

    public UserEntity userAuthDto2Entity(RequestUserDto userDto) {
        BCryptPasswordEncoder bCryptPasswordEncoder = new BCryptPasswordEncoder();
        UserEntity userEntity = new UserEntity();
        userEntity.setFirstName(userDto.getFirstName());
        userEntity.setLastName(userDto.getLastName());
        userEntity.setEmail(userDto.getEmail());
        userEntity.setPassword(bCryptPasswordEncoder.encode(userDto.getPassword()));
        userEntity.setUpdateDateTime(userDto.getUpdateDate());
        userEntity.setCreateDateTime(userDto.getCreationDate());

        return userEntity;
    }


    public ResponseUserDto userAuthEntity2Dto(UserEntity entitySaved) {
        ResponseUserDto dto = new ResponseUserDto();
        dto.setId(entitySaved.getUserId());
        dto.setFirstName(entitySaved.getFirstName());
        dto.setLastName(entitySaved.getLastName());
        dto.setEmail(entitySaved.getEmail());
        //dto.setPassword(entitySaved.getPassword());
        dto.setRole(entitySaved.getRole().getRoleName());
        dto.setUpdateDate(entitySaved.getUpdateDateTime());
        dto.setCreationDate(entitySaved.getCreateDateTime());

        return dto;
    }

    public UserDTO userEntity2DTO(UserEntity user) {
        UserDTO dto = new UserDTO();
        dto.setId(user.getUserId());
        dto.setFirstName(user.getFirstName());
        dto.setLastName(user.getLastName());
        dto.setEmail(user.getEmail());
        dto.setRole(user.getRole().getRoleName());
        dto.setCreationDate(user.getCreateDateTime());
        dto.setUpdateDate(user.getUpdateDateTime());
        dto.setCost(user.getCost());
        dto.setAdress(user.getAdress());
        dto.setPhone(user.getPhone());
        dto.setStatus(user.getStatus());
        dto.setDistrict(user.getDistrict());
        dto.setDocument(user.getDocument());
        dto.setImage(user.getImage());
        dto.setCreationDate(user.getCreateDateTime());
        dto.setMachinesTotals(user.getMachines().stream().count());

        return dto;
    }

    public List<UserDTO> userEntityList2DtoList(List<UserEntity> entities) {

        List<UserDTO> userDtoList = new ArrayList<>();

        for (UserEntity userEntity : entities) {

            userDtoList.add(userEntity2DTO(userEntity));

        }

        return userDtoList;


    }
}
