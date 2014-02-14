package gui;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.SWT;
import org.eclipse.swt.widgets.Text;
import org.eclipse.swt.widgets.Tree;
import org.eclipse.swt.widgets.List;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;

import domainobjects.PayTo;
import system.PFSystem;

public class PayToCreation implements IWindow 
{

	protected Shell shell;
	private Text nameField;
	private Text locationField;

	/**
	 * Open the window.
	 */
	public void open() 
	{
		Display display = Display.getDefault();
	
		createContents();
		shell.open();
		shell.layout();
		
		while (!shell.isDisposed()) 
		{
			if (!display.readAndDispatch()) 
			{
				display.sleep();
			}
		}
	}

	/**
	 * Create contents of the window.
	 */
	protected void createContents() 
	{
		shell = new Shell();
		shell.setSize(450, 300);
		shell.setText("PayTo Creation");
		
		Button cancelButton = new Button(shell, SWT.NONE);
		cancelButton.addSelectionListener(new SelectionAdapter() 
		{
			@Override
			public void widgetSelected(SelectionEvent arg0) 
			{
				shell.close();
			}
		});
		cancelButton.setBounds(10, 227, 75, 25);
		cancelButton.setText("Cancel");
		
		Button okayButton = new Button(shell, SWT.NONE);
		okayButton.addSelectionListener(new SelectionAdapter() 
		{
			@Override
			public void widgetSelected(SelectionEvent arg0) 
			{
				final int newLabelID = PFSystem.getCurrent().getPayToSystem().create();
				PFSystem.getCurrent().getPayToSystem().update(newLabelID, new PayTo(nameField.getText(), locationField.getText()));
				
				shell.close();
			}
		});
		okayButton.setBounds(349, 227, 75, 25);
		okayButton.setText("Okay");
		
		nameField = new Text(shell, SWT.BORDER);
		nameField.setMessage("Name");
		nameField.setBounds(10, 10, 199, 21);
		
		locationField = new Text(shell, SWT.BORDER);
		locationField.setMessage("Location");
		locationField.setBounds(225, 10, 199, 21);
		
		List list = new List(shell, SWT.BORDER);
		list.setBounds(10, 37, 199, 184);
		
		List list_1 = new List(shell, SWT.BORDER);
		list_1.setBounds(225, 37, 199, 184);

	}
}
