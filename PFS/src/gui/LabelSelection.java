package gui;

import java.util.ArrayList;

import org.eclipse.swt.widgets.*;
import org.eclipse.swt.*;
import org.eclipse.swt.events.*;

import system.PFSystem;
import util.StringMatch;
import domainobjects.*;
import domainobjects.Label;

public class LabelSelection implements IDialog
{
	protected Shell shell;
	private Text textSearchLabel;
	private Text textSearchPickLabel;
	protected String transferTo = null;
	protected String transferFrom = null;
	protected final int LIST_OPTIONS = SWT.MULTI | SWT.BORDER |SWT.V_SCROLL;

	private List listLabel;
	private List listPickLabel;
	
	private ArrayList<String> labels = new ArrayList<String>(1);
	private ArrayList<String> pickLabels = new ArrayList<String>(1);
	
	private IDSet startingSet = IDSet.empty();
	
	public void setStartingSet(IDSet set)
	{
		startingSet = set;
	}
	
	/**
	 * @wbp.parser.entryPoint
	 */
	public Object open()
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
		
		return getLabels();
	}

	private String[] getLabels()
	{
		String[] labels = new String[pickLabels.size()];
		
		for(int i = 0; i < labels.length; i++)
		{
			labels[i] = pickLabels.get(i);
		}
		
		return labels;
	}
	
	/**
	 * Create contents of the window.
	 */
	protected void createContents() 
	{
		shell = new Shell(SWT.APPLICATION_MODAL);
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
						
		loadList(startingSet);
		
		//listeners
		textSearchLabel.addKeyListener(new KeyAdapter() 
		{
			public void keyPressed(KeyEvent e) 
			{
				String input = textSearchLabel.getText();
				if(!(input.length() ==0 && (e.character == 8 || e.character == 127)))
				{	
					if((e.character >32 && e.character <127) || e.character == 8 || e.character == 127)
					{
						if(e.character >32 && e.character <127)
							input += e.character;
						refreshList();
						
						for(int i=0; i<listLabel.getItemCount(); i++)
						{
							if(!StringMatch.match(listLabel.getItem(i),input))
							{
								listLabel.remove(listLabel.getItem(i));
								i=-1;
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
				String input = textSearchPickLabel.getText();
				if(!(input.length() ==0 && (e.character == 8 || e.character == 127)))
				{
					if((e.character >32 && e.character <127) || e.character == 8 || e.character == 127)
					{
						if(e.character >32 && e.character <127)
							input += e.character;
						refreshPickList();
						
						for(int i=0; i<listPickLabel.getItemCount(); i++)
						{
							if(!StringMatch.match(listPickLabel.getItem(i),input))
							{
								listPickLabel.remove(listPickLabel.getItem(i));
								i=-1;
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
					labels.remove(transferTo);

					pickLabels.add(transferTo);
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
					pickLabels.remove(transferFrom);
					
					labels.add(transferFrom);
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
				LabelCreation createLabel = new LabelCreation();
				String newLabel = createLabel.open();
				if(newLabel !=null)
				{
					Label inLabel = new Label(newLabel);
					int newID = PFSystem.getCurrent().getLabelSystem().create();
					PFSystem.getCurrent().getLabelSystem().update(newID,inLabel);
					labels.add(newLabel);
					listLabel.add(newLabel);
				}
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
	
	public void loadList(IDSet includedID)
	{
		IDSet labelIDs = PFSystem.getCurrent().getLabelSystem().getAllIDs();
				
		for(int i=0; i <labelIDs.getSize(); i++)
		{			
			if(!includedID.contains(labelIDs.getValue(i)))
			{
				final int id = labelIDs.getValue(i);
				Label label = (Label)PFSystem.getCurrent().getLabelSystem().getDataByID(id);
			
				labels.add(label.getLabelName());
			}	
		}

		for(int i = 0; i < includedID.getSize(); i++)
		{
			final int id = labelIDs.getValue(i);
			Label label = (Label)PFSystem.getCurrent().getLabelSystem().getDataByID(id);
			
			pickLabels.add(label.getLabelName());
		}
		
		refreshList();
		refreshPickList();		
	}
	
	private void refreshList()
	{
		listLabel.removeAll();
		for(int i=0; i <labels.size(); i++)
			listLabel.add(labels.get(i));
	}
	
	private void refreshPickList()
	{
		listPickLabel.removeAll();
		for(int i=0; i <pickLabels.size(); i++)
			listPickLabel.add(pickLabels.get(i));
		
	}

}
