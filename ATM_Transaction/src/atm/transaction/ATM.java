package atm.transaction;

import java.util.Scanner;

public class ATM {

	public static void main(String[] args) {

		//init Scanner
		Scanner sc = new Scanner(System.in);
		 
		//init Bank
		Bank theBank = new Bank("ADCB");
		
		//add a user, which also creates a savings account
		User aUser = theBank.addUser("Jon", "Martell", "1234");
		
		//add a checking account for the user
		Account newAccount = new Account("Checking", aUser, theBank);
		aUser.addAccount(newAccount);
		theBank.addAccount(newAccount);
		
		
		User curUser;
		while(true) {
			
			//stay in the login prompt until succesfull login
			curUser = ATM.mainMenuPrompt(theBank, sc);
			
			//stay in main menu until user quits
			ATM.printUserMenu(curUser, sc);
			
		}
	}
	
	/**
	 * Print the ATM's login menu
	 * @param theBank	The bank object whose accounts to use
	 * @param sc		The scanner object to use for user input
	 * @return
	 */
	public static User mainMenuPrompt(Bank theBank, Scanner sc) {
		
		//inits
		String userID;
		String pin;
		User authUser;
		
		//prompt the user for userID/pin combo until the correct one is reached
		do {
			
			System.out.printf("\n\nWelcome to %s\n\n", theBank.getName());
			System.out.print("Enter user ID: ");
			userID = sc.nextLine();
			System.out.print("Enter the pin: ");
			pin = sc.nextLine();
			
			//try to get the user object corresponding to the ID and pin combo
			authUser = theBank.userLogin(userID, pin);
			if(authUser == null) {
				System.out.println("Incorrect userID/pin combination. Please try again.");
				
			}
			
		} while(authUser == null); //continue looping until successful login
		
		return authUser;
	}

	
	public static void printUserMenu(User theUser, Scanner sc) {
		
		//print a summary of the user's accounts
		theUser.printAccountsSummary();
		
		//init
		int choice;
		
		//user menu
		do {
			System.out.printf("\nWelcome %s, what would you like to do? \n", theUser.getFirstName());
			System.out.println("\t1. Transaction History");
			System.out.println("\t2. Withdrawal");
			System.out.println("\t3. Deposit");
			System.out.println("\t4. Transfer Funds");
			System.out.println("\t5. Quit");
			System.out.println();
			System.out.print("Enter choice: ");
			choice = sc.nextInt();
			
			if(choice < 1 || choice > 5) {
				System.out.println("Invalid option. Please enter a number from 1-5.");
			}
		} while(choice < 1 || choice > 5);
		
		//process the choice
		switch(choice) {
		
		case 1:
			ATM.showTransHistory(theUser, sc);
			break;
			
		case 2:
			ATM.withdrawFunds(theUser, sc);
			break;
			
		case 3:
			ATM.depositFunds(theUser, sc);
			break;
			
		case 4:
			ATM.transferFunds(theUser, sc);
			break;
			
		case 5:
			//gobble up rest of the previous input
			sc.nextLine();
			break;
				
		}
		
		//redisplay this menu, unless the user wants to quit
		if(choice != 5) {
			ATM.printUserMenu(theUser, sc);
		}
		
	}
	
	/**
	 * Show transaction history for an account
	 * @param theUser 		the logged-in User account
	 * @param sc			the Scanner object used for user input	
	 */
	public static void showTransHistory(User theUser, Scanner sc) {
		
		int theAcct;
		
		//get account whose transaction history to look at
		do {
			
			System.out.printf("Enter the number (1-%d) of"
					+ " the account whose transactions you want to see: ", theUser.numAccounts());
			theAcct = sc.nextInt()-1;
			if(theAcct < 0 || theAcct >= theUser.numAccounts()) {
				System.out.println("Invalid account. Please try again.");
			}
		} while(theAcct < 0 || theAcct >= theUser.numAccounts());
		
		
		//print the Transaction history
		theUser.printAcctTransHistory(theAcct);
		
		
	}
	
	/**
	 * Process of transferring funds from one account to another
	 * @param theUser	the logged-in User object
	 * @param sc		the Scanner object used for user input
	 */
	public static void  transferFunds(User theUser, Scanner sc) {
		
		//inits
		int fromAcct;
		int toAcct;
		double amount;
		double acctBal;
		
		//get the account to transfer from
		do {
			System.out.printf("Enter the number (1-%d) of the account to transfer from: ", theUser.numAccounts());
			fromAcct = sc.nextInt()-1;
			if(fromAcct < 0 || fromAcct >= theUser.numAccounts()) {
				System.out.println("Invalid account. Please try again. ");
			}
		} while(fromAcct < 0 || fromAcct >= theUser.numAccounts());
		acctBal = theUser.getAcctBalance(fromAcct);	
		
		//get the account to transfer to
		do {
			System.out.printf("Enter the number (1-%d) of the account to transfer to: ", theUser.numAccounts());
			toAcct = sc.nextInt()-1;
			if(toAcct < 0 || toAcct >= theUser.numAccounts()) {
				System.out.println("Invalid account. Please try again. ");
			}
		} while(toAcct < 0 || toAcct >= theUser.numAccounts());
		
		//get the amount to transfer
		do {
			System.out.printf("Enter the amount to transfer (max $%.02f): $", acctBal);
			amount = sc.nextDouble();
			if(amount < 0) {
				System.out.println("Amount must be greater than zero.");
			}
			else if(amount > acctBal) {
				System.out.printf("Amount must not be greater than the account balance, which is $%.02f\n.", acctBal);
			}
		} while(amount < 0 || amount > acctBal);
		
		//finally, do the transfer
		theUser.addAcctTransaction(fromAcct, -1*amount, String.format("Transfer to account %s",
				theUser.getAcctUUID(toAcct)));
		theUser.addAcctTransaction(toAcct, amount, String.format("Transfer to account %s",
				theUser.getAcctUUID(fromAcct)));
		
	}
	
	/**
	 * Process a withdrawal from an account
	 * @param theUser	the logged-in User object
	 * @param sc		the Scanner object used for user input
	 */
	public static void withdrawFunds(User theUser, Scanner sc) {
		
		//inits
		int fromAcct;
		double amount;
		double acctBal;
		String memo;
		
		//get the account to transfer from
		do {
			System.out.printf("Enter the number (1-%d) of the account to withdraw from: ", theUser.numAccounts());
			fromAcct = sc.nextInt()-1;
			if(fromAcct < 0 || fromAcct >= theUser.numAccounts()) {
				System.out.println("Invalid account. Please try again. ");
			}
		} while(fromAcct < 0 || fromAcct >= theUser.numAccounts());
		acctBal = theUser.getAcctBalance(fromAcct);
		
		//get the amount to withdraw
		do {
			System.out.printf("Enter the amount to withdraw (max $%.02f): $", acctBal);
			amount = sc.nextDouble();
			if(amount < 0) {
				System.out.println("Amount must be greater than zero.");
			}
			else if(amount > acctBal) {
				System.out.printf("Amount must not be greater than the account balance, which is $%.02f\n.", acctBal);
			}
		} while(amount < 0 || amount > acctBal);
		
		//gobble up rest of previous input line (why?)
		sc.nextLine();
		
		//get a memo
		System.out.print("Enter a memo: ");
		memo = sc.nextLine();
		
		//do the withdrawal
		theUser.addAcctTransaction(fromAcct, -1*amount, memo); 
		
	}
	
	/**
	 * Process a deposit to an account
	 * @param theUser	the logged-in User object
	 * @param sc		the Scanner object used for user input
	 */
	public static void depositFunds(User theUser, Scanner sc) {
		
		//inits
		int toAcct;
		String memo;
		double amount;
		double acctBal;
		
		//get the account to transfer from
		do {
			System.out.printf("Enter the number (1-%d) of the account to deposit in: ", theUser.numAccounts());
			toAcct = sc.nextInt()-1;
			if(toAcct < 0 || toAcct >= theUser.numAccounts()) {
				System.out.println("Invalid account. Please try again. ");
			}
		} while(toAcct < 0 || toAcct >= theUser.numAccounts());
		acctBal = theUser.getAcctBalance(toAcct);
		
		//get the amount to deposit
		do {
			System.out.printf("Enter the amount to deposit (max $%.02f): $", acctBal);
			amount = sc.nextDouble();
			if(amount < 0) {
				System.out.println("Amount must be greater than zero.");
			}
		} while(amount < 0);
		
		//gobble up rest of previous input line (why?)
		sc.nextLine();
		
		//get a memo
		System.out.print("Enter a memo: ");
		memo = sc.nextLine();
		
		//do the withdrawal
		theUser.addAcctTransaction(toAcct, amount, memo);
		
	}
	
}
