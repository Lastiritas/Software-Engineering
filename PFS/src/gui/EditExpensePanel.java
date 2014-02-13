package gui;

import javax.swing.ButtonGroup;
import javax.swing.ButtonModel;
import javax.swing.JPanel;
import javax.swing.JLabel;
import javax.swing.JTextField;
import javax.swing.SwingConstants;
import javax.swing.JRadioButton;
import javax.swing.JButton;

import domainobjects.Expense;
import domainobjects.IDSet;
import domainobjects.PaymentMethod;
import domainobjects.SimpleDate;

import java.awt.BorderLayout;

public class EditExpensePanel extends JPanel 
{
	private JPanel datePanel;
	private JPanel payToPanel;
	private JPanel panel_3;
	private JPanel panel_4;
	private JPanel panel_5;
	private JPanel panel_2;
	private JPanel panel;
	private JPanel paymentMethodPanel;
	private JPanel panel_7;
	
	private DatePicker datePicker;
	private JTextField amountField;
	private JTextField payToField;
	private JTextField descriptionField;
	
	private ButtonGroup paymentMethodGroup;
	private JRadioButton cashRadioButton;
	private JRadioButton debitRadioButton;
	private JRadioButton creditRadioButton;
	private JRadioButton otherRadioButton;
		
	public EditExpensePanel() 
	{
		setLayout(new BorderLayout(0, 0));
		
		datePanel = new JPanel();
		add(datePanel, BorderLayout.NORTH);
		datePanel.setLayout(new BorderLayout(0, 0));
		datePicker = new DatePicker(SimpleDate.Now());
		datePanel.add(datePicker, BorderLayout.WEST);
		
		payToPanel = new JPanel();
		add(payToPanel, BorderLayout.CENTER);
		payToPanel.setLayout(new BorderLayout(0, 0));
		
		panel_3 = new JPanel();
		payToPanel.add(panel_3, BorderLayout.CENTER);
		
		payToField = new JTextField();
		panel_3.add(payToField);
		payToField.setColumns(40);
		
		panel_4 = new JPanel();
		payToPanel.add(panel_4, BorderLayout.EAST);
		
		JButton payToButton = new JButton("Change Pay To");
		panel_4.add(payToButton);
		
		panel_5 = new JPanel();
		payToPanel.add(panel_5, BorderLayout.WEST);
		
		JLabel payToLabel = new JLabel("Pay To");
		panel_5.add(payToLabel);
		
		panel_2 = new JPanel();
		add(panel_2, BorderLayout.SOUTH);
		panel_2.setLayout(new BorderLayout(0, 0));
		
		panel = new JPanel();
		panel_2.add(panel, BorderLayout.WEST);
		
		JLabel lblNewLabel = new JLabel("Amount");
		panel.add(lblNewLabel);
		
		amountField = new JTextField();
		amountField.setHorizontalAlignment(SwingConstants.RIGHT);
		amountField.setText("$0.00");
		panel.add(amountField);
		amountField.setColumns(10);
		
		paymentMethodPanel = new JPanel();
		panel_2.add(paymentMethodPanel);
		
		JLabel lblNewLabel_1 = new JLabel("Payment Method");
		paymentMethodPanel.add(lblNewLabel_1);
		
		paymentMethodGroup = new ButtonGroup();
		
		cashRadioButton = new JRadioButton("Cash");
		paymentMethodGroup.add(cashRadioButton);
		paymentMethodPanel.add(cashRadioButton);
		
		debitRadioButton = new JRadioButton("Debit");
		paymentMethodGroup.add(debitRadioButton);
		paymentMethodPanel.add(debitRadioButton);
		
		creditRadioButton = new JRadioButton("Credit");
		paymentMethodGroup.add(creditRadioButton);
		paymentMethodPanel.add(creditRadioButton);
		
		otherRadioButton = new JRadioButton("Other");
		paymentMethodGroup.add(otherRadioButton);
		paymentMethodPanel.add(otherRadioButton);
		
		panel_7 = new JPanel();
		panel_2.add(panel_7, BorderLayout.SOUTH);
		panel_7.setLayout(new BorderLayout(0, 0));
		
		JLabel lblNewLabel_2 = new JLabel("Description");
		panel_7.add(lblNewLabel_2, BorderLayout.WEST);
		
		descriptionField = new JTextField();
		panel_7.add(descriptionField);
		descriptionField.setColumns(10);
	}

	
	public void setExpense(Expense expense)
	{	
		datePicker.setDate(expense.getDate());
		
		payToField.setText("" + expense.getPayTo());
		
		Money money = new Money(expense.getDollars(), expense.getCents());
		amountField.setText(money.toString());
		
		descriptionField.setText(expense.GetDescription());
		
		PaymentMethod paymentMethod = expense.getPaymentMethod();
				
		switch(paymentMethod)
		{
		case CASH: 		
			paymentMethodGroup.setSelected(cashRadioButton.getModel(), true);
		break;
			
		case DEBIT: 
			paymentMethodGroup.setSelected(debitRadioButton.getModel(), true);
		break;
		
		case CREDIT: 
			paymentMethodGroup.setSelected(creditRadioButton.getModel(), true);
		break;
		
		case OTHER: 
			paymentMethodGroup.setSelected(otherRadioButton.getModel(), true);
		break;
		}
	}
	
	public Expense getExpense()
	{
		SimpleDate date = datePicker.getDate();
		
		PaymentMethod method;
		ButtonModel selectedButton = paymentMethodGroup.getSelection();
		if(selectedButton == cashRadioButton.getModel())
		{
			method = PaymentMethod.CASH;
		}
		else if(selectedButton == debitRadioButton.getModel())
		{
			method = PaymentMethod.DEBIT;
		}
		else if(selectedButton == creditRadioButton.getModel())
		{
			method = PaymentMethod.CREDIT;
		}
		else
		{
			method = PaymentMethod.OTHER;
		}
		
		int[] setData = {};
		IDSet labelSet = IDSet.createFromArray(setData);
		
		Money money = Money.fromString(amountField.getText());
		
		return new Expense(date, money.getTotalCents(), method, descriptionField.getText(), -1, labelSet);
	}
	
	public void clear()
	{
		payToField.setText("");
		amountField.setText("$0.00");
		descriptionField.setText("");
		
		cashRadioButton.setEnabled(true);
	}
}
