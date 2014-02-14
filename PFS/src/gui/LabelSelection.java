package gui;

import org.eclipse.swt.widgets.*;
import org.eclipse.swt.*;
import org.eclipse.swt.events.*;

import system.PFSystem;
import util.StringMatch;
import domainobjects.*;

public class LabelSelection implements IWindow
{
	protected Shell shell;
	private Text textSearchLabel;
	private Text textSearchPickLabel;
	protected String transferTo = null;
	protected String transferFrom = null;
	protected final int LIST_OPTIONS = SWT.MULTI | SWT.BORDER |SWT.V_SCROLL;

	private List listLabel;
	private List listPickLabel;
	
	private String labels[];
		
	/**
	 * @wbp.parser.entryPoint
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
		shell.setSize(590, 465);
		shell.setText("Label Management");
		
		textSearchLabel = new Text(shell, SWT.BORDER);
		textSearchLabel.setBounds(20, 20, 200, 22);
		
		textSearchPickLabel = new Text(shell, SWT.BORDER);
		textSearchPickLabel.setBounds(355, 20, 200, 22);
		
		listLabel = new List(shell, LIST_OPTIONS);
		listLabel.setBounds(20, 50, 200, 260);
		
		listPickLabel = new List(shell, LIST_OPTIONS);
		listPickLabel.setBounds(355, 50, 200, 260);
		
		Button btnAdd = new Button(shell, SWT.PUSH);
		btnAdd.setText(">>>");
		btnAdd.setBounds(250, 100, 75, 25);
		
		Button btnRemove = new Button(shell, SWT.PUSH);
		btnRemove.setText("<<<");
		btnRemove.setBounds(250, 130, 75, 25);
		
		Button btnNew = new Button(shell, SWT.PUSH);
		btnNew.setText("Create Label");
		btnNew.setBounds(65, 316, 110, 25);
		
		Button btnCancel = new Button(shell, SWT.PUSH);
		btnCancel.setText("Cancel");
		btnCancel.setBounds(20, 392, 75, 25);
		
		Button btnDone = new Button(shell, SWT.PUSH);
		btnDone.setText("Done");
		btnDone.setBounds(480,392, 75, 25);
		
		//list population
		IDSet labelIDs = PFSystem.getCurrent().getLabelSystem().getAllIDs();
		labels = new String[labelIDs.getSize()];
		
		for(int i=0; i <labelIDs.getSize(); i++)
		{
			final int id = labelIDs.getValue(i);
			domainobjects.Label label = (domainobjects.Label)PFSystem.getCurrent().getLabelSystem().getDataByID(id);
			
			labels[i] = label.getLabelName();
			listLabel.add(labels[i]);
		}	
				
		
		//listeners
		textSearchLabel.addKeyListener(new KeyAdapter() 
		{
			public void keyPressed(KeyEvent e) 
			{
				String input = textSearchLabel.getText() + e.character;
				if(e.character >32 && e.character <127)
				{
					for(int i=0; i<listLabel.getItemCount(); i++)
					{
						if(!StringMatch.match(listLabel.getItem(i),input))
						{
							listLabel.remove(listLabel.getItem(i));
							i=-1;
						}
					}
					
					
					 boolean found = false;
					 for(int i=0; i<labels.length; i++)
					 {
					 	if(StringMatch.match(labels[i], input)) 
					 	{
					 		for(int j=0; j<listLabel.getItemCount(); j++)
					 		{
					 			if(labels[i].equalsIgnoreCase(listLabel.getItem(j)))
					 			{
					 				found = true;
					 				break;
					 			}
					 		}
					 		
					 		if(!found)
					 		{
					 			listLabel.add(labels[i]);
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
					 		for(int j=0; j<listLabel.getItemCount(); j++)
					 		{
					 			if(labels[i].equalsIgnoreCase(listLabel.getItem(j)))
					 			{
					 				found = true;
					 				break;
					 			}
					 		}
					 		
					 		if(!found)
					 		{
					 			listLabel.add(labels[i]);
					 		}
					 	}
					 }
				}
			}
		});
		
		textSearchPickLabel.addKeyListener(new KeyAdapter() 
		{
			public void keyPressed(KeyEvent e) 
			{
				String input = textSearchPickLabel.getText() + e.character;
				if(e.character >32 && e.character <127)
				{
					int length = listPickLabel.getItemCount();
					for(int i=0; i<length; i++)
					{
						if(!StringMatch.match(listPickLabel.getItem(i),input))
						{
							listPickLabel.remove(listPickLabel.getItem(i));
							i=-1;
							length = listPickLabel.getItemCount();
						}
					}
					
					
					boolean found = false;
					for(int i=0; i<labels.length; i++)
					{
						if(StringMatch.match(labels[i], input)) 
					 	{
					 		for(int j=0; j<listPickLabel.getItemCount(); j++)
					 		{
					 			if(labels[i].equalsIgnoreCase(listPickLabel.getItem(j)))
					 			{
					 				found = true;
					 				break;
					 			}
					 		}
					 		
					 		if(!found)
					 		{
					 			listPickLabel.add(labels[i]);
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
					 		for(int j=0; j<listPickLabel.getItemCount(); j++)
					 		{
					 			if(labels[i].equalsIgnoreCase(listPickLabel.getItem(j)))
					 			{
					 				found = true;
					 				break;
					 			}
					 		}
					 		
					 		if(!found)
					 		{
					 			listPickLabel.add(labels[i]);
					 		}
					 	}
					 }
				}
			}
		});
		
		listLabel.addMouseListener(new MouseAdapter() 
		{
			public void mouseDown(MouseEvent e) 
			{
				if(listLabel.getSelection().length!=0)
				{
					transferTo  = listLabel.getSelection()[0];
				}
			}
		});
		
		listPickLabel.addMouseListener(new MouseAdapter() 
		{
			public void mouseDown(MouseEvent e) 
			{
				if(listPickLabel.getSelection().length!=0)
				{
					transferFrom  = listPickLabel.getSelection()[0];
				}
			}
		});
		
		btnAdd.addSelectionListener(new SelectionAdapter() 
		{
			@Override
			public void widgetSelected(SelectionEvent e) 
			{
				if(transferTo !=null)
				{
					listLabel.remove(transferTo);
					//need to add to list of labels here
					listPickLabel.add(transferTo);
					transferTo =null;
				}
			}
		});
		
		btnRemove.addSelectionListener(new SelectionAdapter() 
		{
			@Override
			public void widgetSelected(SelectionEvent e) 
			{
				if(transferFrom !=null)
				{
					listPickLabel.remove(transferFrom);
					//remove from list of labels here
					listLabel.add(transferFrom);
					transferFrom =null;
				}
			}
		});
		
		btnNew.addSelectionListener(new SelectionAdapter() 
		{
			@Override
			public void widgetSelected(SelectionEvent e) 
			{
				IWindow newLabel = new LabelCreation();
				newLabel.open();
			}
		});
		
		btnCancel.addSelectionListener( new SelectionAdapter() 
		{
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
