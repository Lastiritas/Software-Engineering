package tests.domainobjecttests;

import domainobjects.IDSet;
import junit.framework.TestCase;

public class IDSetTest extends TestCase
{
	public IDSetTest(String arg0)
	{
		super(arg0);
	}
	
	public void testCreation()
	{
		int[] data = { 1, 2, 3, 4, 5, 6, 7, 8, 9 };
		IDSet set = IDSet.createFromArray(data);
		
		assertNotNull("Create from Array failed to create a set.", set);
	}
	
	public void testCreationWithDuplicates()
	{
		int[] data = { 1, 1, 2, 2 };
		IDSet set = IDSet.createFromArray(data);
		
		assertEquals("Set does not have correct amount of items", set.getSize(), 2);
	}
	
	public void testContains()
	{
		int[] data = { 1, 2, 3, 4 };
		IDSet set = IDSet.createFromArray(data);
	
		for(int i = 0; i < data.length; i++)
		{
			assertTrue("Set is missing a value", set.contains(data[i]));			
		}
	}
	
	public void testDoesNotContain()
	{
		int[] data = { 1, 2, 3, 4 };
		IDSet set = IDSet.createFromArray(data);
	
		assertFalse("Set should not have 5", set.contains(5));			
	}
	
	public void testUnion()
	{
		int[] data0 = { 1, 2, 3, 4 };
		IDSet set0 = IDSet.createFromArray(data0);
	
		int[] data1 = { 5, 6, 7, 8 };
		IDSet set1 = IDSet.createFromArray(data1);

		IDSet union = set0.union(set1);
		
		for(int i = 0; i < data0.length; i++)
		{
			assertTrue("Union is missing a value", union.contains(data0[i]));
		}
		
		for(int i = 0; i < data1.length; i++)
		{
			assertTrue("Union is missing a value", union.contains(data1[i]));
		}
	}

	public void testIntersection()
	{
		int[] data0 = { 1, 2, 3, 4 };
		IDSet set0 = IDSet.createFromArray(data0);
		
		int[] data1 = { 3, 4, 5, 6 };
		IDSet set1 = IDSet.createFromArray(data1);
		
		IDSet intersection = set0.intersect(set1);
	
		for(int i = 0; i < intersection.getSize(); i++)
		{
			assertTrue("Value in intersection that was not in set 0", set0.contains(intersection.getValue(i)));
			assertTrue("Value in intersection that was not in set 1", set1.contains(intersection.getValue(i)));
		}
	}
	
	public void testSetDifference()
	{
		int[] data0 = { 1, 2, 3, 4, 5};
		IDSet set0 = IDSet.createFromArray(data0);
		
		int[] data1 = { 3, 4, 5, 6, 7};
		IDSet set1 = IDSet.createFromArray(data1);
		
		int[] expectedData = { 1, 2};
		IDSet expectedSet = IDSet.createFromArray(expectedData);
				
		IDSet actualSet = set0.setDifference(set1);
		
		assertTrue(actualSet.equals(expectedSet));
	}

	public void testSetDifferenceWithFullAndEmptySet()
	{
		int[] data0 = { 1 };
		IDSet set0 = IDSet.createFromArray(data0);
		
		int[] data1 = { };
		IDSet set1 = IDSet.createFromArray(data1);
		
		int[] expectedData = { 1 };
		IDSet expectedSet = IDSet.createFromArray(expectedData);
		
		IDSet actualSet = set0.setDifference(set1);
		
		assertTrue(actualSet.equals(expectedSet));
	}
	
	public void testSetDifferenceWithEmptyAndFullSet()
	{
		int[] data0 = { 1 };
		IDSet set0 = IDSet.createFromArray(data0);
		
		int[] data1 = { };
		IDSet set1 = IDSet.createFromArray(data1);
		
		int[] expectedData = { };
		IDSet expectedSet = IDSet.createFromArray(expectedData);
		
		IDSet actualSet = set1.setDifference(set0);
		
		assertTrue(actualSet.equals(expectedSet));
	}
	
	public void testSetDifferenceWithEmptyAndEmptySet()
	{
		int[] data0 = { };
		IDSet set0 = IDSet.createFromArray(data0);
		
		int[] data1 = { };
		IDSet set1 = IDSet.createFromArray(data1);
		
		int[] expectedData = { };
		IDSet expectedSet = IDSet.createFromArray(expectedData);
		
		IDSet actualSet = set0.setDifference(set1);
		
		assertTrue(actualSet.equals(expectedSet));
	}
	
	public void testEquals()
	{
		int[] data0 = { 1, 2, 3, 4 };
		IDSet set0 = IDSet.createFromArray(data0);
		IDSet set1 = IDSet.createFromArray(data0);
		
		assertTrue("Sets built with same data are not equal", set0.equals(set1));
	}
}