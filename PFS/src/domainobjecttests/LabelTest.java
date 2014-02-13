package domainobjecttests;

import domainobjects.Label;
import junit.framework.TestCase;

public class LabelTest extends TestCase 
{
	public void test_Create_a_label()
	{
		Label expected;
		int actualID = 5;
		String actualLabelName = "Food";
		
		expected = new Label(actualID, actualLabelName);
		
		assertEquals("The label ID is incorrect.", expected.getLabelID(), actualID);
		assertEquals("The label Name is incorrect.", expected.getLabelName(), actualLabelName);
	}
	
	public void test_Create_a_label_without_ID()
	{
		Label expected;
		int expectedID = 0;
		String actualLabelName = "Food";
		
		expected = new Label(actualLabelName);
		
		assertEquals("The label ID is incorrect.", expected.getLabelID(), expectedID);
		assertEquals("The label Name is incorrect.", expected.getLabelName(), actualLabelName);
	}
}
