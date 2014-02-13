package domainobjects;

public class Label 
{
	public Label(int inID, String inName)
	{
		id = inID;
		name = inName;
	}
	
	public Label(String inName)
	{
		id = 0;
		name = inName;
	}
	
	public int getLabelID()
	{
		return id;
	}
	
	public String getLabelName()
	{
		return name;
	}
	
	private int id;
	private final String name;
}
