package com.madreTierra.entity;

import com.madreTierra.enumeration.LightSwitch;
import com.madreTierra.enumeration.ValveFill;
import com.madreTierra.enumeration.ValveWash;
import com.madreTierra.enumeration.WaterPumpSwich;
import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;
import java.io.Serializable;
import java.time.LocalDateTime;
import java.util.List;

@Entity
@Table(name="MACHINE")
@Getter
@Setter
public class MachinEntity implements Serializable {

    public static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "MACHINE_ID_INTERN")
    private Long machineIdIntern;
    @Column(name = "MACHINE_ID")
    private String machineId;
    @Column(name="PRICE")
    private Double price;
    @Column(name="STATUS")
    private String status;
    @Column(name = "CURRENCY")
    private String currency;
    @Enumerated(EnumType.STRING)
    @Column(name = "WATER_PUMP")
    private WaterPumpSwich waterPump;
    @Enumerated(EnumType.STRING)
    @Column(name = "LIGHT")
    private LightSwitch light;
    @Enumerated(EnumType.STRING)
    @Column(name = "VALVE_FILL")
    private ValveFill valveFill;
    @Enumerated(EnumType.STRING)
    @Column(name = "VALVE_WASH")
    private ValveWash valveWash;
    @Column(name = "DISTRICT")
    private String district;
    @Column(name = "DETAIL")
    private String detail;
    @Column(name = "STATE_AS")
    private LocalDateTime stateAt;
    @Column(name = "ADRESS")
    private String adress;
    @Column(name = "MODEL")
    private String model;
    @Column(name = "COMENT")
    private String coment;


    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "USER_ID", nullable = true)
    private UserEntity user;

    @OneToMany(mappedBy = "machine", cascade = CascadeType.ALL)
    private List<TransactionEntity> transactions;






}
