package domainobjects;

public class PayTo 
{	
	public PayTo(String inName)
	{
		name = inName;
	}
	
	public String getName()
	{
		return name;
	}
	
	public String toString()
	{
		return name;
	}
	
	private final String name;
}
