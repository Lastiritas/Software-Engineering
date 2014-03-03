package domainobjects;

public class MoneyRange implements IRange<Money> 
{
	public MoneyRange(Money inLower, Money inUpper)
	{
		assert inLower.compareTo(inUpper) <= 0;
		
		lowerBounds = inLower;
		upperBounds = inUpper;
	}
	
	public Money getLower()
	{
		return lowerBounds;
	}
	
	public Money getUpper()
	{
		return upperBounds;
	}
	
	private Money lowerBounds;
	private Money upperBounds;
}
