package thadome23.chatty.impl;

import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.border.EmptyBorder;
import javax.swing.JTextField;

import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.SwingConstants;
import javax.swing.UIManager;
import javax.swing.UnsupportedLookAndFeelException;

import java.awt.Font;

import java.awt.Color;
import java.awt.GridLayout;

public class ConnectGui {
	private JFrame frame;
	private JPanel contentPane;
	private JTextField nick_T;
	private JTextField ip_T;
	private JTextField port_T;
	
	private String nick = "";
	private String ip = "";
	private int port = 0;
	
	

	public ConnectGui() {
		
		try {
			UIManager.setLookAndFeel(
			    UIManager.getSystemLookAndFeelClassName());
		} catch (ClassNotFoundException | InstantiationException | IllegalAccessException
				| UnsupportedLookAndFeelException e) {

		}
		
		initialize();
	}

	/**
	 * Create the frame.
	 * @return 
	 */
	public void initialize() {
		
		frame = new JFrame();
		frame.setTitle("Client connection");
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		frame.setBounds(100, 100, 345, 310);
		contentPane = new JPanel();
		contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));
		frame.setContentPane(contentPane);
		contentPane.setLayout(new GridLayout(0, 1, 10, 10));
		
		JLabel lblNewLabel_3 = new JLabel("CHATTY");
		lblNewLabel_3.setForeground(Color.RED);
		lblNewLabel_3.setFont(new Font("Tahoma", Font.PLAIN, 30));
		lblNewLabel_3.setHorizontalAlignment(SwingConstants.CENTER);
		contentPane.add(lblNewLabel_3);
		
		JLabel lblNewLabel = new JLabel("NickName : ");
		contentPane.add(lblNewLabel);
		
		nick_T = new JTextField();
		contentPane.add(nick_T);
		nick_T.setColumns(10);
		
		JLabel lblNewLabel_1 = new JLabel("Server IP : ");
		contentPane.add(lblNewLabel_1);
		
		ip_T = new JTextField();
		contentPane.add(ip_T);
		ip_T.setColumns(10);
		
		JLabel lblNewLabel_2 = new JLabel("Server Port : ");
		contentPane.add(lblNewLabel_2);
		
		port_T = new JTextField();
		contentPane.add(port_T);
		port_T.setColumns(10);
		
		JButton connect = new JButton("Connect");
		contentPane.add(connect);
		
		frame.setVisible(true);
		connect.addActionListener(e -> {
			nick = nick_T.getText();
			ip = ip_T.getText();
			port = Integer.parseInt(port_T.getText());
			synchronized (this) {
				notifyAll();
			}
		});
		
	}
	
	public synchronized String getIp() {
		while(true) {
			if(ip != "" ) {
				String temp = ip;
				ip= "";
				return temp;
			}
			else {
				try {
					wait();
				} catch (InterruptedException e) {
					e.printStackTrace();
				}
			}
		}
	}
	
	public synchronized int getPort() {
		while(true) {
			if(port != 0 )
			{
				int temp = port;
				port= 0;
				return temp;
			}
			else {
				try {
					wait();
				} catch (InterruptedException e) {
					e.printStackTrace();
				}
			}
		}
	}
	
	public synchronized String getNick() {
		while(true) {
			if(nick != "" ){
				String temp = nick;
				nick= "";
				return temp;
			}
			else {
				try {
					wait();
				} catch (InterruptedException e) {
					e.printStackTrace();
				}
			}
		}
	}
	
	public void close() {
		frame.setVisible(false);
		frame.dispose();
	}
}
