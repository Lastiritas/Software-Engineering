package util;

import org.eclipse.swt.widgets.TableItem;

import domainobjects.Money;
import domainobjects.SimpleDate;

public class Sort 
{
	private static int[] sortByDate(TableItem[] items, SortDirection direction)
	{
		return sortByDate(getIdsFromTableItem(items), getDatesFromTableItem(items), direction);
	}
	
 	private static int[] sortByDate(int[] IDs, SimpleDate[] date, SortDirection direction)
	{
		int length = IDs.length;
		
		for(int i=1; i<length; i++)
		{
			SimpleDate temp = date[i];
			int tempID = IDs[i];
			
			int j=0;
			if(direction == SortDirection.ASCENDING)
			{
				for(j=i-1; (j>=0) && (temp.compareTo(date[j])) < 0; j--)
				{
					date[j+1] = date[j];
					IDs[j+1] = IDs[j];
				}
			}
			else if(direction == SortDirection.DESCENDING)
			{
				for(j=i-1; (j>=0) && (temp.compareTo(date[j])) > 0; j--)
				{
					date[j+1] = date[j];
					IDs[j+1] = IDs[j];
				}
			}
			else
			{
				//SHOULDNT HAPPEN: ERROR
			}
			
			date[j+1]=temp;
			IDs[j+1] = tempID;
		}
		
		return IDs;
	}
	
 	private static int[] sortByMoney(TableItem[] items, SortDirection direction)
 	{
 		return sortByMoney(getIdsFromTableItem(items), getMoneyFromTableItem(items), direction);
 	}
 	
	private static int[] sortByMoney(int[] IDs, Money[] money, SortDirection direction)
	{
		int length = IDs.length;
		
		for(int i=1; i<length; i++)
		{
			Money temp = money[i];
			int tempID = IDs[i];
			
			int j=0;
			if(direction == SortDirection.ASCENDING)
			{
				for(j=i-1; (j>=0) && (temp.compareTo(money[j]) < 0); j--)
				{
					money[j+1] = money[j];
					IDs[j+1] = IDs[j];
				}
			}
			else if(direction == SortDirection.DESCENDING)
			{
				for(j=i-1; (j>=0) && (temp.compareTo(money[j]) > 0); j--)
				{
					money[j+1] = money[j];
					IDs[j+1] = IDs[j];
				}
			}
			else
			{
				//ERROR
			}
			
			money[j+1]=temp;
			IDs[j+1] = tempID;
		}
		
		return IDs;
	}
	
	private static int[] sortByID(TableItem[] items, SortDirection direction)
	{
		return sortByID(getIdsFromTableItem(items), direction);
	}
	
	private static int[] sortByID(int[] IDs, SortDirection direction)
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
	
	private static int[] sortByString(int[] IDs, String[] elements, SortDirection direction)
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
	
	private static Money[] getMoneyFromTableItem(TableItem[] items)
	{
		int length = items.length;
		Money elements[] = new Money[length];
		
		for(int i=0; i< length; i++)
		{
			elements[i] = Money.fromString(items[i].getText(TableCols.MONEY.ordinal()));
		}
		
		return elements;
	}
	
	private static SimpleDate[] getDatesFromTableItem(TableItem[] items)
	{
		int length = items.length;
		SimpleDate dates[] = new SimpleDate[length];
		
		for(int i=0; i< length; i++)
		{
			dates[i] = SimpleDate.Now();
			dates[i].setDate(items[i].getText(TableCols.DATE.ordinal()));
		}
		
		return dates;
	}
	
	public static int[] sortCollection(TableCols column, SortDirection direction, TableItem[] items)
	{
		int[] IDs = null;
		
		switch(column)
		{
			case ID:
				if(direction == SortDirection.ASCENDING)
					return sortByID(items, SortDirection.ASCENDING);
				else
					return sortByID(items, SortDirection.DESCENDING);
			case DATE:
				if(direction == SortDirection.ASCENDING)
					return sortByDate(items, SortDirection.ASCENDING);
				else
					return sortByDate(items, SortDirection.DESCENDING);
			case PAYTO:
				if(direction == SortDirection.ASCENDING)
					return sortByString(items, TableCols.PAYTO, SortDirection.ASCENDING);
				else
					return sortByString(items, TableCols.PAYTO, SortDirection.DESCENDING);
			case MONEY:
				if(direction == SortDirection.ASCENDING)
					return sortByMoney(items, SortDirection.ASCENDING);
				else
					return sortByMoney(items, SortDirection.DESCENDING);
			case DESCRIPTION:
				if(direction == SortDirection.ASCENDING)
					return sortByString(items, TableCols.DESCRIPTION, SortDirection.ASCENDING);
				else
					return sortByString(items, TableCols.DESCRIPTION, SortDirection.DESCENDING);
			default:
				return IDs;
		}
	}
	
	public static String createSQLWhereClause(String whereClause, TableCols column, SortDirection direction)
	{
		switch(column)
		{
			case ID:
				if(direction == SortDirection.ASCENDING)
					return String.format("%s order by expenseId", whereClause);
				else
					return String.format("%s order by expenseId desc", whereClause);
			case DATE:
				if(direction == SortDirection.ASCENDING)
					return String.format("%s order by date", whereClause);
				else
					return String.format("%s order by date desc", whereClause);
			case PAYTO:
				if(direction == SortDirection.ASCENDING)
					return whereClause;
				else
					return whereClause;
			case MONEY:
				if(direction == SortDirection.ASCENDING)
					return String.format("%s order by cents", whereClause);
				else
					return String.format("%s order by cents desc", whereClause);
			case DESCRIPTION:
				if(direction == SortDirection.ASCENDING)
					return String.format("%s order by description", whereClause);
				else
					return String.format("%s order by description desc", whereClause);
			default:
				return whereClause;
		}
	}
}
