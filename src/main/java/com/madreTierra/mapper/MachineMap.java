package com.madreTierra.mapper;

import com.madreTierra.dto.MachineDTO;
import com.madreTierra.dto.MachineEditDTO;
import com.madreTierra.dto.MachineRequestDTO;
import com.madreTierra.entity.MachinEntity;
import com.madreTierra.entity.UserEntity;
import com.madreTierra.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Component
public class MachineMap {
    @Autowired
    UserRepository userRepository;

    public List<MachineDTO> entityList2DTOList(List<MachinEntity> userMachines) {
            List<MachineDTO> dtos = new ArrayList<>();

            for (MachinEntity machine : userMachines ){
                MachineDTO dto = new MachineDTO();
                dto.setMachineId(machine.getMachineId());
                dto.setStatus(machine.getStatus());
                dto.setPrice(machine.getPrice());
                dto.setCurrency(machine.getCurrency());
                dto.setId(machine.getMachineIdIntern());
                dto.setValveFill(machine.getValveFill());
                dto.setLight(machine.getLight());
                dto.setValveWash(machine.getValveWash());
                dto.setWaterPumpSwich(machine.getWaterPump());
                dto.setLight(machine.getLight());
                dto.setUserId(machine.getUser().getUserId());
                dtos.add(dto);
            }
            return dtos;
    }

    public MachineDTO machineEntity2DTO(MachinEntity machine) {
        MachineDTO dto = new MachineDTO();
        dto.setMachineId(machine.getMachineId());
        dto.setStatus(machine.getStatus());
        dto.setPrice(machine.getPrice());
        dto.setCurrency(machine.getCurrency());
        dto.setId(machine.getMachineIdIntern());
        dto.setValveFill(machine.getValveFill());
        dto.setLight(machine.getLight());
        dto.setValveWash(machine.getValveWash());
        dto.setWaterPumpSwich(machine.getWaterPump());
        dto.setLight(machine.getLight());
        dto.setUserId(machine.getUser().getUserId());

        return dto;
    }

    public MachinEntity machineDTO2Entity(MachineRequestDTO machineRequestDTO) {
        MachinEntity machin = new MachinEntity();
        machin.setMachineId(machineRequestDTO.getMachineId());
        return machin;
    }

    public MachinEntity machineCompleteDTO2Entity(MachineRequestDTO machine) {
        MachinEntity machineEntity = new MachinEntity();

        machineEntity.setMachineId(machine.getMachineId());
        machineEntity.setStatus(machine.getStatus());
        machineEntity.setPrice(machine.getPrice());
        machineEntity.setCurrency(machine.getCurrency());
        machineEntity.setValveFill(machine.getValveFill());
        machineEntity.setLight(machine.getLight());
        machineEntity.setValveWash(machine.getValveWash());
        machineEntity.setWaterPump(machine.getWaterPumpSwich());
        machineEntity.setLight(machine.getLight());
        UserEntity user = userRepository.findByEmail(machine.getMail());
        machineEntity.setUser(user);
        machineEntity.setAdress(machine.getAdress());
        machineEntity.setComent(machine.getComent());
        machineEntity.setDetail(machine.getDetail());
        machineEntity.setDistrict(machine.getDistrict());
        machineEntity.setModel(machine.getModel());
        machineEntity.setStateAt(LocalDateTime.now());
        return machineEntity;
    }

    public MachineRequestDTO machineCompleteEntity2DTO(MachinEntity machine) {

        MachineRequestDTO dto = new MachineRequestDTO();
        dto.setMachineId(machine.getMachineId());
        dto.setStatus(machine.getStatus());
        dto.setPrice(machine.getPrice());
        dto.setCurrency(machine.getCurrency());
        dto.setId(machine.getMachineIdIntern());
        dto.setValveFill(machine.getValveFill());
        dto.setLight(machine.getLight());
        dto.setValveWash(machine.getValveWash());
        dto.setWaterPumpSwich(machine.getWaterPump());
        dto.setLight(machine.getLight());
        dto.setUserId(machine.getUser().getUserId());
        dto.setAdress(machine.getAdress());
        dto.setComent(machine.getComent());
        dto.setDetail(machine.getDetail());
        dto.setDistrict(machine.getDistrict());
        dto.setModel(machine.getModel());
        dto.setStateAt(LocalDateTime.now());

        return dto;
    }

    public MachineEditDTO machineEntityEdit2DTO(MachinEntity machine) {
        MachineEditDTO dto = new MachineEditDTO();
        dto.setPrice(machine.getPrice());
        dto.setAdress(machine.getAdress());
        dto.setComent(machine.getComent());
        dto.setDistrict(machine.getDistrict());

        return dto;
    }
}
