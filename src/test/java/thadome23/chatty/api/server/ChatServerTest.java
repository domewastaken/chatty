package thadome23.chatty.api.server;

import java.io.IOException;
import java.net.ServerSocket;
import java.util.ArrayList;
import java.util.List;

public class ChatServerTest {

	private ServerSocket server;                //this accept all the requests
	private int port;                            //the port of the server
	private int maxServers;                    //the maximum numbers of servers(connections) that can hold
	private List<SocketThreadTest> sockets;            //the sockets
	private boolean run = true;
	private int connections_alive = 0;            //the currents connections alive


	/****************************constructors start*********************************************/
	public ChatServerTest(int maxservers){

		this(maxservers,8080);
	}

	public ChatServerTest(int maxservers, int port) {

		this.port = port;
		this.maxServers = maxservers;                  /*Initialization of fields*/
		this.sockets = new ArrayList<>();

		try {  server= new ServerSocket(port);  }
		catch (IOException e)
        {
            System.out.println("cannot bind to the specified port");
            System.out.println(e.getMessage());
            close();
		}
		
		if(run)
			System.out.println("server bind to port: "+ port);
	}
	/****************************constructors end***********************************************/

	public void start() {
		
		while (run) {
			
			if (connections_alive < maxServers) {

				try {

					SocketThreadTest s = new SocketThreadTest(server.accept(), this);   //<<-------- //it passes also the current instance
					s.start();                                                           	 //for make possible to delete the connection
					sockets.add(s);            //add the current connection to the list		 //with @method release_resource
					System.out.println("connected to " + s.getStringClientIp());
				
				} catch (IOException e) {
					System.out.println("error connecting to client");
				}

				synchronized (this) {
					connections_alive++;
				}
			}
		}
	}

	public synchronized void release_resource(SocketThreadTest thread) {
		System.out.println("closed connection with " + thread.getStringClientIp());
		sockets.remove(thread);
		synchronized (this) {
			connections_alive--;
		}

	}

	public void close() {
		run = false;
	}

	public int getPort() {
		return port;
	}


}
