***PLEASE GO OVER FINAL ASSESSMENT FILE FIRST***

Client:
For Location: cd C:\Users\Khai Phan Thanh\Downloads\Documents\BTP400\Final Assessment\src
For Compile: javac ./com/client/Client.java ./com/seneca/accounts/Account.java
For Running: java com.client.Client com.seneca.accounts.Account.java

Server
For Location: cd C:\Users\Khai Phan Thanh\Downloads\Documents\BTP400\Final Assessment\src
For Compile: javac ./com/server/Server.java ./com/seneca/accounts/Account.java
For Running: java com.server.Server com.seneca.accounts.Account.java

Image Explaination:
CMD on the left: Client 1
CMD on the right: Client 2

Add-Remove Picture
Client 1 begins by adding the account (John M. Doe; A1234; 1000.00; 1.5; 2) to the server
Client 2 begins by delete the account (C0005), after the delete, the account A1234 appear

IF(OPTION #1)
	Deposit Picture (Option #1)
	Client 1 try to deposit into account (C0005) but it doesn't exist anymore
	Client 2 try to add 3000 into account (A1111) and it was successfully
	Client 1 try to add 200 into account (A1111) and it was successfully (reflected with the pervious add)
	Client 1 try to add 300 into account (D1234) which is GIC and it was unsuccessfully (no data has been reflected)
	There was a typo error on Client 2 that caused the Exception

	Withdraw-Display picture (Option #1)
	Client 1 try to deduct 323 from account (A1111) and it was successfully
	Client 2 try to deduct 325 from account (A1111) and it was successfully (reflected with the pervious deduct 
	Client 1 try to display account detail from account name John Doe (A1111, D1234)
	Client 2 try to display account detail from account balance of 12552 (A1111)

	Display-Tax-Server picture (Option #1)
	Client 1 (now to left top) display all account information
	Client 2 (now to left bottom) display tax information of all GIC account
	Left side is the server input while 2 client interact to the server

IF(OPTION #2)
	Deposit-Withdraw Option #2
	Client 1 try to add 1000 into account (A1111) and it was successfully, but it show no update on the client
	Client 1 logout and login again and is able to see the new balance
	Client 2 try to deduct 10500 from account (A1111) and it was successfully (because the server has updated 1000 previously)
	Client 2 logout and login again and is able to see the new balance