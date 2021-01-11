package thadome23.chatty.api.client;

import java.io.PrintStream;

class OutputThreadTest extends Thread {
	private PrintStream stream;
	private boolean test = true;
	private BufferTest userInput;
	//private WindowPrinter p;
	//private String username;
	
	public OutputThreadTest(PrintStream c, BufferTest inputBuffer){
		this.stream=c;
		this.userInput=inputBuffer;
		//this.username=username;
		//this.p =p;
	}

	@Override
	public void run(){
		
		String text = "",textReplaced;
		
		while(test){
			
			synchronized (userInput) {
				try {
					text = userInput.getString();	//get the message
				} catch (InterruptedException e) {  e.printStackTrace();  }	
			}
	
			textReplaced= text.replaceAll("_","/_");
			textReplaced = textReplaced.replaceAll("<","/<");
			textReplaced = textReplaced.replaceAll(">","/>");
				
			stream.println(textReplaced);		//now print it to the PrintStream
		}
	}

	public void close(){
		test=false;
		stream.close();	
	}
}
