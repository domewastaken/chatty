package servers;

import java.io.PrintStream;

class OutputThread extends Thread {
	private PrintStream stream;
	private boolean test = true;
	private Buffer userInput;
	//private WindowPrinter p;
	//private String username;
	
	public OutputThread(PrintStream c, Buffer buffer/*, String username,WindowPrinter p*/){
		this.stream=c;
		this.userInput=buffer;
		//this.username=username;
		//this.p =p;
	}

	@Override
	public void run(){
		
		userInput.register(this);
		String text,textReplaced;
		
		while(test){

			if (userInput.isReady())
			{
				synchronized (userInput) {
					
					text = userInput.getString();	//get the message
	
				}
	
				textReplaced= text.replaceAll("_","/_");
				textReplaced = textReplaced.replaceAll("<","/<");
				textReplaced = textReplaced.replaceAll(">","/>");
				
				stream.println(textReplaced);		//now print it to the PrintStream
	
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
		stream.close();	
	}
}
