package tests.dataaccesslayer;

import dataaccesslayer.StubDatabase;
import domainobjects.Expense;
import domainobjects.IDSet;
import domainobjects.Label;
import domainobjects.Money;
import domainobjects.PayTo;
import domainobjects.PaymentMethod;
import domainobjects.SimpleDate;
import junit.framework.TestCase;

public class StubDatabaseTests extends TestCase 
{
	protected void setUp() throws Exception 
	{
		database = new StubDatabase();
	}
	
	public void test_Add_and_get_an_expense()
	{
		int newId = 0;
		int[] setData = new int[]{1};
		IDSet set = IDSet.createFromArray(setData);
		Expense expectedExpense = new Expense(SimpleDate.Now(), new Money(10, 0), PaymentMethod.CASH, "", 1, set);
		
		newId = database.getExpenseTable().add(expectedExpense);
		Expense actualExpense = (Expense)database.getExpenseTable().getById(newId);
		
		assertEquals(actualExpense, expectedExpense);
	}
	
	public void test_Update_expense()
	{
		int newId = 0;
		boolean updated;
		int[] setData = new int[]{2};
		IDSet set = IDSet.createFromArray(setData);
		Expense addedExpense = new Expense(SimpleDate.Now(), new Money(0, 50), PaymentMethod.DEBIT, "", 2, set);
		
		newId = database.getExpenseTable().add(addedExpense);
		
		//Update the expense by creating a new similar expense
		Expense expectedExpense = new Expense(SimpleDate.Now(), new Money(0, 55), PaymentMethod.DEBIT, "", 2, set);
		updated = database.getExpenseTable().update(newId, expectedExpense);
		Expense actualExpense = (Expense)database.getExpenseTable().getById(newId);
		
		assertTrue(updated);
		assertEquals(actualExpense, expectedExpense);
	}
	
	public void test_Getting_all_expense_Ids_should_return_the_previously_added_id()
	{
		int expectedId = 0;
		int[] setData = new int[]{1};
		IDSet expenseIds;
		IDSet set = IDSet.createFromArray(setData);
		Expense expense = new Expense(SimpleDate.Now(), new Money(5, 0), PaymentMethod.DEBIT, "", 1, set);
		
		expectedId = database.getExpenseTable().add(expense);
		expenseIds = IDSet.createFromArray(database.getExpenseTable().getAllIds());
		
		assertTrue(expenseIds.contains(expectedId));
	}
	
	public void test_Add_and_delete_an_expense_successfully()
	{
		int newId = 0;
		boolean deleted;
		int[] setData = new int[]{1};
		IDSet expenseIds;
		IDSet set = IDSet.createFromArray(setData);
		Expense addedExpense = new Expense(SimpleDate.Now(), new Money(1, 0), PaymentMethod.DEBIT, "", 2, set);
		
		newId = database.getExpenseTable().add(addedExpense);
		deleted = database.getExpenseTable().delete(newId);
		expenseIds = IDSet.createFromArray(database.getExpenseTable().getAllIds());
		
		assertTrue(deleted);
		assertFalse(expenseIds.contains(newId));
	}
	
	public void test_Add_and_get_a_label()
	{
		int newId = 0;
		Label expectedLabel = new Label("School");
		
		newId = database.getLabelTable().add(expectedLabel);
		Label actualLabel = (Label)database.getLabelTable().getById(newId);
		
		assertEquals(actualLabel, expectedLabel);
	}
	
	public void test_Update_label()
	{
		int newId = 0;
		boolean updated;
		Label addedLabel = new Label("House");
		
		newId = database.getLabelTable().add(addedLabel);
		
		//Update the label by creating a new similar label
		Label expectedLabel = new Label("Kyle's House");
		updated = database.getLabelTable().update(newId, expectedLabel);
		Label actualLabel = (Label)database.getLabelTable().getById(newId);
		
		assertTrue(updated);
		assertEquals(actualLabel, expectedLabel);
	}
	
	public void test_Getting_all_label_Ids_should_return_the_previously_added_id()
	{
		int expectedId = 0;
		IDSet labelIds;
		Label label = new Label("Sports");
		
		expectedId = database.getLabelTable().add(label);
		labelIds = IDSet.createFromArray(database.getLabelTable().getAllIds());
		
		assertTrue(labelIds.contains(expectedId));
	}
	
	public void test_Add_and_get_a_payTo()
	{
		int newId = 0;
		PayTo expectedPayTo = new PayTo("U of M");
		
		newId = database.getPayToTable().add(expectedPayTo);
		PayTo actualPayTo = (PayTo)database.getPayToTable().getById(newId);
		
		assertEquals(actualPayTo, expectedPayTo);
	}
	
	public void test_Update_payTo()
	{
		int newId = 0;
		boolean updated;
		PayTo addedPayTo = new PayTo("McDonalds");
		
		newId = database.getPayToTable().add(addedPayTo);
		
		//Update the payTo by creating a new similar payTo
		PayTo expectedPayTo = new PayTo("McDonalds");
		updated = database.getPayToTable().update(newId, expectedPayTo);
		PayTo actualPayTo = (PayTo)database.getPayToTable().getById(newId);
		
		assertTrue(updated);
		assertEquals(actualPayTo, expectedPayTo);
	}
	
	public void test_Getting_all_payTo_Ids_should_return_the_previously_added_id()
	{
		int expectedId = 0;
		IDSet payToIds;
		PayTo payTo = new PayTo("Tim Hortons");
		
		expectedId = database.getPayToTable().add(payTo);
		payToIds = IDSet.createFromArray(database.getPayToTable().getAllIds());
		
		assertTrue(payToIds.contains(expectedId));
	}
	
	private StubDatabase database;
}
