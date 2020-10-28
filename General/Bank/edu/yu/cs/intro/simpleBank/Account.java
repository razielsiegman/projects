package edu.yu.cs.intro.simpleBank;

import edu.yu.cs.intro.simpleBank.exceptions.InsufficientAssetsException;

public class Account {
    private final long accountNumber;
    private double cash;

    protected Account(long accountNumber){
        this.accountNumber = accountNumber;
    }
    protected long getAccountNumber() {
        return this.accountNumber;
    }
    protected double getAvailableBalance(){
        return this.cash;
    }
    //*************************************************
    //below are methods you must complete inside this class
    //*************************************************
    protected void depositCash(double amount){
        Bank bank = Bank.getBank();
        double transactionFee = bank.transactionFee;
        double deposit = amount - transactionFee;
        this.cash += deposit;
    }
    protected void withdrawCash(double amount) throws InsufficientAssetsException{
        Bank bank = Bank.getBank();
        double transactionFee = bank.transactionFee;
        double withdraw = amount + transactionFee;
        if(this.cash < withdraw){
            throw new InsufficientAssetsException();
        }
        this.cash -= withdraw;
    }
}