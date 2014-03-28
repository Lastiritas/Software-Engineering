package tests.util;

import util.Sanitizer;
import junit.framework.TestCase;

public class SanitizerTests extends TestCase {

	public SanitizerTests(String arg0)
	{
		super(arg0);
	}
	
	public void testSanitizeOneChar()
	{
		String result = Sanitizer.sanitize("thisis'atest");
		
		assertEquals("Does not sanitize one char",result.indexOf("'"),-1);
	}
	
	public void testSanitizeOneCharReplace()
	{
		String result = Sanitizer.sanitize("thisis'atest");
		boolean san = result.indexOf("~!?")!=-1;
		
		assertEquals("Does not replace one char",san,true);
	}
	
	public void testSanitizeMultChar()
	{
		String result = Sanitizer.sanitize("th'isis''atest");
		
		assertEquals("Does not sanitize mult char",result.indexOf("'"),-1);
	}
	
	public void testSanitizeMultCharReplace()
	{
		String result = Sanitizer.sanitize("thi'sis''atest'");
		boolean san = result.indexOf("~!?", result.indexOf("~!?")+3)!=-1;
		
		assertEquals("Does not replace mult char",san,true);
	}
	
	public void testSanitizeNoChar()
	{
		String result = Sanitizer.sanitize("thisisatest");
		
		assertEquals("Does not handle no char",result.indexOf("'"),-1);
		assertEquals("Does not handle no char",result.indexOf("~!?"),-1);
	}

}
