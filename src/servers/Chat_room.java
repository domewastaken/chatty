package servers;

import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.List;

public class Chat_room {

	private String room_name;
	private List<SocketThread> users;
	private int max;

	private static List<Chat_room> roomsRegistry = new ArrayList<>();

//private String[] cronology;

	public Chat_room(String name, int max) throws Exception {
		this.max = max;
		this.setName(name);            //assign the name to the room
		this.users = new ArrayList<>(max);    //initialize a list for roomsRegistry' components
		if (!Chat_room.addRoom(this)) {
			throw new Exception();
		}//add the current room to the registry
	}

	public String getName() {
		return room_name;
	}

	public void setName(String room_name) {
		this.room_name = room_name;
	}

	public synchronized boolean joinRoom(SocketThread c) {
	if(this.activeUsers()<max){
		serverMessage(""+c.getUsername()+" joined the chat") ;
		users.add(c);

		return true;
	}else{
		return false;
	}
}

	private void serverMessage(String text) {
		users.forEach((SocketThread s) -> s.sendMessage(text));

	}

	public void textMessage(String msg, String username) {
		users.forEach((SocketThread s) -> s.sendMessage("[" + username + "]: " + msg));
	}

	private static boolean addRoom(@NotNull Chat_room c) {
		if (!isNamePresent(c.room_name)) {//return 1 if element is present, 0 if not
			roomsRegistry.add(c);
			return true;
		}
		return false;
	}

	public static ArrayList<Chat_room> getAvailableRooms() {
		return (ArrayList<Chat_room>) roomsRegistry;
	}

	public static Chat_room getRoomByName(String c) {
		Chat_room[] d = new Chat_room[roomsRegistry.size()];
		roomsRegistry.toArray(d);
		for (int i = 0; i < roomsRegistry.size(); i++) {
		
		if( d[i].getName().equals(c) ){
			return d[i];
		}
	}

		return null;
	}

	public void deleteUser(SocketThread s) {
		users.remove(s);
		serverMessage("" + s.getUsername() + " left the chat");
		if (users.isEmpty())
			deleteRoom();
	}

	public int maxUsers() {
		return max;
	}

	public int activeUsers() {
		return users.size();
	}

	private void deleteRoom() {
		roomsRegistry.remove(this);
	}

	private static boolean isNamePresent(String name) {
		final boolean[] test = {false};
		roomsRegistry.forEach(chat_room -> {
			if (name == chat_room.room_name) {
				test[0] = true;
			}
		});
		return test[0];
	}
}