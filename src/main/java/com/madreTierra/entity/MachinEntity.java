package com.madreTierra.entity;

import com.madreTierra.enumeration.LightSwitch;
import com.madreTierra.enumeration.ValveFill;
import com.madreTierra.enumeration.ValveWash;
import com.madreTierra.enumeration.WaterPumpSwich;
import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;
import java.io.Serializable;
import java.util.List;

@Entity
@Table(name="MACHINE")
@Getter
@Setter
public class MachinEntity implements Serializable {

    public static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "MACHINE_ID")
    private Long machineId;

    @Column(name="END_STATE")
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


    @ManyToOne(fetch = FetchType.EAGER,optional = false)
    @JoinColumn(name = "USER_ID", nullable = false)
    private UserEntity user;

    @OneToMany(mappedBy = "machine", cascade = CascadeType.ALL)
    private List<TransactionEntity> transactions;






}
