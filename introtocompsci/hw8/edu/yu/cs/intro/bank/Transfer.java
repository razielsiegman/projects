package edu.yu.cs.intro.bank;

import edu.yu.cs.intro.bank.exceptions.InsufficientAssetsException;
import edu.yu.cs.intro.bank.exceptions.NoSuchAccountException;
import edu.yu.cs.intro.bank.exceptions.UnauthorizedActionException;

public class Transfer extends Transaction {
    protected final Account source;

    /**
     * in addition to the checks done by the super constructor, you must check that the patron is an owner of the source account
     */
    public Transfer(double amount, Account source, Account target, Patron patron) throws NoSuchAccountException, InsufficientAssetsException, UnauthorizedActionException {
        super(amount,target,patron);
        this.source = source;
    }

    public Account getSource(){
        return source;
    }

    @Override
    public long getTime(){
        return time;
    }

    @Override
    public double getAmount(){
        return amount;
    }

    @Override
    public Account getTarget(){
        return target;
    }

    @Override
    public Patron getPatron() {
        return patron;
    }

    /**
     * Check that there us enough cash in the source account. If so, transfer from source to target and set the transaction time.
     * If not, throw InsufficientAssetsException
     */
    @Override
    public void execute() throws InsufficientAssetsException {
        Account withdrawAccount = this.getSource();
        Account depositAccount = this.getTarget();
        withdrawAccount.withdrawCash(amount);
        depositAccount.depositCash(amount);
        this.setTime();
        withdrawAccount.addTransactionToHistory(this);
        depositAccount.addTransactionToHistory(this);
    }
    
    protected void setTime(){
        this.time = System.currentTimeMillis();
    }
}