package com.ssp.modal;

import lombok.Data;

import javax.persistence.*;
import java.sql.Date;

@Data
@Entity
@Table(name = "MEDICINE")
public class Medicine extends HBSSPObject {

    @Column(name = "MEDICINE_NAME")
    private String medicineName;

    @Column(name = "CONTENT_WEIGHT")
    private long contentWeight;

    @Column(name = "MEDICINE_SALT")
    private String medicineSalt;

    @Column(name = "EXPIRY_DATE")
    private Date expiryDate;

    @Column(name = "MFD_DATE")
    private Date mfdDate;

    @Column(name = "STORE_DATE")
    private Date storeDate;

    @Column(name = "PRICE")
    private long price;

    @Column(name = "COMPANY_NAME")
    private String CompanyName;

    @Id
    @GeneratedValue
    @Column(name = "PRIMARY_KEY")
    private long primaryKey;

    @Override
    public Long primaryKey() {
        return this.primaryKey;
    }
}
