package thadome23.chatty.impl;

import thadome23.chatty.api.server.ChatServer;

public class Server_app {

	public static void main(String[] args) {
		ChatServer s = new ChatServer(10);
		s.start();
	}

}
