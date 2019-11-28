package servers;


import java.io.BufferedReader;
import java.io.IOException;


public class InputThread extends Thread {
	private BufferedReader c		;
	private boolean 	   test=true;
	private WindowPrinter  printer;
	public InputThread(BufferedReader from_client) {this(from_client,null);}
	
	public InputThread(BufferedReader from_client, WindowPrinter printer){
		this.c=from_client;
		this.printer=printer;
	}
	
	@Override
	public void run(){
	
	while(test){
	String text = null;
	
	try {
		if((text = c.readLine())!=null)
		text =text.replaceAll("_", " ");
		printer.println(text);
	
	} catch (IOException e) { e.printStackTrace(); }	
	
	}}
	
	public void close(){
		test=false;
	}
	protected boolean getTest(){
		return test;
	}
}
