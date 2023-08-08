package com.madreTierra.controller;

import com.madreTierra.dto.*;
import com.madreTierra.entity.TransactionEntity;
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

    @GetMapping("/all")
    public ResponseEntity<List<TransactionDto>> allTxs(){
        List<TransactionDto> txList = transactionService.txsAll();
        return ResponseEntity.ok().body(txList);
    }

    @GetMapping("/byUserLoginAndMonth")
    public List<TransactionDto> getTransactionsByUserLoginAndMonth() {
        return transactionService.transactionsByUserLoginAndMonth();
    }

    @GetMapping("/byUserLoginSeparateByMonth")
    public ResponseEntity<List<TransactionDto>> getTransactionsByUserLoginSeparateByMonth() {
        return ResponseEntity.ok().body(transactionService.transactionsByUserLoginSeparateByMonth());
    }

    @GetMapping("/monthlySummaryByUserLogin")
    public ResponseEntity<List<MonthlyMachineSummaryDto>> getMonthlySummaryByUserLogin() {
        return ResponseEntity.ok().body(transactionService.monthlySummaryByUserLogin());
    }
    @GetMapping("/currentMonthSummaryByUserLogin")
    public ResponseEntity<List<MonthlyMachineSummaryDto>> getCurrentMonthSummaryByUserLogin() {
        return ResponseEntity.ok().body(transactionService.currentMonthSummaryByUserLogin());
    }

    @GetMapping("/currentMonthSummaryByMachineId/{machineId}")
    public ResponseEntity<List<MonthlyMachineSummaryDto>> getCurrentMonthSummaryByUserLogin(@PathVariable String machineId) {
        return ResponseEntity.ok().body(transactionService.currentMonthSummaryByMachineId(machineId));
    }

    @GetMapping("/statsAdmin")
    public ResponseEntity<StatsAdminDTO> statsAdmin(){
        StatsAdminDTO response = transactionService.stastAdmin();
        return ResponseEntity.ok().body(response);
    }

    @GetMapping("/summary/{year}/{month}")
    public ResponseEntity<List<TransactionSummaryDto>> getAllTransactionsSummaryForYearAndMonth(
            @PathVariable("year") int year,
            @PathVariable("month") int month
    ) {
        List<TransactionSummaryDto> summaryDtoList = transactionService.getAllTransactionsSummaryForYearAndMonth(year, month);
        return ResponseEntity.ok(summaryDtoList);
    }

    @GetMapping("/monthlySummaryAllMachines")
    public List<MonthlyMachineSummaryDto> getMonthlySummaryForAllMachines() {
        return transactionService.monthlySummaryForAllMachines();
    }

}
