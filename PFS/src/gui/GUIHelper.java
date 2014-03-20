package gui;

import org.eclipse.swt.SWT;
import org.eclipse.swt.widgets.Table;
import org.eclipse.swt.widgets.TableItem;

import domainobjects.IDSet;
import domainobjects.PayTo;
import system.PFSystem;

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
		newItem.setText(1, label.getLabelName());
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
		tableItem.setText(1, payto.getPayToName());
		tableItem.setText(2, payto.getPayToBranch());
	}
}
