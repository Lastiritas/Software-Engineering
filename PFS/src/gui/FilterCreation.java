package gui;

import org.eclipse.swt.*;
import org.eclipse.swt.widgets.*;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;

import sun.java2d.pipe.SpanShapeRenderer.Simple;
import domainobjects.DateRange;
import domainobjects.ExpenseFilter;
import domainobjects.Money;
import domainobjects.MoneyRange;
import domainobjects.PaymentMethod;
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
	private Tree labelList;
	
	private Button payToFilterCheck;
	private Tree payToList;
	
	private Button expenseAllRadio;
	private Button expenseSomeRadio;
	
	private ExpenseFilter outputFilter = null;
	
	/**
	 * Open the window.
	 * @wbp.parser.entryPoint
	 * 
	 */
	public Object open()
	{
		Display display =Display.getDefault();
		
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
		
		return outputFilter;
	}

	protected void createContents()
	{
		shell = new Shell(SWT.APPLICATION_MODAL);
		shell.setSize(506, 515);
		shell.setText("Label Creation");
		
		Composite composite = new Composite(shell, SWT.NONE);
		composite.setBounds(10, 10, 486, 37);
		
		dateFilterCheck = new Button(composite, SWT.CHECK);
		dateFilterCheck.setBounds(10, 10, 147, 18);
		dateFilterCheck.setText("Filter by date");
		
		lowerDateSelect = new DateTime(composite, SWT.BORDER);
		lowerDateSelect.setBounds(214, 10, 128, 27);
		
		upperDateSelect = new DateTime(composite, SWT.BORDER);
		upperDateSelect.setBounds(348, 10, 128, 27);
		
		Composite composite_1 = new Composite(shell, SWT.NONE);
		composite_1.setBounds(10, 53, 486, 37);
		
		amountFilterCheck = new Button(composite_1, SWT.CHECK);
		amountFilterCheck.setText("Filter by amount");
		amountFilterCheck.setBounds(10, 10, 145, 18);
		
		upperAmountText = new Text(composite_1, SWT.BORDER);
		upperAmountText.setText("$0.00");
		upperAmountText.setBounds(343, 10, 133, 19);
		
		lowerAmountText = new Text(composite_1, SWT.BORDER);
		lowerAmountText.setText("$0.00");
		lowerAmountText.setBounds(204, 10, 133, 19);
		
		Composite composite_2 = new Composite(shell, SWT.NONE);
		composite_2.setBounds(10, 96, 486, 63);
		
		paymentFilterCheck = new Button(composite_2, SWT.CHECK);
		paymentFilterCheck.setText("Filter by payment");
		paymentFilterCheck.setBounds(10, 13, 148, 18);
		
		Group group = new Group(composite_2, SWT.NONE);
		group.setBounds(164, 13, 312, 44);
		
		cashRadio = new Button(group, SWT.RADIO);
		cashRadio.setSelection(true);
		cashRadio.setText("Cash");
		cashRadio.setBounds(10, 10, 62, 18);
		
		creditRadio = new Button(group, SWT.RADIO);
		creditRadio.setText("Credit");
		creditRadio.setBounds(146, 10, 74, 18);
		
		debitRadio = new Button(group, SWT.RADIO);
		debitRadio.setText("Debit");
		debitRadio.setBounds(78, 10, 62, 18);
		
		otherRadio = new Button(group, SWT.RADIO);
		otherRadio.setText("Other");
		otherRadio.setBounds(226, 10, 74, 18);
		
		Composite composite_3 = new Composite(shell, SWT.NONE);
		composite_3.setBounds(10, 165, 210, 307);
		
		labelFilterCheck = new Button(composite_3, SWT.CHECK);
		labelFilterCheck.setText("Filter by labels");
		labelFilterCheck.setBounds(10, 10, 116, 18);
		
		labelList = new Tree(composite_3, SWT.BORDER);
		labelList.setBounds(10, 34, 190, 132);
		
		Button selectLabelsButton = new Button(composite_3, SWT.NONE);
		selectLabelsButton.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent arg0) {
				IDialog selection = new LabelSelection();
				String[] labels = (String[])selection.open();
				
				for(String label : labels)
				{
					// add each label to the list
				}
			}
		});
		selectLabelsButton.setBounds(93, 172, 107, 28);
		selectLabelsButton.setText("Select Labels");
		
		Group grpLabelSet = new Group(composite_3, SWT.NONE);
		grpLabelSet.setText("Expense Must Have");
		grpLabelSet.setBounds(10, 206, 190, 91);
		
		Button expenseAllRadio = new Button(grpLabelSet, SWT.RADIO);
		expenseAllRadio.setBounds(10, 10, 91, 18);
		expenseAllRadio.setText("All");
		
		Button expenseSomeRadio = new Button(grpLabelSet, SWT.RADIO);
		expenseSomeRadio.setSelection(true);
		expenseSomeRadio.setText("Some");
		expenseSomeRadio.setBounds(10, 36, 91, 18);
		
		Composite composite_4 = new Composite(shell, SWT.NONE);
		composite_4.setBounds(286, 165, 210, 307);
		
		payToFilterCheck = new Button(composite_4, SWT.CHECK);
		payToFilterCheck.setText("Filter by pay to");
		payToFilterCheck.setBounds(10, 10, 116, 18);
		
		payToList = new Tree(composite_4, SWT.BORDER);
		payToList.setBounds(10, 34, 190, 229);
		
		Button selectPayToButton = new Button(composite_4, SWT.NONE);
		selectPayToButton.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent arg0) {
				IDialog selection = new PaytoSelection();
				int payToID = (int)selection.open();
				
				// set the payTo id
			}
		});
		selectPayToButton.setBounds(91, 269, 109, 28);
		selectPayToButton.setText("Select Pay To");
		
		Button filterButton = new Button(shell, SWT.NONE);
		filterButton.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent arg0) {
				closeWithReturn(generateFilterFromGUI());
			}
		});
		filterButton.setBounds(402, 478, 94, 28);
		filterButton.setText("Filter");
		
		Button cancelButton = new Button(shell, SWT.NONE);
		cancelButton.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent arg0) {
				closeWithReturn(null);
			}
		});
		cancelButton.setBounds(10, 478, 94, 28);
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
			//output.assignLabels(inLabelSet, inSetOperation);
		}
		
		if(payToFilterCheck.getSelection())
		{
			//output.assignPayTo(inPayToSet);
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
	
	private void closeWithReturn(ExpenseFilter filter)
	{
		assert filter == null || filter != outputFilter;	// do not set the filter to itself
		
		outputFilter = filter;
		shell.close();
	}
}
