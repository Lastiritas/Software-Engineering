package system;

import dataaccesslayer.IDatabase;
import domainobjects.Label;

public class LabelManagement extends ManagementBase
{
	public LabelManagement(IDatabase inDatabase)
	{
		super(inDatabase.getLabelTable());
	}

	@Override
	protected Object getDefaultItem() 
	{
		return new Label("New Label");
	}
}
