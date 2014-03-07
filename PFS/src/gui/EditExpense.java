package gui;

import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.swt.SWT;
import org.eclipse.swt.widgets.Text;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Group;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.DateTime;
import org.eclipse.swt.widgets.List;

import domainobjects.Expense;
import domainobjects.IDSet;
import domainobjects.Money;
import domainobjects.PayTo;
import domainobjects.PaymentMethod;
import domainobjects.SimpleDate;
import system.PFSystem;
import org.eclipse.swt.widgets.Table;
import org.eclipse.swt.widgets.TableColumn;

public class EditExpense implements IWindow
{
	protected Shell shell;
	private Text descriptionField;
	private Text amountField;
	private DateTime datePicker;
	
	private Button cashRadio;
	private Button creditRadio;
	private Button debitRadio;
	private Button otherRadio;
	
	private Button payToButton;
	
	private int expenseID;
	private Table labelTable;

	/**
	 * Open the window.
	 */
	public void open() 
	{
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

	public void setExpenseId(int inExpenseID)
	{
		expenseID = inExpenseID;
	}
	
	/**
	 * Create contents of the window.
	 * @wbp.parser.entryPoint
	 */
	protected void createContents() 
	{
		shell = new Shell();
		shell.setSize(502, 333);
		shell.setText("Expense Edit");
		
		final Expense expense = (Expense)PFSystem.getCurrent().getExpenseSystem().getDataByID(expenseID);
		
		PaymentMethod method = expense.getPaymentMethod();
		
		descriptionField = new Text(shell, SWT.BORDER);
		descriptionField.setBounds(10, 127, 213, 129);
		descriptionField.setText(expense.getDescription());
		
		Label lblDescription = new Label(shell, SWT.NONE);
		lblDescription.setText("Description");
		lblDescription.setBounds(10, 107, 83, 14);
		//labelsList.set...
		
		Button btnEditLabels = new Button(shell, SWT.NONE);
		btnEditLabels.addSelectionListener(new SelectionAdapter() 
		{
			@Override
			public void widgetSelected(SelectionEvent arg0) 
			{	
				final IDSet startingLabels = GUIHelper.getLabelIDSetFromTable(labelTable);
				
				LabelSelection window = new LabelSelection();
				window.setStartingSet(startingLabels);
				final IDSet labelIds = (IDSet)window.open();

				labelTable.removeAll();
				GUIHelper.addLabelsToTable(labelTable, labelIds);
			}
		});
		btnEditLabels.setText("Edit Labels");
		btnEditLabels.setBounds(280, 262, 94, 28);
		
		Label lblLabels = new Label(shell, SWT.NONE);
		lblLabels.setText("Labels");
		lblLabels.setBounds(261, 107, 83, 14);
		
		Group grpPaymentMethod = new Group(shell, SWT.NONE);
		grpPaymentMethod.setText("Payment Method");
		grpPaymentMethod.setBounds(251, 10, 223, 91);
		
		cashRadio = new Button(grpPaymentMethod, SWT.RADIO);
		cashRadio.setText("Cash");
		cashRadio.setBounds(10, 10, 91, 18);
		
		creditRadio = new Button(grpPaymentMethod, SWT.RADIO);
		creditRadio.setText("Credit");
		creditRadio.setBounds(10, 34, 91, 18);
		
		debitRadio = new Button(grpPaymentMethod, SWT.RADIO);
		debitRadio.setText("Debit");
		debitRadio.setBounds(114, 10, 91, 18);
		
		otherRadio = new Button(grpPaymentMethod, SWT.RADIO);
		otherRadio.setText("Other");
		otherRadio.setBounds(114, 34, 91, 18);
		
		cashRadio.setSelection(method == PaymentMethod.CASH);
		debitRadio.setSelection(method == PaymentMethod.DEBIT);
		creditRadio.setSelection(method == PaymentMethod.CREDIT);
		otherRadio.setSelection(method == PaymentMethod.OTHER);
		
		final SimpleDate date = expense.getDate();
		datePicker = new DateTime(shell, SWT.BORDER);
		datePicker.setDate(date.getMonth(), date.getDay(), date.getYear());
		datePicker.setBounds(100, 37, 123, 27);
		
		amountField = new Text(shell, SWT.BORDER);
		amountField.setText(((Expense) PFSystem.getCurrent().getExpenseSystem().getDataByID(expenseID)).getAmount().toString());
		amountField.setBounds(100, 67, 123, 19);
		
		Label lblAmount = new Label(shell, SWT.NONE);
		lblAmount.setText("Amount");
		lblAmount.setBounds(10, 70, 59, 14);
		
		payToButton = new Button(shell, SWT.NONE);
		payToButton.setAlignment(SWT.LEFT);
		payToButton.setText("Pay To");
		payToButton.setBounds(10, 7, 213, 24);
		payToButton.addSelectionListener(new SelectionAdapter() 
		{
			@Override
			public void widgetSelected(SelectionEvent arg0) 
			{
				PaytoSelection window = new PaytoSelection();
				
				int payToID = (int)window.open();
				
				final PayTo payTo = (PayTo)PFSystem.getCurrent().getPayToSystem().getDataByID(payToID);
				
				payToButton.setText(payTo.toString()); 
			}
		});
		
	 	final int payToID = expense.getPayTo();
	 	final PayTo payTo = (PayTo)PFSystem.getCurrent().getPayToSystem().getDataByID(payToID);
	 	payToButton.setText(payTo.toString());
		
		Label lblDate = new Label(shell, SWT.NONE);
		lblDate.setText("Date");
		lblDate.setBounds(10, 40, 59, 14);
		
		Button btnSave = new Button(shell, SWT.NONE);
		btnSave.setText("Save");
		btnSave.setBounds(380, 262, 94, 28);
		btnSave.addSelectionListener(new SelectionAdapter() 
		{
			@Override
			public void widgetSelected(SelectionEvent arg0) 
			{	
				final Expense newExpense = createExpenseFromGUI();
				PFSystem.getCurrent().getExpenseSystem().update(expenseID, newExpense);	
				
				shell.close();
			}
		});
		
		Button btnCancel = new Button(shell, SWT.NONE);
		btnCancel.setText("Cancel");
		btnCancel.setBounds(10, 262, 94, 28);
		
		labelTable = new Table(shell, SWT.BORDER | SWT.FULL_SELECTION);
		labelTable.setBounds(251, 127, 223, 129);
		labelTable.setHeaderVisible(true);
		labelTable.setLinesVisible(true);
		
		TableColumn tblclmnId = new TableColumn(labelTable, SWT.NONE);
		tblclmnId.setWidth(100);
		tblclmnId.setText("ID");
		
		TableColumn tblclmnLabel = new TableColumn(labelTable, SWT.NONE);
		tblclmnLabel.setWidth(100);
		tblclmnLabel.setText("Label");
		btnCancel.addSelectionListener(new SelectionAdapter()
		{
			@Override
			public void widgetSelected(SelectionEvent arg0)
			{
				shell.close();
			}
		});

	}
	
	private SimpleDate getDateFromGUI()
	{
		SimpleDate date = SimpleDate.Now();
		date.setDay(datePicker.getDay());
		date.setMonth(datePicker.getMonth());
		date.setYear(datePicker.getYear());
		
		return date;
	}
	
	private PaymentMethod getPaymentMethodFromGUI()
	{
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
		
		return method;
	}
	
	private Expense createExpenseFromGUI()
	{
		SimpleDate date = getDateFromGUI();
		
		PaymentMethod method = getPaymentMethodFromGUI();

		IDSet labelIds = GUIHelper.getLabelIDSetFromTable(labelTable);
				
		return new Expense(date, Money.fromString(amountField.getText()), method, descriptionField.getText(), 1, labelIds);
	}
}
