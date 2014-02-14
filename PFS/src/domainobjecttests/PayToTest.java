package domainobjecttests;

import domainobjects.PayTo;
import junit.framework.TestCase;

public class PayToTest extends TestCase 
{
	public PayToTest(String arg0)
	{
		super(arg0);
	}
	
	public void test_Create_a_payTo_with_Branch()
	{
		PayTo actual;
		String expectedPayToName = "Burger King";
		String expectedPayToBranch = "St. Vital";
		
		actual = new PayTo(expectedPayToName, expectedPayToBranch);
		
		assertEquals("The PayTo Name is incorrect.", expectedPayToName, actual.getPayToName());
		assertEquals("The PayTo Branch is incorrect.", expectedPayToBranch, actual.getPayToBranch());
	}
	
	public void test_Create_a_payTo_without_Branch()
	{
		PayTo actual;
		String expectedPayToName = "Burger King";
		String expectedPayToBranch = "";
		
		actual = new PayTo(expectedPayToName);
		
		assertEquals("The PayTo Name is incorrect.", expectedPayToName, actual.getPayToName());
		assertEquals("The PayTo Branch is incorrect.", expectedPayToBranch, actual.getPayToBranch());
	}
}
