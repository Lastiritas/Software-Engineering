package gui;

import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.SWT;
import org.eclipse.swt.widgets.TableColumn;
import org.eclipse.swt.widgets.TableItem;
import org.eclipse.swt.widgets.Table;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.graphics.Point;
import org.eclipse.swt.widgets.Menu;
import org.eclipse.swt.widgets.MenuItem;

import domainobjects.Expense;
import domainobjects.ExpenseFilter;
import domainobjects.IDSet;
import domainobjects.Money;
import domainobjects.PayTo;
import domainobjects.SimpleDate;
import system.ExpenseManagement;
import system.ManagementBase;
import system.PFSystem;
import util.Sort;
import util.SortDirection;
import util.TableCols;

import org.eclipse.swt.widgets.Composite;


public class ViewExpense implements IWindow 
{
	private Shell shlExpenseView;
	private Table expenseTable;
	
	protected int currID;
	
	private int sCountID=1;
	private int sCountDate =0;
	private int sCountMoney=0;
	private int sCountPay=0;
	private int sCountDesc=0;
	
	/**
	 * Open the window.
	 */
	public void open() 
	{
		Display display = Display.getDefault();
	
		createContents();
		shlExpenseView.open();
		shlExpenseView.layout();
		
		while (!shlExpenseView.isDisposed()) 
		{
			if (!display.readAndDispatch()) 
			{
				display.sleep();
			}
		}
		PFSystem.getCurrent().closePFSystem();
	}

	/**
	 * Create contents of the window.
	 * @wbp.parser.entryPoint
	 */
	protected void createContents() 
	{
		shlExpenseView = new Shell();
		shlExpenseView.setMinimumSize(new Point(800, 600));
		shlExpenseView.setSize(905, 737);
		shlExpenseView.setText("Expense View");
		
		Composite composite = new Composite(shlExpenseView, SWT.NONE);
		composite.setBounds(0, 0, 883, 651);
		
		expenseTable = new Table(composite, SWT.BORDER | SWT.FULL_SELECTION);
		expenseTable.setBounds(10, 10, 863, 597);
		expenseTable.setHeaderVisible(true);
		expenseTable.setLinesVisible(true);
		
		TableColumn tblclmnId = new TableColumn(expenseTable, SWT.NONE);
		tblclmnId.setWidth(50);
		tblclmnId.setText("ID");
		
		TableColumn tblclmnDate = new TableColumn(expenseTable, SWT.NONE);
		tblclmnDate.setWidth(125);
		tblclmnDate.setText("Date");
		
		TableColumn tblclmnPayTo = new TableColumn(expenseTable, SWT.NONE);
		tblclmnPayTo.setWidth(250);
		tblclmnPayTo.setText("Pay To");
		
		TableColumn tblclmnAmount = new TableColumn(expenseTable, SWT.NONE);
		tblclmnAmount.setWidth(100);
		tblclmnAmount.setText("Amount");
		
		TableColumn tblclmnDescription = new TableColumn(expenseTable, SWT.NONE);
		tblclmnDescription.setWidth(250);
		tblclmnDescription.setText("Description");
		
		Button addButton = new Button(composite, SWT.NONE);
		addButton.setBounds(210, 613, 94, 28);
		addButton.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent arg0) {
				newExpense();
			}
		});
		addButton.setText("+");
		
		Button duplicateButton = new Button(composite, SWT.NONE);
		duplicateButton.setBounds(110, 613, 94, 28);
		duplicateButton.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent arg0) {
				duplicateSelectedExpense();
			}
		});
		duplicateButton.setText("Duplicate");
		
		Button deleteButton = new Button(composite, SWT.NONE);
		deleteButton.setBounds(10, 613, 94, 28);
		deleteButton.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent arg0) {
				removeSelectedExpense();
			}
		});
		deleteButton.setText("-");
		
		Button graphButton = new Button(composite, SWT.NONE);
		graphButton.setBounds(679, 613, 94, 28);
		graphButton.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent arg0) {
				PerDayWindow window = new PerDayWindow();
				window.open();
			}
		});
		graphButton.setText("Graph");
		
		Button btnOpen = new Button(composite, SWT.NONE);
		btnOpen.setBounds(779, 613, 94, 28);
		btnOpen.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent arg0) {
				openSelectedExpense();
			}
		});
		btnOpen.setText("Open");
		
		expenseTable.getColumn(TableCols.ID.ordinal()).addSelectionListener(new SelectionAdapter()
		{
			@Override
			public void widgetSelected(SelectionEvent arg0)
			{
				if(expenseTable.getItemCount() <2)
				{
					return;
				}
				
				int sortedIDs[] = null;
				if(sCountID == 0)
				{
					sortedIDs = Sort.sortCollection(TableCols.ID, SortDirection.ASCENDING, expenseTable.getItems());
					sCountID=1;
					sCountDate =0;
					sCountMoney=0;
					sCountPay=0;
					sCountDesc=0;
				}
				else if(sCountID ==1)
				{
					sortedIDs = Sort.sortCollection(TableCols.ID, SortDirection.DESCENDING, expenseTable.getItems());
					sCountID=0;
				}
				refreshETable(sortedIDs);
			}
		});
		expenseTable.getColumn(TableCols.DATE.ordinal()).addSelectionListener(new SelectionAdapter()
		{
			@Override
			public void widgetSelected(SelectionEvent arg0)
			{
				if(expenseTable.getItemCount() <2)
				{
					return;
				}
				
				int sortedIDs[] = null;
				if(sCountDate == 0)
				{
					sortedIDs = Sort.sortCollection(TableCols.DATE, SortDirection.ASCENDING, expenseTable.getItems());
					sCountDate =1;
					sCountMoney=0;
					sCountID=0;
					sCountPay=0;
					sCountDesc=0;
				}
				else if(sCountDate ==1)
				{
					sortedIDs = Sort.sortCollection(TableCols.DATE, SortDirection.DESCENDING, expenseTable.getItems());
					sCountDate=0;
				}
				refreshETable(sortedIDs);
			}
		});
		expenseTable.getColumn(TableCols.PAYTO.ordinal()).addSelectionListener(new SelectionAdapter()
		{
			@Override
			public void widgetSelected(SelectionEvent arg0)
			{
				if(expenseTable.getItemCount() <2)
				{
					return;
				}
				
				int sortedIDs[] = null;
				if(sCountPay == 0)
				{
					sortedIDs = Sort.sortCollection(TableCols.PAYTO, SortDirection.ASCENDING, expenseTable.getItems());
					sCountPay=1;
					sCountDate =0;
					sCountMoney=0;
					sCountID=0;
					sCountDesc=0;
				}
				else if(sCountPay ==1)
				{
					sortedIDs = Sort.sortCollection(TableCols.PAYTO, SortDirection.DESCENDING, expenseTable.getItems());
					sCountPay=0;
				}
				refreshETable(sortedIDs);
			}
		});
		expenseTable.getColumn(TableCols.MONEY.ordinal()).addSelectionListener(new SelectionAdapter()
		{
			@Override
			public void widgetSelected(SelectionEvent arg0)
			{
				if(expenseTable.getItemCount() <2)
				{
					return;
				}
				
				int sortedIDs[] = null;
				if(sCountMoney == 0)
				{
					sortedIDs = Sort.sortCollection(TableCols.MONEY, SortDirection.ASCENDING, expenseTable.getItems());
					sCountMoney =1;
					sCountDate = 0;
					sCountID=0;
					sCountPay=0;
					sCountDesc=0;
				}
				else if(sCountMoney ==1)
				{
					sortedIDs = Sort.sortCollection(TableCols.MONEY, SortDirection.DESCENDING, expenseTable.getItems());
					sCountMoney=0;
				}
				refreshETable(sortedIDs);
			}
		});
		expenseTable.getColumn(TableCols.DESCRIPTION.ordinal()).addSelectionListener(new SelectionAdapter()
		{
			@Override
			public void widgetSelected(SelectionEvent arg0)
			{
				if(expenseTable.getItemCount() <2)
				{
					return;
				}
				
				int sortedIDs[] = null;
				if(sCountDesc == 0)
				{
					sortedIDs = Sort.sortCollection(TableCols.DESCRIPTION, SortDirection.ASCENDING, expenseTable.getItems());
					sCountDesc =1;
					sCountDate = 0;
					sCountID=0;
					sCountPay=0;
					sCountMoney=0;
				}
				else if(sCountDesc ==1)
				{
					sortedIDs = Sort.sortCollection(TableCols.DESCRIPTION, SortDirection.DESCENDING, expenseTable.getItems());
					sCountDesc=0;
				}
				refreshETable(sortedIDs);
			}
		});
		
		Menu menu = new Menu(shlExpenseView, SWT.BAR);
		menu.setLocation(new Point(0, 0));
		shlExpenseView.setMenuBar(menu);
		
		MenuItem mntmNewSubmenu = new MenuItem(menu, SWT.CASCADE);
		mntmNewSubmenu.setText("File");
		
		Menu menu_1 = new Menu(mntmNewSubmenu);
		mntmNewSubmenu.setMenu(menu_1);
		
		MenuItem mntmExit = new MenuItem(menu_1, SWT.NONE);
		mntmExit.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent arg0) {
				shlExpenseView.close();
			}
		});
		mntmExit.setText("Exit");
		
		MenuItem mntmFilter = new MenuItem(menu, SWT.CASCADE);
		mntmFilter.setText("Filter");
		
		Menu menu_3 = new Menu(mntmFilter);
		mntmFilter.setMenu(menu_3);
		
		MenuItem mntmCreateFilter = new MenuItem(menu_3, SWT.NONE);
		mntmCreateFilter.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent arg0) {
				IDialog filterDialog = new FilterCreation();
				ExpenseFilter filter = (ExpenseFilter)filterDialog.open();
				
				if(filter != null)
				{
					refreshWholeList(filter);
				}
				// else - do nothing
			}
		});
		mntmCreateFilter.setText("Create Filter");
		
		MenuItem mntmViewCommonLabels = new MenuItem(menu_3, SWT.NONE);
		mntmViewCommonLabels.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent arg0) {
				IDialog miningDialog = new MinedView();
				ExpenseFilter filter = (ExpenseFilter)miningDialog.open();
				
				if(filter != null)
				{
					refreshWholeList(filter);
				}
				// else - do nothing
			}
		});
		mntmViewCommonLabels.setText("View Common Labels");
		
		MenuItem mntmAction = new MenuItem(menu, SWT.CASCADE);
		mntmAction.setText("Action");
		
		Menu menu_2 = new Menu(mntmAction);
		mntmAction.setMenu(menu_2);
		
		MenuItem mntmNewExpense = new MenuItem(menu_2, SWT.NONE);
		mntmNewExpense.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent arg0) {
				newExpense();
			}
		});
		mntmNewExpense.setText("New Expense");
		
		MenuItem mntmOpenSelected = new MenuItem(menu_2, SWT.NONE);
		mntmOpenSelected.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent arg0) {
				openSelectedExpense();
			}
		});
		mntmOpenSelected.setText("Open Selected");
		
		MenuItem mntmDuplicateSelected = new MenuItem(menu_2, SWT.NONE);
		mntmDuplicateSelected.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent arg0) {
				duplicateSelectedExpense();
			}
		});
		mntmDuplicateSelected.setText("Duplicate Selected");
		
		MenuItem mntmDeleteSelected = new MenuItem(menu_2, SWT.NONE);
		mntmDeleteSelected.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent arg0) {
				removeSelectedExpense();
			}
		});
		mntmDeleteSelected.setText("Delete Selected");
		
		refreshWholeList(new ExpenseFilter());
	}
	
	private void refreshETable(int arr[])
	{
		if(arr != null)
		{
			expenseTable.removeAll();
			for(int i=0; i<arr.length; i++)
			{
				addExpenseToTable(arr[i]);
			}
		}
	}
	
	private void newExpense()
	{
		final int id = PFSystem.getCurrent().getExpenseSystem().create();
		
		addExpenseToTable(id);
		expenseTable.select(expenseTable.getItemCount() - 1);
		
		openSelectedExpense();
	}
	
	private void duplicateSelectedExpense()
	{
		if(expenseTable.getSelectionCount() == 0)
		{
			return;
		}
		
		final TableItem[] items = expenseTable.getSelection();
		
		final int selectedID = Integer.parseInt(items[0].getText(0));
		final Expense selectedExpense = (Expense)PFSystem.getCurrent().getExpenseSystem().getDataByID(selectedID);
						
		final int createdID = PFSystem.getCurrent().getExpenseSystem().create();
		PFSystem.getCurrent().getExpenseSystem().update(createdID, selectedExpense);
		
		addExpenseToTable(createdID);
						
		expenseTable.select(expenseTable.getItemCount() - 1);
	}
	
	private void removeSelectedExpense()
	{
		if(expenseTable.getSelectionCount() == 0)
		{
			return;
		}
		
		final int selectedIndex = expenseTable.getSelectionIndex();
		final TableItem[] items = expenseTable.getSelection();
		
		final int id = Integer.parseInt(items[0].getText(0));
		final boolean deleted = PFSystem.getCurrent().getExpenseSystem().delete(id);
		
		if(deleted)
		{
			expenseTable.remove(selectedIndex);
		}
	}
	
	private void openSelectedExpense()
	{
		if(expenseTable.getSelectionCount() == 0)
		{
			return;
		}
		
		final int selectedExpenseIndex = expenseTable.getSelectionIndex();
		
		final int expenseId = Integer.parseInt(expenseTable.getItem(selectedExpenseIndex).getText(0));
			
		EditExpense editExpenseWindow = new EditExpense();
		editExpenseWindow.setExpenseId(expenseId);
		editExpenseWindow.open();
			
		updateExpenseForRow(selectedExpenseIndex, expenseId);
	}
	
	private void addExpenseToTable(int inExpenseID)
	{
		new TableItem(expenseTable, SWT.NONE);	// will add to the end of the list
		
		updateExpenseForRow(expenseTable.getItemCount() - 1, inExpenseID);
	}
	
	private void updateExpenseForRow(int inRowIndex, int inExpenseID)
	{
		final Expense expense = (Expense)PFSystem.getCurrent().getExpenseSystem().getDataByID(inExpenseID);
		
		if(expense == null)
		{
			return;
		}
		
		SimpleDate date = expense.getDate();
				
		final int payToID = expense.getPayTo();
		PayTo payto = (PayTo)PFSystem.getCurrent().getPayToSystem().getDataByID(payToID);		
				
		final Money money = expense.getAmount(); 
		
		final String description = expense.getDescription();
		
		StringBuilder dateBuilder = new StringBuilder();
		dateBuilder.append(date.getDay());
		dateBuilder.append("/");
		dateBuilder.append(date.getMonth());
		dateBuilder.append("/");
		dateBuilder.append(date.getYear());	
				
		String dateString = dateBuilder.toString();
		String payToString = payto.getName();
		String descriptionString = description;
		
		TableItem item = expenseTable.getItem(inRowIndex);
		
		item.setText(0, "" + inExpenseID);
		item.setText(1, dateString);
		item.setText(2, payToString);
		item.setText(3, money.toString());
		item.setText(4, descriptionString);
	}

	private void refreshWholeList(ExpenseFilter filter)
	{
		expenseTable.removeAll();
		
		final ManagementBase expenseSystem = PFSystem.getCurrent().getExpenseSystem();
		
		final IDSet expenseIDs = expenseSystem.getAllIDs(filter, TableCols.ID, SortDirection.ASCENDING);

		final int totalExpenses = expenseIDs.getSize();
		
		for(int i = 0; i < totalExpenses; i++)
		{
			final int id = expenseIDs.getValue(i);
			addExpenseToTable(id);
		}
	}
}
