package system;

import java.util.Vector;
import java.util.Date;

import dataAccessLayer.IDatabase;
import dataAccessLayer.StubDatabase;
import domainobjects.Expense;
import domainobjects.IDSet;
import domainobjects.PaymentMethod;

public class ExpenseManagement implements IIDReader, IDataReader, IDataModifer
{
	public ExpenseManagement(IDatabase inDatabase)
	{
		assert inDatabase != null : "Must provide non-null database";

		database = inDatabase;
	}
	
	public IDSet getAllIDs()
	{
		final Vector<Integer> ids = database.getAllExpenseIDs();
		final int[] setData = new int[ids.size()];
		
		for(int i = 0; i < setData.length; i++)
		{
			setData[i] = ids.get(i).intValue();
		}
		
		final IDSet output = IDSet.createFromArray(setData);
		
		return output;
	}

	public Object getDataByID(int inID)
	{
		assert inID >= 0 : "Invalid ID";

		return database.getExpenseByID(inId);
	}

	public boolean update(int inId, Object inNewValue)
	{
		assert inID >= 0 : "Invalid ID";
		assert inNewValue != null : "Cannot update expense with null value";

		assert inNewValue instanceof Expense : "Can only use expenses in expense system";

		return database.updateExpense(inId, inNewValue);
	}

	public boolean delete(int inId)
	{
		assert inID >= 0 : "Invalid ID";

		return database.deleteExpense(inId);
	}

	public int new()
	{
		final int[] emptySetData = new int[0];
		final IDSet emptySet = IDSet.createFromArray(emptySetData);
		Expense newExpense = new Expense(new Date(), 0, PaymentMethod.CASH, "", -1, emptySet);
		
		return database.addExpense(newExpense);
	}
	
	private IDatabase database;
}
