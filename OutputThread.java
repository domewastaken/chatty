package servers;

import java.io.PrintStream;
import java.util.Scanner;

public class OutputThread extends Thread {
	
	private PrintStream c;
	private boolean test = true;
	private Scanner userInput;
	@SuppressWarnings("unused")
	private String username;
	public OutputThread(PrintStream c, Scanner b,String username){
		this.c=c;
		this.userInput=b;
		this.username=username;
	}
	@Override
	public void run(){
	
	while(test){
	String text = userInput.nextLine();
	c.println(text);

	
	}}
	
	public void close(){
		test=false;
	}
}
