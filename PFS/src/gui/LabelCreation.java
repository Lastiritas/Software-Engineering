package gui;

import org.eclipse.swt.*;
import org.eclipse.swt.widgets.*;
import org.eclipse.swt.events.*;

import system.PFSystem;
import util.StringMatch;
import domainobjects.*;

public class LabelCreation implements IDialog
{
	protected Shell shell;
	protected List listExsistingLabel;
	private Text textNewLabel;

	private String allLabel[];
	private String retLabel=null;
	
	/**
	 * Open the window.
	 * 
	 */

	public String open()
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
		return retLabel;
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
		
		listExsistingLabel = new List(shell, SWT.BORDER);
		listExsistingLabel.setBounds(10, 37, 298, 186);
		
		//list population
		loadList();
		
		
		//listeners
		textNewLabel.addKeyListener(new KeyAdapter() 
		{
			public void keyPressed(KeyEvent e)
			{
				String input = textNewLabel.getText();
				if(!(input.length() ==0 && (e.character == 8 || e.character == 127)))
				{
					if((e.character >32 && e.character <127) ||e.character == 8 || e.character == 127)
					{
						if(e.character >32 && e.character <127)
							input += e.character;
						refreshList();
						
						for(int i=0; i<listExsistingLabel.getItemCount(); i++)
						{
							if(!StringMatch.match(listExsistingLabel.getItem(i),input))
							{
								listExsistingLabel.remove(listExsistingLabel.getItem(i));
								i=-1;
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
				String temp = textNewLabel.getText();
				boolean exsist = false;
				
				for(int i=0; i<allLabel.length; i++)
				{
					if(allLabel[i].equalsIgnoreCase(temp))
					{
						exsist=true;
						break;
					}
				}
				
				if(!exsist)
					retLabel=temp;
				else
					;//notify user of exsisting choice
				
				/* AARON CODE
				final String labelString = textNewLabel.getText(); 
				
				IDataModifer dataModifer = PFSystem.getCurrent().getLabelSystem();
				
				final int labelID = dataModifer.create();
				dataModifer.update(labelID, new domainobjects.Label(labelString));
				
				 */
				
				shell.close();
			}
		});
		
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
		
	}
	
	private void loadList()
	{
		IDSet labelIDs = PFSystem.getCurrent().getLabelSystem().getAllIDs();
		allLabel = new String[labelIDs.getSize()];
		
		for(int i=0; i <labelIDs.getSize(); i++)
		{
			final int id = labelIDs.getValue(i);
			domainobjects.Label label = (domainobjects.Label)PFSystem.getCurrent().getLabelSystem().getDataByID(id);
			
			allLabel[i] = label.getLabelName();
		}	
		
		refreshList();
	}
	
	private void refreshList()
	{
		listExsistingLabel.removeAll();
		for(int i=0; i <allLabel.length; i++)
			listExsistingLabel.add(allLabel[i]);
	}
}
