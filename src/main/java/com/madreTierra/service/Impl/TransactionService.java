package com.madreTierra.service.Impl;

import com.madreTierra.dto.MonthlySummaryDto;
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

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.YearMonth;
import java.util.*;
import java.util.stream.Collectors;

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

    public List<TransactionDto> transactionsByUserId(Long userId) {

        List<MachinEntity> userMachines = userRepository.findById(userId).get().getMachines();
        List<TransactionDto> allTransactions = new ArrayList<>();
        for(MachinEntity machine : userMachines){
            String machineId = machine.getMachineId().toString();
            List<TransactionEntity> transactions = transactionRepository.findAllByMachineId(machineId);
            List<TransactionDto> machineTransactions = transactionsMap.transactionEntityList2DTO(transactions);
            allTransactions.addAll(machineTransactions);
        }
        return allTransactions;
    }

    public List<TransactionDto> txsAll() {
        List<TransactionEntity> txsList = transactionRepository.findAll();
        List<TransactionDto>txListReturn = transactionsMap.transactionEntityList2DTO(txsList);
        return txListReturn;
    }

    public List<TransactionDto> transactionsByUserLoginAndMonth() {
        String email = SecurityContextHolder.getContext().getAuthentication().getName();
        List<MachinEntity> userMachines = userRepository.findByEmail(email).getMachines();

        List<TransactionDto> allTransactions = getAllTransactionsForMachines(userMachines);

        Map<YearMonth, List<TransactionDto>> transactionsByMonth = groupTransactionsByMonth(allTransactions);

        List<TransactionDto> result = new ArrayList<>();
        transactionsByMonth.forEach((yearMonth, transactions) -> {
            TransactionDto transactionDto = new TransactionDto();
            transactionDto.setMonth(yearMonth.getMonthValue());
            transactionDto.setYear(yearMonth.getYear());
            transactionDto.setTransactions(transactions);
            result.add(transactionDto);
        });

        return result;
    }

    public List<TransactionDto> transactionsByUserLoginAndMonthForMachine(String machineId) {
        MachinEntity machine = machineRepository.findByMachineId(machineId);
        if (machine == null) {
            return new ArrayList<>();
        }

        List<TransactionEntity> transactions = transactionRepository.findAllByMachineId(machineId);
        List<TransactionDto> machineTransactions = transactionsMap.transactionEntityList2DTO(transactions);

        Map<YearMonth, List<TransactionDto>> transactionsByMonth = groupTransactionsByMonth(machineTransactions);

        List<TransactionDto> result = new ArrayList<>();
        transactionsByMonth.forEach((yearMonth, transactionsOfMonth) -> {
            TransactionDto transactionDto = new TransactionDto();
            transactionDto.setMonth(yearMonth.getMonthValue());
            transactionDto.setYear(yearMonth.getYear());
            transactionDto.setTransactions(transactionsOfMonth);
            result.add(transactionDto);
        });

        return result;
    }

    private Map<YearMonth, List<TransactionDto>> groupTransactionsByMonth(List<TransactionDto> transactions) {
        Map<YearMonth, List<TransactionDto>> transactionsByMonth = new HashMap<>();
        for (TransactionDto transaction : transactions) {
            LocalDate transactionDate = transaction.getDate() != null
                    ? transaction.getDate()
                    : null;

            if (transactionDate != null) {
                YearMonth yearMonth = YearMonth.from(transactionDate);
                transactionsByMonth.computeIfAbsent(yearMonth, k -> new ArrayList<>()).add(transaction);
            }
        }
        return transactionsByMonth;
    }

    private List<TransactionDto> getAllTransactionsForMachines(List<MachinEntity> userMachines) {
        List<TransactionDto> allTransactions = new ArrayList<>();
        for (MachinEntity machine : userMachines) {
            String machineId = machine.getMachineId();
            List<TransactionEntity> transactions = transactionRepository.findAllByMachineId(machineId);
            List<TransactionDto> machineTransactions = transactionsMap.transactionEntityList2DTO(transactions);
            allTransactions.addAll(machineTransactions);
        }
        return allTransactions;
    }

    public List<TransactionDto> transactionsByUserLoginSeparateByMonth() {
        String email = SecurityContextHolder.getContext().getAuthentication().getName();
        List<MachinEntity> userMachines = userRepository.findByEmail(email).getMachines();
        List<TransactionDto> allTransactions = getAllTransactionsForMachines(userMachines);

        return separateTransactionsByMonth(allTransactions);
    }

    private List<TransactionDto> separateTransactionsByMonth(List<TransactionDto> transactions) {
        Map<YearMonth, List<TransactionDto>> transactionsByMonth = groupTransactionsByMonth(transactions);
        List<TransactionDto> result = new ArrayList<>();

        transactionsByMonth.forEach((yearMonth, transactionsList) -> {
            TransactionDto transactionDto = new TransactionDto();
            transactionDto.setMonth(yearMonth.getMonthValue());
            transactionDto.setYear(yearMonth.getYear());
            transactionDto.setTransactions(transactionsList);
            result.add(transactionDto);
        });

        return result;
    }
                                  // sumas para tabla de totales mensuales para clientes
    public List<MonthlySummaryDto> monthlySummaryByUserLogin() {
        String email = SecurityContextHolder.getContext().getAuthentication().getName();
        List<MachinEntity> userMachines = userRepository.findByEmail(email).getMachines();
        List<TransactionDto> allTransactions = getAllTransactionsForMachines(userMachines);
        Map<YearMonth, List<TransactionDto>> transactionsByMonth = groupTransactionsByMonth(allTransactions);

        List<MonthlySummaryDto> monthlySummaries = new ArrayList<>();
        transactionsByMonth.forEach((yearMonth, transactions) -> {
            MonthlySummaryDto summaryDto = new MonthlySummaryDto();
            summaryDto.setMonth(yearMonth.getMonthValue());
            summaryDto.setYear(yearMonth.getYear());
            double totalAmount = calculateTotalAmount(transactions);
            double totalWaterDispensed = calculateTotalWaterDispensed(transactions);
            summaryDto.setTotalAmount(totalAmount);
            summaryDto.setTotalWaterDispensed(totalWaterDispensed);
            monthlySummaries.add(summaryDto);
        });

        return monthlySummaries;
    }
    private double calculateTotalAmount(List<TransactionDto> transactions) {
        double totalAmount = 0.0;
        for (TransactionDto transaction : transactions) {
            totalAmount += transaction.getAmount();
        }
        return totalAmount;
    }

    private double calculateTotalWaterDispensed(List<TransactionDto> transactions) {
        double totalWaterDispensed = 0.0;
        for (TransactionDto transaction : transactions) {
            totalWaterDispensed += transaction.getDispensedWater();
        }
        return totalWaterDispensed;
    }

    public MonthlySummaryDto currentMonthSummaryByUserLogin() {
        String email = SecurityContextHolder.getContext().getAuthentication().getName();
        List<MachinEntity> userMachines = userRepository.findByEmail(email).getMachines();
        List<TransactionDto> allTransactions = getAllTransactionsForMachines(userMachines);
        List<TransactionDto> currentMonthTransactions = filterTransactionsByCurrentMonth(allTransactions);

        MonthlySummaryDto summaryDto = new MonthlySummaryDto();
        YearMonth currentYearMonth = YearMonth.now();
        summaryDto.setMonth(currentYearMonth.getMonthValue());
        summaryDto.setYear(currentYearMonth.getYear());
        double totalAmount = calculateTotalAmount(currentMonthTransactions);
        double totalWaterDispensed = calculateTotalWaterDispensed(currentMonthTransactions);
        summaryDto.setTotalAmount(totalAmount);
        summaryDto.setTotalWaterDispensed(totalWaterDispensed);

        return summaryDto;
    }

    private List<TransactionDto> filterTransactionsByCurrentMonth(List<TransactionDto> transactions) {
        YearMonth currentYearMonth = YearMonth.now();
        return transactions.stream()
                .filter(transaction -> {
                    LocalDate transactionDate = transaction.getTransactionDate() != null
                            ? transaction.getDate()
                            : null;
                    return transactionDate != null && YearMonth.from(transactionDate).equals(currentYearMonth);
                })
                .collect(Collectors.toList());
    }
}
