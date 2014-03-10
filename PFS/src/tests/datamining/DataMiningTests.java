package tests.datamining;

import java.util.ArrayList;
import java.util.Collection;

import org.jmock.integration.junit3.MockObjectTestCase;

import domainobjects.IDSet;
import system.datamining.DataMiner;

public class DataMiningTests extends MockObjectTestCase
{
	private Collection<IDSet> data;
	
	protected void setUp() throws Exception 
	{
		data = new ArrayList<IDSet>();
		
		data.add(IDSet.createFromArray(new int[]{1}));
		data.add(IDSet.createFromArray(new int[]{1, 2}));
		data.add(IDSet.createFromArray(new int[]{1, 2, 3}));
		data.add(IDSet.createFromArray(new int[]{1, 2, 3, 4}));
		data.add(IDSet.createFromArray(new int[]{1, 2, 3, 4, 5}));
		
		super.setUp();
	}
	
	public void test_min_sup_5()
	{
		ArrayList<IDSet> minedData = DataMiner.mine(data, 5);
		
		assertEquals(minedData.size(), 1);
		assertTrue(minedData.get(0).equals(IDSet.createFromArray(new int[] {1})));
	}
	
	public void test_min_sup_4()
	{
		ArrayList<IDSet> minedData = DataMiner.mine(data, 4);
		
		assertEquals(minedData.size(), 3);
		assertTrue(minedData.get(0).equals(IDSet.createFromArray(new int[] {1})));
		assertTrue(minedData.get(1).equals(IDSet.createFromArray(new int[] {2})));
		
		assertTrue(minedData.get(2).equals(IDSet.createFromArray(new int[] {1, 2})));
	}
	
	public void test_min_sup_3()
	{
		ArrayList<IDSet> minedData = DataMiner.mine(data, 3);
		
		assertEquals(minedData.size(), 7);
		assertTrue(minedData.get(0).equals(IDSet.createFromArray(new int[] {1})));
		assertTrue(minedData.get(1).equals(IDSet.createFromArray(new int[] {2})));
		assertTrue(minedData.get(2).equals(IDSet.createFromArray(new int[] {3})));
		
		assertTrue(minedData.get(3).equals(IDSet.createFromArray(new int[] {1, 2})));
		assertTrue(minedData.get(4).equals(IDSet.createFromArray(new int[] {1, 3})));
		assertTrue(minedData.get(5).equals(IDSet.createFromArray(new int[] {2, 3})));
		
		assertTrue(minedData.get(6).equals(IDSet.createFromArray(new int[] {1, 2, 3})));
	}
	
	public void test_min_sup_2()
	{
		ArrayList<IDSet> minedData = DataMiner.mine(data, 2);
		
		assertEquals(minedData.size(), 15);
		assertTrue(minedData.get(0).equals(IDSet.createFromArray(new int[] {1})));
		assertTrue(minedData.get(1).equals(IDSet.createFromArray(new int[] {2})));
		assertTrue(minedData.get(2).equals(IDSet.createFromArray(new int[] {3})));
		assertTrue(minedData.get(3).equals(IDSet.createFromArray(new int[] {4})));
		
		assertTrue(minedData.get(4).equals(IDSet.createFromArray(new int[] {1, 2})));
		assertTrue(minedData.get(5).equals(IDSet.createFromArray(new int[] {1, 3})));
		assertTrue(minedData.get(6).equals(IDSet.createFromArray(new int[] {1, 4})));
		assertTrue(minedData.get(7).equals(IDSet.createFromArray(new int[] {2, 3})));
		assertTrue(minedData.get(8).equals(IDSet.createFromArray(new int[] {2, 4})));
		assertTrue(minedData.get(9).equals(IDSet.createFromArray(new int[] {3, 4})));
		
		assertTrue(minedData.get(10).equals(IDSet.createFromArray(new int[] {1, 2, 3})));
		assertTrue(minedData.get(11).equals(IDSet.createFromArray(new int[] {1, 2, 4})));
		assertTrue(minedData.get(12).equals(IDSet.createFromArray(new int[] {1, 3, 4})));
		assertTrue(minedData.get(13).equals(IDSet.createFromArray(new int[] {2, 3, 4})));
		
		assertTrue(minedData.get(14).equals(IDSet.createFromArray(new int[] {1, 2, 3, 4})));
	}
	
	public void test_min_sup_1()
	{
		ArrayList<IDSet> minedData = DataMiner.mine(data, 1);
		
		assertEquals(minedData.size(), 31);
		assertTrue(minedData.get(0).equals(IDSet.createFromArray(new int[] {1})));
		assertTrue(minedData.get(1).equals(IDSet.createFromArray(new int[] {2})));
		assertTrue(minedData.get(2).equals(IDSet.createFromArray(new int[] {3})));
		assertTrue(minedData.get(3).equals(IDSet.createFromArray(new int[] {4})));
		assertTrue(minedData.get(4).equals(IDSet.createFromArray(new int[] {5})));
		
		assertTrue(minedData.get(5).equals(IDSet.createFromArray(new int[] {1, 2})));
		assertTrue(minedData.get(6).equals(IDSet.createFromArray(new int[] {1, 3})));
		assertTrue(minedData.get(7).equals(IDSet.createFromArray(new int[] {1, 4})));
		assertTrue(minedData.get(8).equals(IDSet.createFromArray(new int[] {1, 5})));
		assertTrue(minedData.get(9).equals(IDSet.createFromArray(new int[] {2, 3})));
		assertTrue(minedData.get(10).equals(IDSet.createFromArray(new int[] {2, 4})));
		assertTrue(minedData.get(11).equals(IDSet.createFromArray(new int[] {2, 5})));
		assertTrue(minedData.get(12).equals(IDSet.createFromArray(new int[] {3, 4})));
		assertTrue(minedData.get(13).equals(IDSet.createFromArray(new int[] {3, 5})));
		assertTrue(minedData.get(14).equals(IDSet.createFromArray(new int[] {4, 5})));
		
		assertTrue(minedData.get(15).equals(IDSet.createFromArray(new int[] {1, 2, 3})));
		assertTrue(minedData.get(16).equals(IDSet.createFromArray(new int[] {1, 2, 4})));
		assertTrue(minedData.get(17).equals(IDSet.createFromArray(new int[] {1, 2, 5})));
		assertTrue(minedData.get(18).equals(IDSet.createFromArray(new int[] {1, 3, 4})));
		assertTrue(minedData.get(19).equals(IDSet.createFromArray(new int[] {1, 3, 5})));
		assertTrue(minedData.get(20).equals(IDSet.createFromArray(new int[] {1, 4, 5})));
		assertTrue(minedData.get(21).equals(IDSet.createFromArray(new int[] {2, 3, 4})));
		assertTrue(minedData.get(22).equals(IDSet.createFromArray(new int[] {2, 3, 5})));
		assertTrue(minedData.get(23).equals(IDSet.createFromArray(new int[] {2, 4, 5})));
		assertTrue(minedData.get(24).equals(IDSet.createFromArray(new int[] {3, 4, 5})));
		
		assertTrue(minedData.get(25).equals(IDSet.createFromArray(new int[] {1, 2, 3, 4})));
		assertTrue(minedData.get(26).equals(IDSet.createFromArray(new int[] {1, 2, 3, 5})));
		assertTrue(minedData.get(27).equals(IDSet.createFromArray(new int[] {1, 2, 4, 5})));
		assertTrue(minedData.get(28).equals(IDSet.createFromArray(new int[] {1, 3, 4, 5})));
		assertTrue(minedData.get(29).equals(IDSet.createFromArray(new int[] {2, 3, 4, 5})));
		
		assertTrue(minedData.get(30).equals(IDSet.createFromArray(new int[] {1, 2, 3, 4, 5})));
	}
}