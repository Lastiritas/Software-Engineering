package gui;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.swt.SWT;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.events.ModifyEvent;
import org.eclipse.swt.events.ModifyListener;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;

import system.PFSystem;
import util.StringMatch;
import domainobjects.IDSet;

import org.eclipse.swt.widgets.Event;
import org.eclipse.swt.widgets.Listener;
import org.eclipse.swt.widgets.Table;
import org.eclipse.swt.widgets.TableColumn;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Text;


public class PaytoSelection implements IDialog
{
	private Shell shell;
	private Text textSearchPayTo;
	private Table table;
	private int selectedID = 1;
	private boolean madeSelection = false;
	
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
				if(!madeSelection)
				{
					selectedID = 1;
				}
			}
		});
		
		Composite composite = new Composite(shell, SWT.NONE);
		composite.setBounds(0, 0, 481, 319);
		
		textSearchPayTo = new Text(composite, SWT.BORDER);
		textSearchPayTo.setBounds(10, 10, 461, 21);
		
		table = new Table(composite, SWT.BORDER | SWT.FULL_SELECTION);
		table.setBounds(10, 37, 461, 231);
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
					selectedID = 1;
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
		
		Button cancelButton = new Button(composite, SWT.NONE);
		cancelButton.setBounds(10, 274, 63, 35);
		cancelButton.addSelectionListener(new SelectionAdapter() 
		{
			@Override
			public void widgetSelected(SelectionEvent e) 
			{
				selectedID = -1;
				madeSelection = true;
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
				madeSelection = true;
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
				refreshList();
			}
		});
		addButton.setText("+");
		
		textSearchPayTo.addModifyListener(new ModifyListener() {
			public void modifyText(ModifyEvent arg0) {
				refreshList();
				
				final String text = textSearchPayTo.getText();
				if(text.length() > 0)
				{
					filterTable(table, text);
				}
			}
		});
		
		refreshList();
	}
	
	private static void filterTable(Table inTable, String inFilterText)
	{					
		assert(inTable != null);
		assert(inFilterText != null);
		assert(inFilterText.length() > 0);
		
		final int originalTableSize = inTable.getItemCount();
			
		for(int i = originalTableSize - 1; i >= 0; i--)
		{
			final String payTo = inTable.getItem(i).getText(1);
					
			if(!StringMatch.match(payTo, inFilterText))
			{
				inTable.remove(i);	
			}
		}
	}
	
	private void refreshList()
	{
		final IDSet payToIDs = PFSystem.getCurrent().getPayToSystem().getAllIDs();
		
		table.removeAll();
		
		GUIHelper.addPayTosToTable(table, payToIDs);
	}
}
