package com.ssp.modal;

import lombok.Data;

import javax.persistence.*;

@Data
@Entity
@Table(name = "MEDICINE")
public class Medicine {

    @Column(name = "NAME")
    private String name;

    @Id
    @GeneratedValue
    @Column(name = "PRIMARY_KEY")
    private int primaryKey;

}
