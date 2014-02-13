package gui;

import org.eclipse.swt.*;
import org.eclipse.swt.widgets.*;
import org.eclipse.swt.events.*;

import domainobjects.*;
import dataAccessLayer.*;

public class LabelCreate {
	protected Shell shell;
	private Text textNewLabel;


	/**
	 * Open the window.
	 * 
	 */

	public void open()
	{
		Display display =Display.getDefault();
		
		createContents();
		shell.open();
		shell.layout();
		while (!shell.isDisposed()) {
			if (!display.readAndDispatch()) {
				display.sleep();
			}
		}
	}
	
	protected void createContents(){
		shell = new Shell(SWT.APPLICATION_MODAL);
		shell.setSize(320, 300);
		shell.setText("Label Creation");
		
		textNewLabel = new Text(shell, SWT.BORDER);
		textNewLabel.setBounds(10, 10, 300, 21);
		
		Button btnCancel = new Button(shell, SWT.NONE);
		btnCancel.setBounds(10, 263, 75, 25);
		btnCancel.setText("Cancel");
		
		Button btnDone = new Button(shell, SWT.NONE);
		btnDone.setBounds(235, 263, 75, 25);
		btnDone.setText("Done");
		
		final List listExsistingLabel = new List(shell, SWT.BORDER);
		listExsistingLabel.setBounds(10, 37, 298, 186);
		
		//list population
		StubDatabase testDB = new StubDatabase();
		int labelID[] = testDB.getAllLabelIDs();
		String labels[] = new String[labelID.length];
		
		for(int i=0; i <labelID.length; i++)
		{
			labels[i] = labelID[i].getLabelName();
			listLabel.add(labels[i]);
		}	
		
		
		//listeners
		textNewLabel.addKeyListener(new KeyAdapter() {
			public void keyPressed(KeyEvent e) {
				if(e.character >32 && e.character <127)
				{
					String input = textNewLabel.getText() + e.character;
					for(int i=0; i<listExsistingLabel.getItemCount(); i++)
					{
						if(!StringMatch.match(listExsistingLabel.getItem(i),input))
						{
							listExsistingLabel.remove(listExsistingLabel.getItem(i));
							i=-1;
						}
					}
					
					
					 boolean found = false;
					 for(int i=0; i<labels.length; i++)
					 {
					 	if(StringMatch.match(labels[i], input)) 
					 	{
					 		for(int j=0; j<listExsistingLabel.getItemCount(); j++)
					 		{
					 			if(label[i].equalsIgnoreCase(listExsistingLabel.getItem(j)))
					 			{
					 				found = true;
					 				break;
					 			}
					 		}
					 		if(!found)
					 			listExsistingLabel.add(labels[i]);
					 	}
					 }
					 
					
				}
				else if(e.character == 8 || e.character == 127)
				{
					boolean found = false;
					 for(int i=0; i<labels.length; i++)
					 {
					 	if(StringMatch.match(labels[i], input)) 
					 	{
					 		for(int j=0; j<listExsistingLabel.getItemCount(); j++)
					 		{
					 			if(label[i].equalsIgnoreCase(listExsistingLabel.getItem(j)))
					 			{
					 				found = true;
					 				break;
					 			}
					 		}
					 		if(!found)
					 			listExsistingLabel.add(labels[i]);
					 	}
					 }
				}
			}
		});

		btnCancel.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent e) {
				shell.dispose();
			}
		});
		
		btnDone.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent e) {
				shell.dispose();
			}
		});
		
	}
}
