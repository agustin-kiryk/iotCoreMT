package com.madreTierra.repository;

import com.madreTierra.entity.MachinEntity;
import com.madreTierra.entity.TransactionEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.time.LocalDateTime;
import java.util.List;

public interface TransactionRepository extends JpaRepository<TransactionEntity, Long> {
  // List<TransactionEntity> findByMachineId(String machineId);

   /// TransactionEntity findByTransactionId(String idTransaction);

    TransactionEntity findByIdTransaction(String idTransaction);
    @Query("SELECT t FROM TransactionEntity t WHERE t.idTransaction = :idTransaction AND t.machine.machineId = :machineId")
    List<TransactionEntity> findByTransactionIdAndMachineId(@Param("idTransaction") String idTransaction, @Param("machineId") String machineId);

    List<TransactionEntity> findAllByMachineId(String machineId);

    List<TransactionEntity> findAllByMachineIdAndDateBetween(String machineId, LocalDateTime localDateTime, LocalDateTime localDateTime1);
}
