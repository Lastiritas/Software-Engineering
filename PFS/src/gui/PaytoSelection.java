package gui;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.swt.SWT;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;

import system.PFSystem;
import domainobjects.IDSet;

import org.eclipse.swt.widgets.Event;
import org.eclipse.swt.widgets.Listener;
import org.eclipse.swt.widgets.Table;
import org.eclipse.swt.widgets.TableColumn;
import org.eclipse.swt.widgets.Composite;


public class PaytoSelection implements IDialog
{
	private Shell shell;
	private Table table;
	private int selectedID = -1;
	
	/**
	 * Open the window.
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
		
		return selectedID;
	}

	/**
	 * Create contents of the window.
	 * @wbp.parser.entryPoint
	 */
	protected void createContents() 
	{
		
		//***Place contents inside a panel***//
		shell = new Shell(SWT.SYSTEM_MODAL | SWT.DIALOG_TRIM);
		shell.setSize(487, 359);
		shell.setText("PayTo Manager");
		shell.addListener(SWT.Close, new Listener()
		{
			@Override
			public void handleEvent(Event event)
			{
				selectedID = -1;
			}
		});
		
		Composite composite = new Composite(shell, SWT.NONE);
		composite.setBounds(0, 0, 481, 319);
		
		table = new Table(composite, SWT.BORDER | SWT.FULL_SELECTION);
		table.setBounds(10, 10, 461, 258);
		table.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent arg0) {
				final int selected = table.getSelectionIndex();
				
				if(selected != -1)
				{
					selectedID = Integer.parseInt(table.getItem(selected).getText(0));
				}
				else
				{
					selectedID = -1;
				}
			}
		});
		table.setHeaderVisible(true);
		table.setLinesVisible(true);
		
		TableColumn tblclmnId = new TableColumn(table, SWT.NONE);
		tblclmnId.setWidth(100);
		tblclmnId.setText("ID");
		
		TableColumn tblclmnName = new TableColumn(table, SWT.NONE);
		tblclmnName.setWidth(100);
		tblclmnName.setText("Name");
		
		TableColumn tblclmnLocation = new TableColumn(table, SWT.NONE);
		tblclmnLocation.setWidth(100);
		tblclmnLocation.setText("Location");
		
			populateList(table);
		
		Button cancelButton = new Button(composite, SWT.NONE);
		cancelButton.setBounds(10, 274, 63, 35);
		cancelButton.addSelectionListener(new SelectionAdapter() 
		{
			@Override
			public void widgetSelected(SelectionEvent e) 
			{
				selectedID = -1;
				shell.close();
			}
		});
		cancelButton.setText("Cancel");
		
		Button okayButton = new Button(composite, SWT.NONE);
		okayButton.setBounds(418, 274, 53, 35);
		okayButton.addSelectionListener(new SelectionAdapter() 
		{
			@Override
			public void widgetSelected(SelectionEvent arg0) 
			{
				shell.close();
			}
		});
		okayButton.setText("Okay");
		
		Button addButton = new Button(composite, SWT.NONE);
		addButton.setBounds(203, 274, 63, 35);
		addButton.addSelectionListener(new SelectionAdapter() 
		{
			@Override
			public void widgetSelected(SelectionEvent arg0) 
			{
				IWindow window = new PayToCreation();
				window.open();
				populateList(table);
			}
		});
		addButton.setText("+");
	}
	
	private void populateList(Table table)
	{
		final IDSet payToIDs = PFSystem.getCurrent().getPayToSystem().getAllIDs();
				
		table.removeAll();
		
		GUIHelper.addPayTosToTable(table, payToIDs);
	}
}
