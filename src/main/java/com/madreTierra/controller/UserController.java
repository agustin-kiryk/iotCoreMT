package com.madreTierra.controller;

import com.madreTierra.dto.MachineDTO;
import com.madreTierra.dto.UserDTO;
import com.madreTierra.dto.UserRequestDto;
import com.madreTierra.service.IUserService;
import com.madreTierra.service.Impl.MachineService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@CrossOrigin(origins= "*")
@RequestMapping("/user")
public class UserController {
    @Autowired
    IUserService userService;
    @Autowired
    MachineService machineService;
    @GetMapping("/{id}")
    public ResponseEntity<UserDTO> getUserId(@PathVariable Long id){
        UserDTO response = userService.getUserId(id);
        return ResponseEntity.ok(response);
    }

    @GetMapping("/all")
    public ResponseEntity<List<UserDTO>> getAll()
    {
        List<UserDTO> users= userService.listAllUsers();
        return ResponseEntity.ok().body(users);
    }
    @GetMapping("/allMachines")
    public ResponseEntity<List<MachineDTO>> getAllMachines(){
        return ResponseEntity.ok().body(machineService.getAllMachines());
    }

    @PutMapping("/userLoged/{id}")
    public ResponseEntity<UserDTO> updateUserLoged(@RequestParam UserRequestDto updatedDto) {
        UserDTO dto = userService.updateUserLoged(updatedDto);
        return ResponseEntity.ok().body(dto);
    }

    @PutMapping("/edit/{id}")
    public ResponseEntity<UserDTO> updateUser(@PathVariable Long id, @RequestParam UserRequestDto updatedDto) {
        UserDTO dto = userService.updateUser(id, updatedDto);
        return ResponseEntity.ok().body(dto);
    }



}
