package thadome23.chatty.api.client;

public class BufferTest {
	
	private String buff ="";

	public synchronized void add(String text)
	{	
		buff = buff.concat(text);	
		notifyAll();
	}

	public synchronized String getString() throws InterruptedException
	{	
		if(buff == "") {
			wait(); 
		}
		
		String str = buff;
		buff = "";
	
		return str;
	}
}
