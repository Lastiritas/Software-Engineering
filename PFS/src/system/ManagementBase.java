package system;

import util.Sort;
import util.SortDirection;
import util.TableCols;
import dataaccesslayer.IDatabase;
import domainobjects.ExpenseFilter;
import domainobjects.IDHelper;
import domainobjects.IDSet;

public abstract class ManagementBase implements IIDReader, IDataReader, IDataModifer
{
	public ManagementBase(IDatabase inDatabase)
	{
		assert inDatabase != null : "Must provide non-null database";
		
		database = inDatabase;
		cache = new Cache(CACHE_SIZE);
	}
	
	public IDSet getAllIDs()
	{	
		final int[] setData = dbCallGetIds(database);
		assert setData != null : "Database returned null array";
	
		return IDSet.createFromArray(setData);
	}
	
	public IDSet getAllIDs(ExpenseFilter filter, TableCols sortBy, SortDirection direction)
	{
		String filterWhereClause = filter.createSQLWhereClause();	
		String whereClause = Sort.createSQLWhereClause(filterWhereClause, sortBy, direction);
		
		int[] setData = dbCallGetIds(database, whereClause);
		
		assert setData != null : "Database returned null array";
	
		return IDSet.createFromArray(setData);
	}
	
	public Object getDataByID(int inID)
	{
		assert IDHelper.isIdValid(inID) : "Invalid ID";

		Object value = cache.tryGet(inID);
		
		if(value == null)
		{
			value = dbCallGetItem(database, inID);
			cache.set(inID, value);
		}
		
		return value;
	}
	
	public boolean update(int inID, Object inNewValue)
	{
		assert IDHelper.isIdValid(inID) : "Invalid ID";
		assert inNewValue != null : "Cannot update expense with null value";
		
		if(dbCallUpdateItem(database, inID, inNewValue))
		{
			cache.set(inID, inNewValue);
			return true;
		}
		
		return false;
	}
	
	public boolean delete(int inID)
	{
		assert IDHelper.isIdValid(inID) : "Invalid ID";

		if(dbCallDeleteItem(database, inID))
		{
			cache.markAsBad(inID);
			return true;
		}
		
		return false;
	}
	
	public int create()
	{
		return dbCallAddNew(database);
	}
	
	protected abstract int[] dbCallGetIds(IDatabase database);
	protected abstract int[] dbCallGetIds(IDatabase database, String whereClause);
	protected abstract Object dbCallGetItem(IDatabase database, int inId);
	protected abstract boolean dbCallUpdateItem(IDatabase database, int inId, Object inNewValue);
	protected abstract boolean dbCallDeleteItem(IDatabase database, int inID);
	protected abstract int dbCallAddNew(IDatabase database);
		
	private IDatabase database;
	private Cache cache;
	
	private static final int CACHE_SIZE = 1000;
}
