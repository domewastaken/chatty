package thadome23.chatty.api.server;


import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;


class Chat_room {

	private String room_name;
	private List<SocketThread> users;
	private int max;

	private static List<Chat_room> roomsRegistry = new ArrayList<>();   //this is static!!


	Chat_room(String name, int max) throws Exception {
		this.max = max;
		this.setName(name);            //assign the name to the room
		this.users = new ArrayList<>(max);    //initialize a list for roomsRegistry's components
		if (!Chat_room.addRoom(this)) {
			throw new Exception();
		}//add the current room to the registry
	}

	String getName() {
		return room_name;
	}

	private void setName(String room_name) {
		this.room_name = room_name;
	}

	synchronized boolean joinRoom(SocketThread c) {
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

	void textMessage(String msg, String username) {
		users.forEach((SocketThread s) -> s.sendMessage("[" + username + "]: " + msg));
	}

	private static boolean addRoom( Chat_room c) {
		if (!isNamePresent(c.room_name)) {//return 1 if element is present, 0 if not
			roomsRegistry.add(c);
			return true;
		}
		return false;
	}

	static ArrayList<Chat_room> getAvailableRooms() {
		return (ArrayList<Chat_room>) roomsRegistry;
	}

	static Chat_room getRoomByName(String c) {
		Chat_room[] d = new Chat_room[roomsRegistry.size()];
		roomsRegistry.toArray(d);
		for (int i = 0; i < roomsRegistry.size(); i++) {
		
		if( d[i].getName().equals(c) ){
			return d[i];
		}
	}

		return null;
	}

	void deleteUser(SocketThread socketThread) {
		users.remove(socketThread);
		serverMessage("" + socketThread.getUsername() + " left the chat");
		if (users.isEmpty())
			deleteRoom();
	}

	int maxUsers() {
		return max;
	}

	int activeUsers() {
		return users.size();
	}

	private void deleteRoom() {
		roomsRegistry.remove(this);
	}

	private static boolean isNamePresent(String name) {
		List<String> list= roomsRegistry.stream().map(Chat_room::getName).collect(Collectors.toList());
		return list.contains(name);

	}
}