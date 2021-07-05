package thadome23.chatty.api.server;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.EventQueue;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.border.EmptyBorder;
import javax.swing.text.BadLocationException;
import javax.swing.text.DefaultCaret;
import javax.swing.text.DefaultStyledDocument;
import javax.swing.text.Style;
import javax.swing.text.StyleConstants;
import javax.swing.text.StyleContext;

import javax.swing.JTextPane;
import javax.swing.JScrollPane;

public class LogGui extends JFrame {

	private JPanel contentPane;
	private DefaultStyledDocument document;
	/**
	 * Launch the application.
	 */
	public void invoke(){
		EventQueue.invokeLater(new Runnable() {
			public void run() {
				try {
					LogGui frame = new LogGui();
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		});
	}

	/**
	 * Create the frame.
	 */
	public LogGui() {
		setTitle("Logs");
		setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
		setBounds(100, 100, 450, 300);
		contentPane = new JPanel();
		contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));
		contentPane.setLayout(new BorderLayout(0, 0));
		setContentPane(contentPane);
		
	
		document = new DefaultStyledDocument();
		
		JScrollPane scrollPane = new JScrollPane();
		contentPane.add(scrollPane, BorderLayout.CENTER);
		
		JTextPane textPane = new JTextPane(document);
		textPane.setEditable(false);
		scrollPane.setViewportView(textPane);
		DefaultCaret caret = (DefaultCaret) textPane.getCaret();
		caret.setUpdatePolicy(DefaultCaret.ALWAYS_UPDATE);
		
		setVisible(false);
		
		invoke();
	}
	
	
	public void write(String message) {
		DateTimeFormatter dtf = DateTimeFormatter.ofPattern("HH:mm:ss");  
		LocalDateTime now = LocalDateTime.now();  
		
		Style style = new StyleContext().addStyle("text",null);
		StyleConstants.setForeground(style, Color.BLACK);
		message = "["+now.format(dtf)+"]: "+message;
		try {
			document.insertString(document.getEndPosition().getOffset() -1 ,message+"\n", style );
		} catch (BadLocationException e) {
			e.printStackTrace();
		}
	}
	
	public void setVisible() {
		setVisible(true);
	}
}

	
