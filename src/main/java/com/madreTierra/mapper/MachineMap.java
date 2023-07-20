package com.madreTierra.mapper;

import com.madreTierra.dto.MachineDTO;
import com.madreTierra.entity.MachinEntity;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;

@Component
public class MachineMap {


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
}
