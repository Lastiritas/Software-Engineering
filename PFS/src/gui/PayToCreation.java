package gui;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.SWT;
import org.eclipse.swt.widgets.Text;
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

import acceptanceTests.EventLoop;
import acceptanceTests.Register;

public class PayToCreation implements IWindow 
{

	protected Shell shell;
	protected List list;
	private Text nameField;
	private String allName[];
	private Composite composite;
	private Button okayButton;
	
	/**
	 * Open the window.
	 * @wbp.parser.entryPoint
	 */
	public void open() 
	{
		Display display = Display.getDefault();
		Register.newWindow(this);
		createContents();
		shell.open();
		shell.layout();
		
		if(EventLoop.isEnabled())
		{
			while (!shell.isDisposed()) 
			{
				if (!display.readAndDispatch()) 
				{
					display.sleep();
				}
			}
		}
	}

	/**
	 * Create contents of the window.
	 */
	protected void createContents() 
	{
		shell = new Shell(SWT.SYSTEM_MODAL | SWT.DIALOG_TRIM);
		shell.setSize(444, 327);
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
		
		okayButton = new Button(composite, SWT.NONE);
		okayButton.setBounds(381, 260, 53, 35);
		okayButton.addSelectionListener(new SelectionAdapter() 
		{
			@Override
			public void widgetSelected(SelectionEvent arg0) 
			{
				String tempName = nameField.getText();
				boolean existName = false;
				
				for(int i = 0; i < allName.length && !existName; i++)
				{
					if(allName[i].equalsIgnoreCase(tempName))
					{
						existName = true;
					}
				}
				
				if(!existName)
				{
					final int newLabelID = PFSystem.getCurrent().getPayToSystem().create();
					PFSystem.getCurrent().getPayToSystem().update(newLabelID, new PayTo(nameField.getText()));
				}

				shell.close();
			}
		});
		okayButton.setText("Okay");
		
		nameField = new Text(composite, SWT.BORDER);
		nameField.setBounds(10, 10, 424, 31);
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
		
		list = new List(composite, SWT.BORDER);
		list.setBounds(10, 47, 424, 207);
		
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
	}
}
