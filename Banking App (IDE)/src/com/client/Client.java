package com.client;

import java.io.*;
import java.net.*;
import java.util.Scanner;

import com.seneca.accounts.Account;
import com.seneca.business.Bank;

public class Client {
	
	public synchronized static void main(String[] args) {
		Socket AccountClient;
		Bank myBank = new Bank();
		String display = "";
		int option = 0;		
		
		try {
			//connect to server via an IP address/server name with a port number
			AccountClient = new Socket( InetAddress.getByName( "localhost" ), 5678 );
			System.out.println( "Connected to " + AccountClient.getInetAddress().getHostName() );
			
			//create input/output stream to the socket to transfer data between client and server
			ObjectOutputStream oosToServer = new ObjectOutputStream( AccountClient.getOutputStream() ); 
			ObjectInputStream  oisFromServer= new ObjectInputStream( AccountClient.getInputStream()  );

			System.out.println( "I/O streams connected to the socket" );		
		
			while(option != 7) {	
				//refresh to load data when connecting first time
				System.out.println("Data is receiving...");
				myBank.reset();
				int size = (int) oisFromServer.readObject(); 
			    Account[] tempting = (Account[]) oisFromServer.readObject();
			    for(int a = 0; a < size; a++)
			    	myBank.addAccount(tempting[a]);  	
			    tempting = null;
			    System.out.println(myBank);
				
				displayMenu(display);				//display the menu line	
				option = menuChoice();				//record the option select
				oosToServer.writeObject(option);

				switch(option) {
					//Open an Account (Done)
					case 1:
						//reload the information in case another thread has been modified
						myBank.reset();
						int size1 = (int) oisFromServer.readObject();
					    Account[] tempting1 = (Account[]) oisFromServer.readObject();
					    for(int a = 0; a < size1; a++)
					    	myBank.addAccount(tempting1[a]);
					    tempting1 = null;
					    
					    //get scanner object to fetch information from user
						Scanner option1 = new Scanner(System.in);
						System.out.print("Please enter the account type (CHQ/GIC)> ");
						String account = option1.nextLine();
					    String account_type = account.toUpperCase();
					    oosToServer.writeObject(account_type);
					    
					    //if user want to open a CHQ Account
					    if(account_type.equals("CHQ")) {
					    	System.out.print("Please enter account information at one line\n" + 
						    		   "(e.g. John M. Doe ; A1234 ; 1000.00 ; 0.25 ; 3) " + "\n");
						    String account_info = option1.nextLine();
						    oosToServer.writeObject(account_info);
					    }
					    
					    //if user want to open a GIC Account
					    else if(account_type.equals("GIC")) {
					    	 System.out.print("Please enter account information at one line\n" + 
							    		"(e.g. John M. Doe ; A1234 ; 1000.00 ; 1.5 ; 2) " + "\n");
							    String account_info = option1.nextLine();
							    oosToServer.writeObject(account_info);
					    }
					    else
					    	System.out.println("Command not accepted!" + "\n");
					    
					    String message1 = (String) oisFromServer.readObject();
					    System.out.println(message1);
					    break;
					
					//Close an Account (Done)
					case 2:
						//reload the information in case another thread has been modified
						 myBank.reset();
						int size2 = (int) oisFromServer.readObject(); 
					    Account[] tempting2 = (Account[]) oisFromServer.readObject();
					    for(int a = 0; a < size2; a++) {
					    	myBank.addAccount(tempting2[a]);
					    	//System.out.println(tempting2[a]);
					    }
					    tempting2 = null;
						
					    //get scanner object to fetch information from user
						boolean found2 = false;
						Scanner option2 = new Scanner(System.in);
					    System.out.print("Please enter account number> ");
					    String account_name2 = option2.nextLine();
					    account_name2 = account_name2.toUpperCase();
					    oosToServer.writeObject(account_name2);
					    found2 = (boolean) oisFromServer.readObject();
					    
					    //if an account has been found in the server
					    if(found2 == true) {
					    	System.out.print("Are you sure you want to delete account " + account_name2 + "? (Y/N)> ");
						    String checking = option2.nextLine();
						    checking = checking.toUpperCase();
						    
						    oosToServer.writeObject(checking);
						    String message = (String) oisFromServer.readObject();
						    System.out.println(message);			    
					    }
					    else
					    	System.out.println("Account not exist!\n");
					    
					    break;
					
					//Deposit Money
					case 3:
						//OPTION 2
						//to have the client receive the object from server, but client has to exit the program
						//and re-login to see the change, there's seem to be a bug here, balance won't update
						//even the server has been updated.
 
						//reload the information in case another thread has been modified
						myBank.reset();
						int size3 = (int) oisFromServer.readObject(); 
					    Account[] tempting3 = (Account[]) oisFromServer.readObject();
					    for(int a = 0; a < size3; a++)
					    	myBank.addAccount(tempting3[a]);
					    tempting3 = null;
						
					    //get scanner object to fetch information from user
						boolean found3 = false;
						Scanner option3 = new Scanner(System.in);
						System.out.print("Please enter account number> ");
						String account_name3 = option3.nextLine();
						account_name3 = account_name3.toUpperCase();
						oosToServer.writeObject(account_name3);
						found3 = (boolean) oisFromServer.readObject();
						
						//if an account has been found in the server
						if(found3 == true) {
							System.out.print("Please enter deposit amount> ");
							double mDeposit = option3.nextDouble();
							oosToServer.writeObject(mDeposit);
							String message3 = (String) oisFromServer.readObject();
							System.out.println(message3);						    
					    }
						
						else
					    	System.out.println("Account not exist!\n");
							
						Account[] testing = (Account[]) oisFromServer.readObject();
						Bank test = new Bank();
						test.reset();
						for(int i = 0; i < testing.length; i++)
							test.addAccount(testing[i]);
						//System.out.println(test);
						System.out.println("Please logout and login again to see the reflective changes!");
						
						
						/*
						//OPTION 1
						//receive the deposit amount from client
						//manually adjust the balance in the client side
						//REASON: for some reason when we update the data from the server
						//			the result in Client side doesn't match with the server (option 2)

						myBank.reset();
						int size3 = (int) oisFromServer.readObject(); 
					    Account[] tempting3 = (Account[]) oisFromServer.readObject();
					    for(int a = 0; a < size3; a++)
					    	myBank.addAccount(tempting3[a]);
					    tempting3 = null;
						
					    //get scanner object to fetch information from user
						boolean found3 = false;
						Scanner option3 = new Scanner(System.in);
						System.out.print("Please enter account number> ");
						String account_name3 = option3.nextLine();
						account_name3 = account_name3.toUpperCase();
						oosToServer.writeObject(account_name3);
						found3 = (boolean) oisFromServer.readObject();
						
						//if an account has been found in the server
						if(found3 == true) {
							System.out.print("Please enter deposit amount> ");
							double mDeposit = option3.nextDouble();
							oosToServer.writeObject(mDeposit);
							boolean balance = (boolean) oisFromServer.readObject();
							
							if(balance) {
								Account temp3 = myBank.searchByAccountNumber(account_name3);
								boolean b = temp3.deposit(mDeposit);
								if(b == true)
									System.out.println("DEPOSIT: deposit successfully!");
								else
									System.out.println("DEPOSIT: please check your account!");
								
								int size_3 = myBank.getSize();
								oosToServer.writeObject(size_3);
								Account[] m_deposit = myBank.getAllAccounts();
								oosToServer.writeObject(m_deposit);
								oosToServer.flush();
							}
							String message4 = (String) oisFromServer.readObject();
							System.out.println(message4);
						}					    
						else
					    	System.out.println("Account not exist!\n");
						*/
						
						break;
						
					//Withdraw Money
					case 4:
						//OPTION 2
						//to have the client receive the object from server, but client has to exit the program
						//and re-login to see the change, there's seem to be a bug here, balance won't update
						//even the server has been updated.
						
						//reload the information in case another thread has been modified
						myBank.reset();
						int size4 = (int) oisFromServer.readObject(); 
					    Account[] tempting4 = (Account[]) oisFromServer.readObject();
					    for(int a = 0; a < size4; a++)
					    	myBank.addAccount(tempting4[a]);
					    tempting4 = null;
						
					    //get scanner object to fetch information from user
						boolean found4 = false;
						Scanner option4 = new Scanner(System.in);
						System.out.print("Please enter account number> ");
						String account_name4 = option4.nextLine();
						account_name4 = account_name4.toUpperCase();
						oosToServer.writeObject(account_name4);
						found4 = (boolean) oisFromServer.readObject();
						
						//if an account has been found in the server
						if(found4 == true) {
							System.out.print("Please enter withdraw amount> ");
							double mWithdraw = option4.nextDouble();
							oosToServer.writeObject(mWithdraw);
						    String message = (String) oisFromServer.readObject();
						    System.out.println(message);
					    }
						else
					    	System.out.println("Account not exist!\n");
			
						myBank.reset();
						System.out.println("Please logout and login again to see the reflective changes!");
						myBank = (Bank) oisFromServer.readObject(); 	
						
						/*
						//OPTION 1
						//receive the withdraw amount from client
						//manually adjust the balance in the client side
						//REASON: for some reason when we update the data from the server
						//			the result in Client side doesn't match with the server (option 2)
						myBank.reset();
						int size4 = (int) oisFromServer.readObject(); 
					    Account[] tempting4 = (Account[]) oisFromServer.readObject();
					    for(int a = 0; a < size4; a++)
					    	myBank.addAccount(tempting4[a]);
					    tempting4 = null;
						
					    //get scanner object to fetch information from user
						boolean found4 = false;
						Scanner option4 = new Scanner(System.in);
						System.out.print("Please enter account number> ");
						String account_name4 = option4.nextLine();
						account_name4 = account_name4.toUpperCase();
						oosToServer.writeObject(account_name4);
						found4 = (boolean) oisFromServer.readObject();
						
						//if an account has been found in the server
						if(found4 == true) {
							System.out.print("Please enter withdraw amount> ");
							double mWithdraw = option4.nextDouble();
							oosToServer.writeObject(mWithdraw);
							boolean balance = (boolean) oisFromServer.readObject();
							
							if(balance) {
								Account temp4 = myBank.searchByAccountNumber(account_name4);
								boolean b = temp4.withdraw(mWithdraw);
								if(b == true)
									System.out.println("WITHDRAW: withdraw successfully!");
								else
									System.out.println("WITHDRAW: please check your account!");
								int size_4 = myBank.getSize();
								oosToServer.writeObject(size_4);
								Account[] withdraw = myBank.getAllAccounts();
								oosToServer.writeObject(withdraw);
								oosToServer.flush();
							}
							String message4 = (String) oisFromServer.readObject();
							System.out.println(message4);
						}
						else
					    	System.out.println("Account not exist!\n");
						 */
	
						break;
					
					//Display Account (Done)
					case 5:
						Scanner option5 = new Scanner(System.in);
						System.out.println("1) Display all accounts with the same account name");
						System.out.println("2) Display all accounts with the same final balance");
						System.out.println("3) Display all accounts opened at the bank.");
						System.out.print("Option> ");
						int select5 = option5.nextInt();
						oosToServer.writeObject(select5);
						
						if(select5 == 1 || select5 == 2 || select5 == 3) {
							if(select5 == 1) {
								Scanner temp_5 = new Scanner(System.in);
						    	System.out.print("Please enter account name> ");
								String account_name5 = temp_5.nextLine();
								if(!account_name5.equals(""))
									oosToServer.writeObject(account_name5);
								Account[] temp5 = (Account[]) oisFromServer.readObject();
								for(int i = 0; i < temp5.length; i++)
						    		displayAccount(temp5[i]);
							} 
							else if(select5 == 2) {
								Scanner temp_5 = new Scanner(System.in);
						    	System.out.print("Please enter account balance> ");
						    	double account_balance5 = temp_5.nextDouble();
						    	if(account_balance5 > 0)
						    		oosToServer.writeObject(account_balance5);
						    	Account[] temp5 = (Account[]) oisFromServer.readObject();
								for(int i = 0; i < temp5.length; i++)
						    		displayAccount(temp5[i]);
							} 
							else if(select5 == 3) {
								myBank.reset();
								int size5 = (int) oisFromServer.readObject();
							    Account[] tempting5 = (Account[]) oisFromServer.readObject();
							    for(int a = 0; a < size5; a++)
							    	myBank.addAccount(tempting5[a]);
							    System.out.println(myBank);
							    tempting5 = null;
							}
						}
						else
							System.out.println("Command not accepted!" + "\n");
						break;
					
					//Display a tax statement (Done)
					case 6:
						System.out.println("Displaying Tax Statement...");
						String display6 = (String) oisFromServer.readObject();
						System.out.println(display6);
						break;
					
					//Exit (Done)
					case 7: 
						System.out.println("Thank You!");
						break;
				}
			}
		} catch (Exception error){ 	error.printStackTrace(); } 
	}  
	
	/**
	 * create a display menu for user to select
	 * @param bankName 
	 */
	static void displayMenu(String bankName) {
		System.out.println();
		bankName += "Welcome to Seneca@York Bank!" 	+ "\n" +
					"1. Open an account.		 "  + "\n" +
					"2. Close an account.		 " 	+ "\n" +
					"3. Deposit money.			 "  + "\n" +
					"4. Withdraw money.			 " 	+ "\n" + 
					"5. Display account.		 "  + "\n" +
					"6. Display a tax statement. " 	+ "\n" + 
					"7. Exit.					 "  ;	
		System.out.println(bankName);
	};
	
	/**
	 * record the option user choose
	 * @return the option that user selected
	 */
	static int menuChoice() {
		int option = 0;
		Scanner select = new Scanner(System.in);  // Create a Scanner object
		System.out.print("Please enter your choice> ");
		option = select.nextInt(); 
		return option;
	};
	
	/**
	 * display account information to user
	 * @param account
	 */
	static void displayAccount(Account account) {	System.out.println(account);	};
}