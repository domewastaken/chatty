package servers;


import java.io.BufferedReader;
import java.io.IOException;


public class InputThread extends Thread {
	private BufferedReader stream;
	private boolean test=true;
	private WindowPrinter output;
	
	public InputThread(BufferedReader from)
	{ this(from, System.out::println); }

	
	public InputThread(BufferedReader from, WindowPrinter output){
		this.stream=from;
		this.output=output;
	}
	
	@Override
	public void run(){
	
	while(test){
	String text;
	
	try {
		if((text = stream.readLine())!=null)
		output.println(text);
	
	} catch (IOException e) {
		if (e.getMessage().equals("Connection reset"))
		{
			close();
			output.println("server closed");
		}
		}	
	
	}}
	
	public void close(){
		test=false;
		try {
		stream.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	protected boolean getTest(){
		return test;
	}
}
