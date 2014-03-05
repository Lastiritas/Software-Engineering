package dataAccessLayer;

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
	public StubDatabase()
	{
		labelIds = new Vector<Integer>();
		labels = new Vector<Label>();
		labelIds.add(getNextLabelId());
		labels.add(new Label("Food"));
		
		payToIds = new Vector<Integer>();
		payTos = new Vector<PayTo>();
		payToIds.add(getNextPayToId());
		payTos.add(new PayTo("McDonalds", "St. Vital"));
		
		int[] setData = new int[]{1};
		IDSet set = IDSet.createFromArray(setData);
		expenseIds = new Vector<Integer>();
		expenses = new Vector<Expense>();
		expenseIds.add(getNextExpenseId());
		expenses.add(new Expense(SimpleDate.Now(), new Money(1, 0), PaymentMethod.CASH, "", 1, set));
	}
	
	public void open(String dbName)
	{
		System.out.println("The database has been opened\n");
	}
	
	public void close()
	{
		System.out.println("The database has been closed\n");
	}
	
	public Expense getExpenseByID(int inId)
	{
		Expense expense = null;
		int indexId = expenseIds.indexOf(inId);
		
		if(indexId >= 0)
		{
			expense = expenses.elementAt(indexId);
		}
		
		return expense;
	}
	
	public Expense getExpenseWhere(String whereClause)
	{
		return expenses.elementAt(1);
	}
	
	public int[] getAllExpenseIDs()
	{
		int[] ids = new int[expenseIds.size()];
		
		for(int i=0; i < ids.length; i++)
		{
			ids[i] = expenseIds.get(i).intValue();
		}
		
		return ids;
	}
	
	public int[] getAllExpenseIDsWhere(String whereClause)
	{
		return getAllExpenseIDs();
	}
	
	public int addExpense(Expense inNewValue)
	{
		int newId = 0;
		
		newId = getNextExpenseId();
		expenseIds.add(newId);
		expenses.add(inNewValue);
		
		return newId;
	}
	
	public boolean updateExpense(int inId, Expense inNewValue)
	{
		boolean found = false;
		int indexId = 0;
		
		indexId = expenseIds.indexOf(inId);
		if(indexId >= 0)
		{
			expenses.setElementAt(inNewValue, indexId);
			found = true;
		}
		return found;
	}
	
	public boolean deleteExpense(int inId)
	{
		boolean deleted = false;
		int indexId = 0;
		
		indexId = expenseIds.indexOf(inId);
		if(indexId >= 0)
		{
			expenses.remove(indexId);
			expenseIds.remove(indexId);
			deleted = true;
		}
		return deleted;
	}
	
	public Label getLabelByID(int inId)
	{
		Label label = null;
		int indexId = labelIds.indexOf(inId);
		
		if(indexId >= 0)
		{
			label = labels.elementAt(indexId);
		}
		
		return label;
	}
	
	public int[] getAllLabelIDs()
	{
		int[] ids = new int[labelIds.size()];
		
		for(int i=0; i < ids.length; i++)
		{
			ids[i] = labelIds.get(i).intValue();
		}
		
		return ids;
	}
	
	public int addLabel(Label inNewValue)
	{
		int newId = 0;
		
		newId = getNextLabelId();
		labelIds.add(newId);
		labels.add(inNewValue);
		
		return newId;
	}
	
	public boolean updateLabel(int inId, Label inNewValue)
	{
		boolean found = false;
		int indexId = 0;
		
		indexId = labelIds.indexOf(inId);
		if(indexId >= 0)
		{
			labels.setElementAt(inNewValue, indexId);
			found = true;
		}
		return found;
	}
	
	public PayTo getPayToByID(int inId)
	{
		PayTo payTo = null;
		int indexId = payToIds.indexOf(inId);
		
		if(indexId >= 0)
		{
			payTo = payTos.elementAt(indexId);
		}
		
		return payTo;
	}
	
	public int[] getAllPayToIDs()
	{
		int[] ids = new int[payToIds.size()];
		
		for(int i=0; i < ids.length; i++)
		{
			ids[i] = payToIds.get(i).intValue();
		}
		
		return ids;
	}
	
	public int addPayTo(PayTo inNewValue)
	{
		int newId = 0;
		
		newId = getNextPayToId();
		payToIds.add(newId);
		payTos.add(inNewValue);
		
		return newId;
	}
	
	public boolean updatePayTo(int inId, PayTo inNewValue)
	{
		boolean found = false;
		int indexId = 0;
		
		indexId = payToIds.indexOf(inId);
		if(indexId >= 0)
		{
			payTos.setElementAt(inNewValue, indexId);
			found = true;
		}
		return found;
	}
	
	private int getNextExpenseId()
	{
		final int newId = nextExpenseId;
		nextExpenseId++;
		
		assert nextExpenseId > newId : "int overflow detected";
		return newId;
	}
	
	private int getNextLabelId()
	{
		final int newId = nextLabelId;
		nextLabelId++;
		
		assert nextLabelId > newId : "int overflow detected";
		return newId;
	}
	
	private int getNextPayToId()
	{
		final int newId = nextPayToId;
		nextPayToId++;
		
		assert nextPayToId > newId : "int overflow detected";
		return newId;
	}
	
	private Vector<Integer> expenseIds;
	private Vector<Expense> expenses;
	private int nextExpenseId = 1;
	
	private Vector<Integer> labelIds;
	private Vector<Label> labels;
	private int nextLabelId = 1;
	
	private Vector<Integer> payToIds;
	private Vector<PayTo> payTos;
	private int nextPayToId = 1;
}
