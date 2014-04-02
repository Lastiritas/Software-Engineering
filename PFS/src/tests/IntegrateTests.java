package tests;

import tests.integrationtests.SeamStubTests;
import tests.integrationtests.SeamTests;
import junit.framework.Test;
import junit.framework.TestSuite;

public class IntegrateTests
{
	public static TestSuite suite;
	
	public static Test suite()
	{
		suite = new TestSuite("Integration Tests for the Personal Finance System");
		
		testSeams();
		
		return suite;
	}
	
	private static void testSeams()
	{
		suite.addTestSuite(SeamTests.class);
		suite.addTestSuite(SeamStubTests.class);
	}
	
}
