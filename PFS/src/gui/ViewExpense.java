package gui;
import java.util.ArrayList;

import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.SWT;
import org.eclipse.swt.widgets.TableColumn;
import org.eclipse.swt.widgets.TableItem;
import org.eclipse.swt.widgets.Text;
import org.eclipse.swt.widgets.List;
import org.eclipse.swt.widgets.Table;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Group;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.DateTime;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;

import domainobjects.Expense;
import domainobjects.ExpenseFilter;
import domainobjects.IDSet;
import domainobjects.Money;
import domainobjects.PayTo;
import domainobjects.PaymentMethod;
import domainobjects.SimpleDate;
import system.PFSystem;

public class ViewExpense implements IWindow 
{
	private Shell shell;
	private Table expenseTable;
	private Text payToField;
	private Text amountField;
	private Text descriptionField;
	private DateTime datePicker;
	private Composite editPanel;
	private Group grpPaymentMethod;
	private Button cashRadio;
	private Button creditRadio;
	private Button debitRadio;
	private Button otherRadio;
	private Button saveButton;
	private List labelsList;
	private Button editLabelsButton;
	protected int currID;
	private Button btnFilter;
	
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
	 * @wbp.parser.entryPoint
	 */
	protected void createContents() 
	{
		shell = new Shell();
		shell.setSize(918, 673);
		shell.setText("PayTo Creation");
		
		editPanel = new Composite(shell, SWT.NONE);
		editPanel.setBounds(665, 10, 243, 631);
		
		payToField = new Text(editPanel, SWT.BORDER);
		payToField.setEditable(false);
		payToField.setBounds(110, 50, 123, 19);
		
		Button payToButton = new Button(editPanel, SWT.NONE);
		payToButton.addSelectionListener(new SelectionAdapter() 
		{
			@Override
			public void widgetSelected(SelectionEvent arg0) 
			{
				PaytoSelection window = new PaytoSelection();
				final int payToID = (int)window.open();
				
				final PayTo payTo = (PayTo)PFSystem.getCurrent().getPayToSystem().getDataByID(payToID);
				
				payToField.setText(payTo.getPayToName() + ", " + payTo.getPayToBranch());
			}
		});
		payToButton.setBounds(10, 46, 94, 28);
		payToButton.setText("Pay To");
		
		amountField = new Text(editPanel, SWT.BORDER);
		amountField.setText("$0.00");
		amountField.setBounds(110, 75, 123, 19);
		
		grpPaymentMethod = new Group(editPanel, SWT.NONE);
		grpPaymentMethod.setText("Payment Method");
		grpPaymentMethod.setBounds(10, 116, 223, 74);
		
		cashRadio = new Button(grpPaymentMethod, SWT.RADIO);
		cashRadio.setBounds(14, 10, 91, 18);
		cashRadio.setText("Cash");
		
		
		creditRadio = new Button(grpPaymentMethod, SWT.RADIO);
		creditRadio.setBounds(14, 34, 91, 18);
		creditRadio.setText("Credit");
		
		debitRadio = new Button(grpPaymentMethod, SWT.RADIO);
		debitRadio.setBounds(118, 10, 91, 18);
		debitRadio.setText("Debit");
		
		otherRadio = new Button(grpPaymentMethod, SWT.RADIO);
		otherRadio.setBounds(118, 34, 91, 18);
		otherRadio.setText("Other");
		
		Label lblAmount = new Label(editPanel, SWT.NONE);
		lblAmount.setBounds(20, 78, 59, 14);
		lblAmount.setText("Amount");
		
		datePicker = new DateTime(editPanel, SWT.BORDER);
		datePicker.setBounds(110, 10, 123, 27);
		
		descriptionField = new Text(editPanel, SWT.BORDER);
		descriptionField.setBounds(20, 238, 213, 129);
		
		Label lblDescriptoin = new Label(editPanel, SWT.NONE);
		lblDescriptoin.setBounds(20, 218, 83, 14);
		lblDescriptoin.setText("Description");
		
		labelsList = new List(editPanel, SWT.BORDER);
		labelsList.setBounds(20, 373, 213, 123);
		
		editLabelsButton = new Button(editPanel, SWT.NONE);
		editLabelsButton.addSelectionListener(new SelectionAdapter() 
		{
			@Override
			public void widgetSelected(SelectionEvent arg0) 
			{	
				IDSet allLabelIDs = PFSystem.getCurrent().getLabelSystem().getAllIDs();
				
				String[] exisitingLabel = labelsList.getItems();
				
				int[] rawData = new int[exisitingLabel.length];
				int rawDataCount = 0;
				
				for(int i = 0; i < allLabelIDs.getSize(); i++)
				{
					final int id = allLabelIDs.getValue(i);
					final domainobjects.Label label = (domainobjects.Label)PFSystem.getCurrent().getLabelSystem().getDataByID(id);
					
					for(int j = 0; j < exisitingLabel.length; j++)
					{
						if(exisitingLabel[i].equals(label.getLabelName()))
						{
							// id was found
							rawData[rawDataCount] = id;
							rawDataCount++;
						}
					}
				}
				
				int[] realData = new int[rawDataCount];
				System.arraycopy(rawData, 0, realData, 0, rawDataCount);
				
				IDSet realSet = IDSet.createFromArray(realData);
				
				LabelSelection window = new LabelSelection();
				window.setStartingSet(realSet);
				String[] labels = (String[])window.open();
				
				labelsList.removeAll();
								
				for(int i = 0; i < labels.length; i++)
				{
					labelsList.add(labels[i]);
				}
			}
		});
		editLabelsButton.setBounds(141, 502, 94, 28);
		editLabelsButton.setText("Edit Labels");
		
		saveButton = new Button(editPanel, SWT.NONE);
		saveButton.addSelectionListener(new SelectionAdapter() 
		{
			@Override
			public void widgetSelected(SelectionEvent arg0) 
			{
				if(expenseTable.getSelectionCount() == 0)
				{
					return;
				}
				
				final int selectedIndex = expenseTable.getSelectionIndex();
				final TableItem item = expenseTable.getSelection()[0];
				
				currID = Integer.parseInt(item.getText(0));
				
				saveExpenseFromEditingPane(selectedIndex, currID);
			}
		});
		saveButton.setBounds(139, 593, 94, 28);
		saveButton.setText("Save");
		
		Composite composite_1 = new Composite(shell, SWT.NONE);
		composite_1.setBounds(10, 10, 649, 631);
		
		Button deleteButton = new Button(composite_1, SWT.NONE);
		deleteButton.addSelectionListener(new SelectionAdapter() 
		{
			@Override
			public void widgetSelected(SelectionEvent arg0) 
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
		});
		deleteButton.setBounds(345, 593, 94, 28);
		deleteButton.setText("-");
		
		Button addButton = new Button(composite_1, SWT.NONE);
		addButton.addSelectionListener(new SelectionAdapter() 
		{
			@Override
			public void widgetSelected(SelectionEvent arg0) 
			{
				final int id = PFSystem.getCurrent().getExpenseSystem().create();
				addExpense(id);
				expenseTable.select(expenseTable.getItemCount() - 1);
				openExpenseInEditingPane(id);
			}
		});
		addButton.setBounds(545, 593, 94, 28);
		addButton.setText("+");
		
		expenseTable = new Table(composite_1, SWT.BORDER | SWT.FULL_SELECTION);
		expenseTable.addSelectionListener(new SelectionAdapter() 
		{
			@Override
			public void widgetSelected(SelectionEvent arg0) 
			{
				if(expenseTable.getSelectionCount() == 0)
				{
					return;
				}
				
				final TableItem item = expenseTable.getSelection()[0];
				
				currID = Integer.parseInt(item.getText(0));
				
				openExpenseInEditingPane(currID);
			}
		});
		expenseTable.setBounds(10, 10, 629, 577);
		expenseTable.setHeaderVisible(true);
		expenseTable.setLinesVisible(true);
		
		String[] columnHeaders = {"ID", "Date", "Pay To", "Amount", "Description"};
		
		for(int i = 0; i < columnHeaders.length; i++)
		{
			TableColumn column = new TableColumn (expenseTable, SWT.NONE);
			column.setWidth(100);
			column.setText (columnHeaders[i]);
		}
				
		Button duplicateButton = new Button(composite_1, SWT.NONE);
		duplicateButton.addSelectionListener(new SelectionAdapter() 
		{
			@Override
			public void widgetSelected(SelectionEvent arg0) 
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
				
				addExpense(createdID);
								
				expenseTable.select(expenseTable.getItemCount() - 1);
			}
		});
		duplicateButton.setBounds(445, 593, 94, 28);
		duplicateButton.setText("Duplicate");
		
		Button btnQuit = new Button(composite_1, SWT.NONE);
		btnQuit.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent arg0) {
				shell.close();
			}
		});
		btnQuit.setBounds(10, 593, 94, 28);
		btnQuit.setText("Quit");
		
		btnFilter = new Button(composite_1, SWT.NONE);
		btnFilter.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent arg0) {
				IDialog filterDialog = new FilterCreation();
				ExpenseFilter filter = (ExpenseFilter)filterDialog.open();
				
				if(filter != null)
				{
					// apply the filter to the view
				}
				// else - do nothing
			}
		});
		btnFilter.setText("Filter");
		btnFilter.setBounds(110, 593, 94, 28);
		
		// add all exisiting expenses
		IDSet expenseIDs = PFSystem.getCurrent().getExpenseSystem().getAllIDs();
		for(int i = 0; i < expenseIDs.getSize(); i++)
		{
			final int id = expenseIDs.getValue(i);
			addExpense(id);
		}
	}
	
	private void saveExpenseFromEditingPane(int inTableIndex, int inID)
	{						
		SimpleDate date = SimpleDate.Now();
		date.setYear(datePicker.getYear());
		date.setMonth(datePicker.getMonth());
		date.setDay(datePicker.getDay());
		
		PaymentMethod method = PaymentMethod.CASH;
		
		if(cashRadio.getSelection())
		{
			method = PaymentMethod.CASH;
		}
		else if(debitRadio.getSelection())
		{
			method = PaymentMethod.DEBIT;
		}
		else if(creditRadio.getSelection())
		{
			method = PaymentMethod.CREDIT;
		}
		else
		{
			method = PaymentMethod.OTHER;
		}

		
		int payToId = -1;
		final IDSet payToIDs = PFSystem.getCurrent().getPayToSystem().getAllIDs();
		
		for(int i = 0; i < payToIDs.getSize(); i++)
		{
			final int id = payToIDs.getValue(i);
			
			final PayTo payTo = (PayTo)PFSystem.getCurrent().getPayToSystem().getDataByID(id);
			
			String payToString = payTo.getPayToName() + ", " + payTo.getPayToBranch();
			
			if(payToString.equals(payToField.getText()))
			{
				payToId = id;
				break;
			}
		}
				
		IDSet allLabelIDs = PFSystem.getCurrent().getLabelSystem().getAllIDs();
		
		String[] exisitingLabel = labelsList.getItems();
		
		int[] rawData = new int[exisitingLabel.length];
		int rawDataCount = 0;
		
		for(int i = 0; i < allLabelIDs.getSize(); i++)
		{
			final int id = allLabelIDs.getValue(i);
			final domainobjects.Label label = (domainobjects.Label)PFSystem.getCurrent().getLabelSystem().getDataByID(id);
			
			for(int j = 0; j < exisitingLabel.length; j++)
			{
				if(exisitingLabel[j].equals(label.getLabelName()))
				{
					// id was found
					rawData[rawDataCount] = id;
					rawDataCount++;
					
				}
			}
		}
		
		int[] realData = new int[rawDataCount];
		System.arraycopy(rawData, 0, realData, 0, rawDataCount);
		
		final IDSet set = IDSet.createFromArray(realData);
		
		final Expense expense = new Expense(date, Money.fromString(amountField.getText()), method, descriptionField.getText(), payToId, set);
		
		boolean updated = PFSystem.getCurrent().getExpenseSystem().update(inID, expense);
		
		if(updated)
		{
			updateExpenseForRow(inTableIndex, inID);
		}
	}
	
	private void openExpenseInEditingPane(int inID)
	{
		final Expense expense = (Expense)PFSystem.getCurrent().getExpenseSystem().getDataByID(inID);
	
		datePicker.setYear(expense.getDate().getYear());
		datePicker.setMonth(expense.getDate().getMonth());
		datePicker.setDay(expense.getDate().getDay());
		
		final int payToID = expense.getPayTo();
		PayTo payto = (PayTo)PFSystem.getCurrent().getPayToSystem().getDataByID(payToID);		
		payToField.setText(payto.getPayToName() + ", " + payto.getPayToBranch());
		
		final Money money = expense.getAmount();
		amountField.setText(money.toString());
		
		final PaymentMethod method = expense.getPaymentMethod();
		
		cashRadio.setSelection(method == PaymentMethod.CASH);
		debitRadio.setSelection(method == PaymentMethod.DEBIT);
		creditRadio.setSelection(method == PaymentMethod.CREDIT);
		otherRadio.setSelection(method == PaymentMethod.OTHER);
		
		descriptionField.setText(expense.getDescription());
		
		labelsList.removeAll();
		
		final IDSet labelIDs = expense.getLabels();
		for(int i = 0; i < labelIDs.getSize(); i++)
		{
			final int labelID = labelIDs.getValue(i);
			domainobjects.Label label = (domainobjects.Label)PFSystem.getCurrent().getLabelSystem().getDataByID(labelID);
			
			labelsList.add(label.getLabelName());
		}
	}
	
	private void addExpense(int inExpenseID)
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
		
		SimpleDate date = SimpleDate.Now();
		date.setMonth(datePicker.getMonth());
		date.setDay(datePicker.getDay());
		date.setYear(datePicker.getYear());
				
		final int payToID = expense.getPayTo();
		PayTo payto = (PayTo)PFSystem.getCurrent().getPayToSystem().getDataByID(payToID);		
				
		final Money money = expense.getAmount(); 
		
		final String description = expense.getDescription();
		
		StringBuilder dateBuilder = new StringBuilder();
		dateBuilder.append(date.getMonth());
		dateBuilder.append("/");
		dateBuilder.append(date.getDay());
		dateBuilder.append("/");
		dateBuilder.append(date.getYear());	
				
		String dateString = dateBuilder.toString();
		String payToString = payto.getPayToName() + ", " + payto.getPayToBranch();
		String descriptionString = description;
		
		TableItem item = expenseTable.getItem(inRowIndex);
		
		item.setText(0, "" + inExpenseID);
		item.setText(1, dateString);
		item.setText(2, payToString);
		item.setText(3, money.toString());
		item.setText(4, descriptionString);
	}
}
