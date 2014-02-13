package system;

import dataAccessLayer.IDatabase;
import dataAccessLayer.StubDatabase;

public class FPSystem 
{
	public PayToManagement getPayToSystem()
	{
		return payToSystem;
	}
	
	public LabelManagement getLabelSystem()
	{
		return labelSystem;
	}
	
	public ExpenseManagement getExpenseSystem()
	{
		return expenseSystem;
	}
	
	private IDatabase database = new StubDatabase();
	
	private PayToManagement payToSystem = new PayToManagement(database);
	private LabelManagement labelSystem = new LabelManagement(database);
	private ExpenseManagement expenseSystem = new ExpenseManagement(database);
	
	public static FPSystem getCurrent()
	{
		return current;
	}
	
	private static FPSystem current = new FPSystem();
}
