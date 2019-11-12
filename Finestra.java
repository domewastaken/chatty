package servers;

import java.awt.ComponentOrientation;
import java.awt.FlowLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.JTextField;
import javax.swing.WindowConstants;
import javax.swing.SwingConstants;

public class Finestra {
	
	private JFrame frame;
	private JPanel pane;
	public Finestra() { 
		
		frame=new JFrame("try");
		frame.setBounds(300, 300, 211, 85);
		pane= (JPanel) frame.getContentPane();
		
		frame.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
		pane.setComponentOrientation(ComponentOrientation.LEFT_TO_RIGHT);
		FlowLayout flowLayout = new FlowLayout(FlowLayout.CENTER, 10, 10);
		frame.getContentPane().setLayout(flowLayout);
		
		JTextField field = new JTextField(10);
		
		field.setHorizontalAlignment(SwingConstants.CENTER);
		field.setToolTipText("enter username");
		JButton button = new JButton("login");
		button.addActionListener(new ActionListener() 
			{ 

			@Override
			public void actionPerformed(ActionEvent e) {
				
				System.out.println("ciao");
			}
			
			}
		);
			
	
		pane.add(field);
		pane.add(button);
		frame.setVisible(true);
		
	} 
	public static void main(String[] args) {
		Finestra c= new Finestra();

	}

}4
