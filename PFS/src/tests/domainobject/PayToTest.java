package tests.domainobject;

import domainobjects.PayTo;
import junit.framework.TestCase;

public class PayToTest extends TestCase 
{
	public PayToTest(String arg0)
	{
		super(arg0);
	}
		
	public void test_Create_a_payTo()
	{
		PayTo actual;
		String expectedPayToName = "Burger King";
		
		actual = new PayTo(expectedPayToName);
		
		assertEquals("The PayTo Name is incorrect.", expectedPayToName, actual.getName());
	}
}
