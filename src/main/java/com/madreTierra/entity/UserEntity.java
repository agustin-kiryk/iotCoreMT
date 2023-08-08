package com.madreTierra.entity;

import jdk.jfr.Timestamp;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import javax.persistence.*;
import java.io.Serializable;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
@Entity
@Table(name = "USERS")
@Getter
@Setter
public class UserEntity implements Serializable {

    public static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "USER_ID", nullable = true)
    private Long userId;
    @Column(name = "FIRST_NAME", nullable = false)
    private String firstName;
    @Column(name = "LAST_NAME", nullable = false)
    private String lastName;
    @Column(name = "EMAIL", nullable = false)
    private String email;
    @Column(name = "PASSWORD", nullable = false)
    private String password;
    @Column(name = "IMAGE")
    private String image;
    @Column(name = "CREATION_DATE")
    @CreationTimestamp
    private Date createDateTime;
    @Column(name = "UPDATE_DATE")
    @UpdateTimestamp
    private Date updateDateTime;
    @Column(name="ADRESS")
    private String adress;
    @Column(name="DISTRICT")
    private String district;
    @Column(name="IDIENTIFIER")
    private String idientifier;
    @Column(name="PHONE")
    private String phone;
    @Column(name="COST")
    private Double cost;
    @Column(name="START_AT", updatable = true)
    private LocalDate startAt;
    @Column(name="STATUS")
    private String status;
    @Column(name="DOCUMENT")
    private String document;
    @ManyToOne()
    @JoinColumn(name = "roleId")
    private RoleEntity role;
    @OneToMany(mappedBy = "user",
            fetch = FetchType.EAGER,
            cascade = {
                    CascadeType.DETACH,
                    CascadeType.MERGE,
                    CascadeType.REFRESH,
                    CascadeType.PERSIST
            })
    private List<MachinEntity> machines = new ArrayList<>();

    public void addMachine(MachinEntity machine) {  //asociar una maquina
        if (machine != null) {
            machines.add(machine);
            machine.setUser(this);
        }
    }

    // Método para desasociar una máquina del usuario
    public void removeMachine(MachinEntity machine) {
        if (machine != null) {
            machines.remove(machine);
            machine.setUser(null);
        }
    }
}
/* private Date updateDateTime;
    private String adress;
    private String district;
    private String idientifier;
    private String phone;
    private Double cost;*/

