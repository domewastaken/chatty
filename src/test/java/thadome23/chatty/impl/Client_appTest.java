package thadome23.chatty.impl;

import thadome23.chatty.api.client.*;
public class Client_appTest {
	
	public static void main(String[] args) {
		ChatWindow w = new ChatWindow();
		new ChatClient(8080, w.getPrinter(), w.getBuffer());
	}
}
