package tests;

import tests.dataaccesslayer.DatabaseTests;
import tests.dataaccesslayer.StubDatabaseTests;
import tests.domainobject.ExpenseTest;
import tests.domainobject.IDSetTest;
import tests.domainobject.LabelTest;
import tests.domainobject.PayToTest;
import tests.util.SortTests;
import tests.util.StringMatchTests;
import junit.framework.Test;
import junit.framework.TestSuite;


public class AllTests 
{
	public static TestSuite suite;
	
	public static Test suite()
	{
		suite = new TestSuite("All Tests for the Personal Finance System");
		
		testDomainObjects();
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
	
	private static void testDataAccessLayer()
	{
		suite.addTestSuite(DatabaseTests.class);
		suite.addTestSuite(StubDatabaseTests.class);
	}
	
	private static void testUtilities()
	{
		suite.addTestSuite(StringMatchTests.class);
		suite.addTestSuite(SortTests.class);
	}
}
