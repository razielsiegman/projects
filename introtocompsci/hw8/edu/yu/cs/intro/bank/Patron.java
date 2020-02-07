package edu.yu.cs.intro.bank;

import edu.yu.cs.intro.bank.exceptions.NoSuchAccountException;

public class Patron {
    private final String firstName;
    private final String lastName;
    private final long socialSecurityNumber;
    private final String userName;
    private final String password;
    private BrokerageAccount brokerageAccount;
    private SavingsAccount savingsAccount;

    protected Patron(String firstName, String lastName, long socialSecurityNumber, String userName, String password){
        this.firstName = firstName;
        this.lastName = lastName;
        this.socialSecurityNumber = socialSecurityNumber;
        this.userName = userName;
        this.password = password;
    }

    protected String getFirstName(){
        return firstName;
    }

    protected String getLastName(){
        return lastName;
    }

    protected long getSocialSecurityNumber(){
        return socialSecurityNumber;
    }

    protected String getUserName(){
        return userName;
    }

    protected String getPassword(){
        return password;
    }

    protected void addAccount(Account acct){
        if(acct instanceof BrokerageAccount){
            BrokerageAccount brokerageAccount = (BrokerageAccount)acct;
            this.setBrokerageAccount(brokerageAccount);
        }
        if(acct instanceof SavingsAccount){
            SavingsAccount savingsAccount = (SavingsAccount)acct;
            this.setSavingsAccount(savingsAccount);
        }
    }
    protected Account getAccount(long accountNumber) throws NoSuchAccountException {
        BrokerageAccount brokerage = this.getBrokerageAccount();
        SavingsAccount savings = this.getSavingsAccount();
        if((brokerage.getAccountNumber() != accountNumber) & (savings.getAccountNumber() != accountNumber)){
            throw new NoSuchAccountException();
        }
        Account account = null;
        if(brokerage.getAccountNumber() == accountNumber){
            account = brokerage;
        }
        else{
            account = savings;
        }
        return account;
    }

    protected void setBrokerageAccount(BrokerageAccount account) {
        this.brokerageAccount = account;
    }
    protected void setSavingsAccount(SavingsAccount account) {
        this.savingsAccount = account;
    }
    protected BrokerageAccount getBrokerageAccount() {
       return brokerageAccount;
    }
    protected SavingsAccount getSavingsAccount() {
        return savingsAccount;
    }

    /**
     * total cash in savings + total cash in brokerage + total value of shares in brokerage
     * return 0 if the patron doesn't have any accounts
     */
    protected double getNetWorth(){
        double netWorth = 0;
        if(!(this.getSavingsAccount() == null)){
            SavingsAccount savings = this.getSavingsAccount();
            netWorth += savings.getTotalBalance();
        }
        if(!(this.getBrokerageAccount() == null)){
        BrokerageAccount brokerage = this.getBrokerageAccount();
        netWorth += brokerage.getTotalBalance();
        }
        return netWorth;
    }
}