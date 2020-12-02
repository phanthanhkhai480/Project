package com.server;

import java.io.*;
import java.net.*;

import com.seneca.accounts.Chequing;
import com.seneca.accounts.GIC;
import com.seneca.business.Bank;

public class Server {
	public synchronized static void main(String[] args) throws IOException, InterruptedException{
		ServerSocket AccountServer;
		Bank loadBank = new Bank();
		try {
			AccountServer = new ServerSocket(5678);
			int count = 0;
			System.out.println( "\n" + "listening for a connection..." );
			
			loadBank(loadBank);
			while(true) {
				Socket socket = AccountServer.accept();
				count++;
				
				System.out.println();
				System.out.print( "start a thread for client #" + count + ": ");
	        	System.out.print( "host name: " + socket.getInetAddress().getHostName() +
                                    ", IP address: " + socket.getInetAddress().getHostAddress() );
	        	
	        	Thread t = new ClientHandler(socket, loadBank);
            	t.start();
			}
		} catch(IOException e ) { e.printStackTrace();     }
	}
	
	static synchronized void loadBank(Bank bank) {
		bank.addAccount(new Chequing("John Doe","A1111",10000.00, 0.25, 5));
		bank.addAccount(new GIC("John Doe","D1234",6000.00, 1.5, 2));
		
		bank.addAccount(new Chequing("Mary Ryan","C0005",10000.00, 0.25, 5));
		bank.addAccount(new GIC("Mary Ryan","E1234",15000.00, 2.5, 4));
	}
}