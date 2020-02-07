package edu.yu.cs.intro.bank;

import edu.yu.cs.intro.bank.exceptions.*;

public class Bank{
    public static Bank INSTANCE = new Bank();
    public Bank(){
        this.bankPatrons = new Patron[0];
        this.bankAccounts = new Account[0];
        this.stocksOnMarket = new Stock[0];
    }
    public static Bank getBank() {
        return INSTANCE;
    }

    private Patron[] bankPatrons;
    private Account[] bankAccounts;
    private Stock[] stocksOnMarket;

    /**
     * lists a new stock with the given symbol at the given price
     */
    protected Patron[] getBankPatrons(){
        return bankPatrons;
    }

    protected Account[] getBankAccounts(){
        return bankAccounts;
    }

    protected Stock[] getStocksOnMarket(){
        return stocksOnMarket;
    }

    protected void addNewStockToMarket(String tickerSymbol, double sharePrice){
        Stock stock = new Stock(tickerSymbol, sharePrice);
            Stock[] arrayPlusOne = new Stock[stocksOnMarket.length + 1];
            for(int i = 0; i < stocksOnMarket.length; i++){
                arrayPlusOne[i] = stocksOnMarket[i];
            }
            arrayPlusOne[stocksOnMarket.length] = stock;
            this.stocksOnMarket = arrayPlusOne;
    }
    /**
     * return the stock object for the given stock ticker symbol
     */
    public Stock getStockBySymbol(String symbol){
        int stockArraySlot = 0;
        for(int i = 0; i < stocksOnMarket.length; i++){
            if((stocksOnMarket[i].getTickerSymbol()).equals(symbol)){
                stockArraySlot = i;
            }
        }
        Stock stock = stocksOnMarket[stockArraySlot];
        return stock;
    }
    /**
     * @return an array of all the stock ticker symbols
     */
    public String[] getListOfAllStockTickerSymbols(){
        String[] tickerArray = new String[stocksOnMarket.length];
        for(int i = 0; i < stocksOnMarket.length; i++){
            tickerArray[i] = stocksOnMarket[i].getTickerSymbol();
        }
        return tickerArray;
    }
    /**
     * @return the total number of shares of the given stock owned by all patrons combined
     * if there is no such Stock or if the tickerSymbol is empty or null, return 0
     */
    public int getNumberOfOutstandingShares(String tickerSymbol){
        int numberOfShares = 0;
        try{
        Stock stock = this.getStockBySymbol(tickerSymbol);
        for(int i = 0; i < bankAccounts.length; i++){
            if(bankAccounts[i] instanceof BrokerageAccount){
                BrokerageAccount account = (BrokerageAccount)bankAccounts[i];
                numberOfShares += account.getNumberOfShares(stock);
            }
        }
        }catch(ArrayIndexOutOfBoundsException e){}
        return numberOfShares;
    }

    /**
     * @return the total number of shares of the given stock owned by all patrons combined multiplied by the price per share
     * if there is no such Stock or if the tickerSymbol is empty or null, return 0
     */
    public int getMarketCapitalization(String tickerSymbol){
        int capitalizationAsInt = 0;
        try{
        int numberOfShares = this.getNumberOfOutstandingShares(tickerSymbol);
        Stock stock = this.getStockBySymbol(tickerSymbol);
        double sharePrice = stock.getSharePrice();
        double marketCapitalization = (double)numberOfShares * sharePrice;
        capitalizationAsInt = (int)marketCapitalization;
        }catch(ArrayIndexOutOfBoundsException e){}
        return capitalizationAsInt;
    }

    /**
     * @return all the cash in all savings accounts added up
     */
    public double getTotalSavingsInBank(){
        double totalSavings = 0;
        for(int i = 0; i < bankAccounts.length; i++){
            if(bankAccounts[i] instanceof SavingsAccount){
                totalSavings += bankAccounts[i].getAvailableBalance();
            }
        }
        return totalSavings;
    }

    /**
     * @return all the cash in all brokerage accounts added up
     */
    public double getTotalBrokerageCashInBank(){
        double totalBrokerage = 0;
        for(int i = 0; i < bankAccounts.length; i++){
            if(bankAccounts[i] instanceof BrokerageAccount){
                totalBrokerage += bankAccounts[i].getAvailableBalance();
            }
        }
        return totalBrokerage;
    }

    /**
     * Creates a new Patron in the bank.
     * Throws an UnauthorizedActionException if a Patron already exists with that social security number.
     */
    public void createNewPatron(String firstName, String lastName, long socialSecurityNumber, String userName, String password) throws UnauthorizedActionException {
        for(int i = 0; i < bankPatrons.length; i++){
            if(bankPatrons[i].getSocialSecurityNumber() == socialSecurityNumber){
                throw new UnauthorizedActionException();
            }
        }
        Patron patron = new Patron(firstName, lastName, socialSecurityNumber, userName, password);
        Patron[] arrayPlusOne = new Patron[bankPatrons.length + 1];
        for(int i = 0; i < bankPatrons.length; i++){
            arrayPlusOne[i] = bankPatrons[i];
        }
        arrayPlusOne[bankPatrons.length] = patron;
        this.bankPatrons = arrayPlusOne;
    }

    /**
     * @throws AuthenticationException if the user name or password doesn't match for the patron with the given social security number
     * @throws UnauthorizedActionException if the user already has a savings account
     * @retuns the account number of the opened account
     */
    public long openSavingsAccount(long socialSecurityNumber, String userName, String password) throws AuthenticationException,UnauthorizedActionException{
        Patron patron = this.checkIdentity(socialSecurityNumber, userName, password);
        if(patron.getSavingsAccount() != null){
            throw new UnauthorizedActionException();
        }
        long accountNumber = this.accountNumberGenerator();
        SavingsAccount newSavings = new SavingsAccount(accountNumber, patron);
        patron.addAccount(newSavings);
        Account[] arrayPlusOne = new Account[bankAccounts.length + 1];
        for(int i = 0; i < bankAccounts.length; i++){
            arrayPlusOne[i] = bankAccounts[i];
        }
        arrayPlusOne[bankAccounts.length] = (Account)newSavings;
        this.bankAccounts = arrayPlusOne;
        return accountNumber;
    }

    /**
     * @throws AuthenticationException if the user name or password doesn't match for the patron with the given social security number
     * @throws UnauthorizedActionException if the user already has a Brokerage account
     * @retuns the account number of the opened account
     */
    public long openBrokerageAccount(long socialSecurityNumber, String userName, String password) throws AuthenticationException,UnauthorizedActionException{
        Patron patron = this.checkIdentity(socialSecurityNumber, userName, password);
        if(patron.getBrokerageAccount() != null){
            throw new UnauthorizedActionException();
        }
        double transactionFee = 1;
        long accountNumber = this.accountNumberGenerator();
        BrokerageAccount newBrokerage = new BrokerageAccount(accountNumber, patron, transactionFee);
        patron.addAccount(newBrokerage);
        Account[] arrayPlusOne = new Account[bankAccounts.length + 1];
        for(int i = 0; i < bankAccounts.length; i++){
            arrayPlusOne[i] = bankAccounts[i];
        }
        arrayPlusOne[bankAccounts.length] = (Account)newBrokerage;
        this.bankAccounts = arrayPlusOne;
        return accountNumber;
    }

    /**
     * Deposit cash into the given savings account
     * throw AuthenticationException if the SS#, username, and password don't match a bank patron
     * throw UnauthorizedActionException if the given patron does not have a savings account
     */
    public void depositCashIntoSavings(long socialSecurityNumber, String userName, String password, double amount) throws AuthenticationException,UnauthorizedActionException{
        try{
        Patron patron = this.checkIdentity(socialSecurityNumber,userName,password);
        this.checkSavingsAccount(patron);
        Account savingsAccount = patron.getSavingsAccount();
        Deposit newDeposit = new Deposit(amount, savingsAccount, patron);
        newDeposit.execute();
        }catch(NoSuchAccountException e){
            throw new UnauthorizedActionException();
        }catch(InsufficientAssetsException e){
            e.printStackTrace();
        }
    }
    //CALLING NULL OBJECTS exception (not in bank)
    /** 
     * transfer cash from the patron's savings account to his brokerage account
     * throws AuthenticationException if the SS#, username, and password don't match a bank patron
     * throws UnauthorizedActionException if the given patron does not have both a savings account and a brokerage account
     * throws InsufficientAssetsException if that amount of money is not present in the savings account
     */
    public void transferFromSavingsToBrokerage(long socialSecurityNumber, String userName, String password, double amount) throws AuthenticationException,UnauthorizedActionException, InsufficientAssetsException {
        try{
        Patron patron = this.checkIdentity(socialSecurityNumber,userName,password);
        this.checkSavingsAccount(patron);
        this.checkBrokerageAccount(patron);
        Account savingsAccount = patron.getSavingsAccount();
        Account brokerageAccount = patron.getBrokerageAccount();
        Transfer newTransfer = new Transfer(amount, savingsAccount, brokerageAccount, patron);
        newTransfer.execute();
        }catch(NoSuchAccountException e){
            throw new UnauthorizedActionException();
        }
    }

    /**
     * transfer cash from the patron's savings account to his brokerage account
     * throws AuthenticationException if the SS#, username, and password don't match a bank patron
     * throws UnauthorizedActionException if the given patron does not have both a savings account and a brokerage account
     * throws InsufficientAssetsException if that amount of money is not present in CASH in the brokerage account
     */
    public void transferFromBrokerageToSavings(long socialSecurityNumber, String userName, String password, double amount) throws AuthenticationException,UnauthorizedActionException, InsufficientAssetsException{
        try{
        Patron patron = this.checkIdentity(socialSecurityNumber,userName,password);
        this.checkSavingsAccount(patron);
        this.checkBrokerageAccount(patron);
        Account savingsAccount = patron.getSavingsAccount();
        Account brokerageAccount = patron.getBrokerageAccount();
        Transfer newTransfer = new Transfer(amount, brokerageAccount, savingsAccount, patron);
        newTransfer.execute();
        }catch(NoSuchAccountException e){
            throw new UnauthorizedActionException();
        }
    }

    /**
     * withdraw cash from the patron's savings account
     * throw AuthenticationException if the SS#, username, and password don't match a bank patron
     * throw UnauthorizedActionException if the given patron does not have a savings account
     * throw InsufficientAssetsException if that amount of money is not present the savings account
     */
    public void withdrawCashFromSavings(long socialSecurityNumber, String userName, String password, double amount) throws AuthenticationException,UnauthorizedActionException,InsufficientAssetsException{
        try{
        Patron patron = this.checkIdentity(socialSecurityNumber,userName,password);
        this.checkSavingsAccount(patron);
        Account savingsAccount = patron.getSavingsAccount();
        Withdrawal newWithdrawal = new Withdrawal(amount, savingsAccount, patron);
        newWithdrawal.execute();
        }catch(NoSuchAccountException e){
            throw new UnauthorizedActionException();
        }
    }
    /**
     * withdraw cash from the patron's brokerage account
     * throw AuthenticationException if the SS#, username, and password don't match a bank patron
     * throw UnauthorizedActionException if the given patron does not have a brokerage account
     * throw InsufficientAssetsException if that amount of CASH is not present the brokerage account
     */
    public void withdrawCashFromBrokerage(long socialSecurityNumber, String userName, String password, double amount) throws AuthenticationException,UnauthorizedActionException,InsufficientAssetsException{
        try{
        Patron patron = this.checkIdentity(socialSecurityNumber,userName,password);
        this.checkBrokerageAccount(patron);  
        Account brokerageAccount = patron.getBrokerageAccount();
        Withdrawal newWithdrawal = new Withdrawal(amount, brokerageAccount, patron);
        newWithdrawal.execute();
        }catch(NoSuchAccountException e){
            throw new UnauthorizedActionException();
        }
    }

    /**
     * check how much cash the patron has in his brokerage account
     * throw AuthenticationException if the SS#, username, and password don't match a bank patron
     * throw UnauthorizedActionException if the given patron does not have a brokerage account
     */
    public double checkAvailableBalanceBrokerage(long socialSecurityNumber, String userName, String password) throws AuthenticationException,UnauthorizedActionException{
        Patron patron = this.checkIdentity(socialSecurityNumber, userName, password);
        this.checkBrokerageAccount(patron);
        Account brokerageAccount = patron.getBrokerageAccount();
        double balance = brokerageAccount.getAvailableBalance();
        return balance;
    }
    /**
     * check the total value of the patron's brokerage account
     * throw AuthenticationException if the SS#, username, and password don't match a bank patron
     * throw UnauthorizedActionException if the given patron does not have a brokerage account
     */
    public double checkTotalBalanceBrokerage(long socialSecurityNumber, String userName, String password) throws AuthenticationException,UnauthorizedActionException{
        Patron patron = this.checkIdentity(socialSecurityNumber, userName, password);
        this.checkBrokerageAccount(patron);
        Account brokerageAccount = patron.getBrokerageAccount();
        double balance = brokerageAccount.getTotalBalance();
        return balance;
    }
    /**
     * check how much cash the patron has in his savings account
     * throw AuthenticationException if the SS#, username, and password don't match a bank patron
     * throw UnauthorizedActionException if the given patron does not have a savings account
     */
    public double checkBalanceSavings(long socialSecurityNumber, String userName, String password) throws AuthenticationException,UnauthorizedActionException{
        Patron patron = this.checkIdentity(socialSecurityNumber, userName, password);
        this.checkSavingsAccount(patron);
        Account savingsAccount = patron.getSavingsAccount();
        double balance = savingsAccount.getAvailableBalance();
        return balance;
    }

    /**
     * buy shares of the given stock
     * throw AuthenticationException if the SS#, username, and password don't match a bank patron
     * throw UnauthorizedActionException if the given patron does not have a brokerage account
     * throw InsufficientAssetsException if the required amount of CASH is not present in the brokerage account
     */
    public void purchaseStock(long socialSecurityNumber, String userName, String password, String tickerSymbol, int shares) throws AuthenticationException,UnauthorizedActionException,InsufficientAssetsException{
        try{
        Patron patron = this.checkIdentity(socialSecurityNumber, userName, password);
        this.checkBrokerageAccount(patron);
        BrokerageAccount brokerageAccount = patron.getBrokerageAccount();
        Stock stock = this.getStockBySymbol(tickerSymbol);
        StockPurchase stockPurchase = new StockPurchase((double)shares, brokerageAccount, patron, stock);
        stockPurchase.execute();
        }catch(NoSuchAccountException e){
            throw new UnauthorizedActionException();
        }
    }

    /**
     * sell shares of the given stock
     * throw AuthenticationException if the SS#, username, and password don't match a bank patron
     * throw UnauthorizedActionException if the given patron does not have a brokerage account
     * throw InsufficientAssetsException if the patron does not have the given number of shares of the given stock
     */
    public void sellStock(long socialSecurityNumber, String userName, String password, String tickerSymbol, int shares) throws AuthenticationException,UnauthorizedActionException,InsufficientAssetsException{
        try{
        Patron patron = this.checkIdentity(socialSecurityNumber, userName, password);
        this.checkBrokerageAccount(patron);
        BrokerageAccount brokerageAccount = patron.getBrokerageAccount();
        Stock stock = this.getStockBySymbol(tickerSymbol);
        StockSale stockSale = new StockSale((double)shares, brokerageAccount, patron, stock);
        stockSale.execute();
        }catch(NoSuchAccountException e){
            throw new UnauthorizedActionException();
        }        
    }

    /**
     * check the net worth of the patron
     * throw AuthenticationException if the SS#, username, and password don't match a bank patron
     * return 0 if the patron doesn't have any accounts
     */
    public double getNetWorth(long socialSecurityNumber, String userName, String password) throws AuthenticationException{
        Patron patron = this.checkIdentity(socialSecurityNumber, userName, password);
        double netWorth = patron.getNetWorth();
        return netWorth;
    }

    /**
     * Get the transaction history on all of the patron's accounts, i.e. the transaction histories of both the savings account and
     * brokerage account (whichever of the two exist), combined. The merged history should be sorted in time order, from oldest to newest.
     * If the patron has no transactions in his history, return an array of length 0.
     * throw AuthenticationException if the SS#, username, and password don't match a bank patron
     */
    public Transaction[] getTransactionHistory(long socialSecurityNumber, String userName, String password) throws AuthenticationException{
        //create array with length of all transactions
        Patron patron = this.checkIdentity(socialSecurityNumber, userName, password);
        BrokerageAccount brokerage = null;
        SavingsAccount savings = null;
        int brokerageTransactions = 0;
        int savingsTransactions = 0;
        Transaction[] brokerageHist = new Transaction[0];
        Transaction[] savingsHist = new Transaction[0];
        if(patron.getBrokerageAccount() != null){
            brokerage = patron.getBrokerageAccount();
            brokerageTransactions = brokerage.getTransactionHistory().length;
            brokerageHist = brokerage.getTransactionHistory();
        }
        if(patron.getSavingsAccount() != null){
            savings = patron.getSavingsAccount();
            savingsTransactions = savings.getTransactionHistory().length;
            savingsHist = savings.getTransactionHistory();
        }
        Transaction[] jointTransactions = new Transaction[savingsTransactions + brokerageTransactions];
        for(int i = 0; i < brokerageHist.length; i++){
            jointTransactions[i] = brokerageHist[i];     
        }
        for(int i = 0; i < savingsHist.length; i++){
            jointTransactions[brokerageTransactions + i] = savingsHist[i];
        }
        //calculate number of transactions that are the same
        int timesSame = 0;
        for(int i = 0; i < jointTransactions.length; i++){
            for(int j = (i + 1); j < jointTransactions.length; j++){
                if(jointTransactions[i].equals(jointTransactions[j])){
                    timesSame += 1;
                }
            }
        }
        //create array with length of total transactions, not including duplicates
        Transaction[] newJoint = new Transaction[jointTransactions.length - timesSame];
        //add transactions to array, without including duplicates
        int slot = 0;
        boolean duplicate = false;
        int test = 0;
        for(int i = 0; i < (jointTransactions.length); i++){
            for(int j = (i + 1); j < (jointTransactions.length); j++){
                if(jointTransactions[i].equals(jointTransactions[j])){
                    duplicate = true;
                }
            }
            if(duplicate == false){
            newJoint[slot] = jointTransactions[i];
            slot += 1;
            }
            else{
                duplicate = false;
            }
        }
        //arrange transactions in proper order
        for(int i = 0; i < newJoint.length; i++){
            for(int j = (i + 1); j < (newJoint.length); j++){
                if(newJoint[i].getTime() > newJoint[j].getTime()){
                    Transaction temp = newJoint[i];
                    newJoint[i] = newJoint[j];
                    newJoint[j] = temp;
                }    
            }
        }
        return newJoint;
    }

    public static class Stock {
        private final String tickerSymbol;
        private final double sharePrice;
        /**
         * Note that because this constructor is private, the Bank class is the only class that can create instances of Stock.
         * All other classes may refer to, i.e. have variables pointing to, Stock objects, but only Bank can create new Stock Objects.
         */
        private Stock(String tickerSymbol, double sharePrice){
            this.tickerSymbol = tickerSymbol;
            this.sharePrice = sharePrice;
        }
        public double getSharePrice(){      
            return this.sharePrice;
        }
        public String getTickerSymbol(){
            return this.tickerSymbol;
        }
    }

    private Patron checkIdentity(long socialSecurityNumber, String userName, String password) throws AuthenticationException{
        int patronArraySlot = 0;
        boolean checkIdentity = false;
        for(int i = 0; i < bankPatrons.length; i++){
            if((bankPatrons[i].getSocialSecurityNumber()) == (socialSecurityNumber)){
                patronArraySlot = i;
            }
        }               
        if(((bankPatrons[patronArraySlot].getSocialSecurityNumber()) == (socialSecurityNumber)) & ((bankPatrons[patronArraySlot].getUserName()).equals(userName)) & ((bankPatrons[patronArraySlot].getPassword()).equals(password))){
            checkIdentity = true;
        }
        else{
            checkIdentity = false;
        }
            if(checkIdentity == false){
            throw new AuthenticationException();
        }
        return bankPatrons[patronArraySlot];
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

    private long accountNumberGenerator(){
        long accountNumber = 0;
        do{
        double number = (Math.random() * 100000);
        accountNumber = (long)number;
        for(int i = 0; i < bankAccounts.length; i++){
            if(bankAccounts[i].getAccountNumber() == accountNumber){
                accountNumber = 0;
            }
        }
        }while(accountNumber == 0);   
        return accountNumber;
    }

    protected void clearVariables(){
        this.bankPatrons = new Patron[0];
        this.bankAccounts = new Account[0];
        this.stocksOnMarket = new Stock[0];
    }
}