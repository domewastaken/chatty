package servers;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintStream;
import java.net.InetAddress;
import java.net.Socket;
import java.net.UnknownHostException;

public class ChatClient {
	private Buffer inputBuffer;
	private Socket socket;
	private WindowPrinter userOutput;

	private InetAddress ip;
	private int port;			// normally is 8080 


	public static void main(String[] args){
		ChatWindow c= new ChatWindow();								//these things gets executed
		new ChatClient( 8080, c.getPrinter(), c.getBuffer());
	}
	
	
	//**************************start of constructors************************************************
	public ChatClient(int port, WindowPrinter printer ,Buffer buff, InetAddress ip){

		this.port = port;
		this.ip = ip;
		this.userOutput = printer;
		this.inputBuffer=buff;
		
		inputBuffer.register(this);
		
		connect();
	}
	
	public ChatClient(int port, WindowPrinter printer, Buffer buff){

		this.port = port;
		this.userOutput = printer;
		this.inputBuffer=buff;

		inputBuffer.register(this);
		
		userOutput.println("enter the address",ContentType.Chat_message);
		
		boolean flag ;

		do{
			flag = false;
			try { 
				synchronized (this){  wait();  }  //wait for the input
			}catch (InterruptedException e) {  e.printStackTrace();  }
	
			String addr = inputBuffer.getString();
		
			try {
				
				this.ip = parseStringToAddress(addr);
				
			}catch (ArrayIndexOutOfBoundsException | UnknownHostException e1) {
				
				userOutput.println("---You must enter a valid Ip Address---",ContentType.Chat_message);	
				flag = true;
			}
			
		}while(flag);

		connect();
	}
	//**************************end of constructors************************************************
	
	private void connect() {

		try {
			socket = new Socket(ip, port); 	//connects to the server
		} catch (IOException e1) {

			userOutput.println("---errors during connection---",ContentType.Chat_message);	
		} 	

		userOutput.println("connected to server" ,ContentType.Chat_message);
		
		try {														//	initializes I/O		
			InputThread tr1=new InputThread(new BufferedReader(new InputStreamReader(socket.getInputStream())),userOutput);		
			OutputThread tr2 = new OutputThread(new PrintStream(socket.getOutputStream()),inputBuffer);		
			
			tr1.start();
			tr2.start();

			tr1.join();
			tr2.join();

		} catch (IOException | InterruptedException e) {e.printStackTrace();}
		
	}


	public String getAddress() {
		return ip.getHostAddress();
	}

	public int getPort(){
		return port;
	}
	
	public static InetAddress parseStringToAddress(String address) throws UnknownHostException,ArrayIndexOutOfBoundsException{

        return InetAddress.getByAddress( new byte[]{ (byte) Integer.parseInt( address.split("\\.")[0]),
                                                     (byte) Integer.parseInt( address.split("\\.")[1]),
                                                     (byte) Integer.parseInt( address.split("\\.")[2]),
                                                     (byte) Integer.parseInt( address.split("\\.")[3]) } );
    }
}