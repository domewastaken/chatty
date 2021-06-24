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
import javax.swing.JButton;

public class ServerGUI {

	private JFrame		frame;
	private Runnable 	app;
	private ChatServer 	server;
	private JTextField 	port;
	private JLabel 		status;
	private JButton 	startBtn ;
	private JButton		stopBtn;
	/**
	 * Create the application.
	 */
	public ServerGUI(ChatServer c) {
		
		server = c;
		app = new Runnable() {
			public void run() {
				c.start();
			}
		};
		initialize();
	}

	/**
	 * Initialize the contents of the frame.
	 */
	private void initialize() {
		frame = new JFrame();
		frame.setResizable(false);
		frame.setBounds(100, 100, 259, 190);
		frame.setDefaultCloseOperation(JFrame.HIDE_ON_CLOSE);
		GridBagLayout gridBagLayout = new GridBagLayout();
		gridBagLayout.columnWidths = new int[]{0, 0, 0, 0, 0};
		gridBagLayout.rowHeights = new int[]{0, 0, 22, 0, 0, 0};
		gridBagLayout.columnWeights = new double[]{0.0, 0.0, 0.0, 0.0, Double.MIN_VALUE};
		gridBagLayout.rowWeights = new double[]{0.0, 0.0, 0.0, 0.0, 0.0, Double.MIN_VALUE};
		frame.getContentPane().setLayout(gridBagLayout);
		
		JLabel label1 = new JLabel("Server port:");
		GridBagConstraints gbc_label1 = new GridBagConstraints();
		gbc_label1.anchor = GridBagConstraints.EAST;
		gbc_label1.insets = new Insets(0, 0, 5, 5);
		gbc_label1.gridx = 1;
		gbc_label1.gridy = 1;
		frame.getContentPane().add(label1, gbc_label1);
		
		port = new JTextField(String.valueOf(server.getPort()));
		port.setEditable(false);
		GridBagConstraints gbc_port = new GridBagConstraints();
		gbc_port.insets = new Insets(0, 0, 5, 5);
		gbc_port.fill = GridBagConstraints.HORIZONTAL;
		gbc_port.gridx = 2;
		gbc_port.gridy = 1;
		frame.getContentPane().add(port, gbc_port);
		port.setColumns(10);
		
		JLabel label2 = new JLabel("Server status: ");
		GridBagConstraints gbc_label2 = new GridBagConstraints();
		gbc_label2.anchor = GridBagConstraints.EAST;
		gbc_label2.insets = new Insets(0, 0, 5, 5);
		gbc_label2.gridx = 1;
		gbc_label2.gridy = 2;
		frame.getContentPane().add(label2, gbc_label2);
		
		status = new JLabel("offline");
		GridBagConstraints gbc_lblNewLabel_1 = new GridBagConstraints();
		gbc_lblNewLabel_1.insets = new Insets(0, 0, 5, 5);
		gbc_lblNewLabel_1.fill = GridBagConstraints.VERTICAL;
		gbc_lblNewLabel_1.anchor = GridBagConstraints.WEST;
		gbc_lblNewLabel_1.gridx = 2;
		gbc_lblNewLabel_1.gridy = 2;
		frame.getContentPane().add(status, gbc_lblNewLabel_1);
		
		startBtn = new JButton("Start");
		GridBagConstraints gbc_btnNewButton = new GridBagConstraints();
		gbc_btnNewButton.fill = GridBagConstraints.HORIZONTAL;
		gbc_btnNewButton.insets = new Insets(0, 0, 0, 5);
		gbc_btnNewButton.gridx = 1;
		gbc_btnNewButton.gridy = 4;
		frame.getContentPane().add(startBtn, gbc_btnNewButton);
		
		stopBtn = new JButton("Stop");
		GridBagConstraints gbc_btnNewButton_1 = new GridBagConstraints();
		gbc_btnNewButton_1.fill = GridBagConstraints.HORIZONTAL;
		gbc_btnNewButton_1.insets = new Insets(0, 0, 0, 5);
		gbc_btnNewButton_1.gridx = 2;
		gbc_btnNewButton_1.gridy = 4;
		frame.getContentPane().add(stopBtn, gbc_btnNewButton_1);
		stopBtn.setEnabled(false);
		addListeners();
		frame.setVisible(true);
	}
	
	private void addListeners() {
		
		startBtn.addActionListener(new ActionListener() {
				
				@Override
				public void actionPerformed(ActionEvent e) {
					
					new Thread(app).start();
					status.setText("online"); 
					startBtn.setEnabled(false);
					stopBtn.setEnabled(true);
					
				}
			});
		stopBtn.addActionListener(new ActionListener() {
			
			@Override
			public void actionPerformed(ActionEvent e) {
				
				server.close();

				System.exit(0);
				
				
			}
		});
		
		frame.addWindowListener(new WindowListener() {
			
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
				server.close();
			}
			
			@Override
			public void windowClosed(WindowEvent e) {
				server.close();
				System.exit(0);
			}
			
			@Override
			public void windowActivated(WindowEvent e) {}
		
		});
		
	}
}














