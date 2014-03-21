package system;

import domainobjects.IDSet;

public class GroupedCollection
{
	public GroupedCollection(String inName)
	{
		name = inName;
	}
	
	public String getName()
	{
		return name;
	}
	
	public void expandToInclude(IDSet inSet)
	{
		items = items.union(inSet);
	}
	
	public IDSet getAllItems()
	{
		return items;
	}
	
	private String name;
	private IDSet items = IDSet.empty();
}
