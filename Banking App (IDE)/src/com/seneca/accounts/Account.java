/**
* Application to display user information
* with associated bank account and their account balance
*
* @author Khai Phan (02/11/2020)
*/

package com.seneca.accounts;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Arrays;

@SuppressWarnings("serial")
public class Account implements Comparable<Account>, Serializable {
	private String full_name = "";
	private String account_number = "";
	private BigDecimal account_balance = new BigDecimal(0);
	
	/**
	 * default constructor with no arguments
	 * @return nothing
	 */
	public Account() {	this("", "", 0);	}

	/**
	 * constructor with 3 arguments
	 * @param full_name contains user's full name
	 * @param accountNumber contain user's account number
	 * @param accounBalance contain user's balance with associated account
	 * @return nothing
	 */
	public Account(String full_name, String accountNumber, double accountBalance) {
		if(full_name == null) {
			this.full_name = "";
			throw new IllegalArgumentException();
		}
		else
			this.full_name = full_name;
		
		if(accountNumber == null) {
			this.account_number = "";
			throw new IllegalArgumentException();
		}
		else
			this.account_number = accountNumber;
		
		if(accountBalance < 0) {
			this.account_balance = new BigDecimal(0);
			throw new IllegalArgumentException();
		}
		else
			this.account_balance = new BigDecimal(accountBalance);
	}
	
	/**
	 * toString method, display the value
	 * @return nothing
	 */
	public String toString() {
		String[] temp = this.full_name.split(" ");
		String s;
		String rounding = String.format("%.2f", this.account_balance);
	       s =  "Account Name  : " + Arrays.toString(temp) + "\n" +
	    		"Account Number: " + this.account_number + "\n" +
	            "Balance       : $" + rounding + "\n";
	       return s;
	}
	
	/**
	 * equals method, compare the passing value with the stored value
	 * @param instance of Account class
	 * @return boolean
	 */
	@Override
	public boolean equals(Object acc) {
		if(this == acc)
			return true;
		if(acc == null)
			return false;
		if(this.getClass() != acc.getClass())
			return false;
		
		Account acc2 = (Account)acc;
		return ((acc2.full_name.equals(this.full_name)) && 
				(acc2.account_number.equals(this.account_number)) && 
				(acc2.account_balance == this.account_balance)); 
	}
	
	/**
	 * hashCode method, use to calculate all the variable value altogether
	 * @return value integer support for equals()
	 */
	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((account_balance == null) ? 0 : account_balance.hashCode());
		result = prime * result + ((account_number == null) ? 0 : account_number.hashCode());
		result = prime * result + ((full_name == null) ? 0 : full_name.hashCode());
		return result;
	}
	
	/**
	 * compare current AccountNumber to existing AccountNumber
	 * @return true or false as int
	 */
	public int compareTo(Account temp) {
		int m_compare = temp.getAccountNumber().compareTo(account_number);
		return m_compare;
	}
	
	/**
	 * full_name getter
	 * @return full_name string
	 */
	public String getFullName() {	return this.full_name;	}
	
	/**
	 * account_number getter
	 * @return account_number string
	 */
	public String getAccountNumber() {	return this.account_number;	}
	
	/**
	 * account_balance getter
	 * @return account_balance value
	 */
	public BigDecimal getAccountBalance() {	return this.account_balance;	}
	
	/**
	 * account_number setter
	 * @param accNum contain account number to be passed into current account_number
	 */
	public void setAccountNumber(String accNum) {	this.account_number = accNum;	}
	
	/**
	 * account_balance setter
	 * @param accBalance contain account balance to be passed into current account_balance
	 */
	public void setAccountBalance(double accBalance) {	this.account_balance = new BigDecimal(accBalance);	}
	
	/**
	 * subtract funds out of account_balance
	 * @param amount user need to withdraw from their account
	 * @return true if the account_balance isn't negative after withdrawal
	 */
	public synchronized boolean withdraw(double amount) {
		BigDecimal temp = new BigDecimal(amount);
		if(amount > 0 && this.account_balance.compareTo(temp) > 0) {
			this.account_balance = this.account_balance.subtract(temp);
			return true;
		}
		return false;
	}
	
	/**
	 * add funds to current account_balance
	 * @param amount stored double type to add into account_balance
	 */
	public synchronized boolean deposit(double amount) {
		boolean mDeposit = false;
		BigDecimal temp = new BigDecimal(amount);
		if(amount > 0) {
			this.account_balance = this.account_balance.add(temp);
			mDeposit = true;
		}
			return mDeposit;
	}
}
