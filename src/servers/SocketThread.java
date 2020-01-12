package servers;

import org.jetbrains.annotations.NotNull;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintStream;
import java.net.Socket;

public  class SocketThread extends Thread{
	private Multi_Server	 delete;
	private Socket			 socket;
	private PrintStream 	 to_client;
	private BufferedReader 	 from_client;
	private Chat_room 		 room;
	private String			 userName;

	public SocketThread(Socket s , Multi_Server m)
	{
		this.delete = m ;
		this.socket = s;

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
		try { play(); }
		catch (IOException e1) {
			if( !(e1.getMessage()=="Connection reset") )
			{ e1.printStackTrace(); }
		}

		delete.release_resource(this);
		room.deleteUser(this);
		to_client.close();
		try {
			from_client.close();
		} catch (IOException e) {
			e.printStackTrace();
		}


	}
	
	public void play() throws IOException {
		boolean start=false;
		to_client.println("connection");
		
		to_client.println("enter your username");
		userName= from_client.readLine();

		while(!start){
			
		to_client.println("enter h for help");
		String answer =from_client.readLine();
		
		String name;
	
		switch (answer) {
		case "c":
			
			to_client.println("enter a name for the room ");
			name=from_client.readLine();
			room = new Chat_room(name, 5);
			
			room.joinRoom(this);

			start=true;
			break;
		case "j":
			
			to_client.println("enter the name of the room ");
			name=from_client.readLine();
			room = Chat_room.getRoomByName(name);
			if (room == null){
				to_client.println(""+name+" room doesn't exist. Enter for continue");
				from_client.readLine();
				break;
			}
			if (!room.joinRoom(this)){
				to_client.println(""+name+" room is full. Enter for continue");
				from_client.readLine();
				break;
			}
			start=true;
			break;
		case "s":
			
			Chat_room[] d =new Chat_room[Chat_room.getAvailableRooms().toArray().length];
			Chat_room.getAvailableRooms().toArray(d);
			String tmp =toStringFromArray(d);
			to_client.println(tmp+" click enter for continue");
			from_client.readLine();
			break;
		case "h":

			to_client.println("enter c for create new room or _ j for join an existing room or _ s for showing available rooms");
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

	public String getStringClientIp(){
		byte[] ipraw = socket.getInetAddress().getAddress();
		return  ""+ipraw[0]+"."+ipraw[1]+"."+ipraw[2]+"."+ipraw[3];
		
	}

	private String toStringFromArray(@NotNull Chat_room[] c)
	{
		String i ="";
		if(!(c.length ==0)) {

			for (Chat_room b : c) {
				i = i.concat(b.getName() + "[" + b.activeUsers() + "/" + b.maxUsers() + "]" + ", ");
			}

		}else{
			i = "no room available";
		}
		return i;

	}
	public String getUsername() {
		return userName;
	}


}
