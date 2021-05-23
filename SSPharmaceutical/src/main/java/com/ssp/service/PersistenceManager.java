/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.ssp.service;

import org.hibernate.Session;

/**
 * @author Shrey
 */
public class PersistenceManager {

    public boolean isTranscationStarted;
    public boolean isTranscationEnded;
    public Session session;

    public PersistenceManager(Session session) {
        this.session = session;
    }

    public void startTranscation() {
        this.isTranscationStarted = Boolean.TRUE;
    }
}
