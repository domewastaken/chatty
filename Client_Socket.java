package servers;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintStream;
import java.net.InetAddress;
import java.net.Socket;

public class Client_Socket {
	private Socket socket;
	
	private WindowPrinter userOutput;
	private PrintStream to_server;
	private BufferedReader from_server;
	private String address;
	private String username; 	
	private boolean stopExecution= false;
	private int port;
	private Buffer buffer;

	public static void main(String[] args){
		
		new Client_Socket("127.0.0.1", 8080, new ChatWindow());
	}
	
	
	
	
	public Client_Socket(String address, int port,ChatWindow window)  {
		this.userOutput = window.getPrinter();
		
		this.address = address ;
		this.port = port;
		this.buffer=window.getBuffer();
		try {
			
			socket = new Socket(InetAddress.getByAddress(new byte[]{ (byte) Integer.parseInt( address.split("\\.")[0]), 
																	 (byte) Integer.parseInt( address.split("\\.")[1]),
																	 (byte) Integer.parseInt( address.split("\\.")[2]),
																	 (byte) Integer.parseInt( address.split("\\.")[3]) 
															         } ), port);
	
		} catch (IOException e) {
			userOutput.println("errors during the connection");
			stopExecution=true;
		}
		if(!stopExecution){
		userOutput.println("connect to "+address);
		
		
	
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
		
		}
		
	}
	
	
	private void play() throws IOException {
		buffer.register(this);
		boolean test=true;
		String c = null;
		
		c = from_server.readLine();	
		userOutput.println("received "+ c);
	
		while(test){
			c="";
			c=from_server.readLine();
			
			if(!c.equals("start_chat")){
				
				userOutput.println(c);
				while(true) {       
				
					if(buffer.isReady()) {
						
						
						to_server.println(buffer.getString().trim());
						buffer.clearBuffer();
						break;
						}
				
					else {
					try {
						synchronized (this) {
							wait();
						}
						
					} catch (InterruptedException e) {
						e.printStackTrace();
						System.out.println("lollo");
					}
					
				}}
			
			}else{
				test=false;
			}
		}
	
	
		
		
		userOutput.println("\n CHAT STARTED");
		InputThread tr1=new InputThread(from_server,userOutput);
		OutputThread tr2 = new OutputThread(to_server,buffer/*,username,userOutput*/);
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