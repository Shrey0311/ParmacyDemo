/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.ssp.service;

import com.ssp.modal.HBSSPObject;
import lombok.extern.log4j.Log4j;
import org.hibernate.Query;
import org.hibernate.Session;

import java.io.Serializable;
import java.util.*;

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

    public <T> HBSSPObject createObject(Class<T> classObj, HashMap<String, Object> newMap) {
        HBSSPObject hbsspObject = null;
        try {
            hbsspObject = this.createObject(classObj.getName(), newMap);
        } catch (Exception eobj) {
            throw new RuntimeException(eobj);
        }
        return hbsspObject;
    }

    public HBSSPObject createObject(String className, HashMap<String, Object> objMap) throws Exception {
        Class<?> cls;
        HBSSPObject hbsspObject = null;
        try {
            cls = Class.forName(className);
            hbsspObject = (HBSSPObject) cls.newInstance();
            this.session.save(hbsspObject);
        } catch (ClassNotFoundException | InstantiationException | IllegalAccessException e) {
            throw e;
        } catch (Exception eobj) {
            eobj.printStackTrace();
            log.error(" Create Object : " + eobj);
        }
        return hbsspObject;
    }

    public ArrayList<Object> fetchHSSPSQLQuery(String sqlStr, Map<String, Object> newMap) {
        ArrayList<Object> list = new ArrayList<>();
        Query query = this.session.createSQLQuery(sqlStr);
        this.setSQLProperty(query, newMap);
        list.addAll(query.list());
        return list;
    }

    private void setSQLProperty(Query query, Map<String, Object> newMap) {
        if (Objects.nonNull(newMap) && newMap.size() != 0) {
            for (String key : newMap.keySet()) {
                Object value = newMap.get(key);
                if (value instanceof ArrayList) {
                    query.setParameterList(key, (Collection) value);
                } else {
                    query.setParameter(key, newMap.get(key));
                }
            }
        }
    }

    private ArrayList<Long> fetchNativeNamedSQLQuery(String namedQuery, HashMap<String, Object> newMap) {
        ArrayList<Long> list = new ArrayList<>();
        try {
            Query query = this.session.getNamedQuery(namedQuery);
            this.setSQLProperty(query, newMap);
            for (Object obj : query.list()) {
                list.add(((Long) obj).longValue());
            }
        } catch (Exception eobj) {
            throw eobj;
        }
        return list;
    }

    public void deleteObject(Object object) {
        try {
            this.session.delete(object);
        } catch (Exception eobj) {
            throw eobj;
        }
    }
}
