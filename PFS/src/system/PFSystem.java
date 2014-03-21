package system;

import java.util.ArrayList;
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
	public PFSystem()
	{
		database.open("PFS");
	}
	
	public void closePFSystem()
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
	
	public GroupedCollection[] getFrequentLabelsForDayOfWeek(double inFrequencyPercent)
	{
		IDSet expenseIds = expenseSystem.getAllIDs();
		Collection<IDSet> sets = new ArrayList<IDSet>();
		
		for(int i = 0; i < expenseIds.getSize(); i++)
		{
			int expenseId = expenseIds.getValue(i);
			Expense expense = (Expense)expenseSystem.getDataByID(expenseId);
			
			IDSet labels = expense.getLabels();
			IDSet daySet = getSetFromDay(expense.getDate().getDayOfWeek());
			
			sets.add(labels.union(daySet));
		}
		
		int minSup = calculateMinSupForSet(sets, inFrequencyPercent);
		Collection<IDSet> minedResults = DataMiner.mine(sets, minSup);
		
		return groupResults(minedResults);
	}
	
	public GroupedCollection[] getFrequentPlacesForDayOfWeek(double inFrequencyPercent)
	{
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
				IDSet daySet = getSetFromDay(expense.getDate().getDayOfWeek());
				current = current.union(daySet);
				sets.add(current);
				
				current = payToSet;
				currentDate = expense.getDate();
			}
		}
		
		int minSup = calculateMinSupForSet(sets, inFrequencyPercent);
		
		Collection<IDSet> minedResults = DataMiner.mine(sets, minSup);
		
		return groupResults(minedResults);
	}
	
	public Collection<IDSet> getAllFrequentLabelCombinations(double inFrequencyPercent)
	{
		final IDSet expenseIDs = expenseSystem.getAllIDs();
		
		LinkedList<IDSet> sets = new LinkedList<IDSet>();
		
		for(int i = 0; i < expenseIDs.getSize(); i++)	// for each expense id
		{
			final int expenseID = expenseIDs.getValue(i);
			final Expense expense = (Expense)getExpenseSystem().getDataByID(expenseID);
			
			sets.add(expense.getLabels());
		}
		
		int minSup = calculateMinSupForSet(sets, inFrequencyPercent);
		
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
	
	private static int calculateMinSupForSet(Collection<IDSet> inSet, double inFrequencyPercent)
	{
		assert inSet != null;
		assert inFrequencyPercent >= 0.0 && inFrequencyPercent <= 1.0 : "Invalid percent value, should be 0.0 to 1.0";
		
		return (int)(inSet.size() * inFrequencyPercent);
	}
	
	private static GroupedCollection[] createGroupsForDaysOfWeek()
	{
		GroupedCollection[] groups = new GroupedCollection[7];	// 7 days in the week
		
		groups[SimpleDate.SUNDAY] 		= new GroupedCollection("Sunday");
		groups[SimpleDate.MONDAY] 		= new GroupedCollection("Monday");
		groups[SimpleDate.TUESDAY] 		= new GroupedCollection("Tuesday");
		groups[SimpleDate.WEDNESDAY] 	= new GroupedCollection("Wednesday");
		groups[SimpleDate.THURSDAY] 	= new GroupedCollection("Thursday");
		groups[SimpleDate.FRIDAY] 		= new GroupedCollection("Friday");
		groups[SimpleDate.SATURDAY] 	= new GroupedCollection("Saturday");
		
		return groups;
	}
	
	private static IDSet getSetFromDay(int inDay)
	{
		assert inDay >= SimpleDate.SUNDAY && inDay <= SimpleDate.SATURDAY;
		
		return IDSet.createFromValue(-(inDay + 1));
	}
	
	private static int getDayFromSet(IDSet inSet)
	{
		assert inSet.getSize() == 1;
		assert inSet.getValue(0) < 0;
		
		int unnormalizedDay = inSet.getValue(0);	// first value will be day because it is negative and the set is sorted smallest to largest			
	
		return Math.abs(unnormalizedDay) - 1;	// undo the one offset done earlier
	}
	
	private static GroupedCollection[] groupResults(Collection<IDSet> results)
	{
		GroupedCollection[] groups = createGroupsForDaysOfWeek();
		
		for(IDSet set : results)
		{	
			int firstValue = set.getValue(0);	// first value will be day because it is negative and the set is sorted smallest to largest			
			
			if(set.getSize() > 1 && firstValue < 0)	// must have size greater than one to avoid returning just the day
			{
				IDSet daySet = IDSet.createFromValue(set.getValue(0));
				IDSet paytos = set.difference(daySet);
				
				int day = getDayFromSet(daySet);
				
				groups[day].expandToInclude(paytos);
			}
		}
		
		return groups;
	}
	
	private static PFSystem current = new PFSystem();
}
