package domainobjecttests;

import domainobjects.Label;
import junit.framework.TestCase;

public class LabelTest extends TestCase 
{	
	public void test_Create_a_label()
	{
		Label expected;
		String actualLabelName = "Food";
		
		expected = new Label(actualLabelName);
		
		assertEquals("The label Name is incorrect.", expected.getLabelName(), actualLabelName);
	}
}
