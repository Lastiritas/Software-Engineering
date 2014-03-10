package tests.domainobjecttests;

import domainobjects.DateRange;
import domainobjects.SimpleDate;
import junit.framework.TestCase;

public class DateRangeTest extends TestCase
{
	public void test_Create_and_get_date_range()
	{
		SimpleDate actualLower;
		SimpleDate actualUpper;
		SimpleDate expectedLower = SimpleDate.Now();
		SimpleDate expectedUpper = SimpleDate.Now();
		
		expectedLower.setDate("3/8/2014");
		expectedUpper.setDate("9/8/2014");
		
		DateRange expectedDate = new DateRange(expectedLower, expectedUpper);
		
		actualLower = expectedDate.getLower();
		actualUpper = expectedDate.getUpper();
		
		assertEquals(actualLower.toInteger(), expectedLower.toInteger());
		assertEquals(actualUpper.toInteger(), expectedUpper.toInteger());
	}
}