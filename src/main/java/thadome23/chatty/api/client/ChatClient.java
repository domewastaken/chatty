package thadome23.chatty.api.client;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintStream;
import java.net.InetAddress;
import java.net.Socket;
import java.net.UnknownHostException;
import java.rmi.ConnectException;


public class ChatClient {
	private Buffer inputBuffer;		
	private WindowPrinter userOutput;
	private Socket socket;	
	
	/**************** Constructor ***************/
	public ChatClient(WindowPrinter printer, Buffer buff){

		this.userOutput = printer;
		this.inputBuffer=buff;	
	}
	
	/**************** Public Method*******************/

	public void connect(InetAddress ip, int port) throws ConnectException{	
				
		try {  socket = new Socket(ip, port);  }
		catch (IOException e1) {
			throw new ConnectException("server didn't accept connection");
		}

		userOutput.println("connected to " + ip.getHostAddress(),ContentType.Chat_message);
		
		try {															
			InputThread tr1=new InputThread(new BufferedReader(new InputStreamReader(socket.getInputStream())),userOutput);		
			OutputThread tr2 = new OutputThread(new PrintStream(socket.getOutputStream()),inputBuffer);		//	initializes I/O	
			
			tr1.start();
			tr2.start();

			tr1.join();
			tr2.join();
	
		} catch (IOException | InterruptedException e) {  e.printStackTrace();}
	}
	
	/******************* Getters **************************/
	public String getAddress() {
		return socket.getLocalAddress().getHostAddress();
	}

	public int getPort(){
		return socket.getPort();
	}
	
	/****************** Static Method *******************/
	public static InetAddress parseStringToAddress(String address) throws UnknownHostException,ArrayIndexOutOfBoundsException, NumberFormatException {

        return InetAddress.getByAddress( new byte[]{ (byte) Integer.parseInt( address.split("\\.")[0]),
                                                     (byte) Integer.parseInt( address.split("\\.")[1]),
                                                     (byte) Integer.parseInt( address.split("\\.")[2]),
                                                     (byte) Integer.parseInt( address.split("\\.")[3]) } );
    }
}