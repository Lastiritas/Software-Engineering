package gui;

import javax.swing.JFrame;
import java.awt.BorderLayout;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;

public class ExpenseViewWindow extends JFrame 
{
	public ExpenseViewWindow() 
	{
		setTitle("Personal Finance System");
		setSize(800, 600);
		
		addWindowListener(new WindowAdapter() {
			public void windowClosing(WindowEvent inEvent) { onWindowClose(); }
		});
		
		ExpensePanel panel = new ExpensePanel();
		getContentPane().add(panel, BorderLayout.CENTER);
	}
	
	private void onWindowClose()
	{
		System.exit(0);
	}
	
	public static void main(String[] args)
	{
		ExpenseViewWindow window = new ExpenseViewWindow();
		window.show();
	}
}
