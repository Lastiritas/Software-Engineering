package gui;

import javax.swing.JFrame;

import java.awt.BorderLayout;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
/**
 * @author Josh
 *
 */
public class LabelMgmtWindow extends JFrame 
{
	public LabelMgmtWindow() 
	{
		setTitle("Label Management");
		setSize(590, 465);
		
		addWindowListener(new WindowAdapter() {
			public void windowClosing(WindowEvent inEvent) { onWindowClose(); }
		});
		
		LabelMgmt swtApp = new LabelMgmt();
		swtApp.open();
	
	}
	
	private void onWindowClose()
	{
		System.exit(0);
	}
	
	public void show()
	{
		LabelMgmtWindow window = new LabelMgmtWindow();
		window.show();
	}
}
