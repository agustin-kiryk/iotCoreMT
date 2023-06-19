package com.madreTierra.entity;

import com.madreTierra.enumeration.RoleName;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@Entity
@Table(name = "ROLES")
@Getter
@Setter
public class RoleEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "ROLE_ID")
    private Long id;

    @Enumerated(EnumType.STRING)
    @Column(name = "ROLE_NAME", nullable = false, unique = true)
    private RoleName roleName;

    @Column(name = "ROLE_DESCRIPTION", length = 50)
    private String description;

    @Column(name="CREATION_DATE")
    @CreationTimestamp
    private Date createDateTime;

    @Column(name="UPDATE_DATE")
    @UpdateTimestamp
    private Date updateDateTime;

    @OneToMany(mappedBy = "role",
            fetch = FetchType.EAGER,
            cascade = {
                    CascadeType.DETACH,
                    CascadeType.MERGE,
                    CascadeType.REFRESH,
                    CascadeType.PERSIST
            })
    private List<UserEntity> users = new ArrayList<>();

}
