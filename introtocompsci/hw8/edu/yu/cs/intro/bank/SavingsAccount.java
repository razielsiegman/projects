package edu.yu.cs.intro.bank; ;


public class SavingsAccount extends Account {

    protected SavingsAccount(long accountNumber, Patron patron) {
        super(accountNumber,patron);
    }

    @Override
    protected double getTotalBalance() {
        return cash;
    }

    @Override
    protected double getAvailableBalance() {
        return cash;}
}