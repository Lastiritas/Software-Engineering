package dataAccessLayer;

import java.util.Date;
import java.util.Vector;

import domainobjects.Expense;
import domainobjects.IDSet;
import domainobjects.Label;
import domainobjects.PayTo;
import domainobjects.PaymentMethod;

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
		expenses.add(new Expense(new Date(), 1000, PaymentMethod.CASH, "", 1, set));
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
	
	public Vector<Integer> getAllExpenseIDs()
	{
		return expenseIds;
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
	
	public Vector<Integer> getAllLabelIDs()
	{
		return labelIds;
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
	
	public Vector<Integer> getAllPayToIDs()
	{
		return payToIds;
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
