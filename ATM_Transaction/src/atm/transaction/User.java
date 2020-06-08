package atm.transaction;

import java.util.ArrayList;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

public class User {
	
	private String firstName;
	private String lastName;
	private String uuid;
	
	//The MD5 hash of the user's pin number
	private byte pinHash[];
	private ArrayList<Account> accounts;
	
	/**
	 * Creates a new User
	 * @param firstName 	the user's first name
	 * @param lastName 		the user's last name
	 * @param pin			the user's account pin number
	 * @param theBank		the Bank object that the user is a customer of	
	 */
	public User(String firstName, String lastName, String pin, Bank theBank) {
		
		//set the user's name
		this.firstName = firstName;
		this.lastName = lastName;
		
		//store the pin's MD5 hash, rather than the original value for security reasons.
		try {
			MessageDigest md = MessageDigest.getInstance("MD5");
			this.pinHash = md.digest(pin.getBytes());
		} catch (NoSuchAlgorithmException e) {
			System.err.println("error, caught NoSuchAlgorithException"); 
			e.printStackTrace();
			System.exit(1);
		}
		
		//get a new unique universal ID for the user
		this.uuid = theBank.getNewUserUUID();
		
		//create empty list of accounts 
		this.accounts = new ArrayList<Account>();
		
		//print log message
		System.out.printf("New user %s, %s with ID %s created.\n", lastName, firstName, this.uuid);
	}
	
	/**
	 * Get the first name of the user
	 * @return 		first name
	 */
	public String getFirstName() {
		return this.firstName;
	}
	
	/**
	 * Get the last name of the user
	 * @return		last name
	 */
	public String getLastName() {
		return this.lastName;
	}
	
	/**
	 * Add an account for the user
	 * @param anAcct	the account to add	
	 */
	public void addAccount(Account anAcct) {
		this.accounts.add(anAcct);
	}
	
	/**
	 * Returns the UUID
	 * @return the uuid
	 */
	public String getUUID() {
		return this.uuid;
	}
	
	/**
	 * Check if a given pin matches the true User pin
	 * @param aPin		the pin to check
	 * @return			whether the pin is valid or not
	 */
	public boolean validatePin(String aPin) {
		
		try {
			MessageDigest md = MessageDigest.getInstance("MD5");
			//this.pinHash = md.digest(pin.getBytes());
			return MessageDigest.isEqual(md.digest(aPin.getBytes()), this.pinHash);
		} catch (NoSuchAlgorithmException e) {
			System.err.println("error, caught NoSuchAlgorithException"); 
			e.printStackTrace();
			System.exit(1);
		}
		
		return false;
	}
	
	/**
	 * Prints summaries of the accounts of this user.
	 */
	public void printAccountsSummary() {
		
		System.out.printf("\n\n %s's accounts summaries: \n", this.firstName);
		for(int a=0; a<this.accounts.size(); a++) {
			System.out.printf("%d) %s", a+1, this.accounts.get(a).getSummaryLine());
			System.out.println();
			
		}
	}
	
	/**
	 * Get number of accounts
	 * @return 		number of accounts
	 */
	public int numAccounts() {
		return this.accounts.size();
	}
	
	/**
	 * Print transaction history for a particular account
	 * @param accIdx 	Index of the account to use
	 */
	public void printAcctTransHistory(int accIdx) {
		this.accounts.get(accIdx).printTransHistory();
	}
	
	/**
	 * Get the balance of a particular account
	 * @param accIdx	the index of the account to use
	 * @return	balance of the account
	 */
	public double getAcctBalance(int accIdx) {
		return this.accounts.get(accIdx).getBalance();
	}
	
	/**
	 * Get the UUID of a particular account
	 * @param accIdx	the index of the account to use
	 * @return uuid of the account
	 */
	public String getAcctUUID(int accIdx) {
		return this.accounts.get(accIdx).getUUID();
	}
	
	/**
	 * Add a transaction to a particular account
	 * @param accIdx	the index of the account
	 * @param amount	the amount of the transaction
	 * @param memo		the memo of the transaction
	 */
	public void addAcctTransaction(int accIdx, double amount, String memo) {
		this.accounts.get(accIdx).addTransaction(amount, memo);
	}
	
}
