package edu.yu.cs.intro.bank;
import org.junit.Test;
import static org.junit.Assert.*;
import edu.yu.cs.intro.bank.exceptions.*;
import org.junit.After;

public class BankTest{

	private Bank bank;
	private Patron[] bankPatrons;
	private Account[] bankAccounts;
	private Bank.Stock[] stocksOnMarket;

	public BankTest()throws Exception{
	this.bank = Bank.getBank();
	bank.createNewPatron("raziel", "siegman", 123456789, "rsiegman", "myPassword");
	bank.createNewPatron("Yeshiva", "University", 254254254, "yuniversity", "yeshiva");
	bank.createNewPatron("Dovid", "Gelbtuch", 613613613, "dgmitzvahboy", "iLoveMitzvos");
	bank.createNewPatron("zach", "israeli", 888888888, "zisraeli", "raziel123");
	bank.createNewPatron("Lubavitcher", "Rebbe", 770770770, "Admor", "iAmMashiach");
	Patron[] patrons = bank.getBankPatrons();
	this.bankPatrons = patrons;
	bank.openSavingsAccount(123456789, "rsiegman", "myPassword");
	bank.openBrokerageAccount(123456789, "rsiegman", "myPassword");
	bank.openSavingsAccount(613613613, "dgmitzvahboy", "iLoveMitzvos");
	bank.openBrokerageAccount(613613613, "dgmitzvahboy", "iLoveMitzvos");
	bank.openSavingsAccount(888888888, "zisraeli", "raziel123");
	bank.openBrokerageAccount(770770770, "Admor", "iAmMashiach");
	Account[] accounts = bank.getBankAccounts();
	this.bankAccounts = accounts;
	bank.addNewStockToMarket("BLF", 500);
	bank.addNewStockToMarket("MUSS", .50);
	bank.addNewStockToMarket("GLCK", 120);
	bank.addNewStockToMarket("LBR", 30);
	Bank.Stock[] stocks = bank.getStocksOnMarket();
	this.stocksOnMarket = stocks;
	}
	
	@After
	public void resetAfterTest(){
		bank.clearVariables();
	}

	@Test(expected = AuthenticationException.class)
	public void authenticationTester()throws Exception{
		bank.depositCashIntoSavings(770770770, "Admor", "iAmNotMashiach", 254.54);
	}

	@Test(expected = UnauthorizedActionException.class)
	public void unauthorizedActionTester()throws Exception{
		bank.depositCashIntoSavings(770770770, "Admor", "iAmMashiach", 5234);
	}

	@Test(expected = UnauthorizedActionException.class)
	public void unauthorizedActionTesterTwo()throws Exception{
		bank.openSavingsAccount(123456789, "rsiegman", "myPassword");
	}

	@Test(expected = UnauthorizedActionException.class)
	public void unauthorizedActionTesterThree()throws Exception{
		bank.createNewPatron("Identity", "Theft", 770770770, "scammer", "stealer");
	}

	@Test
	public void createAccountTest()throws Exception{
		bank.openSavingsAccount(770770770, "Admor", "iAmMashiach");
		assertEquals(0, bank.checkBalanceSavings(770770770, "Admor", "iAmMashiach"),0);
	}

	@Test
	public void basicStockTest()throws Exception{
		bank.depositCashIntoSavings(123456789, "rsiegman", "myPassword", 500);
		bank.transferFromSavingsToBrokerage(123456789, "rsiegman", "myPassword", 400);
		bank.purchaseStock(123456789, "rsiegman", "myPassword", "LBR", 2);
		assertEquals(2, bank.getNumberOfOutstandingShares("LBR"));
	}


	@Test
	public void basicTransactionTest()throws Exception{
		bank.depositCashIntoSavings(123456789, "rsiegman", "myPassword", 50);
		assertEquals(50, bank.checkBalanceSavings(123456789, "rsiegman", "myPassword"),0);
		assertEquals("rsiegman", bankPatrons[0].getUserName());
	}

	@Test
	public void multipleTransactionTest()throws Exception{
		bank.depositCashIntoSavings(123456789, "rsiegman", "myPassword", 5000);
		Thread.sleep(5);
		bank.transferFromSavingsToBrokerage(123456789, "rsiegman", "myPassword", 10);
		assertEquals(4990, bank.checkBalanceSavings(123456789, "rsiegman", "myPassword"),0);
		assertEquals(10, bank.checkAvailableBalanceBrokerage(123456789, "rsiegman", "myPassword"),0);
		assertEquals(10, bank.checkTotalBalanceBrokerage(123456789, "rsiegman", "myPassword"),0);
		Transaction[] txHist = bank.getTransactionHistory(123456789, "rsiegman", "myPassword");
		Transaction transaction = txHist[0];
		assertEquals(5000, transaction.getAmount(), 0);
	}

	@Test
	public void TickerSymbolListTest()throws Exception{
		String[] tickerList = bank.getListOfAllStockTickerSymbols();
		assertEquals(4, tickerList.length);
		assertEquals("BLF", tickerList[0]);
		assertEquals("LBR", tickerList[3]);
	}

	@Test
	public void stockTesting()throws Exception{
		bank.depositCashIntoSavings(123456789, "rsiegman", "myPassword", 1100);
		Thread.sleep(5);
		bank.transferFromSavingsToBrokerage(123456789, "rsiegman", "myPassword", 1000);
		Thread.sleep(5);		
		bank.depositCashIntoSavings(613613613, "dgmitzvahboy", "iLoveMitzvos", 5000);
		Thread.sleep(5);
		bank.withdrawCashFromSavings(613613613, "dgmitzvahboy", "iLoveMitzvos", 2010);
		Thread.sleep(5);
		bank.transferFromSavingsToBrokerage(613613613, "dgmitzvahboy", "iLoveMitzvos", 2000);
		Thread.sleep(5);
		bank.transferFromBrokerageToSavings(613613613, "dgmitzvahboy", "iLoveMitzvos", 100);
		Thread.sleep(5);
		bank.withdrawCashFromBrokerage(613613613, "dgmitzvahboy", "iLoveMitzvos", 50);
		Thread.sleep(5);
		Transaction[] txHist = bank.getTransactionHistory(613613613, "dgmitzvahboy", "iLoveMitzvos");
		Transaction transactionOne = txHist[0];
		Transaction transactionTwo = txHist[1];
		Transaction transactionThree = txHist[2];
		Transaction transactionFour = txHist[3];
		Transaction transactionFive = txHist[4];
		assertEquals(5000, transactionOne.getAmount(), 0);
		assertEquals(2010, transactionTwo.getAmount(), 0);
		assertEquals(2000, transactionThree.getAmount(), 0);
		assertEquals(100, transactionFour.getAmount(), 0);
		assertEquals(50, transactionFive.getAmount(), 0);
		//dovid has 1850 total in brokerage, and 1110 in savings
		Bank.Stock[] stocks = bank.getStocksOnMarket();
		assertEquals(stocks[0], bank.getStockBySymbol("BLF"));
		assertEquals(stocks[3], bank.getStockBySymbol("LBR"));
		assertEquals(4, stocks.length);
		String[] tickerSymbols = bank.getListOfAllStockTickerSymbols();
		assertEquals("GLCK", tickerSymbols[2]);
		assertEquals(4, tickerSymbols.length);
		assertEquals(0, bank.getNumberOfOutstandingShares("LBR"));
		assertEquals(0, bank.getMarketCapitalization("LBR"));
		bank.purchaseStock(613613613, "dgmitzvahboy", "iLoveMitzvos", "MUSS", 10);
		assertEquals(1849, bank.checkTotalBalanceBrokerage(613613613, "dgmitzvahboy", "iLoveMitzvos"),0);
		assertEquals(1844, bank.checkAvailableBalanceBrokerage(613613613, "dgmitzvahboy", "iLoveMitzvos"),0);
		bank.purchaseStock(613613613, "dgmitzvahboy", "iLoveMitzvos", "BLF", 3);
		assertEquals(3, bank.getNumberOfOutstandingShares("BLF"));
		assertEquals(1848, bank.checkTotalBalanceBrokerage(613613613, "dgmitzvahboy", "iLoveMitzvos"),0);
		assertEquals(343, bank.checkAvailableBalanceBrokerage(613613613, "dgmitzvahboy", "iLoveMitzvos"),0);
		bank.sellStock(613613613, "dgmitzvahboy", "iLoveMitzvos", "MUSS", 1);
		assertEquals(1847, bank.checkTotalBalanceBrokerage(613613613, "dgmitzvahboy", "iLoveMitzvos"),0);
		assertEquals(342.5, bank.checkAvailableBalanceBrokerage(613613613, "dgmitzvahboy", "iLoveMitzvos"),0);
		assertEquals(2937, bank.getNetWorth(613613613, "dgmitzvahboy", "iLoveMitzvos"),0);
		bank.purchaseStock(613613613, "dgmitzvahboy", "iLoveMitzvos", "GLCK", 2);
		bank.sellStock(613613613, "dgmitzvahboy", "iLoveMitzvos", "GLCK", 2);
		assertEquals(2935, bank.getNetWorth(613613613, "dgmitzvahboy", "iLoveMitzvos"),0);
		bank.purchaseStock(123456789, "rsiegman", "myPassword", "MUSS", 81);
		assertEquals(45, bank.getMarketCapitalization("MUSS"), 0);
		assertEquals(1299, bank.getTotalBrokerageCashInBank(), 0);
	}
}