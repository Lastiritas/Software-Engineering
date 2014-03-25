package system;

import java.util.ArrayList;
import java.util.Collection;

import domainobjects.IDSet;

public class GroupedCollection
{
	public GroupedCollection(String inName)
	{
		name = inName;
		sets = new ArrayList<IDSet>();
	}
	
	public String getName()
	{
		return name;
	}
	
	public void include(IDSet inSet)
	{
		sets.add(inSet);
	}
	
	public void include(Collection<IDSet> inSets)
	{
		sets.addAll(inSets);
	}
	
	public Collection<IDSet> getAll()
	{
		return sets;
	}
	
	private String name;
	private Collection<IDSet> sets;
}
