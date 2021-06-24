package thadome23.chatty.impl;

import java.awt.Color;

import javax.swing.text.Style;
import javax.swing.text.StyleConstants;
import javax.swing.text.StyleContext;

enum StyleType {
	MESSAGE_STYLE("message",Color.BLACK),
	INFO_STYLE("info",new Color(128,128,128)),
	ERROR_STYLE("error",Color.RED);
	
	private static class init { static StyleContext context = new StyleContext();}
	private Style style;
	
	private StyleType(String name,Color color) {

		style = init.context.addStyle(name,null);
		StyleConstants.setForeground(style, color);
	}
	Style getStyle(){return style;}
}
