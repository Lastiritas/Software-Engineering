package system;

import dataaccesslayer.IDatabase;
import domainobjects.Label;

public class LabelManagement extends ManagementBase
{
	public LabelManagement(IDatabase inDatabase)
	{
		super(inDatabase);
	}

	@Override
	protected int[] dbCallGetIds(IDatabase database)
	{
		return database.getLabelTable().getAllIds();
	}
	
	@Override
	protected int[] dbCallGetIds(IDatabase database, String whereClause)
	{
		assert false : "Getting labels with a where statement is not supported";

		return null;
	}
	
	@Override
	protected Object dbCallGetItem(IDatabase database, int inId)
	{		
		return database.getLabelTable().getById(inId);
	}
	
	@Override
	protected boolean dbCallUpdateItem(IDatabase database, int inId, Object inNewValue)
	{
		assert inNewValue instanceof Label : "Can only update with a label";

		return database.getLabelTable().update(inId, (Label)inNewValue);
	}
	
	@Override
	protected boolean dbCallDeleteItem(IDatabase database, int inID)
	{
		assert false : "Labels can not be deleted, do not call this!";

		return false;
	}
	
	@Override
	protected int dbCallAddNew(IDatabase database)
	{
		Label newLabel = new Label("New Label");
		return database.getLabelTable().add(newLabel);
	}
}
