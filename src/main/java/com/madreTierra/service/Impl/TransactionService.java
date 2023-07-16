package com.madreTierra.service.Impl;

import com.madreTierra.dto.TransactionDto;
import com.madreTierra.entity.MachinEntity;
import com.madreTierra.entity.TransactionEntity;
import com.madreTierra.entity.UserEntity;
import com.madreTierra.mapper.TransactionsMap;
import com.madreTierra.repository.MachineRepository;
import com.madreTierra.repository.TransactionRepository;
import com.madreTierra.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class TransactionService {
    @Autowired
    MachineRepository machineRepository;
    @Autowired
    TransactionRepository transactionRepository;
    @Autowired
    TransactionsMap transactionsMap;
    @Autowired
    UserRepository userRepository;

    public List<TransactionDto> transactionsByMachine(String machineId) {
        MachinEntity machine = machineRepository.findByMachineId(machineId);
        List<TransactionDto> txResponse = null;
        if (machine != null) {
            List<TransactionEntity> transactions = transactionRepository.findAllByMachineId(machineId);
            txResponse = transactionsMap.transactionEntityList2DTO(transactions);
        }
        return txResponse;
    }

    public List<TransactionDto> transactionByUserLogin(){
        String email = SecurityContextHolder.getContext().getAuthentication().getName();
        List<MachinEntity> userMachines = userRepository.findByEmail(email).getMachines();
        List<TransactionDto> allTransactions = new ArrayList<>();
        for(MachinEntity machine : userMachines){
            String machineId = machine.getMachineId().toString();
            List<TransactionEntity> transactions = transactionRepository.findAllByMachineId(machineId);
            List<TransactionDto> machineTransactions = transactionsMap.transactionEntityList2DTO(transactions);
            allTransactions.addAll(machineTransactions);
        }
        return allTransactions;
    }
}
