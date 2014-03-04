package gui;

import org.eclipse.swt.*;
import org.eclipse.swt.widgets.*;
import org.eclipse.swt.events.*;

import system.IDataModifer;
import system.PFSystem;
import util.StringMatch;
import domainobjects.*;
import dataAccessLayer.*;

public class FilterCreation implements IDialog
{
	protected Shell shell;

	private String allLabel[];
	private String retLabel=null;
	private Text text;
	private Text text_1;
	
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
		
		Button btnFilterByDate = new Button(composite, SWT.CHECK);
		btnFilterByDate.setBounds(10, 10, 147, 18);
		btnFilterByDate.setText("Filter by date");
		
		DateTime dateTime = new DateTime(composite, SWT.BORDER);
		dateTime.setBounds(163, 10, 93, 27);
		
		DateTime dateTime_1 = new DateTime(composite, SWT.BORDER);
		dateTime_1.setBounds(262, 10, 93, 27);
		
		Composite composite_1 = new Composite(shell, SWT.NONE);
		composite_1.setBounds(10, 53, 365, 37);
		
		Button btnFilterByAmount = new Button(composite_1, SWT.CHECK);
		btnFilterByAmount.setText("Filter by amount");
		btnFilterByAmount.setBounds(10, 10, 145, 18);
		
		text = new Text(composite_1, SWT.BORDER);
		text.setText("$0.00");
		text.setBounds(261, 10, 94, 19);
		
		text_1 = new Text(composite_1, SWT.BORDER);
		text_1.setText("$0.00");
		text_1.setBounds(161, 10, 94, 19);
		
		Composite composite_2 = new Composite(shell, SWT.NONE);
		composite_2.setBounds(10, 96, 365, 98);
		
		Button btnFilterByPayment = new Button(composite_2, SWT.CHECK);
		btnFilterByPayment.setText("Filter by payment");
		btnFilterByPayment.setBounds(10, 10, 148, 18);
		
		Group group = new Group(composite_2, SWT.NONE);
		group.setBounds(10, 40, 345, 44);
		
		Button button_1 = new Button(group, SWT.RADIO);
		button_1.setText("Cash");
		button_1.setBounds(14, 10, 62, 18);
		
		Button button_2 = new Button(group, SWT.RADIO);
		button_2.setText("Credit");
		button_2.setBounds(150, 10, 74, 18);
		
		Button button_3 = new Button(group, SWT.RADIO);
		button_3.setText("Debit");
		button_3.setBounds(82, 10, 62, 18);
		
		Button button_4 = new Button(group, SWT.RADIO);
		button_4.setText("Other");
		button_4.setBounds(230, 10, 74, 18);
		
		Composite composite_3 = new Composite(shell, SWT.NONE);
		composite_3.setBounds(10, 200, 173, 307);
		
		Button btnFilterByPayto = new Button(composite_3, SWT.CHECK);
		btnFilterByPayto.setText("Filter by pay to");
		btnFilterByPayto.setBounds(10, 10, 116, 18);
		
		Tree tree = new Tree(composite_3, SWT.BORDER);
		tree.setBounds(10, 34, 153, 132);
		
		Button btnSelectLabels = new Button(composite_3, SWT.NONE);
		btnSelectLabels.setBounds(56, 172, 107, 28);
		btnSelectLabels.setText("Select Labels");
		
		Group grpLabelSet = new Group(composite_3, SWT.NONE);
		grpLabelSet.setText("Expense Must Have");
		grpLabelSet.setBounds(10, 206, 153, 91);
		
		Button btnUnion = new Button(grpLabelSet, SWT.RADIO);
		btnUnion.setBounds(10, 10, 91, 18);
		btnUnion.setText("All");
		
		Button btnSome = new Button(grpLabelSet, SWT.RADIO);
		btnSome.setText("Some");
		btnSome.setBounds(10, 36, 91, 18);
		
		Composite composite_4 = new Composite(shell, SWT.NONE);
		composite_4.setBounds(202, 200, 173, 307);
		
		Button button = new Button(composite_4, SWT.CHECK);
		button.setText("Filter by pay to");
		button.setBounds(10, 10, 116, 18);
		
		Tree tree_1 = new Tree(composite_4, SWT.BORDER);
		tree_1.setBounds(10, 34, 153, 229);
		
		Button btnSelectPayTo = new Button(composite_4, SWT.NONE);
		btnSelectPayTo.setBounds(54, 269, 109, 28);
		btnSelectPayTo.setText("Select Pay To");
		
		Button btnFilter = new Button(shell, SWT.NONE);
		btnFilter.setBounds(281, 511, 94, 28);
		btnFilter.setText("Filter");
		
		Button btnCancel = new Button(shell, SWT.NONE);
		btnCancel.setBounds(10, 511, 94, 28);
		btnCancel.setText("Cancel");		
	}
}
