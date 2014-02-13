package gui;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.SWT;
import org.eclipse.swt.widgets.TableColumn;
import org.eclipse.swt.widgets.TableItem;
import org.eclipse.swt.widgets.Text;
import org.eclipse.swt.widgets.Tree;
import org.eclipse.swt.widgets.List;
import org.eclipse.swt.widgets.Table;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Group;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.DateTime;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;

import domainobjects.Expense;
import domainobjects.IDSet;
import domainobjects.Money;
import domainobjects.PaymentMethod;
import domainobjects.SimpleDate;
import system.FPSystem;

public class ViewExpense {

	protected Shell shell;
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
	
	/**
	 * Launch the application.
	 * @param args
	 */
	public static void main(String[] args) {
		try {
			ViewExpense window = new ViewExpense();
			window.open();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	/**
	 * Open the window.
	 */
	public void open() {
		Display display = Display.getDefault();
		createContents();
		shell.open();
		shell.layout();
		while (!shell.isDisposed()) {
			if (!display.readAndDispatch()) {
				display.sleep();
			}
		}
	}

	/**
	 * Create contents of the window.
	 * @wbp.parser.entryPoint
	 */
	protected void createContents() {
		shell = new Shell();
		shell.setSize(918, 673);
		shell.setText("PayTo Creation");
		
		editPanel = new Composite(shell, SWT.NONE);
		editPanel.setBounds(665, 10, 243, 631);
		
		payToField = new Text(editPanel, SWT.BORDER);
		payToField.setEditable(false);
		payToField.setBounds(110, 50, 123, 19);
		
		Button payToButton = new Button(editPanel, SWT.NONE);
		payToButton.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent arg0) {
				PaytoSelection window = new PaytoSelection();
				window.open();
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
		
		List labelsList = new List(editPanel, SWT.BORDER);
		labelsList.setBounds(20, 373, 213, 123);
		
		Button editLabelsButton = new Button(editPanel, SWT.NONE);
		editLabelsButton.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent arg0) {
				LabelMgmt window = new LabelMgmt();
				window.open();
			}
		});
		editLabelsButton.setBounds(141, 502, 94, 28);
		editLabelsButton.setText("Edit Labels");
		
		saveButton = new Button(editPanel, SWT.NONE);
		saveButton.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent arg0) {
				if(expenseTable.getSelectionCount() == 0)
				{
					return;
				}
				
				final int selectedIndex = expenseTable.getSelectionIndex();
				final TableItem[] items = expenseTable.getSelection();
				
				final int id = Integer.parseInt(items[0].getText(0));
				
				int[] data = {};
				IDSet set = IDSet.createFromArray(data);
				
				Expense expense = new Expense(SimpleDate.Now(), new Money(), PaymentMethod.CASH, "", -1, set);
				FPSystem.getCurrent().getExpenseSystem().update(id, expense);
				
				updateExpenseForRow(selectedIndex, id);
			}
		});
		saveButton.setBounds(139, 593, 94, 28);
		saveButton.setText("Save");
		
		Composite composite_1 = new Composite(shell, SWT.NONE);
		composite_1.setBounds(10, 10, 649, 631);
		
		Button deleteButton = new Button(composite_1, SWT.NONE);
		deleteButton.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent arg0) {
				if(expenseTable.getSelectionCount() == 0)
				{
					return;
				}
				
				final int selectedIndex = expenseTable.getSelectionIndex();
				final TableItem[] items = expenseTable.getSelection();
				
				final int id = Integer.parseInt(items[0].getText(0));
				final boolean deleted = FPSystem.getCurrent().getExpenseSystem().delete(id);
				
				if(deleted)
				{
					expenseTable.remove(selectedIndex);
				}
			}
		});
		deleteButton.setBounds(10, 593, 94, 28);
		deleteButton.setText("-");
		
		Button addButton = new Button(composite_1, SWT.NONE);
		addButton.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent arg0) {
				final int id = FPSystem.getCurrent().getExpenseSystem().create();
				addExpense(id);
				expenseTable.select(expenseTable.getItemCount() - 1);
			}
		});
		addButton.setBounds(545, 593, 94, 28);
		addButton.setText("+");
		
		expenseTable = new Table(composite_1, SWT.BORDER | SWT.FULL_SELECTION);
		expenseTable.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent arg0) {
				if(expenseTable.getSelectionCount() == 0)
				{
					return;
				}
				
				final TableItem[] items = expenseTable.getSelection();
				
				final int id = Integer.parseInt(items[0].getText(0));

				final Expense expense = (Expense)FPSystem.getCurrent().getExpenseSystem().getDataByID(id);
			
				datePicker.setYear(expense.getDate().getYear());
				datePicker.setMonth(expense.getDate().getMonth());
				datePicker.setDay(expense.getDate().getDay());
				
				Money money = expense.getAmount();
				amountField.setText(money.toString());
				
				switch(expense.getPaymentMethod())
				{
				case CASH:	
					cashRadio.setSelection(true);
					debitRadio.setSelection(false);
					creditRadio.setSelection(false);
					otherRadio.setSelection(false);
					break;
				case DEBIT:		
					cashRadio.setSelection(false);
					debitRadio.setSelection(true);
					creditRadio.setSelection(false);
					otherRadio.setSelection(false);
					break;
				case CREDIT:	
					cashRadio.setSelection(false);
					debitRadio.setSelection(false);
					creditRadio.setSelection(true);
					otherRadio.setSelection(false);
					break;
				case OTHER:		
					cashRadio.setSelection(false);
					debitRadio.setSelection(false);
					creditRadio.setSelection(false);
					otherRadio.setSelection(true);
					break;
				}
				
				descriptionField.setText(expense.getDescription());
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
		duplicateButton.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent arg0) {
				final int id = FPSystem.getCurrent().getExpenseSystem().create();
				addExpense(id);
				expenseTable.select(expenseTable.getItemCount() - 1);
			}
		});
		duplicateButton.setBounds(445, 593, 94, 28);
		duplicateButton.setText("Duplicate");		
	}
		
	private void addExpense(int inExpenseID)
	{
		new TableItem(expenseTable, SWT.NONE);	// will add to the end of the list
		
		updateExpenseForRow(expenseTable.getItemCount() - 1, inExpenseID);
	}
	
	private void updateExpenseForRow(int inRowIndex, int inExpenseID)
	{
		final Expense expense = (Expense)FPSystem.getCurrent().getExpenseSystem().getDataByID(inExpenseID);
		
		if(expense == null)
		{
			return;
		}
		
		SimpleDate date = SimpleDate.Now();
		date.setMonth(datePicker.getMonth());
		date.setDay(datePicker.getDay());
		date.setYear(datePicker.getYear());
				
		String payTo = "" + expense.getPayTo();
		Money money = expense.getAmount(); 
		String description = expense.getDescription();

		IDSet labelIDs = expense.getLabels();
		String[] labels = new String[labelIDs.getSize()];
		for(int i = 0; i < labels.length; i++)
		{
			labels[i] = "" + i;
		}
		
		
		StringBuilder dateBuilder = new StringBuilder();
		
		dateBuilder.append(date.getMonth());
		dateBuilder.append("/");
		dateBuilder.append(date.getDay());
		dateBuilder.append("/");
		dateBuilder.append(date.getYear());	
				
		String dateString = dateBuilder.toString();
		String payToString = payTo;
		String descriptionString = description;
		
		TableItem item = expenseTable.getItem(inRowIndex);
		
		item.setText(0, "" + inExpenseID);
		item.setText(1, dateString);
		item.setText(2, payToString);
		item.setText(3, money.toString());
		item.setText(4, descriptionString);
	}
	
}
