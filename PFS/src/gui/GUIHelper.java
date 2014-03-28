package gui;

import org.eclipse.swt.SWT;
import org.eclipse.swt.widgets.Table;
import org.eclipse.swt.widgets.TableItem;

import domainobjects.IDSet;
import domainobjects.PayTo;
import system.PFSystem;
import util.StringMatch;

public class GUIHelper 
{
	public static void addLabelsToTable(Table inTable, IDSet inIds)
	{
		assert(inTable != null);
		assert(inIds != null);
		
		final int totalIds = inIds.getSize();
		
		for(int i = 0; i < totalIds; i++)
		{
			addLabelToTable(inTable, inIds.getValue(i));
		}
	}
	
	public static void addLabelToTable(Table inTable, int inLabelId)
	{
		assert(inTable != null);
		assert(inLabelId >= 0);
		
		final domainobjects.Label label = (domainobjects.Label)PFSystem.getCurrent().getLabelSystem().getDataByID(inLabelId);
		
		final TableItem newItem = new TableItem(inTable, SWT.NONE);
		newItem.setText(0, "" + inLabelId);
		newItem.setText(1, label.getName());
	}

	public static IDSet getIdsFromTable(Table inTable)
	{
		assert(inTable != null);
		
		final int totalItems = inTable.getItemCount();
		
		int[] array = new int[totalItems];
		
		for(int i = 0; i < totalItems; i++)
		{
			array[i] = Integer.parseInt(inTable.getItem(i).getText(0));
		}
		
		return IDSet.createFromArray(array);
	}
	
	public static void addPayTosToTable(Table inTable, IDSet inIds)
	{
		final int totalIds = inIds.getSize();
		
		for(int i = 0; i < totalIds; i++)
		{
			final int id = inIds.getValue(i);
			addPayToToTable(inTable, id);
		}
	}
	
	public static void addPayToToTable(Table inTable, int inId)
	{
		final PayTo payto = (PayTo)PFSystem.getCurrent().getPayToSystem().getDataByID(inId);
		
		final TableItem tableItem = new TableItem(inTable, SWT.NONE);
		tableItem.setText(0, "" + inId);
		tableItem.setText(1, payto.getName());
	}
	
	public static void filterTable(Table inTable, String inFilterText)
	{					
		assert(inTable != null);
		assert(inFilterText != null);
		assert(inFilterText.length() > 0);
		
		final int originalTableSize = inTable.getItemCount();
			
		for(int i = originalTableSize - 1; i >= 0; i--)
		{
			final String payTo = inTable.getItem(i).getText(1);
					
			if(!StringMatch.match(payTo, inFilterText))
			{
				inTable.remove(i);	
			}
		}
	}
}
