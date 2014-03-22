package system;

import util.Sort;
import util.SortDirection;
import util.TableCols;
import dataaccesslayer.IDatabaseTable;
import domainobjects.ExpenseFilter;
import domainobjects.IDHelper;
import domainobjects.IDSet;

public abstract class Manager implements IIDReader, IDataReader, IDataModifer
{
	public Manager(IDatabaseTable inTable)
	{
		assert inTable != null : "Must provide non-null table";
		
		table = inTable;
		cache = new Cache(CACHE_SIZE);
	}
	
	public IDSet getAllIDs()
	{	
		final int[] setData = table.getAllIds();
		assert setData != null : "Database returned null array";
	
		return IDSet.createFromArray(setData);
	}
	
	public IDSet getAllIDs(ExpenseFilter filter, TableCols sortBy, SortDirection direction)
	{
		String filterWhereClause = filter.createSQLWhereClause();	
		String whereClause = Sort.createSQLWhereClause(filterWhereClause, sortBy, direction);
		
		int[] setData = table.getAllIdsWhere(whereClause);
		
		assert setData != null : "Database returned null array";
	
		return IDSet.createFromArray(setData);
	}
	
	public Object getDataByID(int inID)
	{
		assert IDHelper.isIdValid(inID) : "Invalid ID";

		Object value = cache.tryGet(inID);
		
		if(value == null)
		{
			value = table.getById(inID);
			cache.set(inID, value);
		}
		
		return value;
	}
	
	public boolean update(int inID, Object inNewValue)
	{
		assert IDHelper.isIdValid(inID) : "Invalid ID";
		assert inNewValue != null : "Cannot update expense with null value";
		
		if(table.update(inID, inNewValue))
		{
			cache.set(inID, inNewValue);
			return true;
		}
		
		return false;
	}
	
	public boolean delete(int inID)
	{
		assert IDHelper.isIdValid(inID) : "Invalid ID";

		if(table.delete(inID))
		{
			cache.markAsBad(inID);
			return true;
		}
		
		return false;
	}
	
	public int create()
	{
		return table.add(getDefaultItem());
	}
	
	protected abstract Object getDefaultItem();
		
	private IDatabaseTable table;
	private Cache cache;
	
	private static final int CACHE_SIZE = 1024;
}
