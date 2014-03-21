package tests.datamining;

import org.jmock.integration.junit3.MockObjectTestCase;

import domainobjects.PayTo;
import system.PFSystem;

public class GroupedDataMiningTests extends MockObjectTestCase
{
	public void test_a()
	{
		PFSystem.GroupedCollection[] results = PFSystem.getCurrent().getFrequentPlacesForDayOfWeek(0.01);
		
		for(int j = 0; j < results.length; j++)
		{
			PFSystem.GroupedCollection collection = results[j];
			
			System.out.format("%s:\n", collection.group, collection.collection);

			for(int i = 0; i < collection.collection.getSize(); i++)
			{
				PayTo payTo = (PayTo)PFSystem.getCurrent().getPayToSystem().getDataByID(collection.collection.getValue(i));
				System.out.format("\t%s\n", payTo);
			}
			
			System.out.println();
		}
	}
}
