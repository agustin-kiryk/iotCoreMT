package com.madreTierra.auth.controller;

import com.madreTierra.auth.dto.*;
import com.madreTierra.auth.service.UserDetailsCustomService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;

@RestController
@RequestMapping("/auth")
@CrossOrigin("https://consorcio-production.up.railway.app")


public class UserAuthController {

    @Autowired
    private UserDetailsCustomService userDetailsServices;





    @PostMapping("/register")
    public ResponseEntity<ResponseUserDto> signUp(@Valid @RequestBody RequestUserDto user) {
        ResponseUserDto userRegister = this.userDetailsServices.save(user);
        return ResponseEntity.status(HttpStatus.CREATED).body(userRegister);
    }

    //Todo terminar este endpoint auth/registerAdmin, crea el admin
    @PostMapping("/registerAdmin")
    public ResponseEntity<ResponseUserDto> signUpAdmin(@Valid @RequestBody RequestUserDto user) {
        ResponseUserDto userRegister = this.userDetailsServices.saveAdmin(user);
        return ResponseEntity.status(HttpStatus.CREATED).body(userRegister);
    }


    @PostMapping("/login")
    public ResponseEntity<AuthenticationResponse> signIn(@RequestBody AuthenticationRequest authenticationRequest) {

        AuthenticationResponse response = userDetailsServices.signIn(authenticationRequest);
        return ResponseEntity.status(HttpStatus.OK).body(response);
    }


}
