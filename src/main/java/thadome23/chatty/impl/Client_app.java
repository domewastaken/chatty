package thadome23.chatty.impl;

import java.net.UnknownHostException;
import java.rmi.ConnectException;

import thadome23.chatty.api.client.*;

public class Client_app {
			
	public static void main(String[] args){
		
		ClientGui gui = new ClientGui();
		
		WindowPrinter print = gui.getPrinter();
		
		Buffer buff = gui.getBuffer();		
		
		ChatClient client = new ChatClient(print,buff);	
		
		askAndConnect(buff,print,client);
	
	}		
		
	private static void askAndConnect(Buffer buff,WindowPrinter print , ChatClient client) {

		boolean error;
		
		ConnectGui cg = new ConnectGui();
		
		do {
			error = false;
				
			try {
				
				client.connect(cg.getIp() , cg.getPort());
					
			} catch (ConnectException | ArrayIndexOutOfBoundsException | NumberFormatException e) {
					
				print.println("invalid ip address", ContentType.Error);
				error = true;
				
			} 
		}while(error);
		buff.add(cg.getNick());
		cg.close();
	}
	
}