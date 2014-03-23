package gui;

import java.util.Vector;

import domainobjects.Expense;
import domainobjects.IDSet;
import domainobjects.Label;
import domainobjects.PayTo;
import domainobjects.PaymentMethodHelper;
import system.Manager;
import system.PFSystem;
import util.Sort;
import util.SortDirection;
import util.XAxis;

public class ChartHelper 
{
	private IDSet expenseIds;
	private int numberOfExpenses;
	
	private int[] originalExpenseIds;
	private int[] originalExpenseAmounts;
	private int[] originalPaymentMethod;
	private int[] originalDates;
	private String[] originalPayTos;
	
	private int[] sortedExpenseAmounts;
	private int[] sortedPaymentMethod;
	private int[] sortedDates;
	private String[] sortedPayTos;
	
	private Vector<Integer> labelRecord;
	private Vector<Integer> amountRecord;
	private int vectorSize;
	
	private int[] sortedLabelIds;
	private int[] sortedLabelAmounts;
	private int[] labelIds;
	
	public ChartHelper(IDSet ids)
	{
		expenseIds = ids;
		retrieveLists();
	}
	
	private void retrieveLists()
	{
		final Manager expenseSystem = PFSystem.getCurrent().getExpenseSystem();
		numberOfExpenses = expenseIds.getSize();
		
		initializeArrays();
		expenseIds.copyToArray(originalExpenseIds);
		originalExpenseIds = Sort.sortByID(originalExpenseIds, SortDirection.ASCENDING);
		
		for(int i = 0; i < numberOfExpenses; i++)
		{
			final int id = originalExpenseIds[i];
			final Expense expense = (Expense)expenseSystem.getDataByID(id);
			
			originalExpenseAmounts[i] = expense.getAmount().getTotalCents();
			originalPaymentMethod[i] = PaymentMethodHelper.toInteger(expense.getPaymentMethod());
			originalDates[i] = expense.getDate().toInteger()/100;
			originalPayTos[i] = getPayToName(expense.getPayTo());
			
			retrieveLabels(expense);
		}
		
		sortArrays();
		extractLabelLists();
	}
	
	private String getPayToName(int payToId)
	{
		final PayTo payTo = (PayTo)PFSystem.getCurrent().getPayToSystem().getDataByID(payToId);
		return payTo.getName();
	}
	
	private void initializeArrays()
	{
		originalExpenseIds = new int[numberOfExpenses];
		originalExpenseAmounts = new int[numberOfExpenses];
		originalPaymentMethod = new int[numberOfExpenses];
		originalDates = new int[numberOfExpenses];
		originalPayTos = new String[numberOfExpenses];
		
		labelRecord = new Vector<Integer>();
		amountRecord = new Vector<Integer>();
		vectorSize = 0;
	}
	
	private void sortArrays()
	{
		sortedExpenseAmounts = new int[numberOfExpenses];
		sortedPaymentMethod = new int[numberOfExpenses];
		sortedDates = new int[numberOfExpenses];
		sortedPayTos = new String[numberOfExpenses];
		
		System.arraycopy(originalExpenseAmounts, 0, sortedExpenseAmounts, 0, numberOfExpenses);
		System.arraycopy(originalPaymentMethod, 0, sortedPaymentMethod, 0, numberOfExpenses);
		System.arraycopy(originalDates, 0, sortedDates, 0, numberOfExpenses);
		System.arraycopy(originalPayTos, 0, sortedPayTos, 0, numberOfExpenses);
		
		sortedExpenseAmounts = Sort.sortByID(sortedExpenseAmounts, SortDirection.ASCENDING);
		sortedPaymentMethod = Sort.sortByID(sortedPaymentMethod, SortDirection.ASCENDING);
		sortedDates = Sort.sortByID(sortedDates, SortDirection.ASCENDING);
		sortedPayTos = Sort.sortByString(sortedPayTos, SortDirection.ASCENDING);
	}
	
	public int[] getDistinctIntArray(int[] array)
	{
		int size = array.length;
		int[] distinctArray = new int[size];
		
		int sizeDistinct = 0;
		int latestValue = -1;
		
		for(int i=0; i<size; i++)
		{
			int newValue = array[i];
			if(newValue != latestValue)
			{
				distinctArray[sizeDistinct] = newValue;
				latestValue = newValue;
				sizeDistinct++;
			}
		}
		
		int[] output = new int[sizeDistinct];
		System.arraycopy(distinctArray, 0, output, 0, sizeDistinct);
		
		return output;
	}
	
	public String[] getDistinctStringArray(String[] array)
	{
		int size = array.length;
		String[] distinctArray = new String[size];
		
		int sizeDistinct = 0;
		String latestValue = array[sizeDistinct];
		distinctArray[sizeDistinct] = latestValue;
		sizeDistinct++;
		
		for(int i=1; i<size; i++)
		{
			String newValue = array[i];
			if(newValue.compareTo(latestValue) != 0)
			{
				distinctArray[sizeDistinct] = newValue;
				latestValue = newValue;
				sizeDistinct++;
			}
		}
		
		String[] output = new String[sizeDistinct];
		System.arraycopy(distinctArray, 0, output, 0, sizeDistinct);
		
		return output;
	}
	
	public int[] getXAxisIntValues(XAxis axis)
	{
		switch(axis)
		{
			case AMOUNT:
				return null;
			case PAYMENT_METHOD:
				return getDistinctIntArray(sortedPaymentMethod);
			case DATES:
				return getDistinctIntArray(sortedDates);
			default:
				return null;
		}
	}
	
	public String[] getXAxisStringValues(XAxis axis)
	{
		switch(axis)
		{
			case LOCATION:
				return getDistinctStringArray(sortedPayTos);
			case LABELS:
				return getLabelNames();
			default:
				return null;
		}
	}
	
	public double[] getYAxisIntValues(int[] xAxisValues, XAxis axis)
	{
		switch(axis)
		{
			case PAYMENT_METHOD:
				return getYAxisIntValues(xAxisValues, originalPaymentMethod, sortedPaymentMethod);
			case DATES:
				return getYAxisIntValues(xAxisValues, originalDates, sortedDates);
			default:
				return null;
		}
	}
	
	public double[] getYAxisStringValues(String[] xAxisValues, XAxis axis)
	{
		switch(axis)
		{
			case LOCATION:
				return getYAxisStringValues(xAxisValues, originalPayTos, sortedPayTos);
			case LABELS:
				return getYAxisInt(labelIds, sortedLabelIds, sortedLabelAmounts, vectorSize);
			default:
				return null;
		}
	}
	
	//xAxisValues - labelIds {1,2,3,4,5,6,7,8,9...}
		//sortedInput - sortedLabelIds {1,1,1,1,1,1,1,1,1,2,2,2,2,2,2,...}
		//sortedAmountsByFilter - sortedLabelAmounts
	private double[] getYAxisStringValues(String[] xAxisValues, String[] originalInput, String[] sortedInput)
	{
		int[] sortedAmountsByFilter = new int[numberOfExpenses];
		System.arraycopy(originalExpenseAmounts, 0, sortedAmountsByFilter, 0, numberOfExpenses);
		sortedAmountsByFilter = Sort.sortByString(sortedAmountsByFilter, originalInput, SortDirection.ASCENDING);
		double[] amountYAxis = new double[xAxisValues.length];
		
		int currentXAxisValue = 0;
		for(int i=0; i<numberOfExpenses; i++)
		{
			while(xAxisValues[currentXAxisValue].compareTo(sortedInput[i]) != 0)
			{
				currentXAxisValue++;
			}
			int temp = sortedAmountsByFilter[i];
			double tq = (double)temp;
			double amount = tq/100;
			amountYAxis[currentXAxisValue] += amount;
		}
		
		return amountYAxis;
	}
	
	private double[] getYAxisIntValues(int[] xAxisValues, int[] originalInput, int[] sortedInput)
	{
		int[] sortedAmountsByFilter = new int[numberOfExpenses];
		System.arraycopy(originalExpenseAmounts, 0, sortedAmountsByFilter, 0, numberOfExpenses);
		sortedAmountsByFilter = Sort.sortByIntArrays(sortedAmountsByFilter, originalInput, SortDirection.ASCENDING);
		
		return getYAxisInt(xAxisValues, sortedInput, sortedAmountsByFilter, numberOfExpenses);
	}
	
	private double[] getYAxisInt(int[] xAxisValues, int[] sortedInput, int[] sortedAmountsByFilter, int size)
	{
		double[] amountYAxis = new double[xAxisValues.length];
		
		int currentXAxisValue = 0;
		for(int i=0; i<size; i++)
		{
			while(xAxisValues[currentXAxisValue] != sortedInput[i])
			{
				currentXAxisValue++;
			}
			int temp = sortedAmountsByFilter[i];
			double tq = (double)temp;
			double amount = tq/100;
			amountYAxis[currentXAxisValue] += amount;
		}
		
		return amountYAxis;
	}	
	
	public String[] createCategoriesForDates(int[] xAxisValues)
	{
		int length = xAxisValues.length;
		String[] xAxis = new String[length];
		
		for(int i=0; i<length; i++)
		{
			int month = xAxisValues[i]%100;
			int year = xAxisValues[i]/100;
			xAxis[i] = MonthAbbreviation.values()[month] + " " + year;
		}
		return xAxis;
	}
	
	public String[] convertToStringArray(int[] array)
	{
		int size = array.length;
		String[] newArray = new String[size];
		for(int i=0; i<size; i++)
		{
			newArray[i] = Integer.toString(array[i]);
		}
		return newArray;
	}
	
	private void retrieveLabels(Expense expense)
	{
		int amount = expense.getAmount().getTotalCents();
		IDSet labelIds = expense.getLabels();
		int numberOfLabels = labelIds.getSize();
		
		for(int i=0; i<numberOfLabels; i++)
		{
			labelRecord.add(labelIds.getValue(i));
			amountRecord.add(amount);
			vectorSize++;
		}
	}
	
	private void extractLabelLists()
	{
		sortedLabelIds = new int[vectorSize];
		sortedLabelAmounts = new int[vectorSize];
		
		for(int i=0; i<vectorSize; i++)
		{
			sortedLabelIds[i] = labelRecord.elementAt(i);
			sortedLabelAmounts[i] = amountRecord.elementAt(i);
		}
		
		sortedLabelAmounts = Sort.sortByIntArrays(sortedLabelAmounts, sortedLabelIds, SortDirection.ASCENDING);
	}
	
	private String[] getLabelNames()
	{
		final Manager labelMgmt = PFSystem.getCurrent().getLabelSystem();
		final IDSet labelIdSet = labelMgmt.getAllIDs();
		
		int size = labelIdSet.getSize();
		String[] labelNames = new String[size];
		labelIds = new int[size];
		
		for(int i=0; i<size; i++)
		{
			int id = labelIdSet.getValue(i);
			labelIds[i] = id;
			Label label = (Label)labelMgmt.getDataByID(id);
			labelNames[i] = label.getName();
		}
		
		return labelNames;
	}
	
	public enum MonthAbbreviation
	{
		INVALID,
		Jan,
		Feb,
		Mar,
		Apr,
		May,
		June,
		July,
		Aug,
		Sept,
		Oct,
		Nov,
		Dec
	}
}
