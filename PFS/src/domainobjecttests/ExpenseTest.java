package domainobjecttests;

import domainobjects.Expense;
import domainobjects.IDSet;
import domainobjects.Money;
import domainobjects.PaymentMethod;
import domainobjects.SimpleDate;
import junit.framework.TestCase;

public class ExpenseTest extends TestCase
{
	public ExpenseTest(String arg0)
	{
		super(arg0);
	}
	
	public void testCreation()
	{
		int[] setData = { 0 };
		Expense expense = new Expense(SimpleDate.Now(), new Money(1, 0), PaymentMethod.CASH, "Something to eat", 0, IDSet.createFromArray(setData));

		assertNotNull("Constructor for Expense failed", expense);
	}
	
	public void testCentsAndDollars()
	{
		int[] setData = { 0 };
		Expense expense = new Expense(SimpleDate.Now(), new Money(1, 5), PaymentMethod.CASH, "Something to eat", 0, IDSet.createFromArray(setData));
	
		assertEquals("Dollars are wrong", expense.getAmount().getDollars(), 1);
		assertEquals("Cents are wrong", expense.getAmount().getCents(), 5);
	}
	
	public void testGetters()
	{
		SimpleDate date = SimpleDate.Now();
		Money money = new Money(1, 5);
		PaymentMethod method = PaymentMethod.CASH;
		String description = "Something to eat";
		int payToID = 55;		
		
		int[] setData = { 1, 2, 3 };
		IDSet set = IDSet.createFromArray(setData);
		
		Expense expense = new Expense(date, money, method, description, payToID, set);
		
		assertEquals("Date is not what was put in", expense.getDate(), date);
		assertEquals("Amount in cents is not what was put in", expense.getAmount(), money);
		assertEquals("Payment method is not what was put in", expense.getPaymentMethod(), method);
		assertEquals("Description is not what was put in", expense.getDescription(), description);
		assertEquals("Pay To is not what was put in", expense.getPayTo(), payToID);
		assertEquals("ID set is not what was put in", expense.getLabels(), set);
	}
}
