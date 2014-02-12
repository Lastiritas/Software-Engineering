package dataAccessLayerTests;

import java.util.Date;
import java.util.Vector;

import dataAccessLayer.StubDatabase;
import domainobjects.Expense;
import domainobjects.IDSet;
import domainobjects.Label;
import domainobjects.PayTo;
import domainobjects.PaymentMethod;
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
		Expense expectedExpense = new Expense(new Date(), 1000, PaymentMethod.CASH, "", 1, set);
		
		newId = database.addExpense(expectedExpense);
		Expense actualExpense = database.getExpenseByID(newId);
		
		assertEquals(actualExpense, expectedExpense);
	}
	
	public void test_Update_expense()
	{
		int newId = 0;
		boolean updated;
		int[] setData = new int[]{2};
		IDSet set = IDSet.createFromArray(setData);
		Expense addedExpense = new Expense(new Date(), 50, PaymentMethod.DEBIT, "", 2, set);
		
		newId = database.addExpense(addedExpense);
		
		//Update the expense by creating a new similar expense
		Expense expectedExpense = new Expense(new Date(), 55, PaymentMethod.DEBIT, "", 2, set);
		updated = database.updateExpense(newId, expectedExpense);
		Expense actualExpense = database.getExpenseByID(newId);
		
		assertTrue(updated);
		assertEquals(actualExpense, expectedExpense);
	}
	
	public void test_Getting_all_expense_Ids_should_return_the_previously_added_id()
	{
		int expectedId = 0;
		Vector<Integer> expenseIds;
		int[] setData = new int[]{1};
		IDSet set = IDSet.createFromArray(setData);
		Expense expense = new Expense(new Date(), 500, PaymentMethod.DEBIT, "", 1, set);
		
		expectedId = database.addExpense(expense);
		expenseIds = database.getAllExpenseIDs();
		
		assertTrue(expenseIds.contains(expectedId));
	}
	
	public void test_Add_and_delete_an_expense_successfully()
	{
		int newId = 0;
		boolean deleted;
		Vector<Integer> expenseIds;
		int[] setData = new int[]{1};
		IDSet set = IDSet.createFromArray(setData);
		Expense addedExpense = new Expense(new Date(), 100, PaymentMethod.DEBIT, "", 2, set);
		
		newId = database.addExpense(addedExpense);
		deleted = database.deleteExpense(newId);
		expenseIds = database.getAllExpenseIDs();
		
		assertTrue(deleted);
		assertFalse(expenseIds.contains(newId));
	}
	
	public void test_Add_and_get_a_label()
	{
		int newId = 0;
		Label expectedLabel = new Label("School");
		
		newId = database.addLabel(expectedLabel);
		Label actualLabel = database.getLabelByID(newId);
		
		assertEquals(actualLabel, actualLabel);
	}
	
	public void test_Update_label()
	{
		int newId = 0;
		boolean updated;
		Label addedLabel = new Label("House");
		
		newId = database.addLabel(addedLabel);
		
		//Update the label by creating a new similar label
		Label expectedLabel = new Label("Kyle's House");
		updated = database.updateLabel(newId, expectedLabel);
		Label actualLabel = database.getLabelByID(newId);
		
		assertTrue(updated);
		assertEquals(actualLabel, expectedLabel);
	}
	
	public void test_Getting_all_label_Ids_should_return_the_previously_added_id()
	{
		int expectedId = 0;
		Vector<Integer> labelIds;
		Label label = new Label("Sports");
		
		expectedId = database.addLabel(label);
		labelIds = database.getAllLabelIDs();
		
		assertTrue(labelIds.contains(expectedId));
	}
	
	public void test_Add_and_get_a_payTo()
	{
		int newId = 0;
		PayTo expectedPayTo = new PayTo("U of M", "Fort Garry");
		
		newId = database.addPayTo(expectedPayTo);
		PayTo actualPayTo = database.getPayToByID(newId);
		
		assertEquals(actualPayTo, expectedPayTo);
	}
	
	public void test_Update_payTo()
	{
		int newId = 0;
		boolean updated;
		PayTo addedPayTo = new PayTo("McDonalds", "St. Vital");
		
		newId = database.addPayTo(addedPayTo);
		
		//Update the payTo by creating a new similar payTo
		PayTo expectedPayTo = new PayTo("McDonalds", "Polo Park");
		updated = database.updatePayTo(newId, expectedPayTo);
		PayTo actualPayTo = database.getPayToByID(newId);
		
		assertTrue(updated);
		assertEquals(actualPayTo, expectedPayTo);
	}
	
	public void test_Getting_all_payTo_Ids_should_return_the_previously_added_id()
	{
		int expectedId = 0;
		Vector<Integer> payToIds;
		PayTo payTo = new PayTo("Tim Hortons", "EITC");
		
		expectedId = database.addPayTo(payTo);
		payToIds = database.getAllPayToIDs();
		
		assertTrue(payToIds.contains(expectedId));
	}
	
	private StubDatabase database;
}
