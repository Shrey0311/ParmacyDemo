/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.ssp.service;

import lombok.extern.log4j.Log4j;
import org.hibernate.Session;

import java.io.Serializable;
import java.util.Objects;

/**
 * @author Shrey
 */
@Log4j
public class HBPersistenceManager {

    public boolean isTranscationStarted;
    public boolean isTranscationEnded;
    public Session session;

    public HBPersistenceManager(Session session) {
        this.session = session;
    }

    public void startTranscation() {
        this.isTranscationStarted = Boolean.TRUE;
        if (this.session.isConnected()) {
            this.session.getTransaction().begin();
        }
    }

    public void endTranscation() {
        this.isTranscationStarted = Boolean.FALSE;
        this.isTranscationEnded = Boolean.TRUE;
        try {
            this.session.flush();
            this.session.getTransaction().commit();
        } catch (Exception eObj) {
            eObj.printStackTrace();
            log.error("Exception Occured endTranscation(): " + eObj);
            this.session.getTransaction().rollback();
            this.session.clear();
        }
    }

    public void closeSession() {
        if (Objects.nonNull(this.session) && this.session.isConnected()) {
            this.session.disconnect();
            this.session.close();
        }
    }

    public void rollback() {
        if (Objects.nonNull(this.session) && this.session.isConnected()) {
            this.session.flush();
            this.session.getTransaction().rollback();
            this.isTranscationStarted = Boolean.FALSE;
            this.isTranscationEnded = Boolean.TRUE;
        }
    }

    public Serializable persistObject(Object object) {
        Serializable insertedPk = null;
        try {
            insertedPk = this.session.save(object);
        } catch (Exception eObj) {
            throw eObj;
        }
        return insertedPk;
    }

    public <T> Object findOBject(Class<T> classObj, Number id) {
        Object object = null;
        try {
            object = this.session.get(classObj, id);
        } catch (Exception eObj) {
            throw new RuntimeException(eObj);
        }
        return object;
    }
}
