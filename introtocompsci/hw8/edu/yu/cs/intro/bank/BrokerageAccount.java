package edu.yu.cs.intro.bank;

import edu.yu.cs.intro.bank.exceptions.InsufficientAssetsException;

public class BrokerageAccount extends Account {
    private double transactionFee;
    /**an array of stocks owned*/
    private Bank.Stock[] stocksOwned;
    /**the number of shares of each stock owned by this account*/
    private int[] numberOfShares;

    protected BrokerageAccount(long accountNumber, Patron patron, double transactionFee){
        super(accountNumber, patron);
        if(transactionFee < 0){
            throw new IllegalArgumentException("Can't have negative transactionfee");
        }
        this.transactionFee = transactionFee;
        this.stocksOwned = new Bank.Stock[0];
        this.numberOfShares = new int[0];
    }

    protected int getNumberOfShares(Bank.Stock stock){
        int number = 0;
        for(int i = 0; i < stocksOwned.length; i++){
            if((stocksOwned[i]).equals(stock)){
                number = numberOfShares[i];
            }
        }
        return number;
    }
    /**
     * Buy the given amount of the given stock. Must have enough cash in the account to purchase them.
     * If there is enough cash, reduce cash and increase shares of the given stock
     * If there is not enough cash, throw an InsufficientAssetsException
     */
    protected void buyShares(Bank.Stock stock, int shares) throws InsufficientAssetsException {
        int stockArraySlot = 0;
        double fundsNeeded = ((stock.getSharePrice() * (double)shares) + transactionFee);
        if(this.getAvailableBalance() < fundsNeeded){
            throw new InsufficientAssetsException();
        }
        this.withdrawCash(fundsNeeded);
        if(stocksOwned.length == 0){
            Bank.Stock[] startArray = new Bank.Stock[1];
            startArray[0] = stock;
            this.stocksOwned = startArray;
            int[] startArrayTwo = new int[1];
            startArrayTwo[0] = shares;
            this.numberOfShares = startArrayTwo;
        }
        else{
            for(int i = 0; i < stocksOwned.length; i++){
                if((stocksOwned[i]).equals(stock)){
                    stockArraySlot = i;
                    numberOfShares[i] += shares;
                }
                else{
                    Bank.Stock[] newArray = new Bank.Stock[(stocksOwned.length + 1)];
                    for(int j = 0; j < stocksOwned.length; j++){
                        newArray[j] = stocksOwned[j];
                    }
                    newArray[stocksOwned.length] = stock;
                    this.stocksOwned = newArray;
                    int[] newArrayTwo = new int[(numberOfShares.length + 1)];
                    for(int k = 0; k < numberOfShares.length; k++){
                    newArrayTwo[k] = numberOfShares[k];
                    }
                    newArrayTwo[numberOfShares.length] = shares;
                    this.numberOfShares = newArrayTwo;
                    break;
                }
            } 
        }
    }

    /**
     * Sell the given amount of the given stock. Must have enough shares in the account to sell.
     * If there are enough shares, reduce shares and increase cash.
     * If there are not enough shares, throw an InsufficientAssetsException
     */
    protected void sellShares(Bank.Stock stock, int shares) throws InsufficientAssetsException{
        int stockArraySlot = 0;
        for(int i = 0; i < stocksOwned.length; i++){
            if((stocksOwned[i]).equals(stock)){
                stockArraySlot = i;
            }
        } 
        double saleValue = stock.getSharePrice() * (double)shares;
        double fundsNeeded = transactionFee;
        if((this.getAvailableBalance() + saleValue) < fundsNeeded){
            throw new InsufficientAssetsException();
        }
        if(numberOfShares[stockArraySlot] < shares){
            throw new InsufficientAssetsException();
        }
        numberOfShares[stockArraySlot] -= shares;
        this.depositCash(saleValue);
        this.withdrawCash(fundsNeeded);
        if(numberOfShares[stockArraySlot] == 0){
            Bank.Stock[] newArray = new Bank.Stock[(stocksOwned.length - 1)];
            for(int i = 0; i < stockArraySlot; i++){
                newArray[i] = stocksOwned[i];
            }
            for(int i = (stockArraySlot + 1); i < stocksOwned.length; i++){
                newArray[i - 1] = stocksOwned[i];
            }
            this.stocksOwned = newArray;
            int[] newArrayTwo = new int[(numberOfShares.length - 1)];
            for(int i = 0; i < stockArraySlot; i++){
                newArrayTwo[i] = numberOfShares[i];
            }
            for(int i = (stockArraySlot + 1); i < stocksOwned.length; i++){
                newArrayTwo[i - 1] = numberOfShares[i];
            }
            this.numberOfShares = newArrayTwo;
        }
    }

    /**
     * this method must return the total amount of cash + the total market value of all stocks owned.
     * The market value of a single stock is determined by multiplying the share price of the stock times the number of shares owned
     * @return
     */
    @Override
    protected double getTotalBalance(){
        double marketValue = 0;
        for(int i = 0; i < stocksOwned.length; i++){
            marketValue += (this.getNumberOfShares(stocksOwned[i]) * stocksOwned[i].getSharePrice());
        }
        marketValue += this.getAvailableBalance();
        return marketValue;
    }   

    /**
     * this method must return total cash
     * @return
     */
    @Override
    protected double getAvailableBalance() {
        double cash = this.cash;
        return cash;
    }
}