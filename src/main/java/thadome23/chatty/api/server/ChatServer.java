package thadome23.chatty.api.server;

import java.io.IOException;
import java.net.ServerSocket;
import java.util.ArrayList;
import java.util.List;

public class ChatServer {

	private ServerSocket server;                //this accept all the requests
	private int port;                            //the port of the server
	private int maxServers;                    //the maximum numbers of servers(connections) that can hold
	private List<ChatUser> sockets;            //the sockets
	private boolean run = false;

	private LogGui logger;

	public ChatServer(int maxservers){

		this(maxservers,8080);
	}

	public ChatServer(int maxservers, int port) {

		this.port = port;
		this.maxServers = maxservers;                  /*Initialization of fields*/
		this.sockets = new ArrayList<>(maxservers);
		this.logger = new LogGui();

	}
	
	public void start() {
		logger.write("server started");
		run = true;
		
		try {  server= new ServerSocket(port);  }
		catch (IOException e)
        {
            logger.write("cannot bind to the specified port");
            logger.write(e.getMessage());
            close();
            
		}
		
		if(run)
			logger.write("server bind to port: "+ port);
		
		while (run) {
			
			if (sockets.size() <= maxServers) {

				try {

					ChatUser s = new ChatUser(server.accept(), this);   //<<-------- //it passes also the current instance
					s.start();                                                           	 //for make possible to delete the connection
					sockets.add(s);            //add the current connection to the list		 //with @method release_resource
					logger.write("connected to client on" + s.getStringClientIp()+":"+s.getPort());
				
				} catch (IOException e) {
					logger.write("error connecting to client");
				}

			}
		}

	
	}

	public synchronized void disconnect(ChatUser thread) {
		thread.closeConnection();
		logger.write("closed connection with " + thread.getStringClientIp() +":"+thread.getPort());
		sockets.remove(thread);

	}


	public void close() {
		run = false;
		
		synchronized (sockets) {
			sockets.forEach( (s)->{
				s.closeConnection();
				logger.write("closed connection with " + s.getStringClientIp() +":"+s.getPort());
				} );
		}
		sockets.clear();
		
		try {
			if(server != null)
				server.close();
		} catch (IOException e) {
			
			logger.write("error 123");
		}
		logger.write("server closed");
	}
	
	public void destroy() {
	
		System.exit(0);
	}
	
	public boolean isOnline() {
		return run;
	}

	public int getPort() {
		return port;
	}
	
	public void setPort(int p) {
		this.port = p;
	}
	
	public LogGui getLog() {
		return logger;
	}

}
