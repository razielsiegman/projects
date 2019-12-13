package edu.yu.cs.intro.bank;

import edu.yu.cs.intro.bank.exceptions.InsufficientAssetsException;
import edu.yu.cs.intro.bank.exceptions.NoSuchAccountException;
import edu.yu.cs.intro.bank.exceptions.UnauthorizedActionException;

public class StockPurchase extends StockTransaction {
    /**
     * check that the given brokerage account has enough cash in it to but the "amount" number of shares of the given stock.
     * if not, throw InsufficientAssetsException
     */
    public StockPurchase(double amount, BrokerageAccount target, Patron patron, Bank.Stock stock) throws NoSuchAccountException, InsufficientAssetsException, UnauthorizedActionException {
        super(amount, target, patron, stock);
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
     * check that the given brokerage account has enough cash in it to but the "amount" number of shares of the given stock. If not, throw InsufficientAssetsException.
     * Buy the stock, i.e. use the methods on the patron's brokerage account to make the purchase.
     * Set the time of transaction execution.
     */
    @Override
    protected void execute() throws InsufficientAssetsException {
        BrokerageAccount brokerageAccount = patron.getBrokerageAccount();
        brokerageAccount.buyShares(stock, (int)amount);
        this.setTime();
        brokerageAccount.addTransactionToHistory(this);
    }

    protected void setTime(){
        this.time = System.currentTimeMillis();
    }
}