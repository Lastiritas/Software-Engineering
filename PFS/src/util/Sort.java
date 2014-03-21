package util;

import org.eclipse.swt.widgets.TableItem;

import domainobjects.Money;
import domainobjects.SimpleDate;

public class Sort 
{
	private static int[] sortByDate(TableItem[] items, SortDirection direction)
	{
		return sortByIntArrays(getIdsFromTableItem(items), getDatesFromTableItem(items), direction);
	}
	
 	private static int[] sortByMoney(TableItem[] items, SortDirection direction)
 	{
 		return sortByIntArrays(getIdsFromTableItem(items), getMoneyFromTableItem(items), direction);
 	}
 	
	public static int[] sortByIntArrays(int[] IDs, int[] elements, SortDirection direction)
	{
		int length = IDs.length;
		
		for(int i=1; i<length; i++)
		{
			int temp = elements[i];
			int tempID = IDs[i];
			
			int j=0;
			if(direction == SortDirection.ASCENDING)
			{
				for(j=i-1; (j>=0) && temp < elements[j]; j--)
				{
					elements[j+1] = elements[j];
					IDs[j+1] = IDs[j];
				}
			}
			else if(direction == SortDirection.DESCENDING)
			{
				for(j=i-1; (j>=0) && temp > elements[j]; j--)
				{
					elements[j+1] = elements[j];
					IDs[j+1] = IDs[j];
				}
			}
			else
			{
				//ERROR
			}
			
			elements[j+1]=temp;
			IDs[j+1] = tempID;
		}
		
		return IDs;
	}
	
	private static int[] sortByID(TableItem[] items, SortDirection direction)
	{
		return sortByID(getIdsFromTableItem(items), direction);
	}
	
	public static int[] sortByID(int[] IDs, SortDirection direction)
	{
		int length = IDs.length;
		
		for(int i=1; i<length; i++)
		{
			int tempID = IDs[i];
			
			int j=0;
			if(direction == SortDirection.ASCENDING)
			{
				for(j=i-1; (j>=0) && (tempID < IDs[j]); j--)
				{
					IDs[j+1] = IDs[j];
				}
			}
			else if(direction == SortDirection.DESCENDING)
			{
				for(j=i-1; (j>=0) && (tempID > IDs[j]); j--)
				{
					IDs[j+1] = IDs[j];
				}
			}
			else
			{
				//ERROR
			}
			
			IDs[j+1] = tempID;
		}
		
		return IDs;
	}
	
	private static int[] sortByString(TableItem[] items, TableCols colName, SortDirection direction)
	{
		return sortByString(getIdsFromTableItem(items), getStringsFromTableItem(items, colName), direction);
	}
	
	public static int[] sortByString(int[] IDs, String[] elements, SortDirection direction)
	{
		int length = IDs.length;
		
		for(int i=1; i<length; i++)
		{
			int tempID = IDs[i];
			String temp = elements[i];
			
			int j=0;
			if(direction == SortDirection.ASCENDING)
			{
				for(j=i-1; (j>=0) && (temp.compareToIgnoreCase(elements[j])<0); j--)
				{
					IDs[j+1] = IDs[j];
					elements[j+1] = elements[j];
				}
			}
			else if(direction == SortDirection.DESCENDING)
			{
				for(j=i-1; (j>=0) && (temp.compareToIgnoreCase(elements[j])>0); j--)
				{
					IDs[j+1] = IDs[j];
					elements[j+1] = elements[j];
				}
			}
			else
			{
				//ERROR
			}
			
			IDs[j+1] = tempID;
			elements[j+1] = temp;
		}
		
		return IDs;
	}
	
	private static int[] getIdsFromTableItem(TableItem[] items)
	{
		int length = items.length;
		int IDs[] = new int[length];
		
		for(int i=0; i<length; i++)
		{
			IDs[i] = Integer.parseInt(items[i].getText(TableCols.ID.ordinal()));
		}
		
		return IDs;
	}
	
	private static String[] getStringsFromTableItem(TableItem[] items, TableCols colName)
	{
		int length = items.length;
		String elements[] = new String[length];
		
		for(int i=0; i< length; i++)
		{
			elements[i] = items[i].getText(colName.ordinal());
		}
		
		return elements;
	}
	
	private static int[] getMoneyFromTableItem(TableItem[] items)
	{
		int length = items.length;
		int elements[] = new int[length];
		
		for(int i=0; i< length; i++)
		{
			elements[i] = Money.fromString(items[i].getText(TableCols.MONEY.ordinal())).getTotalCents();
		}
		
		return elements;
	}
	
	private static int[] getDatesFromTableItem(TableItem[] items)
	{
		int length = items.length;
		SimpleDate date = SimpleDate.Now();
		int [] dates = new int[length];
		
		for(int i=0; i< length; i++)
		{
			date.setDate(items[i].getText(TableCols.DATE.ordinal()));
			dates[i] = date.toInteger();
		}
		
		return dates;
	}
	
	public static int[] sortCollection(TableCols column, SortDirection direction, TableItem[] items)
	{
		int[] IDs = null;
		
		switch(column)
		{
			case ID:
				return (direction == SortDirection.ASCENDING) ? sortByID(items, SortDirection.ASCENDING) : sortByID(items, SortDirection.DESCENDING);
			case DATE:
				return (direction == SortDirection.ASCENDING) ? sortByDate(items, SortDirection.ASCENDING) : sortByDate(items, SortDirection.DESCENDING);
			case PAYTO:
				return (direction == SortDirection.ASCENDING) ? sortByString(items, TableCols.PAYTO, SortDirection.ASCENDING) : sortByString(items, TableCols.PAYTO, SortDirection.DESCENDING);
			case MONEY:
				return (direction == SortDirection.ASCENDING) ? sortByMoney(items, SortDirection.ASCENDING) : sortByMoney(items, SortDirection.DESCENDING);
			case DESCRIPTION:
				return (direction == SortDirection.ASCENDING) ? sortByString(items, TableCols.DESCRIPTION, SortDirection.ASCENDING) : sortByString(items, TableCols.DESCRIPTION, SortDirection.DESCENDING);  
			default:
				return IDs;
		}
	}
	
	public static String createSQLWhereClause(String whereClause, TableCols column, SortDirection direction)
	{
		switch(column)
		{
			case ID:
				return (direction == SortDirection.ASCENDING) ? String.format("%s order by expenseId", whereClause) : String.format("%s order by expenseId desc", whereClause);
			case DATE:
				return (direction == SortDirection.ASCENDING) ? String.format("%s order by date", whereClause) : String.format("%s order by date desc", whereClause);
			case PAYTO:
				return (direction == SortDirection.ASCENDING) ? whereClause : whereClause;
			case MONEY:
				return (direction == SortDirection.ASCENDING) ? String.format("%s order by cents", whereClause) : String.format("%s order by cents desc", whereClause);
			case DESCRIPTION:
				return (direction == SortDirection.ASCENDING) ? String.format("%s order by description", whereClause) : String.format("%s order by description desc", whereClause);
			case PAYMENT_METHOD:
				return (direction == SortDirection.ASCENDING) ? String.format("%s order by paymentMethod", whereClause) : String.format("%s order by paymentMethod desc", whereClause);
			default:
				return whereClause;
		}
	}
}
