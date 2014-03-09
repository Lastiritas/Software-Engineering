package system;

import dataAccessLayer.*;
import domainobjects.IDHelper;
import domainobjects.IDSet;
import domainobjects.PayTo;

public class PayToManagement implements IIDReader, IDataReader, IDataModifer
{
	public PayToManagement(IDatabase inDatabase)
	{
		assert inDatabase != null : "Must provide non-null database";

		database = inDatabase;
	}
	
	public IDSet getAllIDs()
	{
		final int[] setData = database.getAllPayToIDs();
		assert setData != null : "Database returned null array";
		
		final IDSet output = IDSet.createFromArray(setData);
		
		return output;
	}
	
	public Object getDataByID(int inId)
	{
		if(IDHelper.isIdValid(inId))
		{
			return database.getPayToByID(inId);	
		}
		
		return NULL_PAYTO;
	}
	
	public int create()
	{
		PayTo newPayTo = new PayTo("Somewhere New", "");

		return database.addPayTo(newPayTo);
	}
	
	public boolean update(int inId, Object inNewValue)
	{
		assert IDHelper.isIdValid(inId) : "Invalid ID";
		assert inNewValue != null : "Cannot update with null value";
		assert inNewValue instanceof PayTo : "Can only update with PayTo objects";

		return database.updatePayTo(inId, (PayTo)inNewValue);
	}
	
	public boolean delete(int inID)
	{
		assert false : "Cannot delete a payto location, do not call this function";

		return false;
	}

	private IDatabase database;

	private final static PayTo NULL_PAYTO = new PayTo("None"); 
}
