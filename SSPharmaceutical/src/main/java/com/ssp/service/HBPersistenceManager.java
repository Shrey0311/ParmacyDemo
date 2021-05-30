/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.ssp.service;

import com.ssp.factory.HBSessionFactory;
import com.ssp.modal.HBSSPObject;
import lombok.extern.log4j.Log4j;
import org.hibernate.Query;
import org.hibernate.Session;

import java.io.Serializable;
import java.lang.reflect.Field;
import java.util.*;

/**
 * @author Shrey
 */
@Log4j
public class HBPersistenceManager {

    public boolean isTranscationStarted;
    public boolean isTranscationEnded;

    public Session getSession() {
        return HBSessionFactory.getSessionFactory().getSession();
    }

    public void startTranscation() {
        this.isTranscationStarted = Boolean.TRUE;
        if (this.getSession().isConnected()) {
            this.getSession().getTransaction().begin();
        }
    }

    public void endTranscation() {
        this.isTranscationStarted = Boolean.FALSE;
        this.isTranscationEnded = Boolean.TRUE;
        try {
            this.getSession().getTransaction().commit();
            this.getSession().flush();
        } catch (Exception eObj) {
            eObj.printStackTrace();
            log.error("Exception Occured endTranscation(): " + eObj);
            this.getSession().getTransaction().rollback();
            this.getSession().clear();
        }
    }

    public void closeSession() {
        if (Objects.nonNull(this.getSession()) && this.getSession().isConnected()) {
            this.getSession().disconnect();
            this.getSession().close();
        }
    }

    public void rollback() {
        if (Objects.nonNull(this.getSession()) && this.getSession().isConnected()) {
            this.getSession().flush();
            this.getSession().getTransaction().rollback();
            this.isTranscationStarted = Boolean.FALSE;
            this.isTranscationEnded = Boolean.TRUE;
        }
    }

    public Serializable persistObject(Object object) throws Exception {
        return this.getSession().save(object);
    }

    public void persistObject(Collection<?> collection) {
        try {
            for (Object object : collection) {
                this.getSession().save(object);
            }
        } catch (Exception eobj) {
            throw eobj;
        }
    }

    public <T> Object findOBject(Class<T> classObj, Number id) {
        Object object = null;
        try {
            object = this.getSession().get(classObj, id);
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
            this.setKeyValueProperty(hbsspObject, objMap);
            this.getSession().save(hbsspObject);
        } catch (ClassNotFoundException | InstantiationException | IllegalAccessException e) {
            throw e;
        } catch (Exception eobj) {
            eobj.printStackTrace();
            log.error(" Create Object : " + eobj);
        }
        return hbsspObject;
    }

    private void setKeyValueProperty(HBSSPObject hbsspObject, HashMap<String, Object> objMap) throws IllegalAccessException {
        for (Field field : hbsspObject.getClass().getDeclaredFields()) {
            Object object = objMap.get(field.getName());
            if (Objects.nonNull(object)) {
                field.setAccessible(true);
                field.set(hbsspObject, object);
            }
        }
    }

    public <T> ArrayList<T> fetchHSSPSQLQuery(Class<T> cls, String sqlStr, Map<String, Object> newMap) {
        ArrayList<T> list = new ArrayList<>();
        try {
            Query query = this.getSession().getNamedQuery(sqlStr);
            this.setSQLProperty(query, newMap);
            list.addAll(query.list());
        } catch (Exception eobj) {
            eobj.printStackTrace();
        }
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
            Query query = this.getSession().getNamedQuery(namedQuery);
            this.setSQLProperty(query, newMap);
            for (Object obj : query.list()) {
                list.add(((Long) obj).longValue());
            }
        } catch (Exception eobj) {
            throw eobj;
        }
        return list;
    }

    public void updateObject(Object object) {
        try {
            this.getSession().update(object);
        } catch (Exception eobj) {
            throw eobj;
        }
    }

    public void deleteObject(Object object) {
        try {
            this.getSession().delete(object);
        } catch (Exception eobj) {
            throw eobj;
        }
    }
}
