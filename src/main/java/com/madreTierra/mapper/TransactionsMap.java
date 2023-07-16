package com.madreTierra.mapper;

import com.madreTierra.dto.TransactionDto;
import com.madreTierra.entity.TransactionEntity;
import org.springframework.stereotype.Component;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

@Component
public class TransactionsMap {
    public List<TransactionDto> transactionEntityList2DTO(List<TransactionEntity> transactions) {
        List<TransactionDto> transactionDtos = new ArrayList<>();
        TransactionDto transactionDto;
        for(TransactionEntity transaction: transactions){
            transactionDto = new TransactionDto();
            transactionDto.setTransactionId(transaction.getIdTransaction());
            transactionDto.setCurrency(transaction.getCurrency());
            transactionDto.setDate(LocalDate.from(transaction.getDate()));
            transactionDto.setAmount(transaction.getAmount());
            transactionDto.setDispensedWater(transaction.getDispensedWater());
            transactionDto.setMachineId(transaction.getMachineId());
            transactionDto.setIdBack(transaction.getIdBack());
            transactionDtos.add(transactionDto);
        }
        return transactionDtos;
    }
}
