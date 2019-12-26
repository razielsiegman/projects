package edu.yu.cs.intro.simpleBank;

import edu.yu.cs.intro.simpleBank.exceptions.InsufficientAssetsException;
import java.util.*;

public class BrokerageAccount extends Account {
    private Map<String,Integer> stocksToNumberOfShares;

    protected BrokerageAccount(long accountNumber) {
        super(accountNumber);
        this.stocksToNumberOfShares = new TreeMap<String,Integer>();
    }

    protected int getNumberOfShares(String stock){
        int shares = stocksToNumberOfShares.getOrDefault(stock, 0);
        return shares;
    }
    /**
     * Buy the given amount of the given stock. Must have enough cash in the account to purchase them.
     * If there is enough cash, reduce cash and increase shares of the given stock
     * If there is not enough cash, throw an InsufficientAssetsException
     */
    protected void buyShares(String stock, int shares) throws InsufficientAssetsException {
        Bank bank = Bank.getBank();
        double sharePrice = bank.getStockPrice(stock);
        double transactionFee = bank.transactionFee;
        double stockPrice = shares * sharePrice;
        double price = stockPrice + transactionFee;
        if(super.getAvailableBalance() < price){
            throw new InsufficientAssetsException();
        }
        else{
        int previousShares = this.getNumberOfShares(stock);
        int newShares = previousShares + shares;
        stocksToNumberOfShares.put(stock,newShares);
        super.withdrawCash(stockPrice);
        }
    }

    /**
     * Sell the given amount of the given stock. Must have enough shares in the account to sell.
     * If there are enough shares, reduce shares and increase cash.
     * If there are not enough shares, throw an InsufficientAssetsException
     */
    protected void sellShares(String stock, int shares) throws InsufficientAssetsException{
        Bank bank = Bank.getBank();
        double transactionFee = bank.transactionFee;
        if(super.getAvailableBalance() < transactionFee){
            throw new InsufficientAssetsException();
        }
        else{
        double sharePrice = bank.getStockPrice(stock);
        double stockPrice = shares * sharePrice;
        int previousShares = this.getNumberOfShares(stock);
        if(previousShares < shares){
            throw new InsufficientAssetsException();
        }
        int newShares = previousShares - shares;
        stocksToNumberOfShares.put(stock,newShares);
        stocksToNumberOfShares.remove(stock, 0);
        super.depositCash(stockPrice);
        }
    }

    /**
     * this method must return the total amount of cash + the total market value of all stocks owned.
     * The market value of a single stock is determined by multiplying the share price of the stock times the number of shares owned
     * @return
     */
    protected double getTotalBalance(){
        Bank bank = Bank.getBank();
        double balance = 0;
        for(String key : stocksToNumberOfShares.keySet()){
            double sharePrice = bank.getStockPrice(key);
            int numberOfShares = stocksToNumberOfShares.get(key);
            double stockValue = sharePrice * numberOfShares;
            balance += stockValue;
        }
        double cash = super.getAvailableBalance();
        balance += cash;
        return balance;
    }
}