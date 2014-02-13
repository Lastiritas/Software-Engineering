package domainobjecttests;

import domainobjects.PayTo;
import junit.framework.TestCase;

public class PayToTest extends TestCase 
{
	public void test_Create_a_payTo()
	{
		PayTo expected;
		int actualID = 5;
		String actualPayToName = "Burger King";
		String actualPayToBranch = "St. Vital";
		
		expected = new PayTo(actualID, actualPayToName, actualPayToBranch);
		
		assertEquals("The PayTo ID is incorrect.", expected.getPayToID(), actualID);
		assertEquals("The PayTo Name is incorrect.", expected.getPayToName(), actualPayToName);
		assertEquals("The PayTo Branch is incorrect.", expected.getPayToBranch(), actualPayToBranch);
	}
	
	public void test_Create_a_payTo_without_ID()
	{
		PayTo expected;
		int expectedID = 0;
		String actualPayToName = "Burger King";
		String actualPayToBranch = "St. Vital";
		
		expected = new PayTo(actualPayToName, actualPayToBranch);
		
		assertEquals("The PayTo ID is incorrect.", expected.getPayToID(), expectedID);
		assertEquals("The PayTo Name is incorrect.", expected.getPayToName(), actualPayToName);
		assertEquals("The PayTo Branch is incorrect.", expected.getPayToBranch(), actualPayToBranch);
	}
}
