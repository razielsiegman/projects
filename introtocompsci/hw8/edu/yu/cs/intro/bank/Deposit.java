package edu.yu.cs.intro.bank;

import edu.yu.cs.intro.bank.exceptions.InsufficientAssetsException;
import edu.yu.cs.intro.bank.exceptions.NoSuchAccountException;
import edu.yu.cs.intro.bank.exceptions.UnauthorizedActionException;

public class Deposit extends Transaction {

    protected Deposit(double amount, Account target, Patron patron) throws NoSuchAccountException, InsufficientAssetsException, UnauthorizedActionException {
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
     * Deposit the cash into the account by increasing the accounts cash amount.
     * Set the time of transaction execution.
     */
    @Override
    protected void execute() throws InsufficientAssetsException {
        Account account = this.getTarget();
        account.depositCash(amount);
        this.setTime();
        account.addTransactionToHistory(this);
    }
    
    protected void setTime(){
        this.time = System.currentTimeMillis();
    }
}   