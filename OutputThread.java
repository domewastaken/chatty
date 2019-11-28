package servers;

import java.io.PrintStream;
import java.util.Scanner;

public class OutputThread extends Thread {
	private String text;
	private PrintStream c;
	private boolean test = true;
	private Buffer userInput;
	@SuppressWarnings("unused")
	private String username;
	
	public OutputThread(PrintStream c, Buffer buffer,String username){
		this.c=c;
		this.userInput=buffer;
		this.username=username;
	}
	@Override
	public void run(){
	userInput.register(this);
	while(test){
		
	if (userInput.isReady()) 
	{
	synchronized (userInput) {
		text = userInput.getString();
		userInput.clearBuffer();
		}
	
	c.println(text);
	System.out.println(text);
	
	}
	else {
		try {
		synchronized (this) {
			wait();
		}
			
	} catch (InterruptedException e) {

		e.printStackTrace();
	}}
	
	}
	}

	
	public void close(){
		test=false;
	}
}
