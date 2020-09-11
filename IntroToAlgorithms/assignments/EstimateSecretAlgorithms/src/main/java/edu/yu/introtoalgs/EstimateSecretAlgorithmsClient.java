package edu.yu.introtoalgs;

public class EstimateSecretAlgorithmsClient {

    public static void main(String[] args) {
        SecretAlgorithm1 algo1 = new SecretAlgorithm1();
        SecretAlgorithm2 algo2 = new SecretAlgorithm2();
        SecretAlgorithm3 algo3 = new SecretAlgorithm3();
        SecretAlgorithm4 algo4 = new SecretAlgorithm4();
        algo(algo1, 1);
        algo(algo1, 1);
        algo(algo1, 1);
        algo(algo1, 1);
        algo(algo2, 2);
        algo(algo2, 2);
        algo(algo2, 2);
        algo(algo2, 2);
        algo(algo3, 3);
        algo(algo3, 3);
        algo(algo3, 3);
        algo(algo3, 3);
        algo(algo4, 4);
        algo(algo4, 4);
        algo(algo4, 4);
        algo(algo4, 4);
    }

    public static void algo(BigOMeasurable alg, int algoNum){
        System.out.println("Algo " + algoNum);
        alg.setup(250);
        long start1 = System.currentTimeMillis();
        alg.execute();
        long finish1 = System.currentTimeMillis();
        alg.setup(500);
        long start2 = System.currentTimeMillis();
        alg.execute();
        long finish2 = System.currentTimeMillis();
        alg.setup(1000);
        long start3 = System.currentTimeMillis();
        alg.execute();
        long finish3 = System.currentTimeMillis();
        alg.setup(2000);
        long start4 = System.currentTimeMillis();
        alg.execute();
        long finish4 = System.currentTimeMillis();
        alg.setup(4000);
        long start5 = System.currentTimeMillis();
        alg.execute();
        long finish5 = System.currentTimeMillis();
        long total1 = finish1 - start1;
        long total2 = finish2 - start2;
        long total3 = finish3 - start3;
        long total4 = finish4 - start4;
        long total5 = finish5 - start5;
        System.out.println("Test 1 Time: " + total1);
        System.out.println("Test 2 Time: " + total2);
        System.out.println("Test 3 Time: " + total3);
        System.out.println("Test 4 Time: " + total4);
        System.out.println("Test 5 Time: " + total5);
        if(algoNum > 1){
            alg.setup(8000);
            long start6 = System.currentTimeMillis();
            alg.execute();
            long finish6 = System.currentTimeMillis();
            alg.setup(16000);
            long start7 = System.currentTimeMillis();
            alg.execute();
            long finish7 = System.currentTimeMillis();
            alg.setup(32000);
            long start8 = System.currentTimeMillis();
            alg.execute();
            long finish8 = System.currentTimeMillis();
            alg.setup(64000);
            long start9 = System.currentTimeMillis();
            alg.execute();
            long finish9 = System.currentTimeMillis();
            alg.setup(128000);
            long start10 = System.currentTimeMillis();
            alg.execute();
            long finish10 = System.currentTimeMillis();
            alg.setup(256000);
            long start11 = System.currentTimeMillis();
            alg.execute();
            long finish11 = System.currentTimeMillis();
            long total6 = finish6 - start6;
            long total7 = finish7 - start7;
            long total8 = finish8 - start8;
            long total9 = finish9 - start9;
            long total10 = finish10 - start10;
            long total11 = finish11 - start11;
            System.out.println("Test 6 Time: " + total6);
            System.out.println("Test 7 Time: " + total7);
            System.out.println("Test 8 Time: " + total8);
            System.out.println("Test 9 Time: " + total9);
            System.out.println("Test 10 Time: " + total10);
            System.out.println("Test 11 Time: " + total11);
        }
        if(algoNum == 2 || algoNum == 4){
            alg.setup(512000);
            long start12 = System.currentTimeMillis();
            alg.execute();
            long finish12 = System.currentTimeMillis();
            alg.setup(1024000);
            long start13 = System.currentTimeMillis();
            alg.execute();
            long finish13 = System.currentTimeMillis();
            alg.setup(2048000);
            long start14 = System.currentTimeMillis();
            alg.execute();
            long finish14 = System.currentTimeMillis();
            alg.setup(4096000);
            long start15 = System.currentTimeMillis();
            alg.execute();
            long finish15 = System.currentTimeMillis();
            alg.setup(8192000);
            long start16 = System.currentTimeMillis();
            alg.execute();
            long finish16 = System.currentTimeMillis();
            long total12 = finish12 - start12;
            long total13 = finish13 - start13;
            long total14 = finish14 - start14;
            long total15 = finish15 - start15;
            long total16 = finish16 - start16;
            System.out.println("Test 12 Time: " + total12);
            System.out.println("Test 13 Time: " + total13);
            System.out.println("Test 14 Time: " + total14);
            System.out.println("Test 15 Time: " + total15);
            System.out.println("Test 16 Time: " + total16);
        }
        if(algoNum == 2){
            alg.setup(16384000);
            long start17 = System.currentTimeMillis();
            alg.execute();
            long finish17 = System.currentTimeMillis();
            alg.setup(32768000);
            long start18 = System.currentTimeMillis();
            alg.execute();
            long finish18 = System.currentTimeMillis();
            alg.setup(65536000);
            long start19 = System.currentTimeMillis();
            alg.execute();
            long finish19 = System.currentTimeMillis();
            alg.setup(131072000);
            long start20 = System.currentTimeMillis();
            alg.execute();
            long finish20 = System.currentTimeMillis();
            long total17 = finish17 - start17;
            long total18 = finish18 - start18;
            long total19 = finish19 - start19;
            long total20 = finish20 - start20;
            System.out.println("Test 17 Time: " + total17);
            System.out.println("Test 18 Time: " + total18);
            System.out.println("Test 19 Time: " + total19);
            System.out.println("Test 20 Time: " + total20);
        }
    }
}
