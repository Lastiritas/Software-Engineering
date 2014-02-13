package gui;

public class Money implements Comparable
{
	public Money(int inDollars, int inCents)
	{
		assert(inCents >= 0 && inCents < 100);
		assert(inDollars >= 0);
		
		cents = inCents;
		dollars = inDollars;
		
		stringForm = String.format("$%d.%02d", inDollars, inCents);
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

	public int getTotalCents()
	{
		return dollars * 100 + cents;
	}
	
	@Override
	public String toString()
	{
		return stringForm;
	}
	
	private int cents;
	private int dollars;
	private String stringForm;

	public static Money fromString(String inString)
	{
		String workingString = inString.trim();
		
		if(workingString.startsWith("$"))
		{
			workingString = workingString.substring(1);
		}
		
		final int index = workingString.indexOf('.');
		
		if(index >= 0)
		{
			if(workingString.length() - index == 3)
			{
				workingString = workingString.replace(".", "");
			}
			else
			{
				return null;	// bad decimal
			}
		}
		else
		{
			workingString += "00";
		}
		
		try
		{
			final int totalCents = Integer.parseInt(workingString);
			
			return new Money(totalCents / 100, totalCents % 100);
		}
		catch(Exception ex)
		{
			return null;
		}
	}
}
