package domainobjects;

public class ExpenseFilter 
{
	public ExpenseFilter()
	{
		
	}
	
	public void assignDateRange(DateRange inRange)
	{
		useDateRange = true;
		dateRange = inRange;
	}

	public void assignMoneyRange(MoneyRange inRange)
	{
		useMoneyRange = true;
		moneyRange = inRange;
	}
	
	public void assignmentPaymentMethod(PaymentMethod inMethod)
	{
		usePaymentMethod = true;
		paymentMethod = inMethod;
	}
	
	public void assignPayTo(IDSet inPayToSet)
	{
		usePayTo = true;
		payTos = inPayToSet;
	}
	
	public void assignLabels(IDSet inLabelSet, SetOperation inSetOperation)
	{
		useLabels = true;
		labels = inLabelSet;
		labelSetOperation = inSetOperation;
	}
	
	public String createSQLStatement()
	{
		StringBuilder statement = new StringBuilder();
		
		//statement.append("SELECT * FROM expenses")
		statement.append("SELECT id FROM expenses WHERE ");
		
		if(useDateRange)
		{
			// add the condition which says the date must be in range
		}
		
		if(useMoneyRange)
		{
			// add the condition which says the amount must be in range
		}
		
		if(usePaymentMethod)
		{
			// add the condition which says the payment method must match
		}
		
		if(usePayTo)
		{
			// add the condition which says the expense must have the payTos
		}
		
		if(useLabels)
		{
			// add the condition which says the label must have the labels
		}
		
		return statement.toString();
	}
	
	private DateRange dateRange = null;
	private boolean useDateRange = false;
	
	private MoneyRange moneyRange = null;
	private boolean useMoneyRange = false;
	
	private PaymentMethod paymentMethod = PaymentMethod.CASH;
	private boolean usePaymentMethod = false;
	
	private IDSet payTos = IDSet.empty();
	// we don't need a set operation for paytos because an expense can only have one payto
	// so we know it must be a union of all the expenses
	private boolean usePayTo = false;
	
	private IDSet labels = IDSet.empty();
	private SetOperation labelSetOperation = SetOperation.UNION;
	private boolean useLabels = false;
}
