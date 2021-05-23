/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.ssp.config;

import com.ssp.factory.HBSessionFactory;
import org.apache.log4j.Logger;
import org.apache.log4j.PropertyConfigurator;

/**
 * @author Shrey
 */
public class Application {

    private static final Logger logger = Logger.getLogger(Application.class);


    public static void main(String[] args) {

        // String dir = System.getProperty("user.dir");
        // System.out.println(dir);
        PropertyConfigurator.configure("src/main/java/com/ssp/resource/log4j.properties");
        logger.info("1111111111111111111111111 " + HBSessionFactory.buildSessionFactory().getSession().hashCode());
        //  new Thread(()->logger.info("2222222222222222222"+HBSessionFactory.buildSessionFactory().getSession().hashCode())).start();

    }

}
