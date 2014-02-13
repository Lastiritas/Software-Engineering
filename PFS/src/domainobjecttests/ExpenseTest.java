package domainobjecttests;

import domainobjects.Expense;
import domainobjects.IDSet;
import domainobjects.PaymentMethod;
import domainobjects.SimpleDate;
import junit.framework.TestCase;

public class ExpenseTest extends TestCase
{
	public void testCreation()
	{
		int[] setData = { 0 };
		Expense expense = new Expense(SimpleDate.Now(), 100, PaymentMethod.CASH, "Something to eat", 0, IDSet.createFromArray(setData));

		assertNotNull("Constructor for Expense failed", expense);
	}
	
	public void testCentsAndDollars()
	{
		int[] setData = { 0 };
		Expense expense = new Expense(SimpleDate.Now(), 105, PaymentMethod.CASH, "Something to eat", 0, IDSet.createFromArray(setData));
	
		assertEquals("Dollars are wrong", expense.getDollars(), 1);
		assertEquals("Cents are wrong", expense.getCents(), 5);
	}
	
	public void testGetters()
	{
		SimpleDate date = SimpleDate.Now();
		int cents = 105;
		PaymentMethod method = PaymentMethod.CASH;
		String description = "Something to eat";
		int payToID = 55;		
		
		int[] setData = { 1, 2, 3 };
		IDSet set = IDSet.createFromArray(setData);
		
		Expense expense = new Expense(date, cents, method, description, payToID, set);
		
		assertEquals("Date is not what was put in", expense.getDate(), date);
		assertEquals("Amount in cents is not what was put in", expense.getTotalAmountInCents(), cents);
		assertEquals("Payment method is not what was put in", expense.getPaymentMethod(), method);
		assertEquals("Description is not what was put in", expense.getDescription(), description);
		assertEquals("Pay To is not what was put in", expense.getPayTo(), payToID);
		assertEquals("ID set is not what was put in", expense.getLabels(), set);
	}
}
