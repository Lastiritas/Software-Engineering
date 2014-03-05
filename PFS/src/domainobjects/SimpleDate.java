package domainobjects;

import java.util.Calendar;

public class SimpleDate implements Comparable<SimpleDate>
{
	private SimpleDate()
	{
		date = Calendar.getInstance();
	}
	
	public void setDate(String inDate)
	{
		final String temp[] = inDate.split("/");

		if(temp.length==3)
		{
			this.setDay(Integer.parseInt(temp[0]));
			this.setMonth(Integer.parseInt(temp[1]));
			this.setYear(Integer.parseInt(temp[2]));
		}
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
	
	public int compareTo(SimpleDate inDate)
	{
		if(this.getYear() > inDate.getYear())
		{
			return 1;
		}
		else if(this.getYear() < inDate.getYear())
		{
			return -1;
		}
		
		if(this.getMonth() > inDate.getMonth())
		{
			return 1;
		}
		else if(this.getMonth() < inDate.getMonth())
		{
			return -1;
		}
		
		if(this.getDay() > inDate.getDay())
		{
			return 1;
		}
		else if(this.getDay() < inDate.getDay())
		{
			return -1;
		}
		
		return 0;
	}
	
	public boolean equals(Object inObject)
	{
		if(inObject ==null)
		{
			return false;
		}
		
		if(!(inObject instanceof SimpleDate))
		{
			return false;
		}
		
		SimpleDate inDate = (SimpleDate)inObject;
		return this.getDay() == inDate.getDay() && this.getMonth() == inDate.getMonth() && this.getYear() == inDate.getYear();
	}
	
	public int toInteger()
	{
		// #### ## ##
		return (getYear() * 10000) + (getMonth() * 100) + getDay(); 
	}
	
	@Override
	public String toString()
	{
		return String.format("%02d/%02d/%04d", getDay(), getMonth(), getYear());
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
