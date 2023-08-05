package com.madreTierra.auth.service;

import com.madreTierra.auth.dto.*;
import com.madreTierra.entity.RoleEntity;
import com.madreTierra.entity.UserEntity;
import com.madreTierra.enumeration.RoleName;
import com.madreTierra.exception.RepeatedUsername;
import com.madreTierra.mapper.UserMap;
import com.madreTierra.repository.RoleRepository;
import com.madreTierra.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

@Service
public class UserDetailsCustomService implements UserDetailsService {

        @Autowired
    UserMap userMap;
    @Autowired
    RoleRepository iRoleRepository;
    @Autowired
    UserRepository iUserRepository;

    @Autowired
    private AuthenticationManager authenticationManager;

    @Autowired
    private JwtUtils jwtTokenUtils;



    @Override
    public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {
        UserEntity userEntity = iUserRepository.findByEmail(email);
        if (userEntity == null) {
            throw new UsernameNotFoundException("username or password not found");
        }
        return UserDetailsImpl.build(userEntity);
    }

    public ResponseUserDto save(RequestUserDto userDto) throws RepeatedUsername {
        if (iUserRepository.findByEmail(userDto.getEmail()) != null){
            throw new RepeatedUsername("Repeted Username");
        }
        UserEntity entity = this.userMap.userAuthDto2Entity(userDto);
        RoleEntity role = this.iRoleRepository.findByRoleName(RoleName.USER);
        entity.setRole(role);
        UserEntity entitySaved = this.iUserRepository.save(entity);

        ResponseUserDto responseUserDto = userMap.userAuthEntity2Dto(entitySaved);


        return responseUserDto;
    }

    public ResponseUserDto saveAdmin(RequestUserDto userDto) {

        if (iUserRepository.findByEmail(userDto.getEmail()) != null){
            throw new RepeatedUsername("Repeted Username");
        }
        UserEntity entity = this.userMap.userAuthDto2Entity(userDto);
        RoleEntity role = this.iRoleRepository.findByRoleName(RoleName.ADMIN);
        entity.setRole(role);
        UserEntity entitySaved = this.iUserRepository.save(entity);

        ResponseUserDto responseUserDto = userMap.userAuthEntity2Dto(entitySaved);


        return responseUserDto;

    }

    public AuthenticationResponse signIn(AuthenticationRequest authenticationRequest) {

        UserDetails userDetails;
        Authentication authentication = authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(
                authenticationRequest.getEmail(), authenticationRequest.getPassword()));


        userDetails = (UserDetails) authentication.getPrincipal();
        final String jwt = jwtTokenUtils.generateToken(userDetails);

        return new AuthenticationResponse(jwt);
    }

        /*
    UserDetails userDetails;

    Authentication auth = authenticationManager.authenticate(
        new UsernamePasswordAuthenticationToken(authenticationRequest.getEmail(),
            authenticationRequest.getPassword())
    );
    userDetails = (UserDetails) auth.getPrincipal();

    final String jwt = jwtTokenUtils.generateToken(userDetails);
    return ResponseEntity.ok(new AuthenticationResponse(jwt));
  }

 */
}
