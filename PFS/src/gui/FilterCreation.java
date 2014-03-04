package gui;

import org.eclipse.swt.*;
import org.eclipse.swt.widgets.*;

public class FilterCreation implements IDialog
{
	protected Shell shell;

	private Text upperAmountText;
	private Text lowerAmountText;
	
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
		
		return null;
	}
	
	protected void createContents()
	{
		shell = new Shell(SWT.APPLICATION_MODAL);
		shell.setSize(385, 549);
		shell.setText("Label Creation");
		
		Composite composite = new Composite(shell, SWT.NONE);
		composite.setBounds(10, 10, 365, 37);
		
		Button dateFilterCheck = new Button(composite, SWT.CHECK);
		dateFilterCheck.setBounds(10, 10, 147, 18);
		dateFilterCheck.setText("Filter by date");
		
		DateTime lowerDateSelect = new DateTime(composite, SWT.BORDER);
		lowerDateSelect.setBounds(163, 10, 93, 27);
		
		DateTime upperDateSelect = new DateTime(composite, SWT.BORDER);
		upperDateSelect.setBounds(262, 10, 93, 27);
		
		Composite composite_1 = new Composite(shell, SWT.NONE);
		composite_1.setBounds(10, 53, 365, 37);
		
		Button amountFilterCheck = new Button(composite_1, SWT.CHECK);
		amountFilterCheck.setText("Filter by amount");
		amountFilterCheck.setBounds(10, 10, 145, 18);
		
		upperAmountText = new Text(composite_1, SWT.BORDER);
		upperAmountText.setText("$0.00");
		upperAmountText.setBounds(261, 10, 94, 19);
		
		lowerAmountText = new Text(composite_1, SWT.BORDER);
		lowerAmountText.setText("$0.00");
		lowerAmountText.setBounds(161, 10, 94, 19);
		
		Composite composite_2 = new Composite(shell, SWT.NONE);
		composite_2.setBounds(10, 96, 365, 98);
		
		Button paymentFilterCheck = new Button(composite_2, SWT.CHECK);
		paymentFilterCheck.setText("Filter by payment");
		paymentFilterCheck.setBounds(10, 10, 148, 18);
		
		Group group = new Group(composite_2, SWT.NONE);
		group.setBounds(10, 40, 345, 44);
		
		Button cashRadio = new Button(group, SWT.RADIO);
		cashRadio.setText("Cash");
		cashRadio.setBounds(14, 10, 62, 18);
		
		Button creditRadio = new Button(group, SWT.RADIO);
		creditRadio.setText("Credit");
		creditRadio.setBounds(150, 10, 74, 18);
		
		Button debitRadio = new Button(group, SWT.RADIO);
		debitRadio.setText("Debit");
		debitRadio.setBounds(82, 10, 62, 18);
		
		Button otherRadio = new Button(group, SWT.RADIO);
		otherRadio.setText("Other");
		otherRadio.setBounds(230, 10, 74, 18);
		
		Composite composite_3 = new Composite(shell, SWT.NONE);
		composite_3.setBounds(10, 200, 173, 307);
		
		Button labelFilterCheck = new Button(composite_3, SWT.CHECK);
		labelFilterCheck.setText("Filter by labels");
		labelFilterCheck.setBounds(10, 10, 116, 18);
		
		Tree labelList = new Tree(composite_3, SWT.BORDER);
		labelList.setBounds(10, 34, 153, 132);
		
		Button selectLabelsButton = new Button(composite_3, SWT.NONE);
		selectLabelsButton.setBounds(56, 172, 107, 28);
		selectLabelsButton.setText("Select Labels");
		
		Group grpLabelSet = new Group(composite_3, SWT.NONE);
		grpLabelSet.setText("Expense Must Have");
		grpLabelSet.setBounds(10, 206, 153, 91);
		
		Button expenseAllRadio = new Button(grpLabelSet, SWT.RADIO);
		expenseAllRadio.setBounds(10, 10, 91, 18);
		expenseAllRadio.setText("All");
		
		Button expenseSomeRadio = new Button(grpLabelSet, SWT.RADIO);
		expenseSomeRadio.setText("Some");
		expenseSomeRadio.setBounds(10, 36, 91, 18);
		
		Composite composite_4 = new Composite(shell, SWT.NONE);
		composite_4.setBounds(202, 200, 173, 307);
		
		Button payToFilterCheck = new Button(composite_4, SWT.CHECK);
		payToFilterCheck.setText("Filter by pay to");
		payToFilterCheck.setBounds(10, 10, 116, 18);
		
		Tree payToList = new Tree(composite_4, SWT.BORDER);
		payToList.setBounds(10, 34, 153, 229);
		
		Button selectPayToButton = new Button(composite_4, SWT.NONE);
		selectPayToButton.setBounds(54, 269, 109, 28);
		selectPayToButton.setText("Select Pay To");
		
		Button filterButton = new Button(shell, SWT.NONE);
		filterButton.setBounds(281, 511, 94, 28);
		filterButton.setText("Filter");
		
		Button cancelButton = new Button(shell, SWT.NONE);
		cancelButton.setBounds(10, 511, 94, 28);
		cancelButton.setText("Cancel");		
	}
}
