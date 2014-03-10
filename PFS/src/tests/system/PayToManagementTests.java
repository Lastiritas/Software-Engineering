package tests.systemTests;

import org.jmock.integration.junit3.MockObjectTestCase;
import org.jmock.Expectations;

import system.PayToManagement;
import dataAccessLayer.IDatabase;
import domainobjects.IDSet;
import domainobjects.PayTo;

public class PayToManagementTests extends MockObjectTestCase
{
	protected void setUp() throws Exception 
	{
		payToMgmt = new PayToManagement(mockDatabase);
		super.setUp();
	}
	
	public void test_Get_all_payTo_Ids()
	{
		final int [] ids = {3, 6, 7};
		IDSet expectedResult = IDSet.createFromArray(ids);
		IDSet actualResult;
        
        //Expectations
        checking(new Expectations() {{
            oneOf (mockDatabase).getAllPayToIDs(); will(returnValue(ids));
        }});
        
        actualResult = payToMgmt.getAllIDs();
        assertTrue(expectedResult.equals(actualResult));
        verify();
	}
	
	public void test_Get_payTo_by_id()
	{
		final PayTo expectedPayTo = new PayTo("Indigo", "St. Vital");
		final int payToId = 10;
		PayTo actualPayTo;
        
        //Expectations
        checking(new Expectations() {{
            oneOf (mockDatabase).getPayToByID(payToId); will(returnValue(expectedPayTo));
        }});
        
        actualPayTo = (PayTo)payToMgmt.getDataByID(payToId);
        assertEquals(expectedPayTo, actualPayTo);
        verify();
	}
	
	public void test_Create_payTo_successfully()
	{
		final int expectedPayToId = 9;
		int actualPayToId;
        
        //Expectations
        checking(new Expectations() {{
            allowing (mockDatabase).addPayTo(with(any(PayTo.class))); will(returnValue(expectedPayToId));
        }});
        
        actualPayToId = payToMgmt.create();
        assertEquals(expectedPayToId, actualPayToId);
        verify();
	}
	
	public void test_Update_payTo_successfully()
	{
		final PayTo payTo = new PayTo("U of M", "Fort Garry");
		final int payToId = 12;
		final boolean expectedResult = true;
		boolean actualResult;
        
        //Expectations
        checking(new Expectations() {{
            oneOf (mockDatabase).updatePayTo(payToId, payTo); will(returnValue(expectedResult));
        }});
        
        actualResult = payToMgmt.update(payToId, payTo);
        assertEquals(expectedResult, actualResult);
        verify();
	}
	
	private PayToManagement payToMgmt;
	final IDatabase mockDatabase = mock(IDatabase.class);
}
