package system;

import dataAccessLayer.IDatabase;
import dataAccessLayer.StubDatabase;

public class PFSystem 
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
	
	public static PFSystem getCurrent()
	{
		return current;
	}
	
	private static PFSystem current = new PFSystem();
}
