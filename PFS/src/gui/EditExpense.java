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
import system.PayToManagement;

import org.eclipse.swt.widgets.Table;
import org.eclipse.swt.widgets.TableColumn;
import org.eclipse.swt.widgets.Composite;

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
	
	private int payToId;
	private Button payToButton;
	
	private int expenseID;
	private Table labelTable;
	private Composite composite;

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
		shell = new Shell(SWT.SYSTEM_MODAL | SWT.DIALOG_TRIM);
		shell.setSize(719, 410);
		shell.setText("Expense Edit");
		
		final Expense expense = (Expense)PFSystem.getCurrent().getExpenseSystem().getDataByID(expenseID);
		
		PaymentMethod method = expense.getPaymentMethod();
		
		composite = new Composite(shell, SWT.NONE);
		composite.setBounds(0, 0, 713, 370);
		
		descriptionField = new Text(composite, SWT.BORDER);
		descriptionField.setBounds(10, 155, 285, 137);
		descriptionField.setText(expense.getDescription());
		
		Label lblDescription = new Label(composite, SWT.NONE);
		lblDescription.setBounds(10, 124, 90, 25);
		lblDescription.setText("Description");
		
		Button btnEditLabels = new Button(composite, SWT.NONE);
		btnEditLabels.setBounds(598, 298, 105, 28);
		btnEditLabels.addSelectionListener(new SelectionAdapter() 
		{
			@Override
			public void widgetSelected(SelectionEvent arg0) 
			{	
				final IDSet startingLabels = GUIHelper.getLabelIDSetFromTable(labelTable);
				
				LabelSelection window = new LabelSelection();
				window.setStartingSet(startingLabels);
				final IDSet labelIds = (IDSet)window.open();
				updateLableList(labelIds);
			}
		});
		btnEditLabels.setText("Edit Labels");
		
		Label lblLabels = new Label(composite, SWT.NONE);
		lblLabels.setBounds(356, 124, 49, 25);
		lblLabels.setText("Labels");
		
		Group grpPaymentMethod = new Group(composite, SWT.NONE);
		grpPaymentMethod.setBounds(356, 56, 347, 62);
		grpPaymentMethod.setText("Payment Method");
		
		cashRadio = new Button(grpPaymentMethod, SWT.RADIO);
		cashRadio.setText("Cash");
		cashRadio.setBounds(30, 27, 67, 25);
		
		creditRadio = new Button(grpPaymentMethod, SWT.RADIO);
		creditRadio.setText("Credit");
		creditRadio.setBounds(181, 27, 76, 25);
		
		debitRadio = new Button(grpPaymentMethod, SWT.RADIO);
		debitRadio.setText("Debit");
		debitRadio.setBounds(103, 27, 72, 25);
		
		otherRadio = new Button(grpPaymentMethod, SWT.RADIO);
		otherRadio.setText("Other");
		otherRadio.setBounds(263, 27, 74, 25);
		
		cashRadio.setSelection(method == PaymentMethod.CASH);
		debitRadio.setSelection(method == PaymentMethod.DEBIT);
		creditRadio.setSelection(method == PaymentMethod.CREDIT);
		otherRadio.setSelection(method == PaymentMethod.OTHER);
		datePicker = new DateTime(composite, SWT.BORDER);
		datePicker.setBounds(532, 15, 171, 35);
		datePicker.setDate(1, 1, 2014);
		
		amountField = new Text(composite, SWT.BORDER);
		amountField.setBounds(10, 87, 285, 31);
		amountField.setText(((Expense) PFSystem.getCurrent().getExpenseSystem().getDataByID(expenseID)).getAmount().toString());
		
		Label lblAmount = new Label(composite, SWT.NONE);
		lblAmount.setBounds(10, 56, 65, 25);
		lblAmount.setText("Amount");
		
		payToButton = new Button(composite, SWT.NONE);
		payToButton.setBounds(10, 15, 285, 35);
		payToButton.setAlignment(SWT.LEFT);
		
		Button btnSave = new Button(composite, SWT.NONE);
		btnSave.setBounds(598, 332, 105, 28);
		btnSave.setText("Save");
		
		Button btnCancel = new Button(composite, SWT.NONE);
		btnCancel.setBounds(10, 332, 94, 28);
		btnCancel.setText("Cancel");
		
		labelTable = new Table(composite, SWT.BORDER | SWT.FULL_SELECTION);
		labelTable.setBounds(356, 154, 347, 138);
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
		payToButton.addSelectionListener(new SelectionAdapter() 
		{
			@Override
			public void widgetSelected(SelectionEvent arg0) 
			{
				PaytoSelection window = new PaytoSelection();
				
				final int newPayToID = (int)window.open();
				
				if(newPayToID != PayToManagement.NULL_ID)
				{
					updatePayToGUI(newPayToID);
				}
			}
		});
		
		final SimpleDate date = expense.getDate();
		datePicker.setDate(date.getMonth(), date.getDay(), date.getYear());
		
		updatePayToGUI(expense.getPayTo());

		updateLableList(expense.getLabels());
	}
	
	private void updatePayToGUI(int inNewPayToId)
	{
		payToId = inNewPayToId;
		
		final PayTo payTo = (PayTo)PFSystem.getCurrent().getPayToSystem().getDataByID(payToId);
		payToButton.setText(payTo.toString()); 
	}
	
	private void updateLableList(IDSet inLabelSet)
	{
		labelTable.removeAll();	
		GUIHelper.addLabelsToTable(labelTable, inLabelSet);
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
				
		return new Expense(date, Money.fromString(amountField.getText()), method, descriptionField.getText(), payToId, labelIds);
	}
}
