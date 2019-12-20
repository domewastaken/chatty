package servers;
import java.io.IOException;
import java.net.ServerSocket;
import java.util.ArrayList;
import java.util.List;



public class Multi_Server {
	
	private ServerSocket server;				//this accept all the requests
	private int port;							//the port of the server
	private int maxServers ;					//the maximum numbers of servers(connections) that can hold
	private List<SocketThread> sockets;			//the sockets
	private boolean run = true;					
	private int connections_alives= 0;			//the currents connections alive
	

	public static void main(String[] args){
		Multi_Server c = new Multi_Server(10);
		c.start();
	}
	
	
	public Multi_Server( int maxservers){
		
		this(maxservers,8080);
	}
	
	public Multi_Server( int maxservers, int port){
	
		this.port = port;                              /**************************/
		this.maxServers = maxservers;                  /*Initialization of fields*/
		this.sockets = new ArrayList<SocketThread>();  /**************************/
		
		/**************************************************************/
		try {  server= new ServerSocket(port);  }
		catch (IOException e)
        {
            System.out.println("cannot bind to the specified port");
            System.out.println(e.getMessage());
		}
		/**************************************************************/
		
		System.out.println("server bind to port: "+ port);
	}

	public void start()
	{
		while(run)
		{
			if (connections_alives < maxServers){
				
				/**************************************************************/
				try {
					
					SocketThread c =new SocketThread(server.accept(),this);     	//it passes also the current instance
					c.start();															//for make possible to delete the connection 
					sockets.add(c);			//add the current connection to the list	//with @method relase_resource
				    System.out.println("connected to "+c.getStringClientIp());
					}
					
				catch (IOException e){  System.out.println("error connecting to client");  }
				/**************************************************************/
				
				synchronized(this){connections_alives++;}
			}
		}
	}
	public synchronized void relase_recource(SocketThread thread)
	{
		System.out.println("closed connection with "+thread.getStringClientIp());
		sockets.remove(thread);
		synchronized (this) {connections_alives--;}
		
	}


	public int getPort(){ return port; }

	
}
