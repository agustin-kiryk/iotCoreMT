package com.madreTierra.controller;

import com.madreTierra.dto.TransactionDto;
import com.madreTierra.service.Impl.TransactionService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/transactions")
@CrossOrigin("*")
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

    @GetMapping("/user/{userId}")
    public ResponseEntity<List<TransactionDto>> getTxById(@PathVariable Long userId){
        List<TransactionDto> txList = transactionService.transactionsByUserId(userId);
        return ResponseEntity.ok().body(txList);
    }

}
