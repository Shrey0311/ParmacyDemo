/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.ssp.config;

import com.ssp.modal.Medicine;
import com.ssp.service.HBPersistenceManager;
import com.ssp.service.HBService;
import com.ssp.util.HBGNUtil;
import lombok.extern.log4j.Log4j;

import java.util.ArrayList;

/**
 * @author Shrey
 */
@Log4j
public class Application {

    private static final HBService hbService = new HBService();

    public static void main(String[] args) {
        try {
            hbService.loadAllConfiguration();
            HBPersistenceManager hbPersistenceManager = new HBPersistenceManager();
            HBGNUtil util = new HBGNUtil();
           hbPersistenceManager.startTranscation();
           /* hbPersistenceManager.createObject("com.ssp.modal.Medicine", util.newKeyValueMap("medicineName~contentWeight~medicineSalt~expiryDate~mfdDate~storeDate~price~CompanyName",
                    "Paracetamol", 150, "2-Dexoy-HydroxineChlorine", new Date(System.currentTimeMillis()), new Date(System.currentTimeMillis()), new Date(System.currentTimeMillis()),
                    50, "Cipla"));*/
            ArrayList<Medicine> medicineArrayList = hbPersistenceManager.fetchHSSPSQLQuery(Medicine.class, "Medicine_get", null);
            log.info(medicineArrayList + " >>>>>>>>>>>>>>>>>>>>>>>>>> medicineArrayList ");
            if (medicineArrayList.size() != 0) {
                for (Medicine medicine : medicineArrayList) {
                    medicine.setMedicineName("ParacetomolChange");
                    hbPersistenceManager.updateObject(medicine);
                   /* if(medicine.primaryKey().intValue() == 9){
                        hbPersistenceManager.deleteObject(medicine);
                    }*/
                }
            }

            hbPersistenceManager.endTranscation();
        } catch (Exception eobj) {
            eobj.printStackTrace();
            log.error("Exception Occur : Main " + eobj);
        }
    }

}
