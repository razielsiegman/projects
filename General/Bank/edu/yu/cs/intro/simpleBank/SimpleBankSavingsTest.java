package edu.yu.cs.intro.simpleBank;

import edu.yu.cs.intro.simpleBank.exceptions.AuthenticationException;
import edu.yu.cs.intro.simpleBank.exceptions.InsufficientAssetsException;
import edu.yu.cs.intro.simpleBank.exceptions.UnauthorizedActionException;
import org.junit.Assert;
import org.junit.Test;
import java.util.*;
import org.junit.After;


public class SimpleBankSavingsTest {

    private Bank getBank() {
        Bank bank = new Bank(1);
        return bank;
    }

    @After
    public void resetAfterTest(){
        Bank bank = getBank();
        bank.clearVariables();
    }
    // @Test
    // public void testPatronCreation() {
    //     //test using just one patron
    //     Bank bank = getBank();
    //     long ssnum = 123456789;
    //     try {
    //         bank.createNewPatron("Judah", "Diament", ssnum, "diament", "1234");
    //     }
    //     catch (UnauthorizedActionException e) {
    //         e.printStackTrace();
    //         Assert.fail("initial creation of patron incorrectly failed");
    //     }
    //     try {
    //         bank.createNewPatron("Judah", "Diament", 999999999, "diament", "1234");
    //     }
    //     catch (UnauthorizedActionException e) {
    //         e.printStackTrace();
    //         Assert.fail("creation of patron with same info but different SS# incorrectly failed");
    //     }
    //     try {
    //         bank.createNewPatron("Judah", "Diament", ssnum, "diament", "1234");
    //         Assert.fail("creation of patron with same exact SS# failed to throw an exception");
    //     }
    //     catch (UnauthorizedActionException e) {
    //     }
    //     //test using just multiple patrons
    //     bank = getBank();
    //     try {
    //         bank.createNewPatron("Judah", "Diament", ssnum, "diament", "1234");
    //         bank.createNewPatron("Judah", "Diament", ssnum + 2, "diament", "1234");
    //         bank.createNewPatron("Judah", "Diament", ssnum + 5, "diament", "1234");
    //         bank.createNewPatron("Judah", "Diament", ssnum, "diament", "1234");
    //         Assert.fail("creation of patron with same exact SS# failed to throw an exception");
    //     }
    //     catch (UnauthorizedActionException e) {
    //     }
    // }

    // @Test
    // public void testOpenSavingsAccount() {
    //     Bank bank = getBank();
    //     long ssnum = 123456789;
    //     String userName = "diament";
    //     String password = "1234";
    //     try {
    //         bank.createNewPatron("Judah", "Diament", ssnum, userName, password);
    //     }
    //     catch (UnauthorizedActionException e) {
    //         e.printStackTrace();
    //         Assert.fail("initial creation of patron incorrectly failed");
    //     }
    //     try {
    //         bank.openSavingsAccount(ssnum, userName, password);
    //     }
    //     catch (UnauthorizedActionException e) {
    //         e.printStackTrace();
    //         Assert.fail("opening savings account incorrectly failed");
    //     }
    //     catch (AuthenticationException e) {
    //         e.printStackTrace();
    //         Assert.fail("opening savings account incorrectly failed");
    //     }
    //     try {
    //         bank.openSavingsAccount(ssnum, userName, password);
    //         Assert.fail("opening savings account should have thrown UnauthorizedActionException when opening with same ssnum, username, and password");
    //     }
    //     catch (Exception e) {
    //     }
    // }

    @Test
    public void testDepositAndWithdrawCashFromSavings() throws AuthenticationException{
        Bank bank = getBank();
        long ssnum1 = 123456789;
        long ssnum2 = 123456789 + 20;
        long ssnum3 = 123456789 + 10;
        String userName = "diament";
        String password = "1234";
        //create 3 accounts
       // try {
            bank.createNewPatron("Judah", "Diament", ssnum2, userName, password);
            bank.createNewPatron("Judah", "Diament", ssnum1, userName, password);
            bank.createNewPatron("Judah", "Diament", ssnum3, userName, password);
            bank.openSavingsAccount(ssnum1, userName, password);
            bank.openSavingsAccount(ssnum3, userName, password);
        // }
        // catch (UnauthorizedActionException e) {
        //     e.printStackTrace();
        //     Assert.fail("initial creation of patron or opening of savings account incorrectly failed");
        // }
        // catch (AuthenticationException e) {
        //     e.printStackTrace();
        //     Assert.fail("opening savings account incorrectly failed");
        // }
        //deposit into 2 of them
        double amount = 1000.57;
        try {
            bank.depositCashIntoSavings(ssnum1, userName, password, amount);
            bank.depositCashIntoSavings(ssnum3, userName, password, amount * 2);
        }
        catch (Exception e) {
            e.printStackTrace();
            Assert.fail("deposit incorrectly failed");
        }
        //withdraw correctly twice, check balance at the end to make sure deposits and withdrawals actually happening
        try {
            bank.withdrawCashFromSavings(ssnum1, userName, password, amount - 10);
            bank.withdrawCashFromSavings(ssnum1, userName, password, 1);
            Assert.assertEquals("Account should've had $1 left, but did not", 6, bank.checkBalanceSavings(ssnum1, userName, password), 0.5);
        }
        catch (Exception e) {
            e.printStackTrace();
            Assert.fail("first withdrawal incorrectly failed");
        }
        //withdraw from someone who has no savings account
        try {
            bank.withdrawCashFromSavings(ssnum2, userName, password, amount);
            Assert.fail("withdrawal from someone who has no savings account should have failed but did not");
        }
        catch (Exception e) {
        }
        //withdraw too much money from someone who has a savings account
        try {
            bank.withdrawCashFromSavings(ssnum3, userName, password, amount * 3);
            Assert.fail("withdrawal of more funds than were in the account should've failed but did not");
        } catch (Exception e) {
        }
    }

    @Test
    public void testGetBalances() {
        Bank bank = getBank();
        long ssnum1 = 123456789;
        long ssnum2 = 123456789 + 20;
        long ssnum3 = 123456789 + 10;
        String userName = "diament";
        String password = "1234";
        double amount = 1000;
        //create 3 accounts
        try {
            bank.createNewPatron("Judah", "Diament", ssnum2, userName, password);
            bank.createNewPatron("Judah", "Diament", ssnum1, userName, password);
            bank.createNewPatron("Judah", "Diament", ssnum3, userName, password);
            bank.openSavingsAccount(ssnum1, userName, password);
            bank.openSavingsAccount(ssnum2, userName, password);
            bank.openSavingsAccount(ssnum3, userName, password);
            bank.depositCashIntoSavings(ssnum1, userName, password, amount);
            bank.depositCashIntoSavings(ssnum2, userName, password, amount * 2);
            bank.depositCashIntoSavings(ssnum3, userName, password, amount * 3);
            //withdraw some cash
            bank.withdrawCashFromSavings(ssnum3, userName, password, amount * 2);
            //check total remaining in ssnum3
            Assert.assertEquals("Incorrect total savings cash in SS# " + ssnum3, 998, bank.checkBalanceSavings(ssnum3, userName, password), 0);
            //check that the correct total remains in the bank
            Assert.assertEquals("Incorrect total savings cash in bank", 3996, bank.getTotalSavingsInBank(), 0);
        }
        catch (Exception e) {
            e.printStackTrace();
            Assert.fail("getTotalSavingsInBank incorrectly failed");
        }
    }

    @Test
    public void testGetNetWorth()throws Exception {
        Bank bank = getBank();
        long ssnum1 = 123456789;
        long ssnum2 = 123456789 + 20;
        long ssnum3 = 123456789 + 10;
        String userName = "diament";
        String password = "1234";
        double amount = 1000;
        //create 3 accounts
        try {
            bank.createNewPatron("Judah", "Diament", ssnum1, userName, password);
            bank.openSavingsAccount(ssnum1, userName, password);
            bank.depositCashIntoSavings(ssnum1, userName, password, amount);
            //do other things with other patrons
            bank.createNewPatron("Judah", "Diament", ssnum2, userName, password);
            bank.openSavingsAccount(ssnum2, userName, password);
            bank.depositCashIntoSavings(ssnum2, userName, password, amount);
            bank.createNewPatron("Judah", "Diament", ssnum3, userName, password);
            bank.openSavingsAccount(ssnum3, userName, password);
            //deposit more cash
            bank.depositCashIntoSavings(ssnum1, userName, password, amount);
            //withdraw some cash from a different patron
            bank.withdrawCashFromSavings(ssnum2, userName, password, amount / 2);
            //check that the correct total remains
            Assert.assertEquals("Incorrect Net Worth for SS# " + ssnum1, 1998, bank.getNetWorth(ssnum1, userName, password), 0);
        }
        catch (Exception e) {
            e.printStackTrace();
            Assert.fail("getTotalSavingsInBank incorrectly failed");
        }
    }

    //test getTransactionHistory
    @Test
    public void testGetTransactionHistory() {
        Bank bank = getBank();
        long ssnum1 = 123456789;
        long ssnum2 = 123456789 + 20;
        long ssnum3 = 123456789 + 10;
        String userName = "diament";
        String password = "1234";
        //create 3 accounts
        try {
            bank.createNewPatron("Judah", "Diament", ssnum2, userName, password);
            bank.createNewPatron("Judah", "Diament", ssnum1, userName, password);
            bank.createNewPatron("Judah", "Diament", ssnum3, userName, password);
            bank.openSavingsAccount(ssnum1, userName, password);
            bank.openSavingsAccount(ssnum3, userName, password);
            //deposit into 2 of them
            double amount = 1000.57;
            bank.depositCashIntoSavings(ssnum1, userName, password, amount);
            //withdraw correctly twice, check balance at the end to make sure deposits and withdrawals actually happening
            bank.withdrawCashFromSavings(ssnum1, userName, password, amount - 2);
            bank.depositCashIntoSavings(ssnum1, userName, password, amount);
            bank.withdrawCashFromSavings(ssnum1, userName, password, amount - 5);
            bank.depositCashIntoSavings(ssnum1, userName, password, amount);
            Transaction[] history = bank.getTransactionHistory(ssnum1,userName,password);
            int depositCount = 0;
            int withdrawalCount = 0;
            for(Transaction tx : history){
                if(tx == null){
                    break;
                }
                if(tx.returnType() == Transaction.TRANSACTION_TYPE.DEPOSIT){
                    depositCount++;
                }
                if(tx.returnType() == Transaction.TRANSACTION_TYPE.WITHDRAW){
                    withdrawalCount++;
                }
            }
            Assert.assertEquals("incorrect number of deposits in TX history",3,depositCount);
            Assert.assertEquals("incorrect number of withdrawals in TX history",2,withdrawalCount);
        }
        catch (Exception e) {
            e.printStackTrace();
            Assert.fail("testGetTransactionHistory failed");
        }
    }
}