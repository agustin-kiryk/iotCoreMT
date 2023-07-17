package com.madreTierra.service.Impl;

import com.madreTierra.dto.MachineDTO;
import com.madreTierra.dto.TransactionDto;
import com.madreTierra.entity.MachinEntity;
import com.madreTierra.entity.UserEntity;
import com.madreTierra.mapper.MachineMap;
import com.madreTierra.repository.MachineRepository;
import com.madreTierra.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class MachineService {
    @Autowired
    MachineRepository machineRepository;
    @Autowired
    MachineMap machineMap;
    @Autowired
    UserRepository userRepository;
    public List<MachineDTO> getMachineLoged() {
        String mail = SecurityContextHolder.getContext().getAuthentication().getName();
        List<MachinEntity> userMachines = userRepository.findByEmail(mail).getMachines();
        List<MachineDTO> machinesDTO = machineMap.entityList2DTOList(userMachines);
        return machinesDTO;
    }

    public List<MachineDTO> getAllMachines() {
        List<MachinEntity> machines = machineRepository.findAll();
        List<MachineDTO> listDtos = machineMap.entityList2DTOList(machines);
        return listDtos;
    }

    public MachineDTO getMachineByIdS(String machineId){
        MachinEntity machinEntity = machineRepository.findByMachineId(machineId);
        MachineDTO machineDTO = new MachineDTO();
        if(machinEntity != null){
            machineDTO = machineMap.machineEntity2DTO(machinEntity);
        }
        return machineDTO;
    }
}
