package com.madreTierra.controller;

import com.madreTierra.dto.TransactionDto;
import com.madreTierra.service.Impl.TransactionService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/transactions")
public class TransactionsController {
    @Autowired
    TransactionService transactionService;

    @GetMapping("/{machineId}")
    public ResponseEntity<List<TransactionDto>> getTxByMachine(@PathVariable String machineId){
        List<TransactionDto> txList = transactionService.transactionsByMachine(machineId);
        return ResponseEntity.ok().body(txList);
    }

    @GetMapping("/machineUserLogin")
    public ResponseEntity<List<TransactionDto>> getTxsByUserLogin(){
        return ResponseEntity.ok().body(transactionService.transactionByUserLogin());
    }

}
