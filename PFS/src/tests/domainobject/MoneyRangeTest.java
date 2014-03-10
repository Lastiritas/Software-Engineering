package tests.domainobjecttests;

import domainobjects.Money;
import domainobjects.MoneyRange;
import junit.framework.TestCase;

public class MoneyRangeTest extends TestCase
{
	public void test_Create_and_get_money_range()
	{
		int dollarsLower = 25;
		int centsLower = 50;
		int dollarsUpper = 50;
		int centsUpper = 20;
		
		Money actualLower;
		Money actualUpper;
		Money expectedLower = new Money(dollarsLower, centsLower);
		Money expectedUpper = new Money(dollarsUpper, centsUpper);
		
		MoneyRange moneyRange = new MoneyRange(expectedLower, expectedUpper);
		
		actualLower = moneyRange.getLower();
		actualUpper = moneyRange.getUpper();
		
		assertEquals(actualLower.getTotalCents(), expectedLower.getTotalCents());
		assertEquals(actualUpper.getTotalCents(), expectedUpper.getTotalCents());
	}
}