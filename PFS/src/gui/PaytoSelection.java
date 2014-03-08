package gui;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.SWT;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.widgets.Combo;
import org.eclipse.swt.widgets.List;
import org.eclipse.swt.widgets.TableItem;
import org.eclipse.swt.widgets.Tree;
import org.eclipse.swt.widgets.TreeColumn;
import org.eclipse.swt.widgets.TreeItem;

import system.PFSystem;
import domainobjects.IDSet;
import domainobjects.PayTo;

import org.eclipse.swt.widgets.Table;
import org.eclipse.swt.widgets.TableColumn;


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
		shell.setSize(450, 300);
		shell.setText("PayTo Manager");
		
		table = new Table(shell, SWT.BORDER | SWT.FULL_SELECTION);
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
		
		table.setBounds(10, 10, 430, 249);
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
		
		Button cancelButton = new Button(shell, SWT.NONE);
		cancelButton.addSelectionListener(new SelectionAdapter() 
		{
			@Override
			public void widgetSelected(SelectionEvent e) 
			{
				selectedID = -1;
				shell.close();
			}
		});
		cancelButton.setBounds(10, 265, 75, 25);
		cancelButton.setText("Cancel");
		
		Button okayButton = new Button(shell, SWT.NONE);
		okayButton.addSelectionListener(new SelectionAdapter() 
		{
			@Override
			public void widgetSelected(SelectionEvent arg0) 
			{
				shell.close();
			}
		});
		okayButton.setBounds(365, 265, 75, 25);
		okayButton.setText("Okay");
		
		Button addButton = new Button(shell, SWT.NONE);
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
		addButton.setBounds(180, 265, 75, 25);
		addButton.setText("+");
	
		populateList(table);
	}
	
	private void populateList(Table table)
	{
		final IDSet payToIDs = PFSystem.getCurrent().getPayToSystem().getAllIDs();
				
		table.removeAll();
		
		GUIHelper.addPayTosToTable(table, payToIDs);
	}
}
