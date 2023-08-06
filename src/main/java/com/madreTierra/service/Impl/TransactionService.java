package com.madreTierra.service.Impl;

import com.madreTierra.dto.MonthlyMachineSummaryDto;
import com.madreTierra.dto.MonthlySummaryDto;
import com.madreTierra.dto.TransactionDto;
import com.madreTierra.entity.MachinEntity;
import com.madreTierra.entity.TransactionEntity;
import com.madreTierra.entity.UserEntity;
import com.madreTierra.mapper.TransactionsMap;
import com.madreTierra.repository.MachineRepository;
import com.madreTierra.repository.TransactionRepository;
import com.madreTierra.repository.UserRepository;
import org.apache.catalina.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
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

    public List<TransactionDto> transactionByUserLogin() {
        String email = SecurityContextHolder.getContext().getAuthentication().getName();
        List<MachinEntity> userMachines = userRepository.findByEmail(email).getMachines();
        List<TransactionDto> allTransactions = new ArrayList<>();
        for (MachinEntity machine : userMachines) {
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
        for (MachinEntity machine : userMachines) {
            String machineId = machine.getMachineId().toString();
            List<TransactionEntity> transactions = transactionRepository.findAllByMachineId(machineId);
            List<TransactionDto> machineTransactions = transactionsMap.transactionEntityList2DTO(transactions);
            allTransactions.addAll(machineTransactions);
        }
        return allTransactions;
    }

    public List<TransactionDto> txsAll() {
        List<TransactionEntity> txsList = transactionRepository.findAll();
        List<TransactionDto> txListReturn = transactionsMap.transactionEntityList2DTO(txsList);
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
            LocalDateTime transactionDate = transaction.getDate() != null
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
    public List<MonthlyMachineSummaryDto> monthlySummaryByUserLogin() {
        String email = SecurityContextHolder.getContext().getAuthentication().getName();
        UserEntity user = userRepository.findByEmail(email);
        List<MachinEntity> userMachines = userRepository.findByEmail(email).getMachines();
        List<MonthlyMachineSummaryDto> monthlyMachineSummaries = new ArrayList<>();

        for (MachinEntity machine : userMachines) {
            List<TransactionDto> machineTransactions = getAllTransactionsForMachine(machine);
            Map<YearMonth, List<TransactionDto>> transactionsByMonth = groupTransactionsByMonth(machineTransactions);

            for (Map.Entry<YearMonth, List<TransactionDto>> entry : transactionsByMonth.entrySet()) {
                YearMonth yearMonth = entry.getKey();
                List<TransactionDto> transactions = entry.getValue();

                MonthlyMachineSummaryDto monthlySummaryDto = new MonthlyMachineSummaryDto();
                monthlySummaryDto.setMachineId(machine.getMachineId().toString());
                monthlySummaryDto.setMonth(yearMonth.getMonthValue());
                monthlySummaryDto.setYear(yearMonth.getYear());
                double totalAmount = calculateTotalAmount(transactions);
                double totalWaterDispensed = calculateTotalWaterDispensed(transactions);
                monthlySummaryDto.setTotalAmount(totalAmount);
                monthlySummaryDto.setTotalWaterDispensed(totalWaterDispensed);
                monthlySummaryDto.setCost(totalAmount * user.getCost()/100);
                monthlySummaryDto.setRevenue(totalAmount - (totalAmount * user.getCost()/100));
                monthlySummaryDto.setStatus("test status");
                monthlySummaryDto.setId(user.getUserId());
                monthlyMachineSummaries.add(monthlySummaryDto);
            }
        }

        return monthlyMachineSummaries;
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

    public List<MonthlyMachineSummaryDto> currentMonthSummaryByUserLogin() {
        String email = SecurityContextHolder.getContext().getAuthentication().getName();
        UserEntity user = userRepository.findByEmail(email);
        List<MachinEntity> userMachines = userRepository.findByEmail(email).getMachines();
        List<MonthlyMachineSummaryDto> currentMonthSummaries = new ArrayList<>();

        for (MachinEntity machine : userMachines) {
            List<TransactionDto> machineTransactions = getAllTransactionsForMachine(machine);
            List<TransactionDto> currentMonthTransactions = filterTransactionsByCurrentMonth(machineTransactions);

            double totalAmount = calculateTotalAmount(currentMonthTransactions);
            double totalWaterDispensed = calculateTotalWaterDispensed(currentMonthTransactions);

            MonthlyMachineSummaryDto currentMonthSummaryDto = new MonthlyMachineSummaryDto();
            currentMonthSummaryDto.setMachineId(machine.getMachineId().toString());
            currentMonthSummaryDto.setMonth(YearMonth.now().getMonthValue());
            currentMonthSummaryDto.setYear(YearMonth.now().getYear());
            currentMonthSummaryDto.setTotalAmount(totalAmount);
            currentMonthSummaryDto.setTotalWaterDispensed(totalWaterDispensed);
            currentMonthSummaryDto.setCost(0.111);
            currentMonthSummaryDto.setRevenue(0.11111);
            currentMonthSummaryDto.setStatus("test status");
            currentMonthSummaryDto.setId(user.getUserId());

            currentMonthSummaries.add(currentMonthSummaryDto);
        }

        return currentMonthSummaries;
    }



    private List<TransactionDto> filterTransactionsByCurrentMonth(List<TransactionDto> transactions) {
        YearMonth currentYearMonth = YearMonth.now();
        return transactions.stream()
                .filter(transaction -> {
                    LocalDateTime transactionDate = transaction.getDate() != null
                            ? transaction.getDate()
                            : null;
                    return transactionDate != null && YearMonth.from(transactionDate).equals(currentYearMonth);
                })
                .collect(Collectors.toList());
    }

    private List<TransactionDto> getAllTransactionsForMachine(MachinEntity machine) {
        String machineId = machine.getMachineId().toString();
        List<TransactionEntity> transactions = transactionRepository.findAllByMachineId(machineId);
        return transactionsMap.transactionEntityList2DTO(transactions);
    }

    public List<MonthlyMachineSummaryDto> currentMonthSummaryByMachineId(String machineId) {
        String email = SecurityContextHolder.getContext().getAuthentication().getName();
        UserEntity user = userRepository.findByEmail(email);
        MachinEntity userMachine = machineRepository.findByMachineId(machineId);
        List<MonthlyMachineSummaryDto> currentMonthSummaries = new ArrayList<>();


            List<TransactionDto> machineTransactions = getAllTransactionsForMachine(userMachine);
            List<TransactionDto> currentMonthTransactions = filterTransactionsByCurrentMonth(machineTransactions);

            double totalAmount = calculateTotalAmount(currentMonthTransactions);
            double totalWaterDispensed = calculateTotalWaterDispensed(currentMonthTransactions);
            Map<String, Object> data = generateInvoiceForMachine(machineId, user);
            MonthlyMachineSummaryDto currentMonthSummaryDto = new MonthlyMachineSummaryDto();
            currentMonthSummaryDto.setMachineId(machineId);
            currentMonthSummaryDto.setMonth(YearMonth.now().getMonthValue());
            currentMonthSummaryDto.setYear(YearMonth.now().getYear());
            currentMonthSummaryDto.setTotalAmount(totalAmount);
            currentMonthSummaryDto.setTotalWaterDispensed(totalWaterDispensed);
            currentMonthSummaryDto.setCost((Double) data.get("amountToPay"));
            currentMonthSummaryDto.setRevenue((Double) data.get("revenue"));
            currentMonthSummaryDto.setStatus("IN PROGRESS");
            currentMonthSummaryDto.setId(user.getUserId());
            currentMonthSummaries.add(currentMonthSummaryDto);


        return currentMonthSummaries;
    }

    public Map<String, Object> generateInvoiceForMachine(String machineId, UserEntity user) {
        MachinEntity machine = machineRepository.findByMachineId(machineId);
        if (machine == null) {
            throw new IllegalArgumentException("La máquina con ID " + machineId + " no existe.");
        }

        // Obtener el primer día del mes en curso
        LocalDate firstDayOfMonth = LocalDate.now().withDayOfMonth(1);

        // Obtener la fecha final del ciclo de facturación (primer día del mes siguiente al de startDate)
        LocalDate endDate = firstDayOfMonth.plusMonths(1);

        // Obtener todas las transacciones de la máquina que ocurrieron en el mes en curso
        List<TransactionEntity> transactions = transactionRepository.findAllByMachineIdAndDateBetween(
                machineId, firstDayOfMonth.atStartOfDay(), endDate.atStartOfDay());

        // Calcular el monto total vendido en el mes en curso
        double totalAmountSold = calculateTotalAmountSold(transactions);

        // Calcular el monto a pagar utilizando el porcentaje de derecho de marca de la máquina
        double percentage = user.getCost();
        double amountToPay = totalAmountSold * percentage / 100;
        double revenue = totalAmountSold - amountToPay;
        Map<String,Object> data = new HashMap<>();
        data.put("amountToPay", amountToPay);
        data.put("revenue", revenue);
        return data;
    }

    private double calculateTotalAmountSold(List<TransactionEntity> transactions) {
        double totalAmountSold = 0.0;
        for (TransactionEntity transaction : transactions) {
            totalAmountSold += transaction.getAmount(); // Ajuste del cálculo
        }
        return totalAmountSold;
    }


}
