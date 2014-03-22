package system;

import dataaccesslayer.IDatabase;
import domainobjects.PayTo;

public class PayToManagement extends ManagementBase
{
	public PayToManagement(IDatabase inDatabase)
	{
		super(inDatabase);
	}

	@Override
	protected int[] dbCallGetIds(IDatabase database)
	{
		return database.getAllPayToIDs();
	}
	
	@Override
	protected int[] dbCallGetIds(IDatabase database, String whereClause)
	{
		assert false : "Getting paytos with a where statement is not supported";
	
		return null;
	}
	
	@Override
	protected Object dbCallGetItem(IDatabase database, int inId)
	{
		return database.getPayToByID(inId);
	}
	
	@Override
	protected boolean dbCallUpdateItem(IDatabase database, int inId, Object inNewValue)
	{
		assert inNewValue instanceof PayTo : "Can only update with PayTo objects";

		return database.updatePayTo(inId, (PayTo)inNewValue);
	}
	
	@Override
	protected boolean dbCallDeleteItem(IDatabase database, int inID)
	{
		assert false : "Cannot delete a payto location, do not call this function";

		return false;
	}
	
	@Override
	protected int dbCallAddNew(IDatabase database)
	{
		PayTo newPayTo = new PayTo("Somewhere New");

		return database.addPayTo(newPayTo);
	}
	
	// the id to the no one entry in the database, if the database does 
	// not have this value something is wrong with the database
	public static final int NO_ONE_ID = 1;	
}
