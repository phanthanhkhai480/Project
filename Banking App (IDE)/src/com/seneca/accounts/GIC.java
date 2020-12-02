/**
* Application to display user GIC account information
*
* @author Khai Phan and Carlianz Pura (02/20/2020)
*/

package com.seneca.accounts;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Arrays;

public class GIC extends Account implements Taxable, Serializable {
	private static final long serialVersionUID = 1L;
	double mInterest = 0, interestRate = 0, mTax = 0;
	int investmentYears = 0;
	BigDecimal current_balance = super.getAccountBalance();
	
	/**
	 * default constructor, set to default values
	 * @return nothing
	 */
	public GIC() {
		this.mInterest = 0;
		this.mTax = 0;
		this.interestRate = 1.25;
		this.investmentYears = 1;
	}
	
	public GIC(String fullName, String accountNumber, double startingBalance, 
			double interestRate, int investmentYear) {
		super(fullName, accountNumber, startingBalance);
		
		if (interestRate < 0 || investmentYear < 0)
			throw new IllegalArgumentException();
		
		this.interestRate = interestRate;
		this.investmentYears = investmentYear;
	}
	
	/**
	 * calculate tax with a given tax rate
	 */
	public void calculateTax() {	this.mTax = this.mInterest * tax_rate;	}
	
	/**
	 * calculate amount of tax based on given rate and given income
	 * @return amount of taxes
	 */
	public double getTaxAmount() {	return this.mTax;	}

	/**
	 * similar to to_string method, use to display information
	 * @return information to be display
	 */
	public String createTaxStatement() {
		// TODO Auto-generated method stub
		String[] temp = getFullName().split(" ");
		StringBuffer str = new StringBuffer();
		str.append("Name:").append(Arrays.toString(temp)).append("\n");
		str.append("Tax Rate: ").append(tax_rate).append("%").append("\n").append("\n");
		str.append("Account Number : ").append(getAccountNumber()).append("\n");
		if(getInterestIncome() > 1000) {
			str.append("Interest Income: $").append(String.format("%8.2f", getInterestIncome())).append("\n");
			str.append("Amount of tax  : $").append(String.format("%8.2f", getInterestIncome()/tax_rate)).append("\n");
		} else {
			str.append("Interest Income: $").append(String.format("%6.2f", getInterestIncome())).append("\n");
			str.append("Amount of tax  : $").append(String.format("%6.2f", getInterestIncome()/tax_rate)).append("\n");
		}
		return str.toString();
	}
	
	/**
	* hashCode() methods, check to compare value of the field and attributes
	*/
	@Override
	public int hashCode() {
		final int prime = 31;
		int result = super.hashCode();
		long temp;
		temp = Double.doubleToLongBits(interestRate);
		result = prime * result + (int) (temp ^ (temp >>> 32));
		result = prime * result + investmentYears;
		temp = Double.doubleToLongBits(mTax);
		result = prime * result + (int) (temp ^ (temp >>> 32));
		return result;
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
		GIC other = (GIC) obj;
		if (Double.doubleToLongBits(interestRate) != Double.doubleToLongBits(other.interestRate))
			return false;
		if (investmentYears != other.investmentYears)
			return false;
		if (Double.doubleToLongBits(mTax) != Double.doubleToLongBits(other.mTax))
			return false;
		return true;
	}

	/**
	 * toString method, display the value
	 * @return nothing
	 */
	@Override
	public String toString() {	
		StringBuffer str = new StringBuffer();
		str.append(super.toString());
		str.append("Account Type                : GIC").append("\n");
		str.append("Annual Interest Rate        : ").append(String.format("%.2f", this.interestRate)).append("%\n");
		str.append("Period of Investments       : ").append(this.investmentYears + " years").append("\n");
		str.append("Interest Income at Maturity : $").append(String.format("%.2f", getInterestIncome())).append("\n");
		str.append("Balance at Maturity         : $").append(String.format("%.2f", getAccountBalance())).append("\n");
	
		return str.toString();
	}
	
	
	@Override
	public synchronized boolean withdraw(double amount) {	return false;	}
	
	@Override
	public synchronized boolean deposit(double amount) {	return false;	}
	
	@SuppressWarnings("deprecation")
	@Override
	public synchronized BigDecimal getAccountBalance() {	
		double val = current_balance.doubleValue() * Math.pow((1 + (interestRate / 100)), this.investmentYears);
		BigDecimal val2 = new BigDecimal(val);
		val2 = val2.setScale(2, BigDecimal.ROUND_HALF_EVEN);
		return val2;	
	}
	
	public synchronized double getInterestIncome() {	
		BigDecimal interestIncome = getAccountBalance().subtract(current_balance);
		return interestIncome.doubleValue();
	}
}
