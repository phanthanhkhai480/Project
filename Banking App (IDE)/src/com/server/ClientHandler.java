package com.server;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.math.BigDecimal;
import java.net.Socket;

import com.seneca.accounts.Account;
import com.seneca.accounts.Chequing;
import com.seneca.accounts.GIC;
import com.seneca.accounts.Taxable;
import com.seneca.business.Bank;

public class ClientHandler extends Thread {
	private Socket socket;
	ObjectOutputStream oosToClient;
	ObjectInputStream  oisFromClient;
	
	Bank myNewBank;		//create Bank object to hold account
	int option = 0;		//hold option selected by user
	
	public ClientHandler(Socket mSocket, Bank mLoadBank) { 
		this.socket = mSocket;
		myNewBank = mLoadBank; 
	}
	
	@Override
	public synchronized void run() {
		synchronized(this) {
			try {
				this.oosToClient = new ObjectOutputStream( socket.getOutputStream() );
				this.oisFromClient = new ObjectInputStream( socket.getInputStream()  );
				
				System.out.println("\n" + socket + "is connected!");
				while(option != 7) { 
					System.out.println("Data is sending...");
					sendDataToClient();
					System.out.println(socket + "is waiting for option... ");
					
					option = (int) oisFromClient.readObject();
					System.out.println(socket + "has selected " + option);
					 
					switch(option) {
						//Open an Account (Done)
						case 1:
							String message1 = "";
							sendDataToClient();
							String account_type = (String) oisFromClient.readObject();
							
							//if user want to open a Chequing Account
						    if(account_type.equals("CHQ")) {
						    	System.out.println(socket + "is opening CHQ Account...");
						    	String account_info = (String) oisFromClient.readObject();
							    String[] temp = account_info.split(";"); 
							    for(int a = 0; a < temp.length; a++)
							    	temp[a] = temp[a].trim().replaceAll("\\s+"," ");

							    myNewBank.addAccount(new Chequing(String.valueOf(temp[0]), String.valueOf(temp[1]), 
							    		Double.valueOf(temp[2]), Double.valueOf(temp[3]), Integer.valueOf(temp[4])));
							    message1 = "ADD ACCOUNT: Chequing account has been created!";
						    }
						    else if(account_type.equals("GIC")) {
						    	System.out.println(socket + "is opening GIC Account...");
						    		String account_info = (String) oisFromClient.readObject();
								    String[] temp = account_info.split(";"); 
								    for(int a = 0; a < temp.length; a++) 
								    	temp[a] = temp[a].trim().replaceAll("\\s+"," ");
								    	
								    myNewBank.addAccount(new GIC(String.valueOf(temp[0]), String.valueOf(temp[1]), 
								    		Double.valueOf(temp[2]), Double.valueOf(temp[3]), Integer.valueOf(temp[4])));
								    message1 = "ADD ACCOUNT: GIC account has been created!";
						    }
						    else {
						    	System.out.println(" - Not on the list, moving on...");
						    	message1 = "Unable to add account!";
						    }
						    	

						    oosToClient.writeObject(message1);
							break;
						
						//Close an Account (Done)
						case 2:
							sendDataToClient();
							boolean found2 = false;
							String message2 = "";
							String account_name2 = (String) oisFromClient.readObject();
							Account temp2 = myNewBank.searchByAccountNumber(account_name2);
						    if(temp2 != null) {
						    	found2 = true;
						    	oosToClient.writeObject(found2);
						    	String checking = (String) oisFromClient.readObject();
						    	
								if(checking.equals("Y")) {
									myNewBank.removeAccount(account_name2);
									message2 = "DELETE ACCOUNT: account deleted!";
								}
								else if(checking.equals("N")) 
									message2 = "DELETE ACCOUNT: delete cancelled!";						    
								else 
									message2 = "DELETE ACCOUNT: command not accepted!";
								oosToClient.writeObject(message2); 
						    } 
						    else {
						    	oosToClient.writeObject(found2);
						    	System.out.println("Account not exist! moving on..." + "\n");
						    }
						   
							break;
						
						//Deposit Money
						case 3:
							//OPTION 2
							//to have the client receive the object from server, but client has to exit the program
							//and re-login to see the change, there's seem to be a bug here, balance won't update
							//even the server has been updated.
							//reload the information in case another thread has been modified
							sendDataToClient();
							boolean found3 = false;
							String message3 = "";
							String account_name3 = (String) oisFromClient.readObject();
							Account temp3 = myNewBank.searchByAccountNumber(account_name3);
							
							if(temp3 != null) {
								found3 = true;
								oosToClient.writeObject(found3);
								double mDeposit = (double) oisFromClient.readObject();
								if(mDeposit < 1)
									message3 = "DEPOSIT: please check deposit amount!";
								else {							
									boolean b = temp3.deposit(mDeposit);
									if(b == true)
										message3 = "DEPOSIT: deposit successfully!";
									else
										message3 = "DEPOSIT: please check your account!";
								}	
								oosToClient.writeObject(message3);	
							} 
							else {
						    	oosToClient.writeObject(found3);
						    	System.out.println("DEPOSIT: account not exist! moving on..." + "\n");
						    }

							Account[] temp = myNewBank.getAllAccounts();
							Bank mTemp = new Bank();
							mTemp.reset();
							for(int i = 0; i < temp.length; i++)
								mTemp.addAccount(temp[i]);
							System.out.println(mTemp);
							oosToClient.writeObject(temp);
							oosToClient.flush();
						    
							
							/*
							//OPTION 1
							//receive the amount of deposit money from the client side
							//use method to adjust the balance and updated in the server

							//reload the information in case another thread has been modified
							sendDataToClient();
							boolean found3 = false;
							String message3 = "";
							String account_name3 = (String) oisFromClient.readObject();
							Account temp3 = myNewBank.searchByAccountNumber(account_name3);
							
							if(temp3 != null) {
								found3 = true;
								oosToClient.writeObject(found3);
								double mDeposit = (double) oisFromClient.readObject();
								boolean balance;
								if(mDeposit < 1) {
									balance = false;
									oosToClient.writeObject(balance);
									message3 = "DEPOSIT: please check deposit amount!";
								} else {
									balance = true;
									oosToClient.writeObject(balance);
									myNewBank.reset();
									int size3 = (int) oisFromClient.readObject();
									Account[] deposit = (Account[]) oisFromClient.readObject();
									for(int c = 0 ; c < size3; c++)
										myNewBank.addAccount(deposit[c]);
									System.out.println(myNewBank);
								}
								oosToClient.writeObject(message3);	
							} 
							else {
						    	oosToClient.writeObject(found3);
						    	System.out.println("DEPOSIT: account not exist! moving on..." + "\n");
						    }
							*/
							
							break;
						
						//Withdraw Money
						case 4:
							//OPTION 2
							//to have the client receive the object from server, but client has to exit the program
							//and re-login to see the change, there's seem to be a bug here, balance won't update
							//even the server has been updated.
							//reload the information in case another thread has been modified
							sendDataToClient();
							boolean found4 = false;
							String message4 = "";
							String account_name4 = (String) oisFromClient.readObject();
							Account temp4 = myNewBank.searchByAccountNumber(account_name4);
							
							if(temp4 != null) {
								found4 = true;
								oosToClient.writeObject(found4);
								double mWithDraw = (double) oisFromClient.readObject();
								if(mWithDraw < 1) 
									message4 = "WITHDRAW: Please check Withdraw Amount!";
								else {
									boolean a = temp4.withdraw(mWithDraw);
									if(a == true)
										message4 = "WITHDRAW: withdraw successfully!";
									else
										message4 = "WITHDRAW: please check your account!";
								}
								oosToClient.writeObject(message4);	
							} 
							else {
						    	oosToClient.writeObject(found4);
						    	System.out.println("WITHDRAW: account not exist! moving on...\" + \"\\n");
						    }
							Bank sending4 = new Bank();
							sending4 = myNewBank;
							System.out.println(sending4);
							oosToClient.writeObject(sending4);
							oosToClient.flush();
							
							
							/*
							//OPTION 1
							//receive the amount of withdraw money from the client side
							//use method to adjust the balance and updated in the server
							
							//reload the information in case another thread has been modified
							sendDataToClient();
							boolean found4 = false;
							String message4 = "";
							String account_name4 = (String) oisFromClient.readObject();
							Account temp4 = myNewBank.searchByAccountNumber(account_name4);
							
							if(temp4 != null) {
								found4 = true;
								oosToClient.writeObject(found4);
								double mWithDraw = (double) oisFromClient.readObject();
								boolean balance;
								if(mWithDraw < 1) {
									balance = false;
									oosToClient.writeObject(balance);
									message4 = "WITHDRAW: please check withdraw amount!";
								}
								else {
									balance = true;
									oosToClient.writeObject(balance);
									myNewBank.reset();
									int size4 = (int) oisFromClient.readObject();
									Account[] withdraw = (Account[]) oisFromClient.readObject();
									for(int d = 0 ; d < size4; d++)
										myNewBank.addAccount(withdraw[d]);
									System.out.println(myNewBank);
								}	
								oosToClient.writeObject(message4);	
							} 
							else {
						    	oosToClient.writeObject(found4);
						    	System.out.println("WITHDRAW: account not exist! moving on..." + "\n");
						    }
							*/
							
							break;
						
						//Display Account (Done)
						case 5:
							System.out.print("Waiting for display option... ");
							int display_option = (int) oisFromClient.readObject();
							System.out.println(display_option);
							
							if(display_option == 1) {
						    	String account_name5 = (String) oisFromClient.readObject();
						    	System.out.println("Fetching Account...");
						    	Account[] temp5 = myNewBank.searchByAccountName(account_name5);
						    	oosToClient.writeObject(temp5);
						    }
						    
						    else if(display_option == 2) {
						    	double account_balance5 = (double) oisFromClient.readObject();
						    	BigDecimal balance5 = new BigDecimal(account_balance5);
						    	System.out.println("Fetching Account...");
					    		Account[] temp5 = myNewBank.searchByBalance(balance5);
					    		oosToClient.writeObject(temp5);
						    } 
						    
						    else if(display_option == 3) {	sendDataToClient();	}
						    	 
						    else
						    	System.out.println(" - Option not available, moving on...");
							
							break;
							 
						//Display a tax statement (Done)
						case 6:
							String stringName = "";
							String display6 = "";
							Account[] temp6 = myNewBank.getTaxableAccounts();
							int[] counter6 = new int[(temp6.length)];
							  
							for(int a = 0; a < (temp6.length); a++) {
								for(int b = 0; b < (temp6.length); b++) {
									if(temp6[a].getFullName().equals(temp6[b].getFullName()))
										counter6[a]++;
								}
							 }
							  
							for(int i = 0; i < temp6.length; i++) {
								stringName = taxStatement1((Taxable)temp6[i]);
								display6 +="[" + counter6[i] + "]" + "\n" + stringName + "\n"; 
							}
							
							oosToClient.writeObject(display6);
							break;
						
						//Exit (Done)
						case 7:
							System.out.println(socket + "is exiting...!");
							System.out.println();
							exitProgram();
							break;	
					}
				}
			} catch (IOException e ) 			{ e.printStackTrace();  } 
			  catch (ClassNotFoundException e) 	{ e.printStackTrace();	} 
		}	
	}
	
	/**
	 * method to send Bank object to Client for display purpuse
	 */
	public synchronized void sendDataToClient() {	
		try {	
			//System.out.println(myNewBank);
			int size = myNewBank.getSize();
			oosToClient.writeObject(size);
			Account[] tempting = myNewBank.getAllAccounts();
			oosToClient.writeObject(tempting);
			oosToClient.flush();
		} catch (IOException e) {	e.printStackTrace();	} 
	}

	/**
	 * display tax information
	 * @param a is called to display information of Taxable Interfaces
	 * @return string of Taxable Interfaces
	 */
	public synchronized String taxStatement( Taxable a ) {	return a.createTaxStatement();	}
	
	/**
	 * method to exit the program with all the necessary closing
	 */
	public synchronized void exitProgram() {
		try {
			oosToClient.close();
			oisFromClient.close();
			socket.close();
		} catch (IOException e) {	e.printStackTrace();	}	
	}
	
	public String taxStatement1( Taxable a ) {

	      return a.createTaxStatement();
	   }
}

