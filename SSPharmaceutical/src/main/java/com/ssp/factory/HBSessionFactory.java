/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.ssp.factory;

import com.ssp.modal.Medicine;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.boot.registry.StandardServiceRegistryBuilder;
import org.hibernate.cfg.Configuration;
import org.hibernate.service.ServiceRegistry;

import java.io.File;
import java.util.Objects;

/**
 * @author Shrey
 */
public class HBSessionFactory {

    public Session sessionObj;
    public SessionFactory sessionFactoryObj;
    public ThreadLocal<Session> threadLocal = new ThreadLocal<Session>();

    private enum SingletonSessionFactory {
        INSTANCE;
        private final HBSessionFactory hbSessionFactory;

        SingletonSessionFactory() {
            hbSessionFactory = new HBSessionFactory();
        }

        public HBSessionFactory getHBSessionFactory() {
            return hbSessionFactory;
        }

    }

    private HBSessionFactory() {
        Configuration configObj = new Configuration();
        configObj.configure(new File("src/main/java/com/ssp/resource/hibernate.cfg.xml"));
        registerAnnotatedClass(configObj);
        ServiceRegistry serviceRegistryObj = new StandardServiceRegistryBuilder().applySettings(configObj.getProperties()).build();
        sessionFactoryObj = configObj.buildSessionFactory(serviceRegistryObj);
    }

    private void registerAnnotatedClass(Configuration configObj) {
        configObj.addAnnotatedClass(Medicine.class);
    }

    public static HBSessionFactory getSessionFactory() {
        return SingletonSessionFactory.INSTANCE.getHBSessionFactory();
    }

    public Session getSession() {
        Session session = this.threadLocal.get();
        if (Objects.isNull(session)) {
            session = sessionFactoryObj.openSession();
            threadLocal.set(session);
        }
        return session;
    }

    public void closeSession() {
        Session session = this.threadLocal.get();
        if (Objects.nonNull(session) && session.isConnected()) {
            this.threadLocal.remove();
            session.disconnect();
            session.close();
        }
    }

    public void dispose() {
        if (Objects.nonNull(this.sessionFactoryObj)) {
            this.sessionFactoryObj.close();
            this.sessionFactoryObj = null;
        }
    }
}
