
package thadome23.chatty.api.client;

import java.io.BufferedReader;
import java.io.IOException;

class InputThreadTest extends Thread {
	
	private BufferedReader stream;
	private boolean test=true;
	private WindowPrinterTest output;
	
	InputThreadTest(BufferedReader from, WindowPrinterTest output){
		this.stream=from;
		this.output=output;
	}

	@Override
	public void run(){

		while(test){
		
			String text = null;
	
			try {
				text = stream.readLine();
		    } catch (IOException e) {
				
		    	if (e.getMessage().equals("Connection reset")){	
				    close();
				    output.println("server closed",ContentTypeTest.Chat_message);
				}
			}
			
		    if (text != null) {
		
		        String textReplaced = text.replaceAll("(?<!/)_", "\n");
		        String correctText = textReplaced.replaceAll("/_","_");
				correctText = correctText.replaceAll("/<","<");
				correctText = correctText.replaceAll("/>",">");
				
				String[] arg, i;
				
				if(correctText.endsWith(">>") && correctText.startsWith("<<")) {
					correctText = correctText.replaceAll("<<","");
					correctText = correctText.replaceAll(">>","");
					
					arg = correctText.split("\\$");
					i = arg[0].split(",");
				}else {  break;  }		
				
				if(i[0].equals("msg")) {
					output.println(arg[1],ContentTypeTest.Chat_message);
				}else if(i[0].equals("info")){
		    		output.println(arg[1],ContentTypeTest.Chat_info);
		    	}else if(i[0].equals("roomname")) {
		    		output.println(arg[1],ContentTypeTest.Room_name);
		    	}
			}
	    }
    }

	void close(){
		test=false;
		try {
			stream.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

}
