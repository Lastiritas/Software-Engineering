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
import org.eclipse.swt.widgets.Composite;

public class PayToCreation implements IWindow 
{

	protected Shell shell;
	protected List list;
	protected List list_1; 
	private Text nameField;
	private Text locationField;
	private String allName[];
	private Composite composite;

	/**
	 * Open the window.
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
		shell = new Shell(SWT.SYSTEM_MODAL | SWT.DIALOG_TRIM);
		shell.setSize(450, 345);
		shell.setText("PayTo Creation");
		
		composite = new Composite(shell, SWT.NONE);
		composite.setBounds(0, 0, 444, 305);
		
		Button cancelButton = new Button(composite, SWT.NONE);
		cancelButton.setBounds(10, 260, 63, 35);
		cancelButton.addSelectionListener(new SelectionAdapter() 
		{
			@Override
			public void widgetSelected(SelectionEvent arg0) 
			{
				shell.close();
			}
		});
		cancelButton.setText("Cancel");
		
		Button okayButton = new Button(composite, SWT.NONE);
		okayButton.setBounds(381, 260, 53, 35);
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
				
				if( !existName || !existBranch)
				{
					final int newLabelID = PFSystem.getCurrent().getPayToSystem().create();
					PFSystem.getCurrent().getPayToSystem().update(newLabelID, new PayTo(nameField.getText()));
				}
				else
				{
					;//display some sort of error
				}
				shell.close();
			}
		});
		okayButton.setText("Okay");
		
		nameField = new Text(composite, SWT.BORDER);
		nameField.setBounds(10, 10, 199, 31);
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
		
		locationField = new Text(composite, SWT.BORDER);
		locationField.setBounds(235, 10, 199, 31);
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
		
		list = new List(composite, SWT.BORDER);
		list.setBounds(10, 47, 199, 207);
		
		list_1 = new List(composite, SWT.BORDER);
		list_1.setBounds(235, 47, 199, 207);
		
		loadList();

	}
	
	private void loadList()
	{
		IDSet payToIDs = PFSystem.getCurrent().getPayToSystem().getAllIDs();
		
		int length = payToIDs.getSize();
		allName = new String[length];
		
		for(int i=0; i<length; i++)
		{
			final int id = payToIDs.getValue(i);
			final PayTo payto = (PayTo)PFSystem.getCurrent().getPayToSystem().getDataByID(id);
			
			allName[i] = payto.getName();
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
	}
}
