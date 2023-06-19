package com.madreTierra.entity;

import lombok.Getter;
import lombok.Setter;

import javax.persistence.Entity;
import javax.persistence.Table;
import java.io.Serializable;

@Entity
@Table(name="MACHINE")
@Getter
@Setter
public class MachinEntity implements Serializable {

    public static final long serialVersionUID = 1L;

}
