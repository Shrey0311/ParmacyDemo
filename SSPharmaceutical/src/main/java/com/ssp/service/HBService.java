package com.ssp.service;

import org.apache.log4j.PropertyConfigurator;

public class HBService {

    private static final String LOG4J = "src/main/java/com/ssp/resource/log4j.properties";

    public void loadAllConfiguration() {
        PropertyConfigurator.configure(LOG4J);
    }
}
