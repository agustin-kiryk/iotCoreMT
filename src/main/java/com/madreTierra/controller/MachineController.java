package com.madreTierra.controller;

import com.madreTierra.dto.MachineDTO;
import com.madreTierra.dto.TransactionDto;
import com.madreTierra.service.Impl.MachineService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/machines")
public class MachineController {
    @Autowired
    MachineService machineService;
    @GetMapping("/machinesUser")
    public ResponseEntity<List<MachineDTO>> getMachineByUserLoged(){
        return ResponseEntity.ok().body(machineService.getMachineLoged());
    }

    @GetMapping("/allMachines")
    public ResponseEntity<List<MachineDTO>> getAllMachines(){
        return ResponseEntity.ok().body(machineService.getAllMachines());
    }

    @GetMapping("/{machineId}")
    public ResponseEntity<MachineDTO> getMachineById(@PathVariable String machineId){
        return ResponseEntity.ok().body(machineService.getMachineByIdS(machineId));
    }

}
