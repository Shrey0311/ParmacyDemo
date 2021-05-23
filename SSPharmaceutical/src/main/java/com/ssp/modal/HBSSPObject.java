package com.ssp.modal;

import javax.persistence.MappedSuperclass;
import java.io.Serializable;

@MappedSuperclass
public abstract class HBSSPObject implements Serializable {

    public abstract Long primaryKey();
}
