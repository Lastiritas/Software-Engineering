package system;

import java.util.Collection;
import java.util.LinkedList;

import system.datamining.DataMiner;
import dataAccessLayer.IDatabase;
import dataAccessLayer.StubDatabase;
import domainobjects.Expense;
import domainobjects.IDSet;

public class PFSystem 
{
	public PayToManagement getPayToSystem()
	{
		return payToSystem;
	}
	
	public LabelManagement getLabelSystem()
	{
		return labelSystem;
	}
	
	public ExpenseManagement getExpenseSystem()
	{
		return expenseSystem;
	}
	
	public Collection<IDSet> getAllFrequentLabelCombinations(double inFrequencyPercent)
	{
		assert inFrequencyPercent >= 0.0 && inFrequencyPercent <= 1.0 : "Invalid percent value, should be 0.0 to 1.0";
		
		final IDSet expenseIDs = expenseSystem.getAllIDs();
		
		LinkedList<IDSet> sets = new LinkedList<IDSet>();
		
		for(int i = 0; i < expenseIDs.getSize(); i++)	// for each expense id
		{
			final int expenseID = expenseIDs.getValue(i);
			final Expense expense = (Expense)getExpenseSystem().getDataByID(expenseID);
			
			sets.add(expense.getLabels());
		}
		
		final int minSup = (int)(sets.size() * inFrequencyPercent);
		
		return DataMiner.mine(sets, minSup);
	}
	
	private IDatabase database = new StubDatabase();
	
	private PayToManagement payToSystem = new PayToManagement(database);
	private LabelManagement labelSystem = new LabelManagement(database);
	private ExpenseManagement expenseSystem = new ExpenseManagement(database);
	
	public static PFSystem getCurrent()
	{
		return current;
	}
	
	private static PFSystem current = new PFSystem();
}
