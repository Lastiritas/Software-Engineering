package tests;

import tests.integrationtests.StubTest;
import tests.integrationtests.DBTest;
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
		suite.addTestSuite(DBTest.class);
		suite.addTestSuite(StubTest.class);
	}
	
}
