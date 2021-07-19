
package thadome23.chatty.api.client;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.Socket;

class InputThread extends Thread {
	
	private BufferedReader stream;
	private boolean test=true;
	private WindowPrinter output;
	private Socket s;


	public InputThread(Socket socket, WindowPrinter userOutput) throws IOException {
		this.stream=new BufferedReader(new InputStreamReader(socket.getInputStream()));
		this.output=userOutput;
		this.s = socket;
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
				    output.println("server closed",ContentType.Error);
				}else {e.printStackTrace();}
		    	e.printStackTrace();
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
					output.println(arg[1],ContentType.Chat_message);
				}else if(i[0].equals("info")){
		    		output.println(arg[1],ContentType.Chat_info);
		    	}else if(i[0].equals("roomname")) {
		    		output.println(arg[1],ContentType.Room_name);
		    	}
			}else {
				
				output.println("Server closed ",ContentType.Error);
				close();
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
