package system;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collection;
import java.util.LinkedList;

import system.datamining.DataMiner;
import util.SortDirection;
import util.TableCols;
import dataaccesslayer.Database;
import dataaccesslayer.IDatabase;
import domainobjects.Expense;
import domainobjects.ExpenseFilter;
import domainobjects.IDSet;
import domainobjects.SimpleDate;

public class PFSystem 
{
	public class GroupedCollection
	{
		public String group;
		public IDSet collection = IDSet.empty();
	}
	
	public PFSystem()
	{
		database.open("PFS");
	}
	
	@Override
	protected void finalize()
	{
		database.close();
	}
	
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
	
	public GroupedCollection[] getFrequentPlacesForDayOfWeek(double inFrequencyPercent)
	{
		assert inFrequencyPercent >= 0.0 && inFrequencyPercent <= 1.0 : "Invalid percent value, should be 0.0 to 1.0";
	
		final IDSet expenseIds = expenseSystem.getAllIDs(new ExpenseFilter(), TableCols.DATE, SortDirection.ASCENDING);
		
		Collection<IDSet> sets = new ArrayList<IDSet>();
		
		SimpleDate currentDate = ((Expense)expenseSystem.getDataByID(expenseIds.getValue(0))).getDate();
		IDSet current = IDSet.empty();
		
		for(int i = 0; i < expenseIds.getSize(); i++)
		{
			int expenseId = expenseIds.getValue(i);
			Expense expense = (Expense)expenseSystem.getDataByID(expenseId);
			
			IDSet payToSet = IDSet.createFromValue(expense.getPayTo());
			
			if(currentDate.compareTo(expense.getDate()) == 0)
			{			
				current = current.union(payToSet);
			}
			else
			{
				current = current.union(IDSet.createFromValue(-expense.getDate().getDayOfWeek()));
				sets.add(current);
				
				current = payToSet;
				currentDate = expense.getDate();
			}
		}
		
		int minSup = (int)(sets.size() * inFrequencyPercent);
		
		Collection<IDSet> minedResults = DataMiner.mine(sets, minSup);
		
		GroupedCollection[] groups = new GroupedCollection[7];	// 7 days in the week
		
		for(int i = 0; i < groups.length; i++)
		{
			groups[i] = new GroupedCollection();
		}
		
		groups[Calendar.MONDAY - 1].group 		= "Monday";
		groups[Calendar.TUESDAY - 1].group 		= "Tuesday";
		groups[Calendar.WEDNESDAY - 1].group	= "Wednesday";
		groups[Calendar.THURSDAY - 1].group 	= "Thursday";
		groups[Calendar.FRIDAY - 1].group 		= "Friday";
		groups[Calendar.SATURDAY - 1].group 	= "Saturday";
		groups[Calendar.SUNDAY - 1].group 		= "Sunday";
		
		for(IDSet set : minedResults)
		{	
			int day = set.getValue(0);	// first value will be day because it is negative and the set is sorted smallest to largest			
			if(set.getSize() > 1 && day < 0)
			{
				IDSet paytos = set.difference(IDSet.createFromValue(day));
				
				day = Math.abs(day) - 1;
				groups[day].collection = groups[day].collection.union(paytos);
			}
		}
		
		return groups;
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
	
	private IDatabase database = new Database("PFS");
	
	private PayToManagement payToSystem = new PayToManagement(database);
	private LabelManagement labelSystem = new LabelManagement(database);
	private ExpenseManagement expenseSystem = new ExpenseManagement(database);
	
	public static PFSystem getCurrent()
	{
		return current;
	}
	
	private static PFSystem current = new PFSystem();
}
