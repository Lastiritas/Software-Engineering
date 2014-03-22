package system;

import dataaccesslayer.IDatabase;
import domainobjects.PayTo;

public class PayToManagement extends ManagementBase
{
	public PayToManagement(IDatabase inDatabase)
	{
		super(inDatabase.getPayToTable());
	}
	
	@Override
	protected Object getDefaultItem() 
	{
		return new PayTo("Somewhere New");
	}
	
	// the id to the no one entry in the database, if the database does 
	// not have this value something is wrong with the database
	public static final int NO_ONE_ID = 1;
}
