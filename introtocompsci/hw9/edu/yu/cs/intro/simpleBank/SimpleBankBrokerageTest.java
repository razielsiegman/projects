package edu.yu.cs.intro.simpleBank;

import org.junit.Assert;
import org.junit.Test;
import java.util.*;
import org.junit.After;


public class SimpleBankBrokerageTest {

    private static final String IBM = "IBM";
    private static final String AMAZON = "AMZN";
    private static final String GOOGLE = "GOOG";
    private final long ssnum = 123456789;
    private final String userName = "diament";
    private final String password = "1234";

    private Bank getBank() {
        Bank bank = new Bank(1);
        return bank;
    }

    @After
    public void resetAfterTest(){
        Bank bank = getBank();
        bank.clearVariables();
    }
    //test open brokerage account

    @Test
    public void testOpenBrokerageAccount() {
        Bank bank = getBank();
        try {
            bank.createNewPatron("Judah", "Diament", ssnum, userName, password);
            bank.openBrokerageAccount(ssnum, userName, password);
        }
        catch (Exception e) {
            e.printStackTrace();
            Assert.fail("failed to open brokerage account: ");
        }
    }

    //test add stock to market
    //test getStockBySymbol
    //test getListOfAllStockTickerSymbols
    @Test
    public void testAddStocksToMarket() {
        Bank bank = getBank();
        bank.addNewStockToMarket(IBM, 100);
        Assert.assertEquals("incorrect stock price", 100, bank.getStockPrice(IBM), 10);
        bank.addNewStockToMarket(AMAZON, 1500);
        bank.addNewStockToMarket(GOOGLE, 2000);
        boolean foundIbm = false;
        boolean foundAmazon = false;
        boolean foundGoogle = false;
        Set<String> allSymbols = bank.getAllStockTickerSymbols();
        for (String symbol : allSymbols) {
            if (symbol == null) {
                break;
            }
            else if (symbol.equals(IBM)) {
                foundIbm = true;
            }
            else if (symbol.equals(AMAZON)) {
                foundAmazon = true;
            }
            else if (symbol.equals(GOOGLE)) {
                foundGoogle = true;
            }
        }
        if (!foundAmazon || !foundGoogle || !foundIbm) {
            Assert.fail("did not find all stocks symbols in array returned by getListOfAllStockTickerSymbols()");
        }

    }

    //test deposit cash in savings, and transfer to and from brokerage - check amounts on both accounts after each step
    //test checkAvailableBalanceBrokerage
    @Test
    public void testCashTransfers() {
        Bank bank = getBank();
        try {
            bank.createNewPatron("Judah", "Diament", ssnum, userName, password);
            bank.openBrokerageAccount(ssnum, userName, password);
            bank.openSavingsAccount(ssnum, userName, password);
        }
        catch (Exception e) {
            e.printStackTrace();
            Assert.fail("failed to open accounts: ");
        }
        try {
            double amount = 5000;
            bank.depositCashIntoSavings(ssnum, userName, password, amount);
            //transfer from savings to brokerage
            bank.transferFromSavingsToBrokerage(ssnum, userName, password, 2000);
            Assert.assertEquals("incorrect cash balance in savings account", 2998, bank.checkBalanceSavings(ssnum, userName, password), 0);
            Assert.assertEquals("incorrect cash balance in brokerage account", 1999, bank.checkCashInBrokerage(ssnum, userName, password), 0);
            //transfer from brokerage to savings
            bank.transferFromBrokerageToSavings(ssnum, userName, password, 1000);
            Assert.assertEquals("incorrect cash balance in savings account", 3997, bank.checkBalanceSavings(ssnum, userName, password), 0);
            Assert.assertEquals("incorrect cash balance in brokerage account", 998, bank.checkCashInBrokerage(ssnum, userName, password), 0);
            //transfer from savings to brokerage
            bank.transferFromSavingsToBrokerage(ssnum, userName, password, 3996);
            Assert.assertEquals("incorrect cash balance in savings account", 0, bank.checkBalanceSavings(ssnum, userName, password), 0);
            Assert.assertEquals("incorrect cash balance in brokerage account", 4993, bank.checkCashInBrokerage(ssnum, userName, password), 0);

        }
        catch (Exception e) {
            e.printStackTrace();
            Assert.fail("failed to transfer money between accounts: ");
        }
    }

    //test withdrawCashFromBrokerage
    @Test
    public void testWithdrawCashFromBrokerage() {
        Bank bank = getBank();
        try {
            bank.createNewPatron("Judah", "Diament", ssnum, userName, password);
            bank.openBrokerageAccount(ssnum, userName, password);
            bank.openSavingsAccount(ssnum, userName, password);
            double amount = 5000;
            System.out.println("expected 0" + bank.checkCashInBrokerage(ssnum, userName, password));
            bank.depositCashIntoSavings(ssnum, userName, password, amount);
            //transfer from savings to brokerage
            bank.transferFromSavingsToBrokerage(ssnum, userName, password, amount - 9);
            bank.withdrawCashFromBrokerage(ssnum, userName, password, amount - 1000);
            Assert.assertEquals("incorrect balance in brokerage account", 989, bank.checkCashInBrokerage(ssnum, userName, password), 0);
        }
        catch (Exception e) {
            e.printStackTrace();
            Assert.fail("failed to withdraw cash from brokerage: ");
        }
    }

    private Bank purchaseStocks() {
        Bank bank = getBank();
        try {
            //add stocks to market
            bank.addNewStockToMarket(IBM, 100);
            bank.addNewStockToMarket(AMAZON, 1500);
            bank.addNewStockToMarket(GOOGLE, 2000);
            //create accounts and deposit money
            bank.createNewPatron("Judah", "Diament", ssnum, userName, password);
            bank.openBrokerageAccount(ssnum, userName, password);
            bank.openSavingsAccount(ssnum, userName, password);
            bank.depositCashIntoSavings(ssnum, userName, password, 75000);
            bank.transferFromSavingsToBrokerage(ssnum, userName, password, 50000);
            //buy stocks
            //buy 100 shares of IBM
            bank.purchaseStock(ssnum, userName, password, IBM, 100);
            //buy 10 shares of Google
            bank.purchaseStock(ssnum, userName, password, GOOGLE, 10);
            //cash left in brokerage: 20000
        }
        catch (Exception e) {
            e.printStackTrace();
            Assert.fail("failed to purchase stocks: ");
        }
        return bank;
    }

    //test purchaseStock
    @Test
    public void testPurchaseStock() {
        Bank bank = this.purchaseStocks();
        try {
            //check cash in brokerage - should be 20000
            Assert.assertEquals("incorrect amount of cash in brokerage account", 20000, bank.checkCashInBrokerage(ssnum, userName, password), 100);
            //check total value of brokerage - should be 50000
            Assert.assertEquals("incorrect amount of cash in brokerage account", 50000, bank.checkTotalBalanceBrokerage(ssnum, userName, password), 100);
        }
        catch (Exception e) {
            e.printStackTrace();
            Assert.fail("incorrect balance in brokerage account after purchase of stocks: ");
        }
    }

    //test getNetWorth
    @Test
    public void testGetNetWorth() {
        Bank bank = this.purchaseStocks();
        try {
            Assert.assertEquals("incorrect net worth", 75000, bank.getNetWorth(ssnum, userName, password), 100);
        }
        catch (Exception e) {
            e.printStackTrace();
            Assert.fail("testGetNetWorthFailed ");
        }
    }

    //test sellStock
    @Test
    public void testSellStock() {
        Bank bank = this.purchaseStocks();
        //sell stocks that you actually have, checking cash and total value of brokerage account afterwards
        try {
            bank.sellStock(ssnum, userName, password, IBM, 50);
            double cash = bank.checkCashInBrokerage(ssnum, userName, password);
            Assert.assertEquals("Incorrect amount of brokerage cash after stock sale", 25000, cash, 10);
            double total = bank.checkTotalBalanceBrokerage(ssnum, userName, password);
            Assert.assertEquals("Incorrect total balance in brokerage after stock sale", 50000, total, 10);
        }
        catch (Exception e) {
            e.printStackTrace();
            Assert.fail("failed to sell stocks ");
        }
        //attempt to sell stocks that you don't actually have, make sure it throws an exception
        try {
            bank.sellStock(ssnum, userName, password, AMAZON, 50);
            Assert.fail("should not have been able to sell a stock I don't have");
        }
        catch (Exception e) {
        }
    }

    //test getTransactionHistory
    @Test
    public void testGetTransactionHistory() {
        Bank bank = this.purchaseStocks();
        try {
            Transaction[] history = bank.getTransactionHistory(ssnum, userName, password);
            int depositCount = 0;
            int purchaseCount = 0;
            for (Transaction tx : history) {
                if (tx == null) {
                    break;
                }
                else if (tx.returnType() == Transaction.TRANSACTION_TYPE.DEPOSIT) {
                    depositCount++;
                }
                else if (tx.returnType() == Transaction.TRANSACTION_TYPE.BUYSTOCK) {
                    purchaseCount++;
                }
            }
            Assert.assertEquals("incorrect number of deposits in TX history", 2, depositCount);
            Assert.assertEquals("incorrect number of stock purchases in TX history", 2, purchaseCount);
        }
        catch (Exception e) {
            e.printStackTrace();
            Assert.fail("testGetTransactionHistory failed: ");
        }

    }

    @Test
    public void testGetTransactionHistoryZero() {
        Bank bank = getBank();
        try {
            bank.createNewPatron("a","b",ssnum,userName,password);
            Transaction[] history = bank.getTransactionHistory(ssnum, userName, password);
            Assert.assertTrue("incorrect history when no TXs executed yet", history == null || history.length == 0 || (history.length == 1 && history[0] == null));
        }
        catch (Exception e) {
            e.printStackTrace();
            Assert.fail("failed to getTransactionHistory ");
        }
    }

    //test getMarketCapitalization
    @Test
    public void testGetMarketCapitalization() {
        Bank bank = this.purchaseStocks();
        try {
            bank.createNewPatron("a", "b", 123, "a", "b");
            bank.openSavingsAccount(123,"a","b");
            bank.openBrokerageAccount(123,"a","b");
            bank.depositCashIntoSavings(123, "a", "b", 100002);
            bank.transferFromSavingsToBrokerage(123,"a","b",100000);
            bank.purchaseStock(123, "a", "b", IBM, 20);
            int mcap = bank.getMarketCapitalization(IBM);
            Assert.assertEquals("incorrect market capitalization", 12000, mcap);
        }
        catch (Exception e) {
            e.printStackTrace();
            Assert.fail("failed to get market capitalization ");
        }
    }

    @Test
    public void testGetMarketCapitalizationZero() {
        Bank bank = getBank();
        try {
            double shares = bank.getMarketCapitalization(AMAZON);
            Assert.assertEquals("incorrect market cap when total = 0", 0, shares, 0.1);
        }
        catch (Exception e) {
            e.printStackTrace();
            Assert.fail("failed to properly get market cap of zero when total there are no shares of the stock on the market ");
        }
    }

    //test getNumberOfOutstandingShares
    @Test
    public void testGetNumberOfOutstandingShares() {
        Bank bank = this.purchaseStocks();
        int two = bank.getNumberOfOutstandingShares(IBM);
        System.out.println("Num Of Shares" + two);
        try {
            bank.createNewPatron("a", "b", 123, "a", "b");
            bank.openSavingsAccount(123,"a","b");
            bank.openBrokerageAccount(123,"a","b");
            bank.depositCashIntoSavings(123, "a", "b", 100000);
            bank.transferFromSavingsToBrokerage(123,"a","b",20000);
            bank.purchaseStock(123, "a", "b", IBM, 20);
            int nos = bank.getNumberOfOutstandingShares(IBM);
            Assert.assertEquals("incorrect number of outstanding shares", 120, nos);
        }
        catch (Exception e) {
            e.printStackTrace();
            Assert.fail("failed to getNumberOfOutstandingShares");
        }
    }

    @Test
    public void testGetNumberOfOutstandingSharesZero() {
        Bank bank = getBank();
        try {
            double shares = bank.getNumberOfOutstandingShares(AMAZON);
            Assert.assertEquals("incorrect total outstanding shares when total = 0", 0, shares, 0.1);
        }
        catch (Exception e) {
            e.printStackTrace();
            Assert.fail("failed to getNumberOfOutstandingShares when total = 0");
        }
    }

    //test getTotalBrokerageCashInBank
    @Test
    public void testGetTotalBrokerageCashInBank() {
        Bank bank = this.purchaseStocks();
        try {
            bank.createNewPatron("a", "b", 123, "a", "b");
            bank.openSavingsAccount(123,"a","b");
            bank.openBrokerageAccount(123,"a","b");
            bank.depositCashIntoSavings(123, "a", "b", 100000);
            bank.transferFromSavingsToBrokerage(123,"a","b",100000 - 2);
            bank.purchaseStock(123, "a", "b", IBM, 20);
            double bcash = bank.getTotalBrokerageCashInBank();
            Assert.assertEquals("incorrect total brokerage cash in bank", 117993, bcash, 0);
        }
        catch (Exception e) {
            e.printStackTrace();
            Assert.fail("failed to getTotalBrokerageCashInBank");
        }
    }

    @Test
    public void testGetTotalBrokerageCashInBankZero() {
        Bank bank = getBank();
        try {
            double bcash = bank.getTotalBrokerageCashInBank();
            Assert.assertEquals("incorrect total brokerage cash in bank when total = 0", 0, bcash, 0.1);
        }
        catch (Exception e) {
            e.printStackTrace();
            Assert.fail("failed to getTotalBrokerageCashInBank when total = 0");
        }
    }
}
