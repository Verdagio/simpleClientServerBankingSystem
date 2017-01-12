package server;

import java.io.*;
import java.net.*;
import java.util.LinkedList;

//Written by Daniel Verdejo - G00282931	3/1/17
public class Responder {
	public static void main(String [] args) throws Exception{
		/*	Setup socket for Server & client 
		 * 	Setup thread for client requests
		 * */
		
		ServerSocket servSock = new ServerSocket(2004,100);
		System.out.println("server waiting..");
		int i = -1;
		while(true){
			System.out.println(". . .");
			Socket clientSock = servSock.accept();//blocking method, wait here until scheduler is ready to handle you
			System.out.println("Client connected.. running thread");
			new Thread(new ClientServiceThread(clientSock, "Client: " + i++)).start();//start a new instance for the client thread
			
			
		}//while
	}//main
}//class responder

//inner class that will service the clinet request
class ClientServiceThread implements Runnable {
	Socket conn;
	String input;
	String cId;
	ObjectOutputStream out;
	ObjectInputStream in;
	
	LinkedList <Account> ls = new LinkedList<>();
	LinkedList <Transaction> transactions = new LinkedList<>();
	Account acc = new Account();
	
	public ClientServiceThread(Socket s, String i){
		conn = s;
		cId = i;
	}//Constructor 
	
	public void run(){
	ls.add(new Account("admin", "12 main street", "ABC1234", "admin", "admin"));//place holder account just to test stuff
		try {
			streams();
			sendMsg("Connecton Active!");
			active();

		} catch (Exception e) {
			show("... " + e.getMessage());
		}//try catch
	}//run
	
	//Register new user
	public void register() throws ClassNotFoundException, IOException{
		show("Registration form: ");
		boolean invalid = true;
		Account acc = new Account();
		
		//String x = (String) in.readObject();
		
		String name = (String) in.readObject();
		String add = (String) in.readObject();	
		String user = (String) in.readObject();
		show(name + ", " + add + ", " + user );
		String pw = (String) in.readObject();	
		String enc="";
		for(int i = 0; i < pw.length(); i ++){
			enc += "*";
		}
		show(enc);
		//take in all the stuff needed for the account 

		//Create an account number by using the beginn9ing of their name + a random number
		int no = (int) (Math.random() * (999999 - 1) + 1);
		String na = name.substring(0, 2);
		String acno = na + no;
		show("A/C no: " + acno);
		sendMsg("Your Account number is: " + acno);
		
		//add the account to the linked list!
		ls.addLast(new Account(name, add, acno, user, pw));
		for(Account a : ls){
			show(a.toString());
		}//print out on server side to ensure addition to list
		login();
	}//register
	
	//log in existing user
	public void login(){
		String user="";
		String pass="";
		int i = 0;
		boolean auth1, auth2, loop;
		auth1 = auth2 = false;
		loop = true;
			
		try {
			do{
				input = (String) in.readObject();
				if(input.equalsIgnoreCase("stop")){
					loop = false;
				}else{
					user = input;
					pass = (String) in.readObject();
					show("log: " + user);
					show("log: " + pass);
								
					for(Account tmp : ls){
						tmp = ls.get(i);
						i++;
						if(user.equalsIgnoreCase(tmp.getUser())){
							show("Authrization 1 pass");
							auth1 = true;
						}// username correct
						if(pass.equals(tmp.getPw())){
							show("Authrization 2 pass");
							auth2 = true;
						}// pw correct
						if(auth1 && auth2){	
							
							sendMsg("ok");
							try {
								Thread.sleep(100);
								session(tmp);
							} catch (Exception e) {
								// TODO Auto-generated catch block
								e.printStackTrace();
							}
						}///if authenticated start a new session
					}//scan through the linked list of accounts
				}//if else
			}while(loop);	
		} catch (ClassNotFoundException | IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
		}
		

		
	}//login
	
	//session method
	public void session(Account a) throws Exception {
		input = null;
		boolean run = true;
		float val = 0;
		sendMsg(a.getName());
		do{	
			input = null;
			sendMsg("€"+ a.getBalance());
			input = (String) in.readObject();
				switch(Integer.parseInt(input)){
				case 1:
					// lodging to account
					val = Float.parseFloat((String)in.readObject());//take in amount they want to lodge & convert to float
					a.setBalance(a.getBalance() + val);	//add that to the current balance
					show("€"+a.getBalance());
					sendMsg("€"+a.getBalance());
					transactions.add(new Transaction(a.getAccNo(), 'L', val));//add the transaction to the list of transactions 
					break;
				case 2:
					//withdrawal from account
					val = Float.parseFloat((String) in .readObject());//pass the object coming in to a string then a float
					if(a.getBalance() < val){//if the user does not have enough money in their account 
						sendMsg("Insuffcient funds");//print this
					}else{//otherwise do the logic
						float newBal = a.getBalance() - val;// set the new balance
						a.setBalance(newBal);//replace whats in the account with the new balance
						sendMsg("€"+a.getBalance());
						show("€"+a.getBalance());
						transactions.add(new Transaction(a.getAccNo(), 'W', val));//add the transaction to the list 
					}//
					break;
				case 3:
					//transaction history
					for(Transaction t : transactions){
						if(t.getAccNo().equals(a.getAccNo())){
							sendMsg(t.toString());//print out the transaction details
						}//if the account number is correct
					}//for each transaction in the list 
					sendMsg("done");
					break;
				case 4:
					//log out
					run = false;
					break;
				}//switch 
		}while(run);//do the above while run is true
		closeStuff();//dont forget to close stuff!!!
	}//session
	
	//setup streams fro sending and receiving data
	private void streams() throws Exception{
		out = new ObjectOutputStream(conn.getOutputStream());//setup the object output stream to client side
		out.flush();// clean out any bytes 

		in = new ObjectInputStream(conn.getInputStream());// setup the input stream coming from the client side
		
		show("\n Streams setup \n");
	}//streams
	
	//send message to client! 
	private void sendMsg(String msg){
		try {
			out.writeObject(msg);//write out the method args to the client
			out.flush();//clear any left over bytes out of the outstream..
			show("\nSERVER:\n" + msg);//show a copy of the sent message
		} catch (Exception e) {
			show(e.getMessage());//if theres an issue
		}//try catch
	}//send message method
	
	//just prints stuff to screen.. 
	public void show(String m){
		System.out.printf("\n%s",m);
	}//show message
	
	//active method, this does the whole menu thing
	private void active() throws Exception {
		String msg = "";
			try {
				do{
				msg = (String) in.readObject();				
				switch(Integer.parseInt(msg)){
					case 1:
						register();
						break;
					case 2: 
						login();
						break;
					case 3:
						closeStuff();
						break;
				}//switch		
				}while(Integer.parseInt(msg) != 3);
			} catch (Exception e) {
				show(e.getMessage());
			}//try catch
			if(msg.equals("end")){
				sendMsg("server:end");
				closeStuff();
			}///if its ending respond with end
		//active();
	}//active
	
	//close everything!!!
	private void closeStuff(){
		show("\n Closing streams & connection... \n");
		
		try {
			out.close();//close all this stuff to free up the resources used by them
			in.close();
			conn.close();//close the connection
		} catch (Exception e) {
			show(e.getMessage());
		}
	}//close stuff
	
}//class client service thread