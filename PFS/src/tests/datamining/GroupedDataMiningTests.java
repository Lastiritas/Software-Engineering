package tests.datamining;

import org.jmock.integration.junit3.MockObjectTestCase;

import domainobjects.IDSet;
import domainobjects.PayTo;
import system.GroupedCollection;
import system.PFSystem;

public class GroupedDataMiningTests extends MockObjectTestCase
{
	public void test_a()
	{
		GroupedCollection[] results = PFSystem.getCurrent().getFrequentPlacesForDayOfWeek(0.01);
		
		for(int j = 0; j < results.length; j++)
		{
			GroupedCollection collection = results[j];
			
			System.out.format("%s:\n", collection.getName());

			IDSet items = collection.getAllItems();
			
			for(int i = 0; i < items.getSize(); i++)
			{
				PayTo payTo = (PayTo)PFSystem.getCurrent().getPayToSystem().getDataByID(items.getValue(i));
				System.out.format("\t%s\n", payTo);
			}
			
			System.out.println();
		}
	}
}
