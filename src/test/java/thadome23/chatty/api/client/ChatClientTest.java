package thadome23.chatty.api.client;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintStream;
import java.net.InetAddress;
import java.net.Socket;
import java.net.UnknownHostException;


public class ChatClientTest {
	private BufferTest inputBuffer;
	private Socket socket;
	private WindowPrinterTest userOutput;

	private InetAddress ip;
	private int port;			// normally is 8080 


	//**************************start of constructors************************************************
	public ChatClientTest(int port, WindowPrinterTest printer ,BufferTest buff, InetAddress ip){

		this.port = port;
		this.ip = ip;
		this.userOutput = printer;
		this.inputBuffer=buff;
		
		connect();
	}
	
	public ChatClientTest(int port, WindowPrinterTest printer, BufferTest buff){

		this.port = port;
		this.userOutput = printer;
		this.inputBuffer=buff;
	
		userOutput.println("enter the address",ContentTypeTest.Chat_message);
		
		boolean flag ;

		do{
			flag = false;
		
			try {
				String addr = inputBuffer.getString();
					
				this.ip = parseStringToAddress(addr);
					
				}catch (ArrayIndexOutOfBoundsException | UnknownHostException e1) {
					userOutput.println("---You must enter a valid Ip Address---",ContentTypeTest.Chat_message);	
					flag = true;
					
				} catch (InterruptedException e) {
					e.printStackTrace();
			}
		
		}while(flag);

		connect();
	}
	//**************************end of constructors************************************************
	
	private void connect()
	{
		boolean test = true;
		
		try {
			socket = new Socket(ip, port); 	//connects to the server
		} catch (IOException e1) {
			test=false;
			userOutput.println("---errors during connection---",ContentTypeTest.Chat_message);	
		} 	
		
		if (test) {
				userOutput.println("connected to server" ,ContentTypeTest.Chat_message);
			
			try {															
				InputThreadTest tr1=new InputThreadTest(new BufferedReader(new InputStreamReader(socket.getInputStream())),userOutput);		
				OutputThreadTest tr2 = new OutputThreadTest(new PrintStream(socket.getOutputStream()),inputBuffer);		//	initializes I/O	
				
				tr1.start();
				tr2.start();
	
				tr1.join();
				tr2.join();
	
			} catch (IOException | InterruptedException e) {  e.printStackTrace();} 
			
		}
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