package thadome23.chatty.impl;

import java.net.UnknownHostException;
import java.rmi.ConnectException;

import thadome23.chatty.api.client.*;

public class Client_app {
			
	public static void main(String[] args){
		
		ClientGui w = new ClientGui();
		
		WindowPrinter print = w.getPrinter();
		
		Buffer buff =  w.getBuffer();		
		
		ChatClient client = new ChatClient(print,buff);	
		
		askAndConnect(print,buff,client);
	
	}		
		
	private static void askAndConnect(WindowPrinter print, Buffer buff, ChatClient client) {
		String addr;							
		boolean error;
		
		do {
			print.println("enter Ip Address", ContentType.Chat_message);
				
			error = false;
				
			try {
				addr = buff.getString();
				print.println(">"+ addr, ContentType.Chat_message);
				client.connect(ChatClient.parseStringToAddress(addr), 8080);
					
			} catch (ConnectException | ArrayIndexOutOfBoundsException | NumberFormatException | UnknownHostException e) {
					
				print.println("invalid ip address", ContentType.Error);
				error = true;
				
			} catch (InterruptedException e) {
		
				e.printStackTrace();
			}
		}while(error);
	}
	
}