package dataaccesslayer;

import java.util.Vector;

import domainobjects.Expense;
import domainobjects.IDSet;
import domainobjects.Label;
import domainobjects.Money;
import domainobjects.PayTo;
import domainobjects.PaymentMethod;
import domainobjects.SimpleDate;

public class StubDatabase implements IDatabase
{	
	private String name;
	
	public StubDatabase()
	{
		labelTable.add(new Label("Food"));
		payToTable.add(new PayTo("McDonalds"));
		expenseTable.add(new Expense(SimpleDate.Now(), new Money(1, 0), PaymentMethod.CASH, "", 1, IDSet.createFromValue(1)));
	}
	
	public void open(String dbName)
	{
		name = dbName;
		System.out.println("Opened Stub Database "+name);
	}
	
	public void close()
	{
		System.out.println("Closed Stub Database "+name);
	}
			
	@Override
	public IDatabaseTable getExpenseTable() 
	{
		return expenseTable;
	}

	@Override
	public IDatabaseTable getLabelTable() 
	{
		return labelTable;
	}

	@Override
	public IDatabaseTable getPayToTable() 
	{
		return payToTable;
	}
	
	StubTable expenseTable = new StubTable();
	StubTable labelTable = new StubTable();
	StubTable payToTable = new StubTable();
	
	private class StubTable implements IDatabaseTable
	{
		private Vector<Integer> ids = new Vector<Integer>();
		private Vector<Object> items = new Vector<Object>();
		private int nextId = 1;
		
		@Override
		public int[] getAllIds() 
		{
			int[] out = new int[ids.size()];

			for(int i=0; i < out.length; i++)
			{
				out[i] = ids.get(i).intValue();
			}
			
			return out;
		}

		@Override
		public int[] getAllIdsWhere(String inWhereClause) 
		{
			return getAllIds();
		}

		@Override
		public Object getById(int inId) 
		{
			int indexId = ids.indexOf(inId);
			
			if(indexId < 0)
			{
				return null;
			}
			
			return items.get(indexId);			
		}

		@Override
		public Object getWhere(String inWhereClause) 
		{
			return items.get(1);
		}

		@Override
		public int add(Object inNewValue) 
		{						
			int newId = getNextId();
			ids.add(newId);
			items.add(inNewValue);
			
			return newId;
		}

		@Override
		public boolean update(int inId, Object inNewValue) 
		{
			int indexId = ids.indexOf(inId);
			
			if(indexId < 0)
			{
				return false;
			}
			
			items.setElementAt(inNewValue, indexId);
			
			return true;
		}

		@Override
		public boolean delete(int inId) 
		{
			int indexId = ids.indexOf(inId);
			
			if(indexId < 0)
			{
				return false;
			}
			
			items.remove(indexId);
			ids.remove(indexId);

			return true;
		}
		
		private int getNextId()
		{
			int id = nextId;
			nextId++;
			
			return id;
		}
	}
}
