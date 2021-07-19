package thadome23.chatty.impl;

import javax.swing.*;
import javax.swing.text.BadLocationException;
import javax.swing.text.DefaultCaret;
import javax.swing.text.DefaultStyledDocument;

import thadome23.chatty.api.client.Buffer;

import thadome23.chatty.api.client.ContentType;
import thadome23.chatty.api.client.WindowPrinter;


import java.awt.*;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;

public class ClientGui{

	private JFrame frame;
	private JPanel pane;
	private JTextField textField;
	private JScrollPane scrollpane;
	private JButton btnSubmit;
	private JTextPane textPane;
	private DefaultStyledDocument document;
	private Buffer buffer;
	private JPanel panel;
	private JLabel lblNewLabel;
	private JLabel room_label;
	private JMenuBar menuBar;
	private JMenu mnNewMenu;
	private JMenuItem mntmNewMenuItem;

	public ClientGui() {
		// Set System L&F
		/*	try {
				UIManager.setLookAndFeel(
				    UIManager.getSystemLookAndFeelClassName());
			} catch (ClassNotFoundException | InstantiationException | IllegalAccessException
					| UnsupportedLookAndFeelException e) {

			}*/
	
		initialize();
	}

	/**
	 * Initialize the contents of the frame.
	 */
	private void initialize() {
		frame = new JFrame();
		frame.setBackground(SystemColor.scrollbar);
		frame.setTitle("Java");
		frame.setBounds(100, 100, 324, 338);
		frame.setMinimumSize(new Dimension(324,338));
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		
		pane=(JPanel)frame.getContentPane();
		GridBagLayout gridBagLayout = new GridBagLayout();
		gridBagLayout.columnWidths = new int[] {10, 0, 10};
		gridBagLayout.rowHeights = new int[] {0, 30, 30, 50, 30, 30, 30, 0, 10};
		gridBagLayout.columnWeights = new double[]{1.0, 0.0, Double.MIN_VALUE};
		gridBagLayout.rowWeights = new double[]{0.0, 1.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0, Double.MIN_VALUE};

		pane.setLayout(gridBagLayout);
		document = new DefaultStyledDocument();
		textPane = new JTextPane(document);
		textField = new JTextField();
		buffer = new Buffer();
		
		btnSubmit = new JButton("Enter");
		textPane.setEditable(false);
		
		panel = new JPanel();
		FlowLayout flowLayout = (FlowLayout) panel.getLayout();
		flowLayout.setAlignment(FlowLayout.LEFT);
		GridBagConstraints gbc_panel = new GridBagConstraints();
		gbc_panel.anchor = GridBagConstraints.NORTHWEST;
		gbc_panel.gridwidth = 2;
		gbc_panel.insets = new Insets(5, 5, 5, 0);
		gbc_panel.gridx = 0;
		gbc_panel.gridy = 0;
		frame.getContentPane().add(panel, gbc_panel);
		
		lblNewLabel = new JLabel("Room name:");
		lblNewLabel.setHorizontalAlignment(SwingConstants.LEFT);
		panel.add(lblNewLabel);
		
		room_label = new JLabel("no room");
		panel.add(room_label);
		scrollpane = new JScrollPane(textPane);

		GridBagConstraints gbc_scrollpane = new GridBagConstraints();
		gbc_scrollpane.gridheight = 6;
		gbc_scrollpane.gridwidth = 2;
		gbc_scrollpane.fill = GridBagConstraints.BOTH;
		gbc_scrollpane.insets = new Insets(5, 5, 5, 0);
		gbc_scrollpane.gridx = 0;
		gbc_scrollpane.gridy = 1;
		pane.add(scrollpane, gbc_scrollpane);

		GridBagConstraints gbc_btnSubmit = new GridBagConstraints();
		gbc_btnSubmit.insets = new Insets(5, 0, 0, 0);
		gbc_btnSubmit.fill = GridBagConstraints.HORIZONTAL;
		gbc_btnSubmit.gridx = 1;
		gbc_btnSubmit.gridy = 7;
		pane.add(btnSubmit, gbc_btnSubmit);

		GridBagConstraints gbc_textField = new GridBagConstraints();
		gbc_textField.insets = new Insets(5, 5, 0, 5);
		gbc_textField.fill = GridBagConstraints.HORIZONTAL;
		gbc_textField.gridx = 0;
		gbc_textField.gridy = 7;
		pane.add(textField, gbc_textField);
		
		textField.setColumns(10);
		
		menuBar = new JMenuBar();
		frame.setJMenuBar(menuBar);
		
		mnNewMenu = new JMenu("help");
		menuBar.add(mnNewMenu);
		
		mntmNewMenuItem = new JMenuItem("?");
		mnNewMenu.add(mntmNewMenuItem);

		btnSubmit.addActionListener(e -> {
			//this is lambda
			String g =textField.getText();
			buffer.add(g);
			textField.setText("");
			//DEBUG USE ONLY //System.out.println("writed:"+g);
		});
		
		textField.addKeyListener(new KeyAdapter() {

			public void keyPressed(KeyEvent e) {
				if (e.getKeyCode() == KeyEvent.VK_ENTER) {
					btnSubmit.doClick();
				}
			}
		});
		
		DefaultCaret caret = (DefaultCaret) textPane.getCaret();
		caret.setUpdatePolicy(DefaultCaret.ALWAYS_UPDATE);
		frame.setVisible(true);

	}
	
	public WindowPrinter getPrinter() {
		
		return (String message,ContentType type)-> {
			
			frame.toFront();
			frame.requestFocus();
			frame.repaint();
			switch(type) {
				
				case Chat_message:
					try {
						document.insertString(document.getEndPosition().getOffset() -1 ,message+"\n", StyleType.MESSAGE_STYLE.getStyle() );
					} catch (BadLocationException e) {
						e.printStackTrace();
					}
				break;	
				
				case Chat_info: 
					try {
						document.insertString(document.getEndPosition().getOffset() -1 ,message+"\n", StyleType.INFO_STYLE.getStyle() );
					} catch (BadLocationException e) {
						e.printStackTrace(); 
					}
				break;
					
				case Room_name:
					room_label.setText(message);
				break;
					
				case Error:
					try {
						document.insertString(document.getEndPosition().getOffset() -1 ,message+"\n", StyleType.ERROR_STYLE.getStyle() );
					} catch (BadLocationException e) {
						e.printStackTrace(); 
					}
				break;
				
				default:
				break;
			}
		};
	}
	
	public Buffer getBuffer() {
		return buffer;
	}
}
