package client;

import java.io.*;
import java.net.*;
import java.util.Scanner;

//written by Daniel Verdejo - G00282931
public class Requester implements Runnable{
	Socket conn;
	ObjectOutputStream out;
 	ObjectInputStream in;
 	String message;
 	
 	Scanner scn = new Scanner(System.in);
 	public static void main(String[]args){
 		System.out.println("Starting...");
 		new Thread(new Requester()).start();//run a new instance for the requester
 		
 	}
	public void run () {
		String msg ="";
		try{
			connect();
			show("Attempting connection . . .");
			streams();//setup the output and input streams
			do{
				show(((String) in.readObject()));//show a confirm of connection 
				show("\t\tDank Bank\n\t[ 1 ] - [ Register\t]\n\t[ 2 ] - [ Log in\t](admin, admin)\n\t[ 3 ] - [ Quit\t\t]\n\t: ");
				msg = scn.nextLine();
				sendMsg(msg);
				switch(Integer.parseInt(msg)){
				case 1:
					reg();//call dat reg method doh
					break;
				case 2:
					login();//see method below
					break;
				case 3:
					msg = "end";
					sendMsg(msg);//stop the instance
					break;
				}
				
			}while(!msg.equals("end"));
			closeStuff();
		}catch(Exception e){
			show(e.getMessage());
		}//try catch
	}//run
	
	private void connect() {
		String msg;
		try{
		show("\t[ 1 ][ Localhost\t]\n\t[ 2 ][ Custom server\t](AWS)\n");
		msg = scn.nextLine();
		switch(Integer.parseInt(msg)){
		case 1://locla host
			conn = new Socket("127.0.0.1", 2004);//create a connection on ip number we/ and port 2004
			show("***Connected to Localhost using port 2004***");
			break;
		case 2://amazon web service or.. any server really that the server side code resides on...
			show("Enter IP: ");
			String ip = scn.nextLine();
			show("Enter Port: ");
			String port = scn.nextLine();
			conn = new Socket(ip, Integer.parseInt(port));//create a connection on ip number we/ and port 2004
			show("***Connected to "+ ip +" using port "+port+"***");	
			break;
		}//switch
		}catch(Exception e){
			show(e.getMessage());
		}
	}//connect method
	
	private void reg() throws ClassNotFoundException, IOException{
		String msg;
		show("Registration enter the following: ");
		show("Full name: ");
		sendMsg(scn.nextLine());
		show("Address: ");
		sendMsg(scn.nextLine());
		show("Username: ");
		sendMsg(scn.nextLine());
		show("Password: ");
		sendMsg(scn.nextLine());
		//send off all required details above then get the ac/ no below
		msg = (String) in.readObject();
		show(msg);//this will show the randomly generated account number
		login();//now make the user login to do stuff
	}//register

	private void login() throws ClassNotFoundException, IOException{
		String msg;
		show("Log In\nUsername: ");
		msg = scn.nextLine();
		sendMsg(msg);
		show("Password: ");
		msg = scn.nextLine();
		sendMsg(msg);
		
		//take in the credentials above and wait for a response from the server that states 'ok'
		msg = (String) in.readObject();
		if(msg.equals("ok")){
			show("Authentication complete");//authentication verified we show the users name balance and the menu 
			show("welcome " + (String) in.readObject());
			do{
				msg = null;//just in case
				show("Balance: "+ (String) in.readObject());
					show("\n[1 - Make a Lodgement\t]\n[2 - Make a Withdrawal\t]\n[3 - View transactions\t](not fully working)\n[4 - Log out\t\t]\n");
					msg = scn.nextLine();//make a choice to do stuff
					sendMsg(msg);//let the server k now aswell that you want to do stuff...
					switch(Integer.parseInt(msg)){
					case 1:
						//make a lodgement into the account
						show("Enter Lodgement amount: ");
						sendMsg(scn.nextLine());
						show((String)in.readObject());
						break;
					case 2:
						//Withdraw from  the account
						show("Enter Withdrawal amount: ");
						sendMsg(scn.nextLine());
						show((String)in.readObject());
						break;
					case 3:
						//show a list of the transactions while the message is not done or the i isn't over 10
						show("Transactions:");
						int i = 0;
						do{
							msg = (String) in.readObject();
								show(i + " | " + msg);
								i++;
						}while(!(msg.equals("done")) || i <= 10);
						break;
					case 4:
						sendMsg("4");//send 4 to the server .... thatll make it close things
						break;
					}//switch

				
			}while(Integer.parseInt(msg)!= 4);// 4 also breaks the loop here
		}else{
			show("U mad brah? ;D");
		}//if else
		
	}//login

	private void streams() throws Exception{
		out = new ObjectOutputStream(conn.getOutputStream());
		out.flush();
		
		in = new ObjectInputStream(conn.getInputStream());
		
		show("Streams setup \n");
	}//streams
	
	//send message to client! 
	private void sendMsg(String msg){
		try {
			out.writeObject(msg);
			out.flush();
			//show("client: " + msg);
		} catch (Exception e) {
			show(e.getMessage());
		}//try catch
	}//send message method#

	private void show(String m){
		System.out.printf("\n%s",m);
	}//show message
	
	//close everything!!!
	private void closeStuff(){
		show("Closing streams & connection...\n");
		
		try {
			out.close();
			in.close();
			conn.close();
		} catch (Exception e) {
			show(e.getMessage());
		}
	}//close stuff
}//class