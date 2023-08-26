package com.madreTierra.controller;

import com.madreTierra.dto.MachineDTO;
import com.madreTierra.dto.MachineEditDTO;
import com.madreTierra.dto.MachineRequestDTO;
import com.madreTierra.dto.StatsWidgetUserDTO;
import com.madreTierra.service.Impl.MachineService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/machines")
@CrossOrigin(origins= "*")
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


    @GetMapping("/statsWidgetUser")
    public ResponseEntity<List<StatsWidgetUserDTO>> stastUser(){
        return ResponseEntity.ok().body(machineService.statsUserwidget());
    }
    @PostMapping("/new")
    public ResponseEntity<MachineRequestDTO> newMachine(@RequestBody MachineRequestDTO machineRequestDTO){
        return ResponseEntity.ok().body(machineService.newMachine(machineRequestDTO));
    }

    @DeleteMapping("/delete/{machineId}")
    public ResponseEntity<String> deleteMachine(@PathVariable String machineId){
        machineService.deleteMachine(machineId);
        return ResponseEntity.ok("maquina borrada correctamente " + machineId);
    }
    @PatchMapping("/edit/{machineId}")
    public ResponseEntity<MachineEditDTO> editMachine(@PathVariable String machineId, @RequestBody MachineEditDTO machineRequestDTO){
        MachineEditDTO response = machineService.editMachine(machineId,machineRequestDTO);
        return ResponseEntity.ok(response);
    }


}
