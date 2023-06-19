package com.madreTierra.entity;

import lombok.Getter;
import lombok.Setter;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import javax.persistence.*;
import java.io.Serializable;
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
    private Long userId;

    @Column(name = "FIRST_NAME", nullable = false)
    private String firstName;

    @Column(name = "LAST_NAME", nullable = false)
    private String lastName;

    @Column(name = "EMAIL", nullable = false)
    private String email;

    @Column(name = "PASSWORD", nullable = false)
    private String password;

    @Column(name = "CREATION_DATE")
    @CreationTimestamp
    private Date createDateTime;

    @Column(name = "UPDATE_DATE")
    @UpdateTimestamp
    private Date updateDateTime;

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
}
