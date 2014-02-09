package gui;

public class Money implements Comparable
{
	public Money(int inDollars, int inCents)
	{
		assert(inCents >= 0 && inCents < 100);
		assert(inDollars >= 0);
		
		cents = inCents;
		dollars = inDollars;
		
		stringForm = "$" + inDollars + "." + inCents;
	}
	
	@Override
	public int compareTo(Object inObject)
	{
		assert inObject != null;
		assert inObject instanceof Money;
		
		Money money = (Money)inObject;
		
		if(dollars < money.dollars)
		{
			return -1;
		}
		else if(dollars > money.dollars)
		{
			return 1;
		}
		else
		{
			if(cents < money.cents)
			{
				return -1;
			}
			else if(cents > money.cents)
			{
				return 1;
			}
			else
			{
				return 0;
			}
		}
	}

	@Override
	public String toString()
	{
		return stringForm;
	}
	
	private int cents;
	private int dollars;
	private String stringForm;
}
