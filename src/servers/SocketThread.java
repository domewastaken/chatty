package servers;


import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintStream;
import java.net.Socket;
import java.util.ArrayList;

public class SocketThread extends Thread{
	private Multi_Server	 delete;
	private Socket			 socket;
	private PrintStream 	 to_client;
	private BufferedReader 	 from_client;
	private Chat_room 		 room;
	private String			 userName;

	SocketThread(Socket s, Multi_Server m)
	{
		this.delete = m ;
		this.socket = s;

		try {
			to_client = new PrintStream( socket.getOutputStream() );
			from_client = new BufferedReader(new InputStreamReader( socket.getInputStream())) ;
		} catch (IOException e) {e.printStackTrace();}
	}

	void sendMessage(String msg){
		to_client.println(msg);
	}

	@Override
	public final void run() {
		
		
		try { play();}

		catch (IOException e1) {
			if( !(e1.getMessage()=="Connection reset") )
				{ e1.printStackTrace();}}

		delete.release_resource(this);
		
		if(room!=null)			//this because if an user quits before entering a room 
		room.deleteUser(this);	//there is a Java.lang.NullPointerException	
					
		to_client.close();
		try {
			from_client.close();
		} catch (IOException e) {
			e.printStackTrace();
		}

	}
	
	private void play() throws IOException {

		to_client.println("connection");

		to_client.println("enter your username");
		userName = from_client.readLine();

		to_client.println("enter /help for help");



		Runnable tr1 = new Runnable() {
			@Override
			public void run() {
				boolean test = true;
				while (test)
				{
					String text = null;
					try {
						text = from_client.readLine();
					} catch (IOException e) {
						if(e.getMessage()=="Connection reset"){test =false;}
						else{e.printStackTrace();}
					}

					if(text!=null) {

						if(text.startsWith("/"))
						{
							String[] parameter = text.split(" ");

							switch (parameter[0]) {

								case "/help":
									to_client.println("Commands:_ /create <name> for create new room and join it;" +
											                   "_ /join <name> for join an existing room;" +
											                   "_ /show for show all available rooms;" +
											                   "_ /switch <name> for change the room" +
											                   "_ /quit for close this application" +
											                   "_ /help for show this list");
								break;

								case "/create":
									boolean isPresent = false;
									if (room != null){
										room.deleteUser(SocketThread.this);
									}
									try {
										room = new Chat_room(parameter[1], 5);
									} catch (Exception e) {
										to_client.println("this name already exist.Try another one");
										isPresent = true;
									}
									if (!isPresent) {
										room.joinRoom(SocketThread.this);
										/*start = true;*/
										//to_client.println(sendRoomName(parameter[1]));
										to_client.println("Chat Started in room:"+room.getName());
									}
									break;

								case "/join":

									room = Chat_room.getRoomByName(parameter[1]);

									if (room == null) {
										to_client.println("" + parameter[1] + " room doesn't exist.");
									} else if (!room.joinRoom(SocketThread.this)) {
										to_client.println("" + parameter[1] + " room is full.");
									}
								break;

								case "/show":
									to_client.println(getStringActiveRooms(Chat_room.getAvailableRooms()));
								break;

								case "/switch":
									Chat_room newRoom = Chat_room.getRoomByName(parameter[1]);
									if (newRoom!=null){
										room.deleteUser(SocketThread.this);
										room = newRoom;
										room.joinRoom(SocketThread.this);
										sendMessage("room changed to "+room.getName());
									}else{sendMessage("no room found");}
								break;

								case "/quit":
									test = false;
								break;

								default:
									to_client.println("Unknown command " + parameter[0] + ".Use /help for help");
								break;
							}

						}else{ if (room !=null)room.textMessage(text, userName); }
					}
				}
			}

	
		};
		
		Thread thr = new Thread(tr1);
		thr.start();
		try {
			thr.join();
		} catch (InterruptedException e) {

			e.printStackTrace();
		}

	}

	String getStringClientIp() {
		byte[] ipraw = socket.getInetAddress().getAddress();
		return "" + ipraw[0] + "." + ipraw[1] + "." + ipraw[2] + "." + ipraw[3];

	}

	private String getStringActiveRooms(ArrayList<Chat_room> c) {
		String str="";
		if (!c.isEmpty()){
			for (Chat_room room: c) {
				str= str.concat(room.getName() + "[" + room.activeUsers() + "/" + room.maxUsers() + "]" + ", ");
			}
		}else str = "no room available";

		return str;
	}

	String getUsername() {
		return userName;
	}

}


