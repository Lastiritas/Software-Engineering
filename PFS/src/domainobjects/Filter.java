package domainobjects;

public class Filter 
{
	public Filter()
	{
		
	}
	
	// --- Date ----------------------------------------------------------------
	public void assignDateRange(DateRange inRange)
	{
		useDateRange = true;
		dateRange = inRange;
	}
	
	public boolean isUsingDateRange()
	{
		return useDateRange;
	}
	
	public DateRange getDateRange()
	{
		assert isUsingDateRange();
		
		return dateRange;
	}

	// --- Money ----------------------------------------------------------------
	public void assignMoneyRange(MoneyRange inRange)
	{
		useMoneyRange = true;
		moneyRange = inRange;
	}
	
	public boolean isUsingMoneyRange()
	{
		return useMoneyRange;
	}
	
	public MoneyRange getMoneyRange()
	{
		assert isUsingMoneyRange();
		
		return moneyRange;
	}
	
	// --- PayTo ----------------------------------------------------------------
	public void assignPayTo(IDSet inPayToSet)
	{
		usePayTo = true;
		payTos = inPayToSet;
	}
	
	public boolean isUsingPayTo()
	{
		return usePayTo;
	}
	
	public IDSet getPayTos()
	{
		assert isUsingPayTo();
		
		return payTos;
	}
	
	// --- Label ----------------------------------------------------------------
	public void assignLabels(IDSet inLabelSet, SetOperation inSetOperation)
	{
		useLabels = true;
		labels = inLabelSet;
		labelSetOperation = inSetOperation;
	}
	
	public boolean isUsingLabels()
	{
		return useLabels;
	}
	
	public IDSet getLabels()
	{
		assert isUsingLabels();
		
		return labels;
	}
	
	public SetOperation getLabelSetOperation()
	{
		assert isUsingLabels();
		
		return labelSetOperation;
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
