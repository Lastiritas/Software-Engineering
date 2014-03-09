package tests;

import tests.dataAccessLayerTests.DatabaseTests;
import tests.dataAccessLayerTests.StubDatabaseTests;
import tests.domainobjecttests.ExpenseTest;
import tests.domainobjecttests.IDSetTest;
import tests.domainobjecttests.LabelTest;
import tests.domainobjecttests.PayToTest;
import tests.systemTests.ExpenseManagementTests;
import tests.systemTests.LabelManagementTests;
import tests.systemTests.PayToManagementTests;
import tests.utiltests.StringMatchTests;
import junit.framework.Test;
import junit.framework.TestSuite;


public class AllTests 
{
	public static TestSuite suite;
	
	public static Test suite()
	{
		suite = new TestSuite("All Tests for the Personal Finance System");
		
		testDomainObjects();
		testSystem();
		testDataAccessLayer();
		testUtilities();
		
		return suite;
	}
	
	private static void testDomainObjects()
	{
		suite.addTestSuite(ExpenseTest.class);
		suite.addTestSuite(IDSetTest.class);
		suite.addTestSuite(LabelTest.class);
		suite.addTestSuite(PayToTest.class);
	}
	
	private static void testSystem()
	{
		suite.addTestSuite(ExpenseManagementTests.class);
		suite.addTestSuite(LabelManagementTests.class);
		suite.addTestSuite(PayToManagementTests.class);
	}
	
	private static void testDataAccessLayer()
	{
		suite.addTestSuite(DatabaseTests.class);
		suite.addTestSuite(StubDatabaseTests.class);
	}
	
	private static void testUtilities()
	{
		suite.addTestSuite(StringMatchTests.class);
	}
}
