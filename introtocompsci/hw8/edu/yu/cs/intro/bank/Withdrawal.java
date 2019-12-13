package edu.yu.cs.intro.bank;

import edu.yu.cs.intro.bank.exceptions.InsufficientAssetsException;
import edu.yu.cs.intro.bank.exceptions.NoSuchAccountException;
import edu.yu.cs.intro.bank.exceptions.UnauthorizedActionException;

public class Withdrawal extends Transaction {

    protected Withdrawal(double amount, Account target, Patron patron) throws NoSuchAccountException, InsufficientAssetsException, UnauthorizedActionException {
        super(amount, target, patron);
    }

    @Override
    protected long getTime() {
        return time;
    }

    @Override
    protected double getAmount() {
        return amount;
    }

    @Override
    protected Account getTarget() {
        return target;
    }

    @Override
    protected Patron getPatron() {
        return patron;
    }

    /**
     * if the account does not have enough cash to withdraw the amount, throw an InsufficientAssetsException.
     * if it does have enough, decrease the cash by the given amount
     */
    @Override
    protected void execute() throws InsufficientAssetsException{
        Account account = this.getTarget();
        account.withdrawCash(amount);
        this.setTime();
        account.addTransactionToHistory(this);
    }

    protected void setTime(){
        this.time = System.currentTimeMillis();
    }
}