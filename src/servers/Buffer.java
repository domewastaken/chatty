package servers;

import java.util.ArrayList;
import java.util.List;

public class Buffer {
	private String internalBuffer ="";
	private List<Object> listenerList = new ArrayList<>();
	
	private void fire() {
		listenerList.forEach(  (Object a)->{ synchronized(a) {a.notify();} }  );
	}

	public void add(String text) {
		synchronized (internalBuffer){
		internalBuffer = internalBuffer.concat(text);	
		}
		fire();
	}

	public String getString() {
		String str = internalBuffer;
		clearBuffer();
	    return str;
	}
	public boolean isReady() {
		return !internalBuffer.isBlank();
	}
	public void register(Object c) {
		listenerList.add(c);
	}
	public void clearBuffer() {internalBuffer = "";}
}
