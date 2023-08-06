package com.madreTierra.entity;

import javax.persistence.*;

@Entity
@Table(name = "STATUS_PAY")
public class StatePayEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "PAY_ID")
    private Long id;

}
