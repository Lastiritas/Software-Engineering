package domainobjects;

import java.util.Calendar;

public class SimpleDate implements Comparable<SimpleDate>
{
	private SimpleDate()
	{
		date = Calendar.getInstance();
	}
	
	public int getYear()
	{
		return date.get(Calendar.YEAR);
	}
	
	public int getMonth()
	{
		return date.get(Calendar.MONTH);
	}
	
	public int getDay()
	{
		return date.get(Calendar.DAY_OF_MONTH);
	}
	
	public void setYear(int inYear)
	{
		date.set(Calendar.YEAR, inYear);
	}
	
	public void setMonth(int inMonth)
	{
		date.set(Calendar.MONTH, inMonth);
	}
	
	public void setDay(int inDay)
	{
		date.set(Calendar.DAY_OF_MONTH, inDay);
	}
	
	@Override
	public String toString()
	{
		return String.format("%02d/%02d/%04d", getMonth(), getDay(), getYear());
	}
	
	@Override
	public int compareTo(SimpleDate o) 
	{
		return date.compareTo(o.date);
	}
	
	private Calendar date;
	
	public static SimpleDate Now()
	{
		return new SimpleDate();
	}
	
	public enum Month
	{
		JANUARY,
		FEBRURARY,
		MARCH,
		APRIL,
		MAY,
		JUNE,
		JULY,
		AUGUST,
		SEPTEMBER,
		OCTOBER,
		NOVEMEBER,
		DECEMBER
	}
}
