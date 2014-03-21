package tests.util;


import org.eclipse.swt.SWT;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.swt.widgets.Table;
import org.eclipse.swt.widgets.TableColumn;
import org.eclipse.swt.widgets.TableItem;

import util.Sort;
import util.SortDirection;
import util.TableCols;
import junit.framework.TestCase;


public class SortTests extends TestCase 
{
	private Table table;
	
	public SortTests(String arg0)
	{
		super(arg0);
	}
	
	protected void setUp() throws Exception 
	{
		table = new Table(new Composite(new Shell(), SWT.NONE), SWT.NONE);
		for(int i=0; i <5; i++)
		{
			new TableColumn(table, SWT.NONE);
		}
		
		String payTo[] = {"mcdonalds, barcelona",
							"Tim hortons, whyte ridge",
							"wendys, kenaston",
							"famous daves",
							"Walart, st vital",
							"walmart, st.vital",
							"sport check, waverly",
							"boston pizza, regent",
							"office depot, polo park",
							"apple store, polo park"};
		
		for(int i=1; i <11; i++)
		{
			TableItem temp = new TableItem(table, SWT.NONE);
			
			String testVal[] = new String[5];
			testVal[0] = ""+i;
			testVal[1] = i+"/"+i+"/20"+(i+10);
			testVal[2] = payTo[i-1];
			testVal[3] = ""+(i*10)+".00";
			testVal[4] = ""+(i*i);
			temp.setText(testVal);
		}
		
	}
	
	public void testSortIDAsc() 
	{
		int actual[] = Sort.sortCollection(TableCols.ID, SortDirection.ASCENDING, table.getItems());
		int expected[] = {1,2,3,4,5,6,7,8,9,10};
		
		boolean result=compareLists(expected, actual);
		
		assertEquals("IDs not Ascending",result, true);
	}
	
	public void testSortIDDesc() 
	{
		int actual[] = Sort.sortCollection(TableCols.ID, SortDirection.DESCENDING, table.getItems());
		int expected[] = {10,9,8,7,6,5,4,3,2,1};
		
		boolean result=compareLists(expected, actual);
		
		assertEquals("IDs not Ascending",result, true);
	}
	
	public void testSortDateAsc() 
	{
		int actual[] = Sort.sortCollection(TableCols.DATE, SortDirection.ASCENDING, table.getItems());
		int expected[] = {1,2,3,4,5,6,7,8,9,10};
		
		boolean result=compareLists(expected, actual);
		
		assertEquals("IDs not Ascending",result, true);
	}
	
	public void testSortDateDesc() 
	{
		int actual[] = Sort.sortCollection(TableCols.DATE, SortDirection.DESCENDING, table.getItems());
		int expected[] = {10,9,8,7,6,5,4,3,2,1};
		
		boolean result=compareLists(expected, actual);
		
		assertEquals("IDs not Ascending",result, true);
	}
	
	public void testSortMoneyAsc() 
	{
		int actual[] = Sort.sortCollection(TableCols.MONEY, SortDirection.ASCENDING, table.getItems());
		int expected[] = {1,2,3,4,5,6,7,8,9,10};
		
		boolean result=compareLists(expected, actual);
		
		assertEquals("IDs not Ascending",result, true);
	}
	
	public void testSortMoneyDesc() 
	{
		int actual[] = Sort.sortCollection(TableCols.MONEY, SortDirection.DESCENDING, table.getItems());
		int expected[] = {10,9,8,7,6,5,4,3,2,1};
		
		boolean result=compareLists(expected, actual);
		
		assertEquals("IDs not Ascending",result, true);
	}
	
	public void testSortStringAsc() 
	{
		int actual[] = Sort.sortCollection(TableCols.PAYTO, SortDirection.ASCENDING, table.getItems());
		int expected[] = {10,8,4,1,9,7,2,5,6,3};
		
		boolean result=compareLists(expected, actual);
		
		assertEquals("IDs not Ascending",result, true);
	}
	
	public void testSortStringDesc() 
	{
		int actual[] = Sort.sortCollection(TableCols.PAYTO, SortDirection.DESCENDING, table.getItems());
		int expected[] = {3,6,5,2,7,9,1,4,8,10};
		
		boolean result=compareLists(expected, actual);
		
		assertEquals("IDs not Ascending",result, true);
	}
	
	private boolean compareLists(int expected[], int actual[])
	{
		for(int i=0;i <actual.length; i++)
		{
			if(actual[i] != expected[i])
			{
				return false;
			}
		}
		
		return true;
	}

}
