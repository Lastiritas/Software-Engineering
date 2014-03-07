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

public class EditExpense {

	protected Shell shell;
	private Text descriptionField;
	private Text payToField;
	private Text amountField;
	
	private int payToID;
	private Expense expense;

	/**
	 * Launch the application.
	 * @param args
	 */
	public static void main(String[] args) {
		try {
			EditExpense window = new EditExpense();
			window.open(0);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	/**
	 * Open the window.
	 */
	public void open(int eID) {
		Display display = Display.getDefault();
		createContents(eID);
		readData();
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
	 */
	protected void createContents(final int eID) {
		shell = new Shell();
		shell.setSize(502, 333);
		shell.setText("Expense Edit");
		
		PaymentMethod method = (((Expense) PFSystem.getCurrent().getExpenseSystem().getDataByID(eID)).getPaymentMethod());
		
		descriptionField = new Text(shell, SWT.BORDER);
		descriptionField.setBounds(10, 127, 213, 129);
		descriptionField.setText(((Expense) PFSystem.getCurrent().getExpenseSystem().getDataByID(eID)).getDescription());
		
		Label lblDescription = new Label(shell, SWT.NONE);
		lblDescription.setText("Description");
		lblDescription.setBounds(10, 107, 83, 14);
		
		final List labelsList = new List(shell, SWT.BORDER);
		labelsList.setBounds(261, 127, 213, 129);
		//labelsList.set...
		
		Button btnEditLabels = new Button(shell, SWT.NONE);
		btnEditLabels.addSelectionListener(new SelectionAdapter() 
		{
			@Override
			public void widgetSelected(SelectionEvent arg0) 
			{	
				IDSet allLabelIDs = ((Expense)PFSystem.getCurrent().getExpenseSystem().getDataByID(eID)).getLabels();
				
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
		btnEditLabels.setText("Edit Labels");
		btnEditLabels.setBounds(280, 262, 94, 28);
		
		Label lblLabels = new Label(shell, SWT.NONE);
		lblLabels.setText("Labels");
		lblLabels.setBounds(261, 107, 83, 14);
		
		Group grpPaymentMethod = new Group(shell, SWT.NONE);
		grpPaymentMethod.setText("Payment Method");
		grpPaymentMethod.setBounds(251, 10, 223, 74);
		
		final Button cashRadio = new Button(grpPaymentMethod, SWT.RADIO);
		cashRadio.setText("Cash");
		cashRadio.setBounds(14, 22, 91, 18);
		
		final Button creditRadio = new Button(grpPaymentMethod, SWT.RADIO);
		creditRadio.setText("Credit");
		creditRadio.setBounds(14, 46, 91, 18);
		
		final Button debitRadio = new Button(grpPaymentMethod, SWT.RADIO);
		debitRadio.setText("Debit");
		debitRadio.setBounds(118, 22, 91, 18);
		
		Button otherRadio = new Button(grpPaymentMethod, SWT.RADIO);
		otherRadio.setText("Other");
		otherRadio.setBounds(118, 46, 91, 18);
		
		cashRadio.setSelection(method == PaymentMethod.CASH);
		debitRadio.setSelection(method == PaymentMethod.DEBIT);
		creditRadio.setSelection(method == PaymentMethod.CREDIT);
		otherRadio.setSelection(method == PaymentMethod.OTHER);
		
		final DateTime datePicker = new DateTime(shell, SWT.BORDER);
		datePicker.setDate(((Expense) PFSystem.getCurrent().getExpenseSystem().getDataByID(eID)).getDate().getDay(),((Expense) PFSystem.getCurrent().getExpenseSystem().getDataByID(eID)).getDate().getMonth(),((Expense) PFSystem.getCurrent().getExpenseSystem().getDataByID(eID)).getDate().getYear());
		datePicker.setBounds(110, 35, 123, 27);
		
		payToField = new Text(shell, SWT.BORDER);
		payToField.setEditable(false);
		
	/*	Better to have actual payTo returned by Expense
	 	int payToID = ((Expense) PFSystem.getCurrent().getExpenseSystem().getDataByID(eID)).getPayTo();
		StubDatabase data = new StubDatabase();
		payToField.setText(data.getPayToByID(payToID).toString());
	*/
		payToField.setBounds(110, 10, 123, 19);
		
		amountField = new Text(shell, SWT.BORDER);
		amountField.setText(((Expense) PFSystem.getCurrent().getExpenseSystem().getDataByID(eID)).getAmount().toString());
		amountField.setBounds(110, 68, 123, 19);
		
		Label lblAmount = new Label(shell, SWT.NONE);
		lblAmount.setText("Amount");
		lblAmount.setBounds(10, 70, 59, 14);
		
		Button payToButton = new Button(shell, SWT.NONE);
		payToButton.setAlignment(SWT.LEFT);
		payToButton.setText("Pay To");
		payToButton.setBounds(10, 7, 94, 24);
		payToButton.addSelectionListener(new SelectionAdapter() 
		{
			@Override
			public void widgetSelected(SelectionEvent arg0) 
			{
				PaytoSelection window = new PaytoSelection();
				payToID = (int)window.open();
				
				PayTo payTo = (PayTo)PFSystem.getCurrent().getPayToSystem().getDataByID(payToID);
				
				payToField.setText(payTo.getPayToName() + ", " + payTo.getPayToBranch());
			}
		});
		
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
				SimpleDate date = SimpleDate.Now();
				date.setDay(datePicker.getDay());
				date.setMonth(datePicker.getMonth());
				date.setYear(datePicker.getYear());
				
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
				
				expense = new Expense(date, Money.fromString(amountField.getText()), method, descriptionField.getText(), payToID, set);
				PFSystem.getCurrent().getExpenseSystem().update(eID, expense);
				
				shell.close();
		
			}
		});
		
		Button btnCancel = new Button(shell, SWT.NONE);
		btnCancel.setText("Cancel");
		btnCancel.setBounds(10, 262, 94, 28);
		btnCancel.addSelectionListener(new SelectionAdapter()
		{
			@Override
			public void widgetSelected(SelectionEvent arg0)
			{
				shell.close();
			}
		});

	}
	
	private void readData(){
	}
	

}
