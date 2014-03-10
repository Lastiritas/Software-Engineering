package tests.util;


import util.StringMatch;
import junit.framework.TestCase;


public class StringMatchTests extends TestCase 
{
	public StringMatchTests(String arg0)
	{
		super(arg0);
	}
	
	public void testMatchChar() 
	{
		boolean result = StringMatch.match("thisafsd", "a");
		
		assertEquals("String match not matching",result, true);
	}
	
	public void testMatchString() 
	{
		boolean result = StringMatch.match("thisafsd", "afs");
		
		assertEquals("String match not matching",result, true);
	}

	public void testMatchFailChar() 
	{
		boolean result = StringMatch.match("thisafsd", "z");
		
		assertEquals("String match not matching",result, false);
	}
	
	public void testMatchFailString() 
	{
		boolean result = StringMatch.match("thisafsd", "zthis");
		
		assertEquals("String match not matching",result, false);
	}
}
