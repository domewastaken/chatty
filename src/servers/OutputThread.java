package servers;

import java.io.PrintStream;

public class OutputThread extends Thread {
	private PrintStream c;
	private boolean test = true;
	private Buffer userInput;
	//private WindowPrinter p;
	//private String username;
	
	public OutputThread(PrintStream c, Buffer buffer/*, String username,WindowPrinter p*/){
		this.c=c;
		this.userInput=buffer;
		//this.username=username;
		//this.p =p;
	}

	@Override
	public void run(){
		userInput.register(this);
		String text;
		while(test){

			if (userInput.isReady()) {

			synchronized (userInput) {
				text = userInput.getString();	//get the message
				userInput.clearBuffer();
			}
			c.println(text);		//now print it to the PrintStream

			//p.println("["+username+"]: "+text);
			//DEBUG USE ONLY //System.out.println("sended:"+text);
			}else {
				try {
					synchronized (this) {
						wait();
					}
				} catch (InterruptedException e) { e.printStackTrace(); close();}
			}
	}
	}

	public void close(){
		test=false;
	}
}
