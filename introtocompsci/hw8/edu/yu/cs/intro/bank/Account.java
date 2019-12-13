package edu.yu.cs.intro.bank;

import edu.yu.cs.intro.bank.exceptions.InsufficientAssetsException;

import java.util.Arrays;

public abstract class Account {
    private final long accountNumber;
    private Patron patron;
    private Transaction[] txHistory;
    protected double cash;

    protected Account(long accountNumber, Patron patron){
        this.accountNumber = accountNumber;
        this.patron = patron;
        this.txHistory = new Transaction[0];
        this.cash = 0;
    }
    protected long getAccountNumber() {
        return this.accountNumber;
    }
    protected Patron getPatron() {
        return this.patron;
    }
    /**
     * returns a copy of the txHistory array
     * why do you think we return a copy and not the original array?
     */
    protected Transaction[] getTransactionHistory(){
        return Arrays.copyOf(this.txHistory,this.txHistory.length);
    }

    protected void depositCash(double amount){
        if(amount < 0){
            throw new IllegalArgumentException();
        }
        this.cash += amount;
    }
    //*************************************************
    //below are methods you must complete in this class
    //*************************************************
    /**add a tx to the tx history of this account*/
    protected void addTransactionToHistory(Transaction tx){
        Transaction[] newArray = new Transaction[(txHistory.length + 1)];
        for(int i = 0; i < txHistory.length; i++){
            newArray[i] = txHistory[i];
        }
        newArray[txHistory.length] = tx;
        this.txHistory = newArray;
    }

    protected void withdrawCash(double amount) throws InsufficientAssetsException{
        if(amount < 0){
            throw new IllegalArgumentException();
        }
        if(cash < amount){
            throw new InsufficientAssetsException();
        }
        this.cash -= amount;
    }

    //*********************************************************
    //below are abstract methods that subclasses must implement
    //*********************************************************
    protected abstract double getTotalBalance();
    protected abstract double getAvailableBalance();
}