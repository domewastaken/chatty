package servers;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintStream;
import java.net.InetAddress;
import java.net.Socket;
import java.util.Scanner;

public class Client_Socket {
	private Socket socket;
	private Scanner userInput;
	private PrintStream to_server;
	private BufferedReader from_server;
	private String address;
	private String username; 	
	private boolean stopExecution= false;
	private int port;
	
	public static void main(String[] args){
		
		new Client_Socket("127.0.0.1", 8080);
	}
	
	
	
	
	public Client_Socket(String address, int port)  {
		
		this.address = address ;
		this.port = port;
	
		try {
			
			socket = new Socket(InetAddress.getByAddress(new byte[]{ (byte) Integer.parseInt( address.split("\\.")[0]), 
																	 (byte) Integer.parseInt( address.split("\\.")[1]),
																	 (byte) Integer.parseInt( address.split("\\.")[2]),
																	 (byte) Integer.parseInt( address.split("\\.")[3]) 
															         } ), port);
	
		} catch (IOException e) {
			System.out.println("errors during the connection");
			stopExecution=true;
		}
		if(!stopExecution){
		System.out.println("connect to "+address);
		
		userInput = new Scanner(System.in);
	
		try {
			to_server = new PrintStream(socket.getOutputStream());
			from_server = new BufferedReader(new InputStreamReader( socket.getInputStream()));
		} catch (IOException e) { e.printStackTrace(); }

		
		try {
			this.play();
		} catch (IOException e) {e.printStackTrace();}
		to_server.close();
		try {
		from_server.close();
		} catch (IOException e) {e.printStackTrace();}
		userInput.close();
		}
		
	}
	
	
	private void play() throws IOException {
		boolean test=true;
		String c = null;
		
		c = from_server.readLine();	
		System.out.println("received "+ c);
		System.out.println("enter your username");
		username = userInput.nextLine();
		to_server.println(username);
		while(test){
			c="";
			c=from_server.readLine();
			
			if(!c.equals("start_chat")){
				System.out.println(c);
				to_server.println(userInput.nextLine());
			}else{
				test=false;
			}
		}
	
	
		
		
		System.out.println("\n");
		InputThread tr1=new InputThread(from_server);
		OutputThread tr2 = new OutputThread(to_server, userInput,username);
		tr1.start();
		tr2.start();
		try {
			tr1.join();
			tr2.join();
		} catch (InterruptedException e) {e.printStackTrace();}
		
		to_server.close();
		
		try {
			from_server.close();
		} catch (IOException e) {e.printStackTrace();}
		
		userInput.close();
	}


	public String getAddress() {
		return address;
	}

	public int getPort(){
		return port;
	}
	public String getUsername() {
		return username;
	}
}