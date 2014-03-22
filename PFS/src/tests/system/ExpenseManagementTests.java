package tests.system;

import org.jmock.integration.junit3.MockObjectTestCase;
import org.jmock.Expectations;

import dataaccesslayer.IDatabase;
import domainobjects.Expense;
import domainobjects.IDSet;
import domainobjects.Money;
import domainobjects.PaymentMethod;
import domainobjects.SimpleDate;
import system.ExpenseManagement;

public class ExpenseManagementTests extends MockObjectTestCase
{
	protected void setUp() throws Exception 
	{
		expenseMgmt = new ExpenseManagement(mockDatabase);
		super.setUp();
	}
	
	public void test_Get_all_expense_Ids()
	{
		final int [] ids = {1, 6, 15};
		IDSet expectedResult = IDSet.createFromArray(ids);
		IDSet actualResult;
        
        //Expectations
        checking(new Expectations() {{
            oneOf (mockDatabase).getExpenseTable().getAllIds(); will(returnValue(ids));
        }});
        
        actualResult = expenseMgmt.getAllIDs();
        assertTrue(expectedResult.equals(actualResult));
        verify();
	}
	
	public void test_Get_expense_by_id()
	{
		Money amount = new Money(10,10);
		int setData[] = {1, 2, 3};
		final Expense expectedExpense = new Expense(SimpleDate.Now(), amount, PaymentMethod.CASH, "Something to eat", 0, IDSet.createFromArray(setData));
		final int expenseId = 5;
		Expense actualExpense;
        
        //Expectations
        checking(new Expectations() {{
            oneOf (mockDatabase).getExpenseTable().getById(expenseId); will(returnValue(expectedExpense));
        }});
        
        actualExpense = (Expense)expenseMgmt.getDataByID(expenseId);
        assertEquals(expectedExpense, actualExpense);
        verify();
	}
	
	public void test_Create_expense_successfully()
	{
		final int expectedExpenseId = 8;
		int actualExpenseId;
        
        //Expectations
        checking(new Expectations() {{
            allowing (mockDatabase).getExpenseTable().add(with(any(Expense.class))); will(returnValue(expectedExpenseId));
        }});
        
        actualExpenseId = expenseMgmt.create();
        assertEquals(expectedExpenseId, actualExpenseId);
        verify();
	}
	
	public void test_Update_payTo_successfully()
	{
		Money amount = new Money(10,10);
		int setData[] = {1, 2, 3};
		final Expense expense = new Expense(SimpleDate.Now(), amount, PaymentMethod.CASH, "Something to eat", 0, IDSet.createFromArray(setData));
		final int expenseId = 3;
		final boolean expectedResult = true;
		boolean actualResult;
        
        //Expectations
        checking(new Expectations() {{
            oneOf (mockDatabase).getExpenseTable().update(expenseId, expense); will(returnValue(expectedResult));
        }});
        
        actualResult = expenseMgmt.update(expenseId, expense);
        assertEquals(expectedResult, actualResult);
        verify();
	}
	
	private ExpenseManagement expenseMgmt;
	final IDatabase mockDatabase = mock(IDatabase.class);
}