package system;

import util.Sort;
import util.SortDirection;
import util.TableCols;
import dataaccesslayer.IDatabase;
import domainobjects.Expense;
import domainobjects.ExpenseFilter;
import domainobjects.IDHelper;
import domainobjects.IDSet;
import domainobjects.Money;
import domainobjects.PaymentMethod;
import domainobjects.SimpleDate;

public class ExpenseManagement implements IIDReader, IDataReader, IDataModifer
{
	public ExpenseManagement(IDatabase inDatabase)
	{
		assert inDatabase != null : "Must provide non-null database";

		database = inDatabase;
		cache = new Cache(CACHE_SIZE);
	}

	public IDSet getAllIDs()
	{	
		final int[] setData = database.getAllExpenseIDs();
		assert setData != null : "Database returned null array";
	
		final IDSet output = IDSet.createFromArray(setData);
		
		return output;
	}
	
	public IDSet getAllIDs(ExpenseFilter filter, TableCols sortBy, SortDirection direction)
	{
		String filterWhereClause = filter.createSQLWhereClause();	
		String whereClause = Sort.createSQLWhereClause(filterWhereClause, sortBy, direction);
		
		int[] setData = database.getAllExpenseIDsWhere(whereClause);
		
		assert setData != null : "Database returned null array";
	
		IDSet output = IDSet.createFromArray(setData);
		
		return output;
	}

	public Object getDataByID(int inID)
	{
		assert IDHelper.isIdValid(inID) : "Invalid ID";

		Object value = cache.tryGet(inID);
		
		if(value == null)
		{
			value = database.getExpenseByID(inID);
			cache.set(inID, value);
		}
		
		return value;
	}

	public boolean update(int inID, Object inNewValue)
	{
		assert IDHelper.isIdValid(inID) : "Invalid ID";
		assert inNewValue != null : "Cannot update expense with null value";

		assert inNewValue instanceof Expense : "Can only use expenses in expense system";
		
		cache.set(inID, inNewValue);
		
		return database.updateExpense(inID, (Expense)inNewValue);
	}

	public boolean delete(int inID)
	{
		assert IDHelper.isIdValid(inID) : "Invalid ID";

		return database.deleteExpense(inID);
	}

	public int create()
	{
		final int[] emptySetData = new int[0];
		final IDSet emptySet = IDSet.createFromArray(emptySetData);
		Expense newExpense = new Expense(SimpleDate.Now(), new Money(), PaymentMethod.CASH, "", IDHelper.getInvalidId(), emptySet);
		
		return database.addExpense(newExpense);
	}
	
	private IDatabase database;
	private Cache cache;
	
	private static final int CACHE_SIZE = 1000;
}
