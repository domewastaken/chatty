package servers;


import java.io.BufferedReader;
import java.io.IOException;


public class InputThread extends Thread {
	private BufferedReader stream;
	private boolean test=true;
	private WindowPrinter output;

	InputThread(BufferedReader from)
	{ this(from, System.out::println); }


	InputThread(BufferedReader from, WindowPrinter output){
		this.stream=from;
		this.output=output;
	}

	@Override
	public void run(){

	while(test){
	String text;

	try {
        if ((text = stream.readLine()) != null) {

            String textReplaced = text.replaceAll("(?<!/)_", "\n");
            String correctText = textReplaced.replaceAll("/_","_");
            output.println(correctText);
        }
    } catch (IOException e) {
		if (e.getMessage().equals("Connection reset"))
		{
			close();
			output.println("server closed");
		}
		}

	}}

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
