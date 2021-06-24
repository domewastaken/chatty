package thadome23.chatty.impl;

import thadome23.chatty.api.client.*;
public class Client_appTest {
	
	public static void main(String[] args) {
		ClientGui w = new ClientGui();
		new ChatClient( w.getPrinter(), w.getBuffer());
	}
}
