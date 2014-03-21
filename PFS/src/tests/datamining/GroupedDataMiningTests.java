package tests.datamining;

import org.jmock.integration.junit3.MockObjectTestCase;

import domainobjects.IDSet;
import domainobjects.Label;
import domainobjects.PayTo;
import system.GroupedCollection;
import system.PFSystem;

public class GroupedDataMiningTests extends MockObjectTestCase
{
	public void test_payto()
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
	
	public void test_label()
	{
		GroupedCollection[] results = PFSystem.getCurrent().getFrequentLabelsForDayOfWeek(0.05);
		
		for(int j = 0; j < results.length; j++)
		{
			GroupedCollection collection = results[j];
			
			System.out.format("%s:\n", collection.getName());

			IDSet items = collection.getAllItems();
			
			for(int i = 0; i < items.getSize(); i++)
			{
				Label label = (Label)PFSystem.getCurrent().getLabelSystem().getDataByID(items.getValue(i));
				System.out.format("\t%s\n", label.getName());
			}
			
			System.out.println();
		}
	}
}
