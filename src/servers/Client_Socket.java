package servers;



import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintStream;
import java.net.Socket;

public class Client_Socket {
	private Buffer inputBuffer;
	private Socket socket;
	private WindowPrinter userOutput;
	private PrintStream to_server;
	private BufferedReader from_server;
	private String address;

	private int port;


	public static void main(String[] args){
		
		new Client_Socket( 8080, new ChatWindow());
	}

	public Client_Socket(int port, ChatWindow window){

		this.port = port;
		this.userOutput = window.getPrinter();
		this.inputBuffer=window.getBuffer();

		inputBuffer.register(this);
		userOutput.println("enter the address",ContentType.Chat_message);

		try {
			synchronized (this){wait();}
		} catch (InterruptedException e) {
			e.printStackTrace();
		}

		this.address = inputBuffer.getString();

		boolean stopExecution = false;

		try {

			socket = new Socket( Utils.parseStringToAddress(address) , port);
	
		} catch (IOException e) {
			userOutput.println("errors during the connection",ContentType.Chat_message);
			stopExecution =true;
		}

		if(!stopExecution){
			userOutput.println("connecting to "+address,ContentType.Chat_message);

			try {
				to_server = new PrintStream(socket.getOutputStream());
				from_server = new BufferedReader(new InputStreamReader( socket.getInputStream()));
			} catch (IOException e) { e.printStackTrace(); }

			try {

				this.play();

			} catch (IOException e) {e.printStackTrace();}

			to_server.close();

			try {
				from_server.close();
			} catch (IOException e) {e.printStackTrace();}
		
		}
		
	}

	private void play() throws IOException {

		String c = from_server.readLine();
		userOutput.println("received "+ c,ContentType.Chat_message);

		InputThread tr1=new InputThread(from_server,userOutput);
		OutputThread tr2 = new OutputThread(to_server,inputBuffer);

		tr1.start();
		tr2.start();

		try {
			tr1.join();
			tr2.join();
		} catch (InterruptedException e) {e.printStackTrace();}
		
		to_server.close();
		
		try {
			from_server.close();
		} catch (IOException e) {e.printStackTrace();}
		
		
	}


	public String getAddress() {
		return address;
	}

	public int getPort(){
		return port;
	}

}
