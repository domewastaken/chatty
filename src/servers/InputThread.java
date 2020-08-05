
package servers;


import java.io.BufferedReader;
import java.io.IOException;



public class InputThread extends Thread {
	private BufferedReader stream;
	private boolean test=true;
	private WindowPrinter output;
	
	InputThread(BufferedReader from, WindowPrinter output){
		this.stream=from;
		this.output=output;
	}

	@Override
	public void run(){

	while(test){
	String text = null;;

	try {
		text = stream.readLine();
        } catch (IOException e) {
		if (e.getMessage().equals("Connection reset"))
		{
			close();
			output.println("server closed",ContentType.Chat_message);
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
	}else {break;}		
		
		if(i[0].equals("msg")) {
        output.println(arg[1],ContentType.Chat_message);
		}else if(i[0].equals("info")){
    		output.println(arg[1],ContentType.Chat_info);
    	}else if(i[0].equals("roomname")) {
    		output.println(arg[1],ContentType.Room_name);
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
	boolean getTest(){
		return test;
	}
}
