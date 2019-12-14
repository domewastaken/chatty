package servers;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintStream;
import java.net.InetAddress;
import java.net.Socket;
import java.net.SocketException;
import java.util.Scanner;

public  class SocketThread extends Thread{
	private Multi_Server delete;
	private Socket socket;
	private Scanner userInput;
	private PrintStream to_client;
	private BufferedReader from_client;
	private Chat_room room ;
	private String userName;
	
	private String toStringFromArray(Chat_room[] c)
	{
		String i ="";
	    for(Chat_room b : c)
	    {
	    	i=i.concat(b.getName() + "[" +b.activeUsers()+ "/" +b.maxUsers()+ "]" +", ");
	    
	    }
		return i;
		
	}
	public SocketThread(Socket s , Multi_Server m) 
	{
	
	this.delete = m ;
	this.socket = s;
	
	userInput = new Scanner(System.in);
	
	try {
		to_client = new PrintStream( socket.getOutputStream() );
		from_client = new BufferedReader(new InputStreamReader( socket.getInputStream())) ;
	} catch (IOException e) {e.printStackTrace();}
	
	}

	
	public void sendMessage(String msg){
		to_client.println(msg);
	}
	
	
	
	@Override
	public final void run() {
	try {
		play();
	} 
	catch (IOException e1) {
		if(e1.getMessage()=="Connection reset"){}
		else{e1.printStackTrace();}
		}



	delete.relase_recource(this);
	room.deleteUser(this);
	to_client.close();
	try {
		from_client.close();
	} catch (IOException e) {

		e.printStackTrace();
	}
	userInput.close();

	}
	
	public void play() throws IOException,SocketException
	{
		boolean start=false;
		to_client.println("connection");
		
		to_client.println("enter your username");
		userName= from_client.readLine();
		while(!start){
			
		to_client.println("enter c for create new room or _ j for join an existing room or _ s for showing avaible rooms");
		String answer =from_client.readLine();
		
		String name="";
	
		switch (answer) {
		case "c":
			
			to_client.println("enter a name for the room ");
			name=from_client.readLine();
			room = new Chat_room(name, 5);
			
			room.joinRoom(this);
			to_client.println("start_chat");
			start=true;
			break;
		case "j":
			
			to_client.println("enter the name of the room ");
			name=from_client.readLine();
			room = Chat_room.getRoomByName(name);
			room.joinRoom(this);
			to_client.println("start_chat");
			start=true;
			break;
		
		case "s":
			
			Chat_room[] d =new Chat_room[Chat_room.getAvaibleRooms().toArray().length];
			Chat_room.getAvaibleRooms().toArray(d);
			String tmp =toStringFromArray(d);
			to_client.println(tmp+" click enter for continue");
			from_client.readLine();
			break;
		
		default:
			System.out.println("error");
			break;
		}}
		
		InputThread tr1=new InputThread(from_client){
			@Override
			public void run(){
			
				while(this.getTest())
				{
					String text = null;
			
					try {
						text = from_client.readLine();
					
					} catch (IOException e) {
						if(e.getMessage()=="Connection reset"){this.close();}
						else{e.printStackTrace();}
						}
			
					if(text!=null)
					//DEBUG USE ONLY //System.out.println("get:"+text);
					room.textMessage(text,userName);
				
				}
				}
		};
		
		tr1.start();
	
		try {
			tr1.join();
		} catch (InterruptedException e) {
	
			e.printStackTrace();
		}
			
	}

	public InetAddress getClientIp(){
		return socket.getInetAddress();
	}
	public String getStringClientIp(){
		byte[] ipraw = socket.getInetAddress().getAddress();
		return  ""+ipraw[0]+"."+ipraw[1]+"."+ipraw[2]+"."+ipraw[3];
		
	}

	public String getUsername() {
		return userName;
	}


}
