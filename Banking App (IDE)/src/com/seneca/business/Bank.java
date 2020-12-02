/**
* Application to display user information
* with associated bank account and their account balance
*
* @author Carlianz Pura (02/11/2020)
*/

package com.seneca.business;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;

import com.seneca.accounts.Account;
import com.seneca.accounts.GIC;

public class Bank implements Serializable{
	private static final long serialVersionUID = 1L;
	private ArrayList<Account> m_bank;
	private String bank_name;
	
	/**
	 * default/no-argument constructor
	 * set bank_name to default 
	 * @return nothing
	 */
	public Bank() {
		this("Seneca@York");
		m_bank = new ArrayList<Account>();
	}	
	
	/**
	 * one argument constructor
	 * @param bank_name contains Bank Name for the program
	 * @return nothing
	 */
	public Bank(String m_bank_name) {
		if(m_bank_name == null) {
			throw new IllegalArgumentException();
		}
		
		this.bank_name = m_bank_name;
		m_bank = new ArrayList<Account>();
	}	
	
	/**
	 * toString method, display the value
	 * @return nothing
	 */
	public String toString() {
	   	  int m = m_bank.size();
	   	  StringBuffer str = new StringBuffer();
	   	  str.append("\n*** Welcome to the Bank of ").append(this.bank_name).append(" ***\n");
	   	  str.append("It has ").append(m).append(" account(s)\n");
	   	  for(int a = 0; a < m; a++) {
	   		  Account temp = this.m_bank.get(a);
	   		  String[] character = temp.getFullName().split("\\s+");
	   		  str.append(a+1).append(". number: ").append(temp.getAccountNumber().trim());
	   		  str.append(", name: ").append(Arrays.toString(character).trim());
	   		  str.append(", balance: $").append(String.format("%.2f", temp.getAccountBalance())).append("\n");
	   	  }
	   	  return str.toString();
	   }
	
	/**
	 * hashCode() method, object create a unique hashCode and compare it with other object
	 */
	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((bank_name == null) ? 0 : bank_name.hashCode());
		result = prime * result + ((m_bank == null) ? 0 : m_bank.hashCode());
		return result;
	}

	/**
	 * equals method, compare the passing value with the stored value
	 * @param contains string of bank name
	 * @param instance of Object class to be compare
	 * @return boolean if Object is match or not	
	 */
	public boolean equals(Object tester){
		boolean check = false;

		if ( tester instanceof Bank ) {
			Bank z2 = (Bank) tester;
			if ( ( z2.getBank_name().equals(this.bank_name)) &&
			     (z2.m_bank.equals(this.m_bank))	)
			     check = true;
		}
		return check;
	}

	/**
	 * addAccount method will check for an existing duplicate account number and avoid adding
	 * @param newAccount contains Account type to be add into ArrayList of m_bank
	 * @return boolean if added successfully
	 */
	public synchronized boolean addAccount(Account newAccount) {
		boolean m_add = false;
		
		if(newAccount.getFullName().equals("") ||
		   newAccount.getAccountNumber().equals("") ||
		   newAccount.getAccountBalance().doubleValue() < 0) {
			return false;
		}
		
		if(m_bank.size() == 0) {
			m_add = true;
		}
		else {
			for(int a = 0; a < m_bank.size(); a++) {
				//loop in each m_bank position and fetch the data
				//convert it into Account type
				Account temp = m_bank.get(a);
				
				//check if an existing account number is exist
				if(newAccount.getAccountNumber().equals(temp.getAccountNumber())	)
					return false;
				
				//if doesn't, add the account
				else
					m_add = true;
			}
		}
		if(m_add == true) {
			m_bank.add(newAccount);
		}
		return m_add;
	}	

	/**
	 * searchByAccountName method, find the account by accountName and return it
	 * @param accountName
	 * @return array of matching accountName
	 */
	public synchronized Account[] searchByAccountName(String accountName) {
		int j = 0;
		
		//get array size of those accounts with account name of accountName argument
		for (int i = 0; i < this.m_bank.size(); i++){
			if (this.m_bank.get(i).getFullName().equals(accountName)){
				j++;
			}
		}
		
		//create temp array to hold the matched accounts
		Account [] accArray = new Account [j];
		j = 0;
		
		//add accounts to accArray
		for (int i = 0; i < this.m_bank.size(); i++){
			if (this.m_bank.get(i).getFullName().equals(accountName)){
				accArray[j] = this.m_bank.get(i);
				j++;
			}
		}
		
		//no accounts found with indicated name
		if (accArray.length == 0) {
			System.out.print("\n*** FAILED: ACCOUNT CANNOT BE RETRIEVED! ***\n\n");
			return accArray;
		}
		
		return accArray;
	}
	
	/**
	 * searchByAccountNumber method, find the account by accountNumber and return it
	 * @param accountNumber
	 * @return matching accountNumber
	 */
	public synchronized Account searchByAccountNumber( String accountNumber ) {
		if(accountNumber.equals(null) || accountNumber == ""){	return null;	}

        for(int j = 0; j < this.m_bank.size();j++) {
            if(this.m_bank.get(j).getAccountNumber().equals(accountNumber)){
                return this.m_bank.get(j);
            }
        }
        return null;
	}
	
	/**
	 * removeAccount method will remove a certain account from the list if matched
	 * @param accountNumber
	 * @return
	 */
	public synchronized Account removeAccount( String accountNumber ){
		//return null if it receives null reference
		if( accountNumber.equals(null) || accountNumber == "")
			return null;
		
		//if account exists remove account
		for(int i = 0; i < this.m_bank.size();i++) {
			if(this.m_bank.get(i).getAccountNumber().equals(accountNumber)){
				Account temp = this.m_bank.get(i);
				this.m_bank.remove(i);
				return temp;
			}
		}
		return null;
	}
	
	/**
	 * @param m_balance
	 * @return array of position where the account balance is match with m_balance
	 */
	public synchronized Account[] searchByBalance(BigDecimal bal) {
		int j = 0;
		
		//get array size of those accounts with account balance of bal argument
		for (int i=0; i < this.m_bank.size(); i++){
			if (this.m_bank.get(i).getAccountBalance().equals(bal)){
				j++;
			}
		}
		
		//create temp array to hold the matched accounts
		Account [] accArray = new Account [j];
		j = 0;
		
		//add accounts to accArray
		for (int i=0; i < this.m_bank.size(); i++){
			if (this.m_bank.get(i).getAccountBalance().equals(bal)){
				accArray[j] = this.m_bank.get(i);
				j++;
			}
		}
		
		//no accounts found with indicated balance
		if (accArray.length == 0) {
			System.out.print("\n***FAILED: ACCOUNT CANNOT BE RETRIEVED! ***\n\n");
			return accArray;
		}
		return accArray;
	}
	
	/**
	 * getTaxableAccount method find all the GIC account and return it
	 * @return array of all Taxable Account
	 */
	public synchronized Account[] getTaxableAccounts( ) {
		int j = 0;
		Account [] temp = new Account [m_bank.size()];
		
		for(int i = 0; i < m_bank.size(); i++) {
			temp[i] = m_bank.get(i);;
			j++; //4
		}
		
		Account [] accArray = new Account [j]; //3
		j = 0;
		
		for(int i = 0; i < temp.length; i++) {
			if ((temp[i] instanceof GIC)) {
				accArray[j] = temp[i];
				j++;
			}
		}
		Account[] taxAble = new Account[j];
		for(int a = 0; a < j; a++)
			taxAble[a] = accArray[a];
		
        return taxAble;
      }
	
	public synchronized Account[] getAllAccounts( ) {
		Account [] accArray = new Account [m_bank.size()];
		for(int i = 0; i < m_bank.size(); i++) {
			accArray[i] = m_bank.get(i);
		}
        return accArray;
      }
	
	/**
	 * sort method from Collections that allow it to sort the ArrayList in order
	 * @return new ArrayList<Account> m_bank with new order
	 */
	public ArrayList<Account> sortAscending() {         
	    Collections.sort(this.m_bank);        
	    return this.m_bank;     
	  }    
	
	/**
	 * bank_name getter
	 * @return bank_name String value
	 */
	public String getBank_name() {	return bank_name;	}
	
	/**
	 * ArrayList<Account> getter
	 * @return list of Account in Bank
	 */
	public ArrayList<Account> getBankArray(){	return this.m_bank;	}
	
	/**
	 * bank_name setter
	 * @param m_name contain bank_name to be passed into current bank_name var
	 */
	public void setBank_name(String m_name) {	this.bank_name = m_name;	}	
	
	public void reset() {	this.m_bank.clear();	}
	
	public int getSize() {	return this.m_bank.size(); 	}
}