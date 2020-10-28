package edu.yu.cs.intro.simpleBank;

import edu.yu.cs.intro.simpleBank.exceptions.*;

import java.util.*;

public class Bank {

    private Set<Patron> bankPatrons;
    private Map<String,Double> stocksSymbolToPrice;
    /**
     * transaction history is no longer stored in each patron object. Instead, the bank maintains a Map of transactions, mapping each Patron to the List of transactions that the given patron has executed.
     */
    private Map<Patron,List<Transaction>> txHistoryByPatron;
    protected final double transactionFee;

    private static Bank INSTANCE;

    public Bank(double transactionFee){
        this.txHistoryByPatron = new HashMap<Patron,List<Transaction>>();
        this.stocksSymbolToPrice = new TreeMap<String,Double>();
        this.bankPatrons = new HashSet<Patron>();
        this.transactionFee = transactionFee;
        INSTANCE = this;
    }

    public static Bank getBank(){
        return INSTANCE;
    }

    /**
     * Lists a new stock with the given symbol at the given price
     * @return false if the stock was previously listed, true if it was added as a result of this call
     */
    protected boolean addNewStockToMarket(String tickerSymbol, double sharePrice){
        boolean contain = true;
        if(stocksSymbolToPrice.containsKey(tickerSymbol)){
            contain = false;
        }
        else{
            stocksSymbolToPrice.put(tickerSymbol, sharePrice);
        }
        return contain;
    }

    /**
     * @return the stock price for the given stock ticker symbol. Return 0 if there is no such stock.
     */
    public double getStockPrice(String symbol){
        double price = 0;
        price = stocksSymbolToPrice.getOrDefault(symbol, 0.0);
        return price;
    }

    /**
     * @return a set of the stock ticker symbols listed in this bank
     */
    public Set<String> getAllStockTickerSymbols(){
        Set<String> allStocks = stocksSymbolToPrice.keySet();
        return allStocks;
    }

    /**
     * @return the total number of shares of the given stock owned by all patrons combined
     * if there is no such Stock or if the tickerSymbol is empty or null, return 0
     */
    public int getNumberOfOutstandingShares(String tickerSymbol){
        int shares = 0;
        for(Patron patron : bankPatrons){
            if(patron.getBrokerageAccount() != null){
                BrokerageAccount brokerage = patron.getBrokerageAccount();
                shares += brokerage.getNumberOfShares(tickerSymbol);
            }
        }
        return shares;
    }

    /**
     * @return the total number of shares of the given stock owned by all patrons combined multiplied by the price per share
     * if there is no such Stock or if the tickerSymbol is empty or null, return 0
     */
    public int getMarketCapitalization(String tickerSymbol){
        double marketCap = 0;
        int shares = this.getNumberOfOutstandingShares(tickerSymbol);
        double sharesAsDouble = (double)shares;
        double price = stocksSymbolToPrice.getOrDefault(tickerSymbol, 0.0);
        marketCap = price * shares;
        return (int)marketCap;
    }

    /**
     * @return all the cash in all savings accounts added up
     */
    public double getTotalSavingsInBank(){
        double cash = 0;
        for(Patron patron : bankPatrons){
            if(patron.getSavingsAccount() != null){
                Account savings = patron.getSavingsAccount();
                cash += savings.getAvailableBalance();
            }
        }
        return cash;
    }

    /**
     * @return all the cash in all brokerage accounts added up
     */
    public double getTotalBrokerageCashInBank(){
        double cash = 0;
        for(Patron patron : bankPatrons){
            if(patron.getBrokerageAccount() != null){
                BrokerageAccount brokerage = patron.getBrokerageAccount();
                cash += brokerage.getAvailableBalance();
            }
        }
        return cash;
    }

    /**
     * Creates a new Patron in the bank.
     */
    public void createNewPatron(String firstName, String lastName, long socialSecurityNumber, String userName, String password){
        Patron patron = new Patron(firstName, lastName, socialSecurityNumber, userName, password);
        List<Transaction> transactionList = new ArrayList<Transaction>();
        this.txHistoryByPatron.put(patron,transactionList);
        bankPatrons.add(patron);
    }

    /**
     * @return the account number of the opened account
     */
    public long openSavingsAccount(long socialSecurityNumber, String userName, String password) throws AuthenticationException{
        Patron patron = patronGenerator(socialSecurityNumber,userName,password);
        long accountNumber = this.accountNumberGenerator();
        Account savingsAccount = new Account(accountNumber);
        patron.addAccount(savingsAccount);
        return accountNumber;
    }

    /**
     * @return the account number of the opened account
     */
    public long openBrokerageAccount(long socialSecurityNumber, String userName, String password) throws AuthenticationException{
        Patron patron = patronGenerator(socialSecurityNumber,userName,password);
        long accountNumber = this.accountNumberGenerator();
        BrokerageAccount brokerageAccount = new BrokerageAccount(accountNumber);
        patron.addAccount(brokerageAccount);
        return accountNumber;
    }

    /**
     * Deposit cash into the given savings account
     */
    public void depositCashIntoSavings(long socialSecurityNumber, String userName, String password, double amount)throws AuthenticationException,UnauthorizedActionException,InsufficientAssetsException{
        Patron patron = patronGenerator(socialSecurityNumber,userName,password);
        this.checkSavingsAccount(patron);
        Transaction deposit = new Transaction(patron, Transaction.TRANSACTION_TYPE.DEPOSIT, patron.getSavingsAccount(), amount);
        this.adjustTxHistory(patron, deposit);
        deposit.execute();
    }

    /**
     * withdraw cash from the patron's savings account
     * throw AuthenticationException if the SS#, username, and password don't match a bank patron
     * throw UnauthorizedActionException if the given patron does not have a savings account
     * throw InsufficientAssetsException if that amount of money is not present the savings account
     */
    public void withdrawCashFromSavings(long socialSecurityNumber, String userName, String password, double amount) throws AuthenticationException,UnauthorizedActionException,InsufficientAssetsException{
        Patron patron = patronGenerator(socialSecurityNumber,userName,password);
        this.checkSavingsAccount(patron);
        Transaction withdraw = new Transaction(patron, Transaction.TRANSACTION_TYPE.WITHDRAW, patron.getSavingsAccount(), amount);
        this.adjustTxHistory(patron, withdraw);
        withdraw.execute();
    }
    /**
     * withdraw cash from the patron's brokerage account
     * throw AuthenticationException if the SS#, username, and password don't match a bank patron
     * throw UnauthorizedActionException if the given patron does not have a brokerage account
     * throw InsufficientAssetsException if that amount of CASH is not present the brokerage account
     */
    public void withdrawCashFromBrokerage(long socialSecurityNumber, String userName, String password, double amount) throws AuthenticationException,UnauthorizedActionException,InsufficientAssetsException{
        Patron patron = patronGenerator(socialSecurityNumber,userName,password);
        this.checkBrokerageAccount(patron);
        Transaction withdraw = new Transaction(patron, Transaction.TRANSACTION_TYPE.WITHDRAW, patron.getBrokerageAccount(), amount);
        this.adjustTxHistory(patron, withdraw);
        withdraw.execute();
    }
    /**
     * transfer cash from the patron's savings account to his brokerage account
    * throws AuthenticationException if the SS#, username, and password don't match a bank patron
    * throws UnauthorizedActionException if the given patron does not have both a savings account and a brokerage account
    * throws InsufficientAssetsException if that amount of money is not present in the savings account
    */
    public void transferFromSavingsToBrokerage(long socialSecurityNumber, String userName, String password, double amount) throws AuthenticationException,UnauthorizedActionException, InsufficientAssetsException {
        Patron patron = patronGenerator(socialSecurityNumber,userName,password);
        this.checkSavingsAccount(patron);
        this.checkBrokerageAccount(patron);
        Transaction withdraw = new Transaction(patron, Transaction.TRANSACTION_TYPE.WITHDRAW, patron.getSavingsAccount(), amount);
        Transaction deposit = new Transaction(patron, Transaction.TRANSACTION_TYPE.DEPOSIT, patron.getBrokerageAccount(), amount);
        this.adjustTxHistory(patron, withdraw);
        this.adjustTxHistory(patron, deposit);
        withdraw.execute();
        deposit.execute();
    }
    /**
    * transfer cash from the patron's savings account to his brokerage account
    * throws AuthenticationException if the SS#, username, and password don't match a bank patron
    * throws UnauthorizedActionException if the given patron does not have both a savings account and a brokerage account
    * throws InsufficientAssetsException if that amount of money is not present in CASH in the brokerage account
    */
    public void transferFromBrokerageToSavings(long socialSecurityNumber, String userName, String password, double amount) throws AuthenticationException,UnauthorizedActionException, InsufficientAssetsException{
        Patron patron = patronGenerator(socialSecurityNumber,userName,password);
        this.checkSavingsAccount(patron);
        this.checkBrokerageAccount(patron);        
        Transaction withdraw = new Transaction(patron, Transaction.TRANSACTION_TYPE.WITHDRAW, patron.getBrokerageAccount(), amount);
        Transaction deposit = new Transaction(patron, Transaction.TRANSACTION_TYPE.DEPOSIT, patron.getSavingsAccount(), amount);
        this.adjustTxHistory(patron, withdraw);
        this.adjustTxHistory(patron, deposit);
        withdraw.execute();
        deposit.execute();
    }
     /**
     * check how much cash the patron has in his brokerage account
     * throw AuthenticationException if the SS#, username, and password don't match a bank patron
     * throw UnauthorizedActionException if the given patron does not have a brokerage account
     */
    public double checkCashInBrokerage(long socialSecurityNumber, String userName, String password) throws AuthenticationException,UnauthorizedActionException{
        Patron patron = patronGenerator(socialSecurityNumber,userName,password);
        this.checkBrokerageAccount(patron);
        BrokerageAccount brokerage = patron.getBrokerageAccount();
        double cash = brokerage.getAvailableBalance();
        return cash;
    }
    /**
     * check the total value of the patron's brokerage account
     * throw AuthenticationException if the SS#, username, and password don't match a bank patron
     * throw UnauthorizedActionException if the given patron does not have a brokerage account
     */
    public double checkTotalBalanceBrokerage(long socialSecurityNumber, String userName, String password) throws AuthenticationException,UnauthorizedActionException{
        Patron patron = patronGenerator(socialSecurityNumber,userName,password);
        this.checkBrokerageAccount(patron);
        BrokerageAccount brokerage = patron.getBrokerageAccount();
        double cash = brokerage.getTotalBalance();
        return cash;
    }
    /**
     * check how much cash the patron has in his savings account
     * throw AuthenticationException if the SS#, username, and password don't match a bank patron
     * throw UnauthorizedActionException if the given patron does not have a savings account
     */
    public double checkBalanceSavings(long socialSecurityNumber, String userName, String password) throws AuthenticationException,UnauthorizedActionException{
        Patron patron = patronGenerator(socialSecurityNumber,userName,password);
        this.checkSavingsAccount(patron);
        Account savings = patron.getSavingsAccount();
        double cash = savings.getAvailableBalance(); 
        return cash;
    }

    /**
     * buy shares of the given stock
     * throw AuthenticationException if the SS#, username, and password don't match a bank patron
     * throw UnauthorizedActionException if the given patron does not have a brokerage account
     * throw InsufficientAssetsException if the required amount of CASH is not present in the brokerage account
     */
    public void purchaseStock(long socialSecurityNumber, String userName, String password, String tickerSymbol, int shares) throws AuthenticationException,UnauthorizedActionException,InsufficientAssetsException{
        Patron patron = patronGenerator(socialSecurityNumber,userName,password);
        this.checkBrokerageAccount(patron);
        Transaction buyStock = new Transaction(patron, Transaction.TRANSACTION_TYPE.BUYSTOCK, patron.getBrokerageAccount(), (double)shares);
        buyStock.setStockSymbol(tickerSymbol);
        this.adjustTxHistory(patron, buyStock);
        buyStock.execute();
    }

    /**
     * sell shares of the given stock
     * throw AuthenticationException if the SS#, username, and password don't match a bank patron
     * throw UnauthorizedActionException if the given patron does not have a brokerage account
     * throw InsufficientAssetsException if the patron does not have the given number of shares of the given stock
     */
    public void sellStock(long socialSecurityNumber, String userName, String password, String tickerSymbol, int shares) throws AuthenticationException,UnauthorizedActionException,InsufficientAssetsException{
        Patron patron = patronGenerator(socialSecurityNumber,userName,password);
        this.checkBrokerageAccount(patron);
        Transaction sellStock = new Transaction(patron, Transaction.TRANSACTION_TYPE.SELLSTOCK, patron.getBrokerageAccount(), (double)shares);
        sellStock.setStockSymbol(tickerSymbol);
        this.adjustTxHistory(patron, sellStock);
        sellStock.execute();
    }

    /**
     * check the net worth of the patron
     * throw AuthenticationException if the SS#, username, and password don't match a bank patron
     * return 0 if the patron doesn't have any accounts
     */
    public double getNetWorth(long socialSecurityNumber, String userName, String password) throws AuthenticationException,UnauthorizedActionException{
        Patron patron = patronGenerator(socialSecurityNumber,userName,password);
        double netWorth = 0;
        if(patron.getBrokerageAccount() != null){
            this.checkBrokerageAccount(patron);
            double brokerageWorth = patron.getBrokerageAccount().getTotalBalance();
            netWorth += brokerageWorth;
        }
        if(patron.getSavingsAccount() != null){
            this.checkSavingsAccount(patron);
            double savingsWorth = patron.getSavingsAccount().getAvailableBalance();
            netWorth += savingsWorth;
        }
        return netWorth;
    }

    /**
     * Get the transaction history on all of the patron's accounts, i.e. the transaction histories of both the savings account and
     * brokerage account (whichever of the two exist), combined. The merged history should be sorted in time order, from oldest to newest.
     * If the patron has no transactions in his history, return an array of length 0.
     * throw AuthenticationException if the SS#, username, and password don't match a bank patron
     */
    public Transaction[] getTransactionHistory(long socialSecurityNumber, String userName, String password) throws AuthenticationException{
        Patron patron = patronGenerator(socialSecurityNumber,userName,password);
        List<Transaction> txHistory = txHistoryByPatron.get(patron);
        Transaction[] txArray = txHistory.toArray(new Transaction[0]);
        return txArray;
    }

    private long accountNumberGenerator(){
        long accountNumber = 0;
        do{
        double number = (Math.random() * 100000);
        accountNumber = (long)number;
        for(Patron patron : bankPatrons){
            if(patron.getSavingsAccount() != null){
                long savingsNumber = patron.getSavingsAccount().getAccountNumber();
                if(accountNumber == savingsNumber){
                    accountNumber = 0;
                }
            }
            if(patron.getBrokerageAccount() != null){
                long brokerageNumber = patron.getBrokerageAccount().getAccountNumber();
                if(accountNumber == brokerageNumber){
                    accountNumber = 0;
                }
            }
        }
        }while(accountNumber == 0);   
        return accountNumber;
    }

    private Patron patronGenerator(long socialSecurityNumber, String userName, String password)throws AuthenticationException{
        Patron thePatron = null;
        boolean ssExists = false;
        for(Patron patron : bankPatrons){
            if(patron.getSocialSecurityNumber() == socialSecurityNumber){
                thePatron = patron;
                ssExists = true;
            }
        }
        if(ssExists == false){
            throw new AuthenticationException();
        }
        if((thePatron.getUserName() != userName) || (thePatron.getPassword() != password)){
            throw new AuthenticationException();
        }
        return thePatron;
    }

    private void checkBrokerageAccount(Patron patron)throws UnauthorizedActionException{
        if(patron.getBrokerageAccount() == null){
        throw new UnauthorizedActionException();
        }
    }

    private void checkSavingsAccount(Patron patron)throws UnauthorizedActionException{
        if(patron.getSavingsAccount() == null){
            throw new UnauthorizedActionException();
        }
    }

    private void adjustTxHistory(Patron patron, Transaction transaction){
        List<Transaction> transactionList = txHistoryByPatron.get(patron);
        transactionList.add(transaction);
        txHistoryByPatron.put(patron, transactionList);
    }

    protected void clearVariables(){
        this.txHistoryByPatron = new HashMap<Patron,List<Transaction>>();
        this.stocksSymbolToPrice = new TreeMap<String,Double>();
        this.bankPatrons = new HashSet<Patron>();
    }
}