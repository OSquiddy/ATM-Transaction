package atm.transaction;

import java.util.ArrayList;
import java.util.Random;

public class Bank {

	private String name;
	private ArrayList<User> users;
	private ArrayList<Account> accounts;
	
	/**
	 * Create a new Bank object
	 * @param name 		name of the bank
	 */
	public Bank(String name) {
		this.name = name;
		this.users = new ArrayList<>();
		this.accounts = new ArrayList<>();
	}
	
	/**
	 * Generate a new UUID for the user
	 * @return the uuid
	 */
	public String getNewUserUUID() {
		
		//inits
		String uuid;
		Random rng = new Random();
		int len = 6;
		boolean nonUnique = false;
		
		// continue looping till we get a unique ID
		do {
			uuid = "";
			for(int i=0; i<len; i++) {
				uuid += ((Integer)rng.nextInt(10)).toString();
			}
			
			//check to make sure its unique
			nonUnique = false;
			for(User u : this.users) {
				if(uuid.compareTo(u.getUUID()) == 0) {
					nonUnique = true;
					break;
				}
			}
		} while(nonUnique);
		
		return uuid;
	}
	
	/**
	 * Generate a new UUID for the account
	 * @return the uuid
	 */
	public String getNewAccountUUID() {

		//inits
		String uuid;
		Random rng = new Random();
		int len = 10;
		boolean nonUnique = false;
		
		// continue looping till we get a unique ID
		do {
			uuid = "";
			for(int i=0; i<len; i++) {
				uuid += ((Integer)rng.nextInt(10)).toString();
			}
			
			//check to make sure its unique
			nonUnique = false;
			for(Account a : this.accounts) {
				if(uuid.compareTo(a.getUUID()) == 0) {
					nonUnique = true;
					break;
				}
			}
		} while(nonUnique);
		
		return uuid;
	}
	
	/**
	 * Add an account
	 * @param anAccount		the account to add
	 */
	public void addAccount(Account anAccount) {
		this.accounts.add(anAccount);
	}
	
	/**
	 * Creates a new user of the bank
	 * @param firstName		first name of the user
	 * @param lastName		last name of the user
	 * @param pin			user's pin
	 * @return	newUser object
	 */
	public User addUser(String firstName, String lastName, String pin) {
		
		//Create a new User object and add it to our list
		User newUser = new User(firstName, lastName, pin, this);
		this.users.add(newUser);
		
		//Create a savings account for the user and add to User and Bank accounts list
		Account newAccount = new Account("Savings", newUser, this);
		newUser.addAccount(newAccount);
		this.addAccount(newAccount);
		
		return newUser;
		}
		
		/**
		 * Get the User object associated with a particular userID and pin
		 * if they are entered correctly
		 * @param userID	the UUID of the user to login
		 * @param pin		the pin of the user
		 * @return User object, if it is succesful, or Null, if it is not
		 */
		public User userLogin(String userID, String pin) {
			
			//search through list of users
			for(User u: this.users) {
				
				//check if userID is correct
				if(u.getUUID().compareTo(userID) == 0 && u.validatePin(pin)) {
					return u;
				}
			}
			
			//if we haven't found the user or if the pin is invalid
			return null;
		}

		/**
		 * Gets the name of the bank
		 * @return bank name
		 */
		public String getName() {
			return this.name;
		}
		
		
}
