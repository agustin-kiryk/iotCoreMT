package com.madreTierra.entity;

import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;
import java.time.LocalDateTime;

@Entity
@Table(name = "TRANSACTIONS")
@Getter
@Setter
public class TransactionEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "ID_BACK")
    private Long idBack;
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "MACHINE_ID_BACK")
    private MachinEntity machine;
    @Column(name = "TRANSACTION_ID")
    private String idTransaction;
    @Column(name = "AMOUNT")
    private Double amount;
    @Column(name = "CURRENCY")
    private String currency;
    @Column(name = "DISPENSED_WATER")
    private Double dispensedWater;
    @Column(name = "DATE")
    private LocalDateTime date;
    @Column(name = "MACHINE_ID")
    private String machineId;

}
