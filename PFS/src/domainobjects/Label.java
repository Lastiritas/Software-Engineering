package domainobjects;

public class Label 
{	
	public Label(String inName)
	{
		name = inName;
	}
	
	public String getLabelName()
	{
		return name;
	}
	
	private final String name;
}
