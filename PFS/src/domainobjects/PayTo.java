package domainobjects;

public class PayTo implements INamed
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
