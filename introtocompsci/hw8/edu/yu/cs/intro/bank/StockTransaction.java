package edu.yu.cs.intro.bank;

import edu.yu.cs.intro.bank.exceptions.InsufficientAssetsException;
import edu.yu.cs.intro.bank.exceptions.NoSuchAccountException;
import edu.yu.cs.intro.bank.exceptions.UnauthorizedActionException;

public abstract class StockTransaction extends Transaction {

	protected final Bank.Stock stock;

    public StockTransaction(double amount, BrokerageAccount target, Patron patron, Bank.Stock stock) throws NoSuchAccountException, InsufficientAssetsException, UnauthorizedActionException {
        super(amount, target, patron);
        this.stock = stock;
    }

    protected Bank.Stock getStock(){
        return stock;
    }
}