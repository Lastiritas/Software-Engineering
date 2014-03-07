package util;

import org.eclipse.swt.widgets.TableItem;

import domainobjects.Money;
import domainobjects.SimpleDate;

public class Sort {
	
	public final static int ASCEND =1;
	public final static int DESCEND =2;

	public static int[] sortDate(TableItem[] items, int order)
	{
		int length = items.length;
		SimpleDate date[] = new SimpleDate[length];
		int IDs[] = new int[length];
		
		for(int i=0; i< length; i++)
		{
			IDs[i] = Integer.parseInt(items[i].getText(0));
			date[i] = SimpleDate.Now();
			date[i].setDate(items[i].getText(1));
		}
		
		for(int i=1; i<length; i++)
		{
			SimpleDate temp = date[i];
			int tempID = IDs[i];
			
			int j=0;
			if(order== ASCEND)
			{
				for(j=i-1; (j>=0) && (temp.compareTo(date[j])) < 0; j--)
				{
					date[j+1] = date[j];
					IDs[j+1] = IDs[j];
				}
			}
			else if(order==DESCEND)
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
	
	public static int[] sortMoney(TableItem[] items, int order)
	{
		int length = items.length;
		Money money[] = new Money[length];
		int IDs[] = new int[length];
		
		for(int i=0; i< length; i++)
		{
			IDs[i] = Integer.parseInt(items[i].getText(0));
			money[i] = Money.fromString(items[i].getText(3));
		}
		
		
		for(int i=1; i<length; i++)
		{
			Money temp = money[i];
			int tempID = IDs[i];
			
			int j=0;
			if(order == ASCEND)
			{
				for(j=i-1; (j>=0) && (temp.compareTo(money[j]) < 0); j--)
				{
					money[j+1] = money[j];
					IDs[j+1] = IDs[j];
				}
			}
			else if(order == DESCEND)
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
	
	public static int[] sortID(TableItem[] items, int order)
	{
		int length = items.length;
		int IDs[] = new int[length];
		
		for(int i=0; i< length; i++)
		{
			IDs[i] = Integer.parseInt(items[i].getText(0));
		}
		
		
		for(int i=1; i<length; i++)
		{
			int tempID = IDs[i];
			
			int j=0;
			if(order == ASCEND)
			{
				for(j=i-1; (j>=0) && (tempID < IDs[j]); j--)
				{
					IDs[j+1] = IDs[j];
				}
			}
			else if(order == DESCEND)
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

	public static int[] sortPay(TableItem[] items, int order)
	{
		int length = items.length;
		int IDs[] = new int[length];
		String payTo[] = new String[length];
		
		
		for(int i=0; i< length; i++)
		{
			IDs[i] = Integer.parseInt(items[i].getText(0));
			payTo[i] = items[i].getText(2);
		}
		
		
		for(int i=1; i<length; i++)
		{
			int tempID = IDs[i];
			String temp = payTo[i];
			
			int j=0;
			if(order==ASCEND)
			{
				for(j=i-1; (j>=0) && (temp.compareToIgnoreCase(payTo[j])<0); j--)
				{
					IDs[j+1] = IDs[j];
					payTo[j+1] = payTo[j];
				}
			}
			else if(order ==DESCEND)
			{
				for(j=i-1; (j>=0) && (temp.compareToIgnoreCase(payTo[j])>0); j--)
				{
					IDs[j+1] = IDs[j];
					payTo[j+1] = payTo[j];
				}
			}
			else
			{
				//ERROR
			}
			
			IDs[j+1] = tempID;
			payTo[j+1] = temp;
		}
		
		return IDs;
	}
	
	
}
