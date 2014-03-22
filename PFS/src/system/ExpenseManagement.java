package system;

import dataaccesslayer.IDatabase;
import domainobjects.Expense;
import domainobjects.IDSet;
import domainobjects.Money;
import domainobjects.PaymentMethod;
import domainobjects.SimpleDate;

public class ExpenseManagement extends ManagementBase
{
	public ExpenseManagement(IDatabase inDatabase)
	{
		super(inDatabase);
	}

	@Override
	protected int[] dbCallGetIds(IDatabase database)
	{
		return database.getExpenseTable().getAllIds();
	}
	
	@Override
	protected int[] dbCallGetIds(IDatabase database, String whereClause)
	{
		return database.getExpenseTable().getAllIdsWhere(whereClause);
	}
	
	@Override
	protected Object dbCallGetItem(IDatabase database, int inId)
	{
		return database.getExpenseTable().getById(inId);
	}
	
	@Override
	protected boolean dbCallUpdateItem(IDatabase database, int inId, Object inNewValue)
	{
		assert inNewValue instanceof Expense : "Can only update with Expense objects";
		return database.getExpenseTable().update(inId, inNewValue);
	}
	
	@Override
	protected boolean dbCallDeleteItem(IDatabase database, int inID)
	{
		return database.getExpenseTable().delete(inID);		
	}
	
	@Override
	protected int dbCallAddNew(IDatabase database)
	{
		Expense newExpense = new Expense(SimpleDate.Now(), new Money(), PaymentMethod.CASH, "", PayToManagement.NO_ONE_ID, IDSet.empty());
		
		return database.getExpenseTable().add(newExpense);		
	}
}
