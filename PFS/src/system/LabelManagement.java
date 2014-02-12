package system;

import java.util.Vector;

import dataAccessLayer.*;
import domainobjects.IDSet;
import domainobjects.Label;

public class LabelManagement implements IIDReader, IDataReader, IDataModifer
{
	public LabelManagement(IDatabase inDatabase)
	{
		assert inDatabase != null : "Must provide non-null database";

		database = inDatabase;
	}
	
	public IDSet getAllIDs()
	{
		final Vector<Integer> ids = database.getAllLabelIDs();
		final int[] setData = new int[ids.size()];
		
		for(int i = 0; i < setData.length; i++)
		{
			setData[i] = ids.get(i).intValue();
		}

		final IDSet output = IDSet.createFromArray(setData);
		
		return output;
	}
	
	public Object getDataByID(int inId)
	{
		assert inId >= 0 : "Invalid id";

		return database.getLabelByID(inId);
	}
	
	public int new()
	{
		Label newLabel = new Label("New Label");
		return database.addLabel(newLabel);
	}
	
	public boolean update(int inId, Object inNewValue)
	{
		assert inId >= 0 : "Invalid id";
		assert inNewValue != null : "Can not update with null value";
		assert inNewValue instanceof Label : "Can only update with a label";

		return database.updateLabel(inId, inNewValue);
	}

	public boolean delete(int inID)
	{
		assert false : "Labels can not be deleted, do not call this!";

		return false;
	}
		
	private IDatabase database;
}
