package com.madreTierra.controller;

import com.madreTierra.dto.MachineDTO;
import com.madreTierra.dto.UserDTO;
import com.madreTierra.dto.UserRequestDto;
import com.madreTierra.entity.MachinEntity;
import com.madreTierra.entity.UserEntity;
import com.madreTierra.repository.UserRepository;
import com.madreTierra.service.IUserService;
import com.madreTierra.service.Impl.MachineService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.util.Date;
import java.util.List;

@RestController
@CrossOrigin(origins= "*")
@RequestMapping("/user")
public class UserController {
    @Autowired
    IUserService userService;
    @Autowired
    MachineService machineService;
    @Autowired
    UserRepository userRepository;

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

    @GetMapping("/userLogin")
    public ResponseEntity<UserDTO> getUserLoged(){
        UserDTO response = userService.getUserLoged();
        return ResponseEntity.ok(response);
    }

    @PostMapping("/{userId}/machines/{machineId}")
    public ResponseEntity<UserEntity> addMachineToUser(@PathVariable Long userId, @PathVariable String machineId) {
        UserEntity user = userService.getUserById(userId);
        MachinEntity machine = machineService.getMachineById(machineId);

        if (user == null || machine == null) {
            return ResponseEntity.notFound().build();
        }
        // Asociar la máquina al usuario
        user.addMachine(machine);
        user.setStartAt(LocalDate.now());
        userRepository.save(user);

        return ResponseEntity.ok(user);
    }

    @DeleteMapping("/{userId}/machines/{machineId}")
    public ResponseEntity<UserEntity> removeMachineFromUser(@PathVariable Long userId, @PathVariable String machineId) {
        UserEntity user = userService.getUserById(userId);
        MachinEntity machine = machineService.getMachineById(machineId);

        if (user == null || machine == null) {
            return ResponseEntity.notFound().build();
        }

        // Desasociar la máquina del usuario
        user.removeMachine(machine);
        userRepository.save(user); // Guardar los cambios en la base de datos

        return ResponseEntity.ok(user);
    }



}
