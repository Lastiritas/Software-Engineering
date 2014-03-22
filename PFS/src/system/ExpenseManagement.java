package system;

import dataaccesslayer.IDatabase;
import domainobjects.Expense;
import domainobjects.IDSet;
import domainobjects.Money;
import domainobjects.PaymentMethod;
import domainobjects.SimpleDate;

public class ExpenseManagement extends Manager
{
	public ExpenseManagement(IDatabase inDatabase)
	{
		super(inDatabase.getExpenseTable());
	}

	@Override
	protected Object getDefaultItem() 
	{
		return new Expense(SimpleDate.Now(), new Money(), PaymentMethod.CASH, "", PayToManagement.NO_ONE_ID, IDSet.empty());
	}
}
