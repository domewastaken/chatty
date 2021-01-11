package thadome23.chatty.api.server;


import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;


class Chat_roomTest {

	private String room_name;
	private List<SocketThreadTest> users;
	private int max;

	private static List<Chat_roomTest> roomsRegistry = new ArrayList<>();   //this is static!!


	Chat_roomTest(String name, int max) throws Exception {
		this.max = max;
		this.setName(name);            //assign the name to the room
		this.users = new ArrayList<>(max);    //initialize a list for roomsRegistry's components
		if (!Chat_roomTest.addRoom(this)) {
			throw new Exception();
		}//add the current room to the registry
	}

	String getName() {
		return room_name;
	}

	private void setName(String room_name) {
		this.room_name = room_name;
	}

	synchronized boolean joinRoom(SocketThreadTest c) {
	if(this.activeUsers()<max){
		serverMessage(""+c.getUsername()+" joined the chat") ;
		users.add(c);

		return true;
	}else{
		return false;
	}
}

	private void serverMessage(String text) {
		users.forEach((SocketThreadTest s) -> s.sendMessage(text));

	}

	void textMessage(String msg, String username) {
		users.forEach((SocketThreadTest s) -> s.sendMessage("[" + username + "]: " + msg));
	}

	private static boolean addRoom( Chat_roomTest c) {
		if (!isNamePresent(c.room_name)) {//return 1 if element is present, 0 if not
			roomsRegistry.add(c);
			return true;
		}
		return false;
	}

	static ArrayList<Chat_roomTest> getAvailableRooms() {
		return (ArrayList<Chat_roomTest>) roomsRegistry;
	}

	static Chat_roomTest getRoomByName(String c) {
		Chat_roomTest[] d = new Chat_roomTest[roomsRegistry.size()];
		roomsRegistry.toArray(d);
		for (int i = 0; i < roomsRegistry.size(); i++) {
		
		if( d[i].getName().equals(c) ){
			return d[i];
		}
	}

		return null;
	}

	void deleteUser(SocketThreadTest s) {
		users.remove(s);
		serverMessage("" + s.getUsername() + " left the chat");
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
		List<String> list= roomsRegistry.stream().map(Chat_roomTest::getName).collect(Collectors.toList());
		return list.contains(name);

	}
}