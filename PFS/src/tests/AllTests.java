package tests;

import systemTests.ExpenseManagementTests;
import systemTests.LabelManagementTests;
import systemTests.PayToManagementTests;
import utiltests.StringMatchTests;
import dataAccessLayerTests.StubDatabaseTests;
import domainobjecttests.ExpenseTest;
import domainobjecttests.IDSetTest;
import domainobjecttests.LabelTest;
import domainobjecttests.PayToTest;
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
		suite.addTestSuite(StubDatabaseTests.class);
	}
	
	private static void testUtilities()
	{
		suite.addTestSuite(StringMatchTests.class);
	}
}
