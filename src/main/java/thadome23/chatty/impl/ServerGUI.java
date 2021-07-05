package thadome23.chatty.impl;

import javax.swing.JFrame;

import thadome23.chatty.api.server.ChatServer;

import javax.swing.JLabel;
import java.awt.GridBagLayout;
import java.awt.GridBagConstraints;
import java.awt.Insets;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.WindowEvent;
import java.awt.event.WindowListener;

import javax.swing.JTextField;
import javax.swing.UIManager;
import javax.swing.UnsupportedLookAndFeelException;
import javax.swing.JButton;

import javax.swing.JMenuBar;
import javax.swing.JMenu;
import javax.swing.JMenuItem;
import java.awt.SystemColor;

public class ServerGUI {

	private JFrame		frm;
	private Thread  	app;
	private ChatServer 	server;
	private JTextField 	port;
	private JLabel 		status;
	private JButton 	startBtn ;
	private JButton		stopBtn;
	private JLabel lblNewLabel;
	private JTextField textField;
	private JMenuBar menuBar;
	private JMenu mnNewMenu;
	private JMenuItem logsM;
	/**
	 * Create the application.
	 */
	public ServerGUI(ChatServer c) {
		
		server = c;
		
		try {
			UIManager.setLookAndFeel(
			    UIManager.getSystemLookAndFeelClassName());
		} catch (ClassNotFoundException | InstantiationException | IllegalAccessException
				| UnsupportedLookAndFeelException e) {

		}
		initialize();
	}

	/**
	 * Initialize the contents of the frame.
	 */
	private void initialize() {
		frm = new JFrame();
		frm.setForeground(SystemColor.infoText);
		frm.setTitle("chatty");
		frm.setBounds(100, 100, 278, 254);
		frm.setDefaultCloseOperation(JFrame.HIDE_ON_CLOSE);
		GridBagLayout gridBagLayout = new GridBagLayout();
		gridBagLayout.columnWidths = new int[] {30, 10, 0, 30, 10};
		gridBagLayout.rowHeights = new int[] {30, 10, 0, 22, 30, 0, 10};
		gridBagLayout.columnWeights = new double[]{0.0, 0.0, 1.0, 0.0, Double.MIN_VALUE};
		gridBagLayout.rowWeights = new double[]{0.0, 0.0, 0.0, 0.0, 0.0, 0.0, Double.MIN_VALUE};
		frm.getContentPane().setLayout(gridBagLayout);
		
		JLabel label1 = new JLabel("Text server port:");
		GridBagConstraints gbc_label1 = new GridBagConstraints();
		gbc_label1.anchor = GridBagConstraints.EAST;
		gbc_label1.insets = new Insets(0, 0, 5, 5);
		gbc_label1.gridx = 1;
		gbc_label1.gridy = 1;
		frm.getContentPane().add(label1, gbc_label1);
		
		port = new JTextField("8080");
		GridBagConstraints gbc_port = new GridBagConstraints();
		gbc_port.insets = new Insets(0, 0, 5, 5);
		gbc_port.fill = GridBagConstraints.HORIZONTAL;
		gbc_port.gridx = 2;
		gbc_port.gridy = 1;
		frm.getContentPane().add(port, gbc_port);
		port.setColumns(10);
		
		lblNewLabel = new JLabel("Audio server port");
		GridBagConstraints gbc_lblNewLabel = new GridBagConstraints();
		gbc_lblNewLabel.anchor = GridBagConstraints.EAST;
		gbc_lblNewLabel.insets = new Insets(0, 0, 5, 5);
		gbc_lblNewLabel.gridx = 1;
		gbc_lblNewLabel.gridy = 2;
		frm.getContentPane().add(lblNewLabel, gbc_lblNewLabel);
		
		textField = new JTextField();
		textField.setText("8081");
		GridBagConstraints gbc_textField = new GridBagConstraints();
		gbc_textField.insets = new Insets(0, 0, 5, 5);
		gbc_textField.fill = GridBagConstraints.HORIZONTAL;
		gbc_textField.gridx = 2;
		gbc_textField.gridy = 2;
		frm.getContentPane().add(textField, gbc_textField);
		textField.setColumns(10);
		
		JLabel label2 = new JLabel("Server status: ");
		GridBagConstraints gbc_label2 = new GridBagConstraints();
		gbc_label2.anchor = GridBagConstraints.EAST;
		gbc_label2.insets = new Insets(0, 0, 5, 5);
		gbc_label2.gridx = 1;
		gbc_label2.gridy = 3;
		frm.getContentPane().add(label2, gbc_label2);
		
		status = new JLabel("offline");
		GridBagConstraints gbc_lblNewLabel_1 = new GridBagConstraints();
		gbc_lblNewLabel_1.insets = new Insets(0, 0, 5, 5);
		gbc_lblNewLabel_1.fill = GridBagConstraints.VERTICAL;
		gbc_lblNewLabel_1.anchor = GridBagConstraints.WEST;
		gbc_lblNewLabel_1.gridx = 2;
		gbc_lblNewLabel_1.gridy = 3;
		frm.getContentPane().add(status, gbc_lblNewLabel_1);
		
		startBtn = new JButton("Start");
		GridBagConstraints gbc_btnNewButton = new GridBagConstraints();
		gbc_btnNewButton.fill = GridBagConstraints.HORIZONTAL;
		gbc_btnNewButton.insets = new Insets(0, 0, 0, 5);
		gbc_btnNewButton.gridx = 1;
		gbc_btnNewButton.gridy = 5;
		frm.getContentPane().add(startBtn, gbc_btnNewButton);
		
		stopBtn = new JButton("Stop");
		GridBagConstraints gbc_btnNewButton_1 = new GridBagConstraints();
		gbc_btnNewButton_1.fill = GridBagConstraints.HORIZONTAL;
		gbc_btnNewButton_1.insets = new Insets(0, 0, 0, 5);
		gbc_btnNewButton_1.gridx = 2;
		gbc_btnNewButton_1.gridy = 5;
		frm.getContentPane().add(stopBtn, gbc_btnNewButton_1);
		stopBtn.setEnabled(false);
		
		menuBar = new JMenuBar();
		frm.setJMenuBar(menuBar);
		
		mnNewMenu = new JMenu("help");
		menuBar.add(mnNewMenu);
		
		logsM = new JMenuItem("logs");
		logsM.setBackground(SystemColor.activeCaptionBorder);
		mnNewMenu.add(logsM);
		addListeners();
		frm.setVisible(true);
	}
	
	private void addListeners() {
		
		startBtn.addActionListener(new ActionListener() {
				
				@Override
				public void actionPerformed(ActionEvent e) {
					server.setPort(Integer.parseInt(port.getText()));
					startS();
					try {
						Thread.sleep(200);
					} catch (InterruptedException e1) {
						// TODO Auto-generated catch block
						e1.printStackTrace();
					}
					if(server.isOnline()) {
						status.setText("online"); 
						startBtn.setEnabled(false);
						stopBtn.setEnabled(true);
					}
				}

				
			});
		
		logsM.addActionListener( (e)-> {server.getLog().setVisible(true);});
		
		stopBtn.addActionListener(new ActionListener() {
			
			@Override
			public void actionPerformed(ActionEvent e) {
				
				stopS();
				status.setText("offline"); 
				startBtn.setEnabled(true);
				stopBtn.setEnabled(false);
				//System.exit(0);
				
				
			}

		
		});
		
		frm.addWindowListener(new WindowListener() {
			
			@Override
			public void windowOpened(WindowEvent e) {}
			
			@Override
			public void windowIconified(WindowEvent e) {}
			
			@Override
			public void windowDeiconified(WindowEvent e) {}
			
			@Override
			public void windowDeactivated(WindowEvent e) {}
			
			@Override
			public void windowClosing(WindowEvent e) {
				if(server.isOnline())
					server.destroy();	
				System.exit(0);
			}
			@Override
			public void windowClosed(WindowEvent e) {}
			@Override
			public void windowActivated(WindowEvent e) {}
		
		});
		
	}
	
	private void startS() {
		app = new Thread( () -> server.start() );
				
		app.start();
		
	}
	private void stopS() {
		
		server.close();
	}
}














