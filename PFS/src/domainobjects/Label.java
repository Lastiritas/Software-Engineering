package domainobjects;

public class Label implements INamed
{	
	public Label(String inName)
	{
		name = inName;
	}
	
	public String getName()
	{
		return name;
	}
	
	private final String name;
}
