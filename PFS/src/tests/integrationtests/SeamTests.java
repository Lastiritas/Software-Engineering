package tests.integrationtests;

import domainobjects.Expense;
import domainobjects.IDSet;
import domainobjects.Label;
import domainobjects.Money;
import domainobjects.PayTo;
import domainobjects.PaymentMethod;
import domainobjects.SimpleDate;
import system.PFSystem;
import junit.framework.TestCase;

public abstract class SeamTests extends TestCase
{
	public abstract PFSystem getPFS();
	private PFSystem test;
	
	public SeamTests(String arg0)
	{
		super(arg0);
	}

	protected void setUp()
	{
		test = getPFS();
	}
	
	protected void tearDown()
	{
		test.closePFSystem();
	}
	
	public void testPFSAddGetEmptyExpense()
	{
		int[] setData = new int[0];
		IDSet set = IDSet.createFromArray(setData);
		Expense expectedExpense = new Expense(SimpleDate.Now(), new Money(), PaymentMethod.CASH, "", 0, set);
		
		addAndGetExpense(expectedExpense);
	}
	
	public void testPFSAddGetExpense()
	{
		int[] setData = new int[]{1};
		IDSet set = IDSet.createFromArray(setData);
		Expense expectedExpense = new Expense(SimpleDate.Now(), new Money(10, 0), PaymentMethod.CASH, "", 1, set);
		
		addAndGetExpense(expectedExpense);
	}
	
	private void addAndGetExpense(Expense expectedExpense) {
		int newId;
		newId = test.getExpenseSystem().create();
		test.getExpenseSystem().update(newId, expectedExpense);
		Expense actualExpense = (Expense)test.getExpenseSystem().getDataByID(newId);
		
		assertEquals(actualExpense.getDate().getDay(), expectedExpense.getDate().getDay());
		assertEquals(actualExpense.getDate().getMonth(), expectedExpense.getDate().getMonth());
		assertEquals(actualExpense.getDate().getYear(), expectedExpense.getDate().getYear());
		assertEquals(actualExpense.getAmount().getTotalCents(), expectedExpense.getAmount().getTotalCents());
		assertEquals(actualExpense.getPaymentMethod(), expectedExpense.getPaymentMethod());
		assertEquals(actualExpense.getDescription(), expectedExpense.getDescription());
		assertEquals(actualExpense.getPayTo(), expectedExpense.getPayTo());
		assertEquals(actualExpense.getLabels().getSize(), expectedExpense.getLabels().getSize());
	}
	
	public void testPFSUpdateExpense()
	{
		int newId = 0;
		boolean updated;
		int[] setData = new int[]{2};
		IDSet set = IDSet.createFromArray(setData);
		Expense addedExpense = new Expense(SimpleDate.Now(), new Money(0, 50), PaymentMethod.DEBIT, "", 2, set);
		
		newId = test.getExpenseSystem().create();
		test.getExpenseSystem().update(newId,addedExpense);
		
		//Update the expense by creating a new similar expense
		Expense expectedExpense = new Expense(SimpleDate.Now(), new Money(0, 55), PaymentMethod.DEBIT, "", 2, set);
		updated = test.getExpenseSystem().update(newId, expectedExpense);
		Expense actualExpense = (Expense)test.getExpenseSystem().getDataByID(newId);
		
		assertTrue(updated);
		assertEquals(actualExpense.getDate().getDay(), expectedExpense.getDate().getDay());
		assertEquals(actualExpense.getDate().getMonth(), expectedExpense.getDate().getMonth());
		assertEquals(actualExpense.getDate().getYear(), expectedExpense.getDate().getYear());
		assertEquals(actualExpense.getAmount().getTotalCents(), expectedExpense.getAmount().getTotalCents());
		assertEquals(actualExpense.getPaymentMethod(), expectedExpense.getPaymentMethod());
		assertEquals(actualExpense.getDescription(), expectedExpense.getDescription());
		assertEquals(actualExpense.getPayTo(), expectedExpense.getPayTo());
		assertEquals(actualExpense.getLabels().getSize(), expectedExpense.getLabels().getSize());
	}
	
	public void testPFSGetAllExpenseIDReturnPrevAddID()
	{
		int expectedId = 0;
		int[] setData = new int[]{1};
		IDSet expenseIds;
		IDSet set = IDSet.createFromArray(setData);
		Expense expense = new Expense(SimpleDate.Now(), new Money(5, 0), PaymentMethod.DEBIT, "", 1, set);
		
		expectedId = test.getExpenseSystem().create();
		test.getExpenseSystem().update(expectedId, expense);
		expenseIds = test.getExpenseSystem().getAllIDs();
		
		assertTrue(expenseIds.contains(expectedId));
	}
	
	public void testPFSAddDeleteExpenseSuccess()
	{
		int newId = 0;
		boolean deleted;
		int[] setData = new int[]{1};
		IDSet expenseIds;
		IDSet set = IDSet.createFromArray(setData);
		Expense addedExpense = new Expense(SimpleDate.Now(), new Money(1, 0), PaymentMethod.DEBIT, "", 2, set);
		
		newId = test.getExpenseSystem().create();
		test.getExpenseSystem().update(newId, addedExpense);
		deleted = test.getExpenseSystem().delete(newId);
		expenseIds = test.getExpenseSystem().getAllIDs();
		
		assertTrue(deleted);
		assertFalse(expenseIds.contains(newId));
	}
	
	public void testPFSAddGetLabel()
	{
		int newId = 0;
		Label expectedLabel = new Label("Craziness");
		
		newId = test.getLabelSystem().create();
		test.getLabelSystem().update(newId, expectedLabel);
		Label actualLabel = (Label)test.getLabelSystem().getDataByID(newId);
		
		assertEquals(actualLabel.getName(), expectedLabel.getName());
	}
	
	public void testPFSUpdateLabel()
	{
		int newId = 0;
		boolean updated;
		Label addedLabel = new Label("House");
		
		newId = test.getLabelSystem().create();
		test.getLabelSystem().update(newId, addedLabel);
		
		//Update the label by creating a new similar label
		Label expectedLabel = new Label("Kyles House");
		updated = test.getLabelSystem().update(newId, expectedLabel);
		Label actualLabel = (Label)test.getLabelSystem().getDataByID(newId);
		
		assertTrue(updated);
		assertEquals(actualLabel.getName(), expectedLabel.getName());
	}
	
	public void testPFSGetAllLabelIDReturnPrevAddedID()
	{
		int expectedId = 0;
		IDSet labelIds;
		Label label = new Label("Sports");
		
		expectedId = test.getLabelSystem().create();
		test.getLabelSystem().update(expectedId, label);
		labelIds = test.getLabelSystem().getAllIDs();
		
		assertTrue(labelIds.contains(expectedId));
	}
	
	public void testPFSAddGetPayto()
	{
		PayTo expectedPayTo = new PayTo("U of M");		
		int newId = test.getPayToSystem().create();
		test.getPayToSystem().update(newId, expectedPayTo);
	
		PayTo actualPayTo = (PayTo)test.getPayToSystem().getDataByID(newId);
		
		assertEquals(actualPayTo.getName(), expectedPayTo.getName());
	}
	
	public void testPFSUpdatePayto()
	{
		int newId = 0;
		boolean updated;
		PayTo addedPayTo = new PayTo("McDonalds");
		
		newId = test.getPayToSystem().create();
		test.getPayToSystem().update(newId, addedPayTo);
		
		//Update the payTo by creating a new similar payTo
		PayTo expectedPayTo = new PayTo("McDonalds");
		updated = test.getPayToSystem().update(newId, expectedPayTo);
		PayTo actualPayTo = (PayTo)test.getPayToSystem().getDataByID(newId);
		
		assertTrue(updated);
		assertEquals(actualPayTo.getName(), expectedPayTo.getName());
	}
	
	public void testPFSGetPaytoIDReturnPrevAddedID()
	{
		int expectedId = 0;
		IDSet payToIds;
		PayTo payTo = new PayTo("Tim Hortons");
		
		expectedId = test.getPayToSystem().create();
		test.getPayToSystem().update(expectedId, payTo);
		payToIds = test.getPayToSystem().getAllIDs();
		
		assertTrue(payToIds.contains(expectedId));
	}
}
