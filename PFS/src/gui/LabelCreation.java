package gui;

import org.eclipse.swt.*;
import org.eclipse.swt.widgets.*;
import org.eclipse.swt.events.*;

import system.PFSystem;
import util.StringMatch;
import domainobjects.*;
import dataAccessLayer.*;

public class LabelCreation 
{
	protected Shell shell;
	private Text textNewLabel;

	private String labels[];
	
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
		
		while (!shell.isDisposed()) 
		{
			if (!display.readAndDispatch()) 
			{
				display.sleep();
			}
		}
	}
	
	protected void createContents()
	{
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
		listExsistingLabel.addSelectionListener(new SelectionAdapter() 
		{
			@Override
			public void widgetSelected(SelectionEvent arg0) 
			{
				if(listExsistingLabel.getSelectionCount() == 0)
				{
					return;
				}
				
				textNewLabel.setText(listExsistingLabel.getSelection()[0]); 
			}
		});
		listExsistingLabel.setBounds(10, 37, 298, 186);
		
		//list population
		IDSet labelIDs = PFSystem.getCurrent().getLabelSystem().getAllIDs();
		labels = new String[labelIDs.getSize()];
		
		for(int i=0; i <labelIDs.getSize(); i++)
		{
			final int id = labelIDs.getValue(i);
			domainobjects.Label label = (domainobjects.Label)PFSystem.getCurrent().getLabelSystem().getDataByID(id);
			
			labels[i] = label.getLabelName();
			//listLabels.add(labels[i]);
		}	
		
		
		//listeners
		textNewLabel.addKeyListener(new KeyAdapter() 
		{
			public void keyPressed(KeyEvent e) 
			{
				String input = textNewLabel.getText() + e.character;
				
				if(e.character >32 && e.character <127)
				{
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
					 			if(labels[i].equalsIgnoreCase(listExsistingLabel.getItem(j)))
					 			{
					 				found = true;
					 				break;
					 			}
					 		}
					 	
					 		if(!found)
					 		{
					 			listExsistingLabel.add(labels[i]);
					 		}
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
					 			if(labels[i].equalsIgnoreCase(listExsistingLabel.getItem(j)))
					 			{
					 				found = true;
					 				break;
					 			}
					 		}
					 		
					 		if(!found)
					 		{
					 			listExsistingLabel.add(labels[i]);
					 		}
					 	}
					 }
				}
			}
		});

		btnCancel.addSelectionListener(new SelectionAdapter() 
		{
			@Override
			public void widgetSelected(SelectionEvent e) 
			{
				shell.close();
			}
		});
		
		btnDone.addSelectionListener(new SelectionAdapter() 
		{
			@Override
			public void widgetSelected(SelectionEvent e) 
			{
				shell.close();
			}
		});
	}
}
