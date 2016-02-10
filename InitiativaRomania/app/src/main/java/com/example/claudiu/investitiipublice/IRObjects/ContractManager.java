package com.example.claudiu.investitiipublice.IRObjects;

import java.util.LinkedList;
import java.util.Random;

/**
 * Created by claudiu on 2/9/16.
 */
public class ContractManager {
    private static final int MAX_MOCKUP_CONTRACTS = 30;

    private static LinkedList<Contract> contracts = new LinkedList<Contract>();

    private static void addMockContracts(Contract test) {
        Contract newContract = null;
        Random r = new Random();


        for (int i = 0; i < MAX_MOCKUP_CONTRACTS; i++) {

            try {
                newContract = (Contract)test.clone();
            } catch (CloneNotSupportedException e) {
                e.printStackTrace();
            }

            System.out.println("Clone " + newContract.latitude + ", " + newContract.longitude);

            int high = 220;
            int low = 10;
            double longitude = low + r.nextInt(high - low);
            newContract.longitude = Math.floor(newContract.longitude) + (double)(longitude / 1000);

            high = 490;
            low = 370;
            double latitude = low + r.nextInt(high - low);
            newContract.latitude = Math.floor(newContract.latitude) + (double)(latitude/ 1000);

            System.out.println("Adding contract at " + newContract.latitude + newContract.longitude + " randoms " + latitude + ", " + longitude);

            contracts.add(newContract);
        }
    }

    public static LinkedList<Contract> getAllContracts() {

        /* MOKUP Contract, replace with the actual contracts */
        Contract testContract = new Contract();
        testContract.description = "Contract de achizitie banci in Tineretului";
        testContract.id = 1;
        testContract.votes = 233;
        testContract.latitude = 44.444356;
        testContract.longitude = 26.098050;
        testContract.valueEUR = 4400000;

        Category testCategory = new Category();
        testCategory.id = 1;
        testCategory.name = "Parcuri";

        Company testCompany = new Company();
        testCompany.id = 1;
        testCompany.name = "SC Banci de parc SRL";

        testContract.addCategory(testCategory);
        testContract.company = testCompany;

        addMockContracts(testContract);

        contracts.add(testContract);
        return contracts;
    }
}
