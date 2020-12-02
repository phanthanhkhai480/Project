/**
* Application to display user chequing account information
* with associated bank account and their account balance, with transaction
*
* @author Khai Phan and Carlianz Pura (02/20/2020)
*/

package com.seneca.accounts;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Arrays;

public class Chequing extends Account implements Serializable {
	private static final long serialVersionUID = 1L;
	double service_charge = 0, total_service = 0;
	int maxTransaction = 0, counterTransaction = 0, counterService = 0;
	BigDecimal current_balance = super.getAccountBalance();
	double[] mTransaction = null;
	
	/**
	 * default constructor, set to default values
	 * @return nothing
	 */
	public Chequing() {
		this.service_charge = 0.25;
		this.maxTransaction = 3;
		this.counterTransaction = 0;
		this.counterService = 0;
		this.mTransaction = new double[this.maxTransaction];
	}
	
	/**
	 * constructor with 5 arguments
	 * @param fullName
	 * @param accountNumber
	 * @param startingBalance
	 * @param serviceCharge
	 * @param maximumTransaction
	 */
	public Chequing(String fullName, String accountNumber, double startingBalance, 
			double serviceCharge, int maximumTransaction) {
		
		super(fullName, accountNumber, startingBalance);
		
		if (maximumTransaction < 0 || serviceCharge < 0)
			throw new IllegalArgumentException();
		
		this.service_charge = serviceCharge;
		this.maxTransaction = maximumTransaction;
		this.mTransaction = new double[this.maxTransaction];
	}
	
	/**
	 * equals() methods, use to compare 2 objects if they are in the same class object
	 */
	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (!super.equals(obj))
			return false;
		if (getClass() != obj.getClass())
			return false;
		Chequing other = (Chequing) obj;
		if (counterTransaction != other.counterTransaction)
			return false;
		if (!Arrays.equals(mTransaction, other.mTransaction))
			return false;
		if (maxTransaction != other.maxTransaction)
			return false;
		if (Double.doubleToLongBits(service_charge) != Double.doubleToLongBits(other.service_charge))
			return false;
		return true;
	}


	/**
	 * toString() methods, use to display information when the object is being called
	 */
	@Override
	public String toString() {
		StringBuffer str = new StringBuffer();
		str.append(super.toString());
		str.append("Account Type        : CHQ").append("\n");
		str.append("Service Charge      : $").append(String.format("%.2f", this.service_charge)).append("\n");
		str.append("Total Charge        : $").append(String.format("%.2f", this.total_service)).append("\n");
		str.append("List of Transactions: ");
		if(this.counterTransaction > 0) {
			for(int a = 0; a < this.maxTransaction; a++) {
				if(this.mTransaction[a] > 0)
					str.append("+").append(this.mTransaction[a]).append(", ");
				else if (this.mTransaction[a] < 0)
					str.append(this.mTransaction[a]).append(", ");
			}
		} else
			str.append("0");
		str.append("\n");
		str.append("Final Balance       : $").append(String.format("%.2f", getFinalAccountBalance())).append("\n");
				
		return str.toString();
	}
	
	/**
	 * hashCode() methods, check to compare value of the field and attributes
	 */
	@Override
	public int hashCode() {
		final int prime = 31;
		int result = super.hashCode();
		result = prime * result + this.counterTransaction;
		result = prime * result + Arrays.hashCode(this.mTransaction);
		result = prime * result + this.maxTransaction;
		long temp;
		temp = Double.doubleToLongBits(this.service_charge);
		result = prime * result + (int) (temp ^ (temp >>> 32));
		return result;
	}
	
	/**
	 * subtract funds out of account_balance
	 * @param amount user need to withdraw from their account
	 * @return true if the account_balance isn't negative after withdrawal
	 */
	@Override
	public synchronized boolean withdraw(double amount) {
		if(this.counterTransaction < this.maxTransaction) {
			if (amount > 1) {
				if((getAccountBalance().doubleValue() - (amount + this.service_charge)) > 0) {
					Double balance = getAccountBalance().doubleValue();
					balance -= amount;
					this.mTransaction[this.counterTransaction] = (amount*-1);
					this.counterTransaction++;
					this.counterService++;
					if(this.service_charge > 0)
						this.total_service = this.counterService * this.service_charge;
					setAccountBalance(balance);
					return true;
				}
			}
			else
				System.out.println("Please check your withdraw amount!");
		} 
		else
			System.out.println("Transaction has reached limit!");
		return false;
	}
	
	/**
	 * add funds to current account_balance
	 * @param amount stored double type to add into account_balance
	 */
	@Override
	public synchronized boolean deposit(double amount) {
		boolean mDeposit = false;
		if(this.counterTransaction < this.maxTransaction) {
			Double balance = getAccountBalance().doubleValue();
			if (amount > 0) {
				balance += amount;
				this.mTransaction[this.counterTransaction] = amount;
				this.counterTransaction++;
				if(this.service_charge > 0)
					this.total_service = this.counterService * this.service_charge;
				setAccountBalance((balance));
				mDeposit = true;
			}
			else
				System.out.println("Please check your deposit amount!");
		}
		else
			System.out.println("Transaction has reached limit!");
	
		return mDeposit;
	}
	
	
	/**
	 * Final account_balance getter
	 * @return Final balance with service charges
	 */
	public synchronized BigDecimal getFinalAccountBalance() {
		BigDecimal temp = new BigDecimal(this.total_service);
		return getAccountBalance().subtract(temp);
		
	}

	/**
	 * counterTransaction getter
	 * @return the number of transaction occurred
	 */
	public int getCounterTransaction() {	return this.counterTransaction;	}
	
	/**
	 * total_service getter
	 * @return the amount of service has been charged
	 */
	public double getTotalServiceCharge() {	return this.total_service;	}

} 

