package servers;


import java.io.BufferedReader;
import java.io.IOException;


public class InputThread extends Thread {
	private BufferedReader c		;
	private boolean 	   test=true;
	
	public InputThread(BufferedReader from_client){
		this.c=from_client;
		
	}
	
	@Override
	public void run(){
	
	while(test){
	String text = null;
	
	try {
		if((text = c.readLine())!=null)
		text =text.replaceAll("_", " ");
		System.out.println(text);
	
	} catch (IOException e) { e.printStackTrace(); }	
	
	}}
	
	public void close(){
		test=false;
	}
	protected boolean getTest(){
		return test;
	}
}
