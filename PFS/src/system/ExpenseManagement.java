package system;

import dataAccessLayer.IDatabase;
import domainobjects.Expense;
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
	}
	
	public IDSet getAllIDs()
	{
		final int[] setData = database.getAllExpenseIDs();
		assert setData != null : "Database returned null array";
	
		final IDSet output = IDSet.createFromArray(setData);
		
		return output;
	}

	public Object getDataByID(int inID)
	{
		assert inID >= 0 : "Invalid ID";

		return database.getExpenseByID(inID);
	}

	public boolean update(int inID, Object inNewValue)
	{
		assert inID >= 0 : "Invalid ID";
		assert inNewValue != null : "Cannot update expense with null value";

		assert inNewValue instanceof Expense : "Can only use expenses in expense system";

		return database.updateExpense(inID, (Expense)inNewValue);
	}

	public boolean delete(int inID)
	{
		assert inID >= 0 : "Invalid ID";

		return database.deleteExpense(inID);
	}

	public int create()
	{
		final int[] emptySetData = new int[0];
		final IDSet emptySet = IDSet.createFromArray(emptySetData);
		Expense newExpense = new Expense(SimpleDate.Now(), new Money(), PaymentMethod.CASH, "", PayToManagement.NULL_ID, emptySet);
		
		return database.addExpense(newExpense);
	}
	
	private IDatabase database;
}
