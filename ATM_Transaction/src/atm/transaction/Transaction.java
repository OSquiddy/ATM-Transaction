package atm.transaction;

import java.util.Date;

public class Transaction {

	private double amount;
	private Date timestamp;
	private String memo;
	private Account inAccount;
	
	/**
	 * Create a new transaction
	 * @param amount		the amount transacted
	 * @param inAccount		the account the transaction belongs to
	 */
	public Transaction (double amount, Account inAccount) {
		
		this.amount = amount;
		this.inAccount = inAccount;
		this.timestamp = new Date();
		this.memo = "";
	}
	
	/**
	 * Create a new transaction
	 * @param amount		the amount transacted
	 * @param memo			the memo for the transaction
	 * @param inAccount		the account the transaction belongs to
	 */
	public Transaction (double amount, String memo, Account inAccount) {
		
		//call the 2-arg constructor first
		this(amount, inAccount);
		
		//set the memo;
		this.memo = memo;
	}
	
	/**
	 * Get the amount of the transaction
	 * @return		the amount
	 */
	public double getAmount() {
		return this.amount;
	}
	
	/**
	 * Get a string summarizing the transaction
	 * @return 		the summary string
	 */
	public String getSummaryLine() {
		
		if(this.amount >= 0) {
			return String.format("%s : $%.02f : %s", this.timestamp.toString(), this.amount, this.memo );
		}
		else {
			return String.format("%s : $(%.02f) : %s", this.timestamp.toString(), -this.amount, this.memo );
		}
	}
	
	
}
