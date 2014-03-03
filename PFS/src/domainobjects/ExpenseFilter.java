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
		return "SELECT * FROM expenses";
	}
	
	private DateRange dateRange = null;
	private boolean useDateRange = false;
	
	private MoneyRange moneyRange = null;
	private boolean useMoneyRange = false;
	
	private IDSet payTos = IDSet.empty();
	private boolean usePayTo = false;
	// we don't need a set operation for paytos because an expense can only have one payto
	// so we know it must be a union of all the expenses
	
	private IDSet labels = IDSet.empty();
	private boolean useLabels = false;
	private SetOperation labelSetOperation = SetOperation.UNION;
}
