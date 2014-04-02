package system;

import java.util.Collection;
import java.util.LinkedList;

import system.datamining.DataMiner;
import dataaccesslayer.Database;
import dataaccesslayer.IDatabase;
import dataaccesslayer.StubDatabase;
import domainobjects.Expense;
import domainobjects.IDSet;
import domainobjects.SimpleDate;

public class PFSystem 
{
	public PFSystem()
	{
		database.open("PFS");
		
		expenseSystem = new ExpenseManagement(database);
		labelSystem = new LabelManagement(database);
		payToSystem = new PayToManagement(database);
	}
	
	private PFSystem(IDatabase db, String dbname)
	{
		database = db;
		database.open(dbname);
		
		expenseSystem = new ExpenseManagement(database);
		labelSystem = new LabelManagement(database);
		payToSystem = new PayToManagement(database);
	}
	
	public void closePFSystem()
	{
		database.close();
	}
	
	public Manager getPayToSystem()
	{
		return payToSystem;
	}
	
	public Manager getLabelSystem()
	{
		return labelSystem;
	}
	
	public Manager getExpenseSystem()
	{
		return expenseSystem;
	}
	
	private GroupedCollection[] groupLabels()
	{
		GroupedCollection[] days = createGroupsForDaysOfWeek();
		
		IDSet expenseIds = expenseSystem.getAllIDs();
	
		for(int i = 0; i < expenseIds.getSize(); i++)
		{
			int expenseId = expenseIds.getValue(i);
			Expense expense = (Expense)expenseSystem.getDataByID(expenseId);
			
			int dayOfWeek = expense.getDate().getDayOfWeek();
			
			days[dayOfWeek].include(expense.getLabels());
		}
		
		return days;
	}
	
	private GroupedCollection[] groupPayTos()
	{
		GroupedCollection[] days = createGroupsForDaysOfWeek();
		
		IDSet expenseIds = expenseSystem.getAllIDs();
	
		for(int i = 0; i < expenseIds.getSize(); i++)
		{
			int expenseId = expenseIds.getValue(i);
			Expense expense = (Expense)expenseSystem.getDataByID(expenseId);
			
			int dayOfWeek = expense.getDate().getDayOfWeek();
			
			days[dayOfWeek].include(IDSet.createFromValue(expense.getPayTo()));
		}
		
		return days;
	}
	
	public GroupedCollection[] getFrequentLabelsForDayOfWeek(double inFrequencyPercent)
	{
		GroupedCollection[] groups = groupLabels();
		GroupedCollection[] output = createGroupsForDaysOfWeek();
		
		for(int i = 0; i < groups.length; i++)
		{
			IDSet sets = IDSet.empty();
			
			Collection<IDSet> source = groups[i].getAll();
			
			int minSup = calculateMinSupForSet(source, inFrequencyPercent);
			Collection<IDSet> minedResults = DataMiner.mine(source, minSup);
			
			for(IDSet set : minedResults)
			{
				sets = sets.union(set);
			}
			
			output[i].include(sets);
		}
		
		return output;
	}
	
	public GroupedCollection[] getFrequentPlacesForDayOfWeek(double inFrequencyPercent)
	{
		GroupedCollection[] groups = groupPayTos();
		GroupedCollection[] output = createGroupsForDaysOfWeek();
		
		for(int i = 0; i < groups.length; i++)
		{
			IDSet sets = IDSet.empty();
			
			Collection<IDSet> source = groups[i].getAll();
			
			int minSup = calculateMinSupForSet(source, inFrequencyPercent);
			Collection<IDSet> minedResults = DataMiner.mine(source, minSup);
			
			for(IDSet set : minedResults)
			{
				sets = sets.union(set);
			}
			
			output[i].include(sets);
		}
		
		return output;
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
	
	private Manager payToSystem;
	private Manager labelSystem;
	private Manager expenseSystem;
	
	private static boolean useStub = false;
	
	public static void forceStub()
	{
		useStub = true;
		current = null;
	}
	
	public static void forceReal()
	{
		useStub = false;
		current = null;
	}
	
	public static PFSystem getCurrent()
	{
		if(current == null)
		{
			if(useStub)
			{
				current = new PFSystem(new StubDatabase(), "Test");
			}
			else
			{
				current = new PFSystem();
			}
		}
		
		return current;
	}
	
	private static int calculateMinSupForSet(Collection<IDSet> inSet, double inFrequencyPercent)
	{
		assert inSet != null;
		assert inFrequencyPercent >= 0.0 && inFrequencyPercent <= 1.0 : "Invalid percent value, should be 0.0 to 1.0";
		
		int minSup = (int)(inSet.size() * inFrequencyPercent);
		
		return Math.max(minSup, 1);	// never allow anything less than zero to be returned
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
	
	private static PFSystem current = null;
}
