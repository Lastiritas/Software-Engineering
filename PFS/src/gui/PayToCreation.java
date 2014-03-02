package gui;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.TreeItem;
import org.eclipse.swt.SWT;
import org.eclipse.swt.widgets.Text;
import org.eclipse.swt.widgets.Tree;
import org.eclipse.swt.widgets.List;
import org.eclipse.swt.events.KeyAdapter;
import org.eclipse.swt.events.KeyEvent;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;

import domainobjects.IDSet;
import domainobjects.PayTo;
import system.PFSystem;
import util.StringMatch;

public class PayToCreation implements IWindow 
{

	protected Shell shell;
	protected List list;
	protected List list_1; 
	private Text nameField;
	private Text locationField;
	private String allName[];
	private String allBranch[];

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
		shell = new Shell(SWT.APPLICATION_MODAL);
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
				String tempName = nameField.getText();
				String tempBranch = locationField.getText();
				boolean existName = false;
				boolean existBranch = false;
				
				for(int i=0; i<allName.length && existName ==false;i++)
				{
					if(allName[i].equalsIgnoreCase(tempName))
					{
						existName = true;
					}
				}
				
				for(int i=0; i<allBranch.length && existBranch ==false;i++)
				{
					if(allBranch[i].equalsIgnoreCase(tempBranch))
					{
						existBranch = true;
					}
				}
				
				if( !existName || !existBranch)
				{
					final int newLabelID = PFSystem.getCurrent().getPayToSystem().create();
					PFSystem.getCurrent().getPayToSystem().update(newLabelID, new PayTo(nameField.getText(), locationField.getText()));
				}
				else
				{
					;//display some sort of error
				}
				shell.close();
			}
		});
		okayButton.setBounds(349, 227, 75, 25);
		okayButton.setText("Okay");
		
		nameField = new Text(shell, SWT.BORDER);
		nameField.addKeyListener(new KeyAdapter()
		{
			public void keyPressed(KeyEvent e)
			{
				String input = nameField.getText();
				if(!(input.length() ==0 && (e.character == 8 || e.character == 127)))
				{
					if((e.character >32 && e.character <127) ||e.character == 8 || e.character == 127)
					{
						if(e.character >32 && e.character <127)
							input += e.character;
						refreshList();
						
						for(int i=0; i<list.getItemCount(); i++)
						{
							if(!StringMatch.match(list.getItem(i),input))
							{
								list.remove(list.getItem(i));
								i=-1;
							}
						}
						
					}
				}
			}
		});
		nameField.setMessage("Name");
		nameField.setBounds(10, 10, 199, 21);
		
		locationField = new Text(shell, SWT.BORDER);
		locationField.addKeyListener(new KeyAdapter()
		{
			public void keyPressed(KeyEvent e)
			{
				String input = locationField.getText();
				if(!(input.length() ==0 && (e.character == 8 || e.character == 127)))
				{
					if((e.character >32 && e.character <127) ||e.character == 8 || e.character == 127)
					{
						if(e.character >32 && e.character <127)
							input += e.character;
						refreshList_1();
						
						for(int i=0; i<list_1.getItemCount(); i++)
						{
							if(!StringMatch.match(list_1.getItem(i),input))
							{
								list_1.remove(list_1.getItem(i));
								i=-1;
							}
						}
						
					}
				}
			}
		});
		locationField.setMessage("Location");
		locationField.setBounds(225, 10, 199, 21);
		
		list = new List(shell, SWT.BORDER);
		list.setBounds(10, 37, 199, 184);
		
		list_1 = new List(shell, SWT.BORDER);
		list_1.setBounds(225, 37, 199, 184);
		
		loadList();

	}
	
	private void loadList()
	{
		IDSet payToIDs = PFSystem.getCurrent().getPayToSystem().getAllIDs();
		
		int length = payToIDs.getSize();
		allName = new String[length];
		allBranch = new String[length];
		
		for(int i=0; i<length; i++)
		{
			final int id = payToIDs.getValue(i);
			final PayTo payto = (PayTo)PFSystem.getCurrent().getPayToSystem().getDataByID(id);
			
			allName[i] = payto.getPayToName();
			allBranch[i] = payto.getPayToBranch();
			
		}
		
		refreshList();
		refreshList_1();
	}
	
	private void refreshList()
	{
		list.removeAll();
		for(int i=0; i< allName.length; i ++)
		{
			list.add(allName[i]);
		}
		
	}
	
	private void refreshList_1()
	{
		list_1.removeAll();
		for(int i=0; i< allBranch.length; i ++)
		{
			list_1.add(allBranch[i]);
		}
	}
}
