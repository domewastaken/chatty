package thadome23.chatty.api.client;

import java.io.PrintStream;

class OutputThread extends Thread {
	private PrintStream stream;
	private boolean test = true;
	private Buffer userInput;

	public OutputThread(PrintStream c, Buffer buffer){
		this.stream=c;
		this.userInput=buffer;

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
	
			textReplaced = text.replaceAll("_","/_");
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
