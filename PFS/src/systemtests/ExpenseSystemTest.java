package systemtests;

import domainobjects.Expense;
import domainobjects.IDSet;
import domainobjects.PaymentMethod;
import domainobjects.SimpleDate;
import system.ExpenseSystem;
import junit.framework.TestCase;

public class ExpenseSystemTest extends TestCase
{
	public void testGetCurrentSystem()
	{
		ExpenseSystem current = ExpenseSystem.getCurrent();
		assertNotNull("Current expense system should never be null", current);
	}
	
	public void testGetIDs()
	{
		ExpenseSystem current = ExpenseSystem.getCurrent();
		
		IDSet expenseIDs = current.getExpenseIDs();
		
		assertNotNull("System should never return a null set", expenseIDs);
		assertEquals("System should return empty set when it no data", 0, expenseIDs.getSize());
	}
	
	public void testNewExpense()
	{
		ExpenseSystem current = ExpenseSystem.getCurrent();
		
		int newExpenseID = current.newExpense();
		
		IDSet expenseIDs = current.getExpenseIDs();
		
		assertTrue("System should have entry for new expense", expenseIDs.contains(newExpenseID));
	}
	
	public void testDeleteExpense()
	{
		ExpenseSystem current = ExpenseSystem.getCurrent();
		
		int id = current.newExpense();
		
		current.deleteExpense(id);
		
		IDSet expenseIDs = current.getExpenseIDs();
		
		assertFalse("System should not have a deleted expense", expenseIDs.contains(id));
	}
	
	public void testUpdateExpense()
	{
		ExpenseSystem current = ExpenseSystem.getCurrent();
		
		int newID = current.newExpense();
		
		int setData[] = {1, 2, 3};
		Expense expense = new Expense(SimpleDate.Now(), 100, PaymentMethod.CASH, "Something to eat", 0, IDSet.createFromArray(setData));
		
		current.updateExpense(newID, expense);
		
		Expense returnedExpense = current.getExpense(newID);
		
		assertEquals("The returned expense is not the same as the updated value",expense, returnedExpense);
	}
}
