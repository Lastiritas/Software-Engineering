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
	
	
	public String createSQLWhereClause()	// returns the where-clause for a sql expense query
	{
		StringBuilder statement = new StringBuilder();
		
		// WHERE...
		
		if(useDateRange)
		{
			// add the condition which says the date must be in range: date >= daterange.min and date <= daterange.max
			statement.append(String.format("(date >= %d and date <= %d)", dateRange.getLower().toInteger(), dateRange.getUpper().toInteger()));
		}
		
		if(useMoneyRange)
		{
			// add the condition which says the amount must be in range: amount >= amountrange.min and amount <= amountrange.max
			statement.append(String.format("(amount >= %d and amount <= %d)", moneyRange.getLower().getTotalCents(), moneyRange.getUpper().getTotalCents()));
		}
		
		if(usePaymentMethod)
		{
			// add the condition which says the payment method must match: method = paymentmethod
			statement.append(String.format("(method = %d)", PaymentMethodHelper.toInteger(paymentMethod)));
		}
		
		if(usePayTo)
		{
			// add the condition which says the expense must have the payTos: payto in selectedpaytos
			statement.append("(payto in (");
			
			for(int i = 0; i < payTos.getSize(); i++)
			{
				statement.append(payTos.getValue(i));
				statement.append(',');
			}
			
			statement.replace(statement.length() - 1, statement.length(), ")");
			statement.append(")");
		}
		
		if(useLabels)
		{
			// ids = (SELECT id FROM expenselabels WHERE label = labels[0]) INTERECT/UNION (SELECT id FROM expenselabels WHERE label = labels[1]) ... 
			
			// add the condition which says the label must have the labels: id in ids
			
			String setOperation = " UNION ";
			if(labelSetOperation == SetOperation.INTERSECTION)
			{
				statement.append(" INTERSECT ");
			}
			
			statement.append("(id in (");
			
			for(int i = 0; i < labels.getSize(); i++)
			{
				if(i > 0)
				{
					statement.append(setOperation);
				}
				
				statement.append(String.format("(SELECT id FROM expenselabels WHERE label = %d)", labels.getValue(i)));				
			}
			
			statement.append("))");
		}
		
		return statement.toString();
	}
	
	@Override
	public String toString()
	{
		return "Where " + createSQLWhereClause();
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
