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
	private Composite composite;
	
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
	
	
	/**
	 * Create contents of the window.
	 * @wbp.parser.entryPoint
	 */
	protected void createContents()
	{
		shell = new Shell(SWT.SYSTEM_MODAL | SWT.DIALOG_TRIM);
		shell.setSize(316, 328);
		shell.setText("Label Creation");
		
		composite = new Composite(shell, SWT.NONE);
		composite.setBounds(0, 0, 310, 288);
		
		textNewLabel = new Text(composite, SWT.BORDER);
		textNewLabel.setBounds(10, 10, 290, 31);
		
		Button btnCancel = new Button(composite, SWT.NONE);
		btnCancel.setBounds(10, 253, 75, 25);
		btnCancel.setText("Cancel");
		
		Button btnDone = new Button(composite, SWT.NONE);
		btnDone.setBounds(225, 253, 75, 25);
		btnDone.setText("Done");
		
		listExsistingLabel = new List(composite, SWT.BORDER);
		listExsistingLabel.setBounds(10, 47, 290, 200);
		
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
		
				btnCancel.addSelectionListener(new SelectionAdapter() 
				{
					@Override
					public void widgetSelected(SelectionEvent e)
					{
						shell.close();
					}
				});
		
		
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
		
		//list population
		loadList();
		
	}
	
	private void loadList()
	{
		IDSet labelIDs = PFSystem.getCurrent().getLabelSystem().getAllIDs();
		allLabel = new String[labelIDs.getSize()];
		
		for(int i=0; i <labelIDs.getSize(); i++)
		{
			final int id = labelIDs.getValue(i);
			domainobjects.Label label = (domainobjects.Label)PFSystem.getCurrent().getLabelSystem().getDataByID(id);
			
			allLabel[i] = label.getName();
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
