/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.ssp.config;

import com.ssp.service.HBService;
import lombok.extern.log4j.Log4j;

/**
 * @author Shrey
 */
@Log4j
public class Application {

    private static final HBService hbService = new HBService();

    public static void main(String[] args) {
        hbService.loadAllConfiguration();

    }

}
