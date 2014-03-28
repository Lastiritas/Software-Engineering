package gui;

import org.eclipse.swt.*;
import org.eclipse.swt.widgets.*;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;

import acceptanceTests.EventLoop;
import acceptanceTests.Register;
import system.PFSystem;
import domainobjects.DateRange;
import domainobjects.ExpenseFilter;
import domainobjects.IDSet;
import domainobjects.Money;
import domainobjects.MoneyRange;
import domainobjects.PayTo;
import domainobjects.PaymentMethod;
import domainobjects.SetOperation;
import domainobjects.SimpleDate;

public class FilterCreation implements IDialog
{
	protected Shell shell;

	private Button dateFilterCheck;
	private DateTime lowerDateSelect;
	private DateTime upperDateSelect;
	
	private Button amountFilterCheck;
	private Text upperAmountText;
	private Text lowerAmountText;
	
	private Button paymentFilterCheck;
	private Button cashRadio;	
	private Button creditRadio;
	private Button debitRadio;
	private Button otherRadio;
	
	private Button labelFilterCheck;
	private Button selectLabelsButton;
	
	private Button payToFilterCheck;
	private Button selectPayToButton;
	private Button removePayToButton;
	
	private Button expenseAllRadio;
	private Button expenseSomeRadio;
	
	private ExpenseFilter outputFilter = null;
	private Table labelTable;
	private Table payToTable;
	private TableColumn tblclmnId;
	private TableColumn tblclmnLabel;
	private TableColumn tblclmnId_1;
	private TableColumn tblclmnName;
	private Composite composite_5;

	
	/**
	 * Open the window.
	 * @wbp.parser.entryPoint
	 * 
	 */
	public Object open()
	{
		Display display =Display.getDefault();
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
		
		return outputFilter;
	}

	protected void createContents()
	{
		shell = new Shell(SWT.SYSTEM_MODAL | SWT.DIALOG_TRIM);
		shell.setSize(767, 719);
		shell.setText("Filter Creation");
		
		composite_5 = new Composite(shell, SWT.NONE);
		composite_5.setBounds(0, 0, 761, 679);
		
		Composite composite = new Composite(composite_5, SWT.NONE);
		composite.setBounds(10, 10, 733, 52);
		
		dateFilterCheck = new Button(composite, SWT.CHECK);
		dateFilterCheck.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent arg0) {
				final boolean isSelected = dateFilterCheck.getSelection(); 
				
				lowerDateSelect.setEnabled(isSelected);
				upperDateSelect.setEnabled(isSelected);
			}
		});
		dateFilterCheck.setBounds(10, 10, 132, 25);
		dateFilterCheck.setText("Filter by date");
		
		lowerDateSelect = new DateTime(composite, SWT.BORDER);
		lowerDateSelect.setEnabled(false);
		lowerDateSelect.setBounds(467, 10, 125, 33);
		
		upperDateSelect = new DateTime(composite, SWT.BORDER);
		upperDateSelect.setEnabled(false);
		upperDateSelect.setBounds(598, 10, 125, 33);
		
		Composite composite_1 = new Composite(composite_5, SWT.NONE);
		composite_1.setBounds(10, 68, 733, 50);
		
		amountFilterCheck = new Button(composite_1, SWT.CHECK);
		amountFilterCheck.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent arg0) {
				final boolean isSelected = amountFilterCheck.getSelection();
				
				lowerAmountText.setEnabled(isSelected);
				upperAmountText.setEnabled(isSelected);
			}
		});
		amountFilterCheck.setText("Filter by amount");
		amountFilterCheck.setBounds(10, 10, 159, 25);
		
		upperAmountText = new Text(composite_1, SWT.BORDER);
		upperAmountText.setEnabled(false);
		upperAmountText.setText("$0.00");
		upperAmountText.setBounds(600, 7, 123, 31);
		
		lowerAmountText = new Text(composite_1, SWT.BORDER);
		lowerAmountText.setEnabled(false);
		lowerAmountText.setText("$0.00");
		lowerAmountText.setBounds(471, 7, 123, 31);
		
		Composite composite_2 = new Composite(composite_5, SWT.NONE);
		composite_2.setBounds(10, 124, 733, 73);
		
		paymentFilterCheck = new Button(composite_2, SWT.CHECK);
		paymentFilterCheck.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent arg0) {
				final boolean isSelected = paymentFilterCheck.getSelection();
				
				cashRadio.setEnabled(isSelected);
				debitRadio.setEnabled(isSelected);
				creditRadio.setEnabled(isSelected);
				otherRadio.setEnabled(isSelected);
			}
		});
		paymentFilterCheck.setText("Filter by payment");
		paymentFilterCheck.setBounds(10, 39, 167, 25);
		
		Group group = new Group(composite_2, SWT.NONE);
		group.setBounds(403, 10, 320, 54);
		
		cashRadio = new Button(group, SWT.RADIO);
		cashRadio.setEnabled(false);
		cashRadio.setSelection(true);
		cashRadio.setText("Cash");
		cashRadio.setBounds(10, 26, 67, 25);
		
		creditRadio = new Button(group, SWT.RADIO);
		creditRadio.setEnabled(false);
		creditRadio.setText("Credit");
		creditRadio.setBounds(161, 26, 76, 25);
		
		debitRadio = new Button(group, SWT.RADIO);
		debitRadio.setEnabled(false);
		debitRadio.setText("Debit");
		debitRadio.setBounds(83, 26, 72, 25);
		
		otherRadio = new Button(group, SWT.RADIO);
		otherRadio.setEnabled(false);
		otherRadio.setText("Other");
		otherRadio.setBounds(243, 26, 74, 25);
		
		Composite composite_3 = new Composite(composite_5, SWT.NONE);
		composite_3.setBounds(10, 203, 733, 201);
		
		labelFilterCheck = new Button(composite_3, SWT.CHECK);
		labelFilterCheck.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent arg0) {
				final boolean isSelected = labelFilterCheck.getSelection();
				
				labelTable.setEnabled(isSelected);
				selectLabelsButton.setEnabled(isSelected);
				expenseAllRadio.setEnabled(isSelected);
				expenseSomeRadio.setEnabled(isSelected);
			}
		});
		labelFilterCheck.setText("Filter by labels");
		labelFilterCheck.setBounds(10, 10, 142, 25);
		
		selectLabelsButton = new Button(composite_3, SWT.NONE);
		selectLabelsButton.setEnabled(false);
		selectLabelsButton.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent arg0) {
				IDialog selection = new LabelSelection();
				IDSet labels = (IDSet)selection.open();
				
				labelTable.removeAll();
				
				final int totalLabels = labels.getSize();
				for(int i = 0; i < totalLabels; i++)
				{
					final int id = labels.getValue(i);
					final domainobjects.Label label = (domainobjects.Label)PFSystem.getCurrent().getLabelSystem().getDataByID(id);
					
					// add each label to the list
					TableItem labelItem = new TableItem(labelTable, SWT.NONE);
					labelItem.setText(0, "" + id);
					labelItem.setText(1, label.getName());
				}
			}
		});
		
		selectLabelsButton.setBounds(10, 158, 125, 28);
		selectLabelsButton.setText("Select Labels");
		
		Group grpLabelSet = new Group(composite_3, SWT.NONE);
		grpLabelSet.setText("Expense Must Have");
		grpLabelSet.setBounds(533, 44, 190, 108);
		
		expenseAllRadio = new Button(grpLabelSet, SWT.RADIO);
		expenseAllRadio.setEnabled(false);
		expenseAllRadio.setBounds(10, 34, 49, 25);
		expenseAllRadio.setText("All");
		
		expenseSomeRadio = new Button(grpLabelSet, SWT.RADIO);
		expenseSomeRadio.setEnabled(false);
		expenseSomeRadio.setSelection(true);
		expenseSomeRadio.setText("Some");
		expenseSomeRadio.setBounds(10, 65, 75, 25);
		
		labelTable = new Table(composite_3, SWT.BORDER | SWT.FULL_SELECTION);
		labelTable.setBounds(10, 44, 517, 108);
		labelTable.setHeaderVisible(true);
		labelTable.setLinesVisible(true);
		
		tblclmnId = new TableColumn(labelTable, SWT.NONE);
		tblclmnId.setWidth(100);
		tblclmnId.setText("ID");
		
		tblclmnLabel = new TableColumn(labelTable, SWT.NONE);
		tblclmnLabel.setWidth(100);
		tblclmnLabel.setText("Label");
		
		Composite composite_4 = new Composite(composite_5, SWT.NONE);
		composite_4.setBounds(10, 410, 733, 225);
		
		payToFilterCheck = new Button(composite_4, SWT.CHECK);
		payToFilterCheck.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent arg0) {
				final boolean isSelected = payToFilterCheck.getSelection();
				
				payToTable.setEnabled(isSelected);
				selectPayToButton.setEnabled(isSelected);
				removePayToButton.setEnabled(isSelected);
			}
		});
		payToFilterCheck.setText("Filter by pay to");
		payToFilterCheck.setBounds(10, 10, 148, 25);
		
		selectPayToButton = new Button(composite_4, SWT.NONE);
		selectPayToButton.setEnabled(false);
		selectPayToButton.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent arg0) {
				IDialog selection = new PaytoSelection();
				final int payToID = (int)selection.open();
				
				PayTo payTo = (PayTo)PFSystem.getCurrent().getPayToSystem().getDataByID(payToID);
				
				// set the payTo id
				TableItem paytoItem = new TableItem(payToTable, SWT.NONE);
				paytoItem.setText("" + payToID);
				paytoItem.setText(1, payTo.getName());
			}
		});
		selectPayToButton.setBounds(10, 189, 125, 28);
		selectPayToButton.setText("Select Pay To");
		
		payToTable = new Table(composite_4, SWT.BORDER | SWT.FULL_SELECTION);
		payToTable.setBounds(10, 44, 713, 139);
		payToTable.setHeaderVisible(true);
		payToTable.setLinesVisible(true);
		
		tblclmnId_1 = new TableColumn(payToTable, SWT.NONE);
		tblclmnId_1.setWidth(100);
		tblclmnId_1.setText("ID");
		
		tblclmnName = new TableColumn(payToTable, SWT.NONE);
		tblclmnName.setWidth(100);
		tblclmnName.setText("Name");
		
		removePayToButton = new Button(composite_4, SWT.NONE);
		removePayToButton.setEnabled(false);
		removePayToButton.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent arg0) {
				if(payToTable.getSelectionCount() == 0)
				{
					return;
				}
				final int selectedIndex = payToTable.getSelectionIndex();
				payToTable.remove(selectedIndex);
				
				
			}
		});
		removePayToButton.setBounds(141, 189, 109, 28);
		removePayToButton.setText("Remove Pay To");
		
		Button filterButton = new Button(composite_5, SWT.NONE);
		filterButton.setBounds(649, 641, 94, 28);
		filterButton.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent arg0) {
				closeWithReturn(generateFilterFromGUI());
			}
		});
		filterButton.setText("Filter");
		
		Button cancelButton = new Button(composite_5, SWT.NONE);
		cancelButton.setBounds(10, 641, 94, 28);
		cancelButton.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent arg0) {
				closeWithReturn(null);
			}
		});
		cancelButton.setText("Cancel");		
	}
	
	private ExpenseFilter generateFilterFromGUI()
	{
		ExpenseFilter output = new ExpenseFilter();
		
		if(dateFilterCheck.getSelection())
		{
			output.assignDateRange(getDateRange());
		}
		
		if(amountFilterCheck.getSelection())
		{
			output.assignMoneyRange(getMoneyRange());
		}
		
		if(paymentFilterCheck.getSelection())
		{
			output.assignmentPaymentMethod(getPaymentMethod());
		}
		
		if(labelFilterCheck.getSelection())
		{
			output.assignLabels(getLabelSet(), getSetOperation());
		}
		
		if(payToFilterCheck.getSelection())
		{
			output.assignPayTo(getPayToSet());
		}
		
		return output;
	}
	
	private DateRange getDateRange()
	{
		SimpleDate lower = SimpleDate.Now();
		lower.setYear(lowerDateSelect.getYear());
		lower.setMonth(lowerDateSelect.getMonth());
		lower.setDay(lowerDateSelect.getDay());

		SimpleDate upper = SimpleDate.Now();
		upper.setYear(upperDateSelect.getYear());
		upper.setMonth(upperDateSelect.getMonth());
		upper.setDay(upperDateSelect.getDay());
		
		return new DateRange(lower, upper);
	}
	
	private MoneyRange getMoneyRange()
	{
		Money lower = Money.fromString(lowerAmountText.getText());
		Money upper = Money.fromString(upperAmountText.getText());
		
		return new MoneyRange(lower, upper);
	}
	
	private PaymentMethod getPaymentMethod()
	{
		if(cashRadio.getSelection())
		{
			return PaymentMethod.CASH;
		}
		else if(debitRadio.getSelection())
		{
			return PaymentMethod.DEBIT;
		}
		else if(creditRadio.getSelection())
		{
			return PaymentMethod.CREDIT;
		}
		else
		{
			return PaymentMethod.OTHER;
		}
	}
	
	private IDSet getLabelSet()
	{
		return GUIHelper.getIdsFromTable(labelTable);
	}
	
	private SetOperation getSetOperation()
	{
		if(expenseAllRadio.getSelection())
		{
			return SetOperation.INTERSECTION;
		}
		else
		{
			return SetOperation.UNION;
		}
	}
	
	private IDSet getPayToSet()
	{
		return GUIHelper.getIdsFromTable(payToTable);
	}
	
	private void closeWithReturn(ExpenseFilter filter)
	{
		assert filter == null || filter != outputFilter;	// do not set the filter to itself
		
		outputFilter = filter;
		shell.close();
	}
}
