package gui;

import java.util.Arrays;
import java.util.Vector;

import domainobjects.Expense;
import domainobjects.ExpenseFilter;
import domainobjects.IDSet;
import domainobjects.PayTo;
import domainobjects.PaymentMethodHelper;
import system.ExpenseManagement;
import system.PFSystem;
import util.Sort;
import util.SortDirection;
import util.TableCols;
import util.XAxis;

public class ChartHelper 
{
	private IDSet expenseIds;
	private int numberOfExpenses;
	private int[] originalExpenseIds; //This array is already sorted
	private int[] originalExpenseAmounts;
	private int[] originalPaymentMethod;
	private int[] originalDates;
	private String[] originalPayTos;
	
	private int[] sortedExpenseAmounts;
	private int[] sortedPaymentMethod;
	private int[] sortedDates;
	private String[] sortedPayTos;
	//private String[] labels;
	
	public ChartHelper(IDSet ids)
	{
		expenseIds = ids;
		retrieveLists();
	}
	
	private void retrieveLists()
	{
		//final ExpenseFilter filter = new ExpenseFilter();
		final ExpenseManagement expenseSystem = PFSystem.getCurrent().getExpenseSystem();
		//final IDSet expenseIDs = expenseSystem.getAllIDs(filter, TableCols.ID, SortDirection.ASCENDING);
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
			originalDates[i] = expense.getDate().toInteger();
			originalPayTos[i] = getPayToName(expense.getPayTo());
			//labels[i] = 
		}
		
		sortArrays();
	}
	
	private String getPayToName(int payToId)
	{
		final PayTo payTo = (PayTo)PFSystem.getCurrent().getPayToSystem().getDataByID(payToId);
		return payTo.toString();
	}
	
	private void initializeArrays()
	{
		originalExpenseIds = new int[numberOfExpenses];
		originalExpenseAmounts = new int[numberOfExpenses];
		originalPaymentMethod = new int[numberOfExpenses];
		originalDates = new int[numberOfExpenses];
		originalPayTos = new String[numberOfExpenses];
		//labels = new String[numberOfExpenses];
	}
	
	private void sortArrays()
	{
		sortedExpenseAmounts = new int[numberOfExpenses];
		sortedPaymentMethod = new int[numberOfExpenses];
		sortedDates = new int[numberOfExpenses];
		
		System.arraycopy(originalExpenseAmounts, 0, sortedExpenseAmounts, 0, numberOfExpenses);
		System.arraycopy(originalPaymentMethod, 0, sortedPaymentMethod, 0, numberOfExpenses);
		System.arraycopy(originalDates, 0, sortedDates, 0, numberOfExpenses);
		
		sortedExpenseAmounts = Sort.sortByID(sortedExpenseAmounts, SortDirection.ASCENDING);
		sortedPaymentMethod = Sort.sortByID(sortedPaymentMethod, SortDirection.ASCENDING);
		sortedDates = Sort.sortByID(sortedDates, SortDirection.ASCENDING);
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
		sizeDistinct++;
		
		for(int i=1; i<size; i++)
		{
			String newValue = array[i];
			if(newValue.compareTo(latestValue) == 0)
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
	
	public int[] getXAxisValues(XAxis axis)
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
	
	public double[] getYAxisValues(int[] xAxisValues, XAxis axis)
	{
		switch(axis)
		{
			case PAYMENT_METHOD:
				return getYAxisValues(xAxisValues, originalPaymentMethod, sortedPaymentMethod);
			case DATES:
				return getYAxisValues(xAxisValues, originalDates, sortedDates);
			default:
				return null;
		}
	}
	
	public double[] getYAxisValues(int[] xAxisValues, int[] originalInput, int[] sortedInput)
	{
		//Sort the Amounts by PaymentMethod
		int[] sortedAmountsByFilter = Sort.sortByIntArrays(originalExpenseAmounts, originalInput, SortDirection.ASCENDING);
		double[] amountYAxis = new double[xAxisValues.length];
		
		int currentXAxisValue = 0;
		for(int i=0; i<numberOfExpenses; i++)
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
}
