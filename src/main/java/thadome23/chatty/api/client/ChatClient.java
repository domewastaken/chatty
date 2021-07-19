package thadome23.chatty.api.client;

import java.io.IOException;
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

	public int connect(String string, int port) throws ConnectException{	
				
		try {  socket = new Socket(string, port);  }
		catch (IOException e1) {
			throw new ConnectException("server didn't accept connection");
		}

		userOutput.println("connected to " + socket.getInetAddress(),ContentType.Chat_message);
		
		try {															
			InputThread tr1=new InputThread(socket,userOutput);		
			OutputThread tr2 = new OutputThread(new PrintStream(socket.getOutputStream()),inputBuffer);		//	initializes I/O	
			
			tr1.start();
			tr2.start();

		} catch (IOException e) {  e.printStackTrace();}
		return 0;
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