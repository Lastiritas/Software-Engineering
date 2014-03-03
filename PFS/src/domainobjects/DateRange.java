package domainobjects;

public class DateRange implements IRange<SimpleDate>
{
	public DateRange(SimpleDate start, SimpleDate end)
	{	
		assert start.compareTo(end) <= 0;
		
		startingDate = start;
		endingDate = end;
	}
	
	public SimpleDate getLower()
	{
		return startingDate;
	}
	
	public SimpleDate getUpper()
	{
		return endingDate;
	}
	
	private final SimpleDate startingDate;
	private final SimpleDate endingDate;
}
