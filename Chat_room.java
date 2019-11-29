package servers;

import java.util.ArrayList;
import java.util.List;

public class Chat_room{
	
private String 		   	room_name;
private	SocketThread[] 	users;

private static List<Chat_room> 	roomsRegistry = 
								new ArrayList<Chat_room>();

//private String[] cronology;
		
public Chat_room(String name, int max) {

	this.setName(name);					//assign the name to the room
	Chat_room.addRoom(this);			//add the current room to the registry
	this.users = new SocketThread[max];	//initialize an array for roomsRegistry' components

}

public String getName() {
	return room_name;
}

public void setName(String room_name) {
	this.room_name = room_name;
}

public boolean joinRoom(SocketThread c) {
	boolean t = false ;
	for (int i= 0; i<users.length;i++)
	{
		if(users[i]==null)
		{
		users[i] = c;
		t=true;
		break;
		}
	}
	return t;
}

public void textMessage(String msg, String username) {
	
	for (int i= 0; i<users.length;i++)
	{	
		if(users[i]!=null /*&& users[i].getUsername()!=username*/)
		users[i].sendMessage("["+username+"]: "+msg);
	}
}

private static void addRoom(Chat_room c) {
	roomsRegistry.add(c);
}

public static ArrayList<Chat_room> getAvaibleRooms() {
	return (ArrayList<Chat_room>) roomsRegistry;
}


public static Chat_room getRoomByName(String c) {
	Chat_room[] d = new Chat_room[roomsRegistry.size()];
	roomsRegistry.toArray(d);
	
	for(int i = 0 ;i<roomsRegistry.size();i++){
		if( d[i].getName().equals(c) ){
			return d[i];}
	
	}
	
	
	return null;
}

public static void deleteRoom() {
	
	
}

public int maxUsers(){
	return users.length;
}
public int activeUsers(){
	int num=0;
	for(SocketThread i : users){
		if(i!=null)
		num++;
	}
	return num;
}
}
