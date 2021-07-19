package thadome23.chatty.api.server;


import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintStream;
import java.net.Socket;
import java.util.ArrayList;

class ChatUser extends Thread{
	private ChatServer		 delete;
	private Socket			 socket;
	private PrintStream 	 to_client;
	private BufferedReader 	 from_client;
	private Chat_room 		 room;
	private String			 userName;

	ChatUser(Socket s, ChatServer chatServer){
		this.delete = chatServer;
		this.socket = s;

		try {
			to_client = new PrintStream( socket.getOutputStream() );
			from_client = new BufferedReader(new InputStreamReader( socket.getInputStream())) ;
		} catch (IOException e) {e.printStackTrace();}
	}
	
	/*************************************/ //methods used by the Chat_room

	void sendInfo(String i) {				to_client.println("<<info$ "+i+" >>");			}
	
	void sendInfo(String i,String type) {	to_client.println("<<info,"+type+"$ "+i+" >>");	}
	
	void sendMessage(String msg){			to_client.println("<<msg$ "+msg+" >>");			}
	/************************************/
	
	@Override
	public final void run() {
		
		try { play();}

		catch (IOException e1) {
			if( !(e1.getMessage()=="Connection reset") )
				 e1.printStackTrace();
		}
		
		delete.disconnect(this);
	}
	
	public void closeConnection() {
		if(room!=null)				//this because if an user quits before entering a room 
			room.deleteUser(this);	//there is a Java.lang.NullPointerException	
					
		try {	
			socket.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	private void play() throws IOException {
		
		sendMessage("enter your username");
		userName = from_client.readLine();
		sendInfo("---registered as "+ userName+ "---");
		sendInfo("enter /help for help");

		boolean test = true;
				
		while (test){
			String text =null;
					
			try {
				text = from_client.readLine();
			} catch (IOException e) {
				if(e.getMessage()=="Connection reset"){test =false;}
				else if(e.getMessage()=="Stream closed" || e.getMessage()=="Socket closed"){test =false;}
				//else{e.printStackTrace();}
			}

			if(text!=null  &&  text!= "\n" && text != "") {
						
				if(text.startsWith("/")){
					
					String[] parameter = text.split(" ");

					switch (parameter[0]) {

						case "/help":
							sendInfo("Commands:_ /create <name> for create new room and join it;" +
								              "_ /join <name> for join an existing room;" +
								              "_ /show for show all available rooms;" +
								              "_ /switch <name> for change the room" +
								              "_ /quit for close this application" +
								              "_ /help for show this list");
						break;

						case "/create":
							if(parameter.length > 1) 
							{
								if (room != null)				//if already joined a room
									room.deleteUser(ChatUser.this);
								
								
								try {
									room = new Chat_room(parameter[1], 5);
								} catch (Exception e) {
									sendInfo("this name already exist.Try another one");
									break;
								}
								
								room.joinRoom(ChatUser.this);
										
								sendInfo(room.getName(),"roomname");
								sendInfo("Chat Started in room:"+room.getName());
								
							}else 
								sendInfo("You must provide a name"); 	
						break;

						case "/join":

							room = Chat_room.getRoomByName(parameter[1]);
							
							if (room == null) {
								sendInfo("" + parameter[1] + " room doesn't exist.");
							} else if (!room.joinRoom(ChatUser.this)) {
								sendInfo("" + parameter[1] + " room is full.");
							}else {
								sendInfo(room.getName(),"roomname");
								sendInfo("Chat Started in room:"+room.getName());
							}
						break;

						case "/show":
							sendInfo(getStringActiveRooms(Chat_room.getAvailableRooms()));
						break;

						case "/switch":
							Chat_room newRoom = Chat_room.getRoomByName(parameter[1]);
							if (newRoom!=null){
								room.deleteUser(ChatUser.this);
								room = newRoom;
								room.joinRoom(ChatUser.this);
								sendInfo("room changed to "+room.getName());
								sendInfo(room.getName(),"roomname");
							}else{sendInfo("no room found");}
						break;

						case "/quit":
							test = false;
						break;

						default:
							sendInfo("Unknown command " + parameter[0] + ". Use /help for help");
						break;
					}

				}else{ 
					if (room !=null)
						room.textMessage(text, userName); 
					else
						sendInfo("You need to join a room first or create a new one");
				
				}
			}
		}
	}

	String getStringClientIp() {

		return socket.getInetAddress().getHostAddress();			
	}
	
	int getPort(){
		return socket.getPort();
		
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
