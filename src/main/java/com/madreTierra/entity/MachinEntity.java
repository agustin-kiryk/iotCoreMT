package com.madreTierra.entity;

import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;
import java.io.Serializable;

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

    @ManyToOne(fetch = FetchType.EAGER,optional = false)
    @JoinColumn(name = "USER_ID", nullable = false)
    private UserEntity user;



}
