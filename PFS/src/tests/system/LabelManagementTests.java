package tests.systemTests;

import org.jmock.integration.junit3.MockObjectTestCase;
import org.jmock.Expectations;

import system.LabelManagement;
import dataAccessLayer.IDatabase;
import domainobjects.IDSet;
import domainobjects.Label;

public class LabelManagementTests extends MockObjectTestCase
{
	protected void setUp() throws Exception 
	{
		labelMgmt = new LabelManagement(mockDatabase);
		super.setUp();
	}
	
	public void test_Get_all_label_Ids()
	{
		final int [] intArray = {1, 2, 4};
		IDSet expectedResult = IDSet.createFromArray(intArray);
		IDSet actualResult;
        
        //Expectations
        checking(new Expectations() {{
            oneOf (mockDatabase).getAllLabelIDs(); will(returnValue(intArray));
        }});
        
        actualResult = labelMgmt.getAllIDs();
        assertTrue(expectedResult.equals(actualResult));
        verify();
	}
	
	public void test_Get_label_by_id()
	{
		final Label expectedLabel = new Label("Food");
		final int labelId = 5;
		Label actualLabel;
        
        //Expectations
        checking(new Expectations() {{
            oneOf (mockDatabase).getLabelByID(labelId); will(returnValue(expectedLabel));
        }});
        
        actualLabel = (Label)labelMgmt.getDataByID(labelId);
        assertEquals(expectedLabel, actualLabel);
        verify();
	}
	
	public void test_Create_label_successfully()
	{
		final int expectedLabelId = 4;
		int actualLabelId;
        
        //Expectations
        checking(new Expectations() {{
            allowing (mockDatabase).addLabel(with(any(Label.class))); will(returnValue(expectedLabelId));
        }});
        
        actualLabelId = labelMgmt.create();
        assertEquals(expectedLabelId, actualLabelId);
        verify();
	}
	
	public void test_Update_label_successfully()
	{
		final Label label = new Label("Healthy Food");
		final int labelId = 2;
		final boolean expectedResult = true;
		boolean actualResult;
        
        //Expectations
        checking(new Expectations() {{
            oneOf (mockDatabase).updateLabel(labelId, label); will(returnValue(expectedResult));
        }});
        
        actualResult = labelMgmt.update(labelId, label);
        assertEquals(expectedResult, actualResult);
        verify();
	}
	
	private LabelManagement labelMgmt;
	final IDatabase mockDatabase = mock(IDatabase.class);
}
