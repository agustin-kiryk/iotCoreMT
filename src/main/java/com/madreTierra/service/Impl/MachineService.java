package com.madreTierra.service.Impl;

import com.madreTierra.dto.*;
import com.madreTierra.entity.MachinEntity;
import com.madreTierra.entity.UserEntity;
import com.madreTierra.exception.ParamNotFound;
import com.madreTierra.mapper.MachineMap;
import com.madreTierra.repository.MachineRepository;
import com.madreTierra.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.time.LocalDate;
import java.time.Period;
import java.util.ArrayList;
import java.util.Date;
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

    public MachineDTO getMachineByIdS(String machineId) {
        MachinEntity machinEntity = machineRepository.findByMachineId(machineId);
        MachineDTO machineDTO = new MachineDTO();
        if (machinEntity != null) {
            machineDTO = machineMap.machineEntity2DTO(machinEntity);
        }
        return machineDTO;
    }

    public MachineRequestDTO newMachine(MachineRequestDTO machineRequestDTO) {

        MachinEntity machine = machineRepository.findByMachineId(machineRequestDTO.getMachineId());
        if (machine != null) {
            throw new ParamNotFound("el nombre o id de maquina ya existe");
        }
        machine = machineMap.machineCompleteDTO2Entity(machineRequestDTO);
        MachinEntity machineSaved = machineRepository.save(machine);
        MachineRequestDTO response = machineMap.machineCompleteEntity2DTO(machineSaved);
        return response;
    }

    public MachinEntity getMachineById(String machineId) {
        MachinEntity machine = machineRepository.findByMachineId(machineId);
        return machine;
    }

    public List<StatsWidgetUserDTO> statsUserwidget() {
        String mail = SecurityContextHolder.getContext().getAuthentication().getName();
        UserEntity user = userRepository.findByEmail(mail);

        LocalDate currentDate = LocalDate.now();
        LocalDate startDate = user.getStartAt();

        int totalDaysInMonth = currentDate.lengthOfMonth();
        Period period = Period.between(startDate, currentDate);
        int daysPassed = period.getDays() + period.getMonths() * totalDaysInMonth;
        int daysRemaining = totalDaysInMonth - daysPassed;

        int percentageCompletedCicle = (daysPassed * 100) / totalDaysInMonth;

        List<MachinEntity> userMachines = user.getMachines();

        List<StatsWidgetUserDTO> statsList = new ArrayList<>();
        for (MachinEntity machine : userMachines) {
            StatsWidgetUserDTO dto = new StatsWidgetUserDTO();
            dto.setMachineId(machine.getMachineId());
            dto.setDaysPercen(percentageCompletedCicle);
            dto.setDaysDif(daysRemaining);


            // Calcular el porcentaje que falta para completar la vida útil del filtro (8 meses)
            int percentageRemainingToFilterChange = 100 - percentageCompletedCicle;
            dto.setPercentageToFilterChange(percentageRemainingToFilterChange);

            // Calcular el mes para el cambio de filtro (8 meses después del inicio)
            LocalDate filterChangeMonth = startDate.plusMonths(8);
            int filterChangeMonthValue = filterChangeMonth.getMonthValue();
            dto.setFilterChangeMonth(filterChangeMonthValue);

            statsList.add(dto);
        }

        return statsList;
    }

    @Transactional
    public void deleteMachine(String machineId) {
        MachinEntity machine = machineRepository.findByMachineId(machineId);
        if (machine != null) {
            machineRepository.deleteById(machine.getMachineIdIntern());
        } else throw new ParamNotFound("El id de maquina es incorrecto");
    }

    public MachineEditDTO editMachine(String machineId, MachineEditDTO machineRequestDTO) {
        MachinEntity machineEntity= machineRepository.findByMachineId(machineId);
        if (machineEntity != null){
        machineEntity.setAdress(machineRequestDTO.getAdress());
        machineEntity.setComent(machineRequestDTO.getComent());
        machineEntity.setDistrict(machineRequestDTO.getDistrict());
        machineEntity.setPrice(machineRequestDTO.getPrice());
            if (machineRequestDTO.getUserId() != null) {
               // addMachineUser; // Todo : terminar metodo
            } 
        MachinEntity entitySaved= machineRepository.save(machineEntity);
        MachineEditDTO result= machineMap.machineEntityEdit2DTO(entitySaved);
        return result;
        }else throw new ParamNotFound("el id de maquina no existe");
        
    }
    private static void addMachineUser(){
        
    }
}

