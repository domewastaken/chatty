package servers;


import java.io.BufferedReader;
import java.io.IOException;


public class InputThread extends Thread {
	private BufferedReader stream;
	private boolean test=true;
	private WindowPrinter output;
	
	public InputThread(BufferedReader from_client) {this(from_client,null);}
	
	public InputThread(BufferedReader from_client, WindowPrinter output){
		this.stream=from_client;
		this.output=output;
	}
	
	@Override
	public void run(){
	
	while(test){
	String text = null;
	
	try {
		if((text = stream.readLine())!=null)
		output.println(text);
	
	} catch (IOException e) {
		if (e.getMessage().equals("Connection reset")) {
			output.println("server closed");
		}
		}	
	
	}}
	
	public void close(){
		test=false;
	}
	protected boolean getTest(){
		return test;
	}
}
