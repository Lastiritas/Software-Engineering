package system;

import java.util.Vector;
import java.util.Date;

import domainobjects.Expense;
import domainobjects.IDSet;
import domainobjects.PaymentMethod;

public class ExpenseSystem
{
	private ExpenseSystem()
	{

	}

	public IDSet getExpenseIDs()
	{
		final int[] setData = new int[ids.size()];

		for(int i = 0; i < setData.length; i++)
		{
			setData[i] = ids.get(i).intValue();
		}

		final IDSet output = IDSet.createFromArray(setData);

		assert output != null : "Returning a null set";
		
		return output;
	}

	public Expense getExpense(int inId)
	{
		assert debugRemoveAt(inId) : "Invalid ID";

		int index = indexOf(new Integer(inId));

		if(index == -1)
		{
			return null;
		}

		final Expense output = expenses.get(index);

		assert output != null : "Found null expense in collection";

		return output;
	}

	public boolean updateExpense(int inId, Expense inNewValue)
	{
		assert debugRemoveAt(inId) : "Invalid ID";
		assert inNewValue != null : "Cannot update expense with null value";

		int index = indexOf(new Integer(inId));

		if(index == -1)
		{
			return false;
		}

		expenses.set(index, inNewValue);		

		return true;
	}

	public boolean deleteExpense(int inId)
	{
		assert debugRemoveAt(inId) : "Invalid ID";

		int index = indexOf(new Integer(inId));

		if(index == -1)
		{
			return false;
		}

		ids.remove(index);
		expenses.remove(index);

		assert ids.size() == expenses.size() : "Id and expense lists do not match";

		return true;
	}

	public int newExpense()
	{
		final int newId = getNewId();

		final int[] emptySetData = new int[0];
		final IDSet emptySet = IDSet.createFromArray(emptySetData);

		ids.add(new Integer(newId));
		expenses.add(new Expense(new Date(), 0, PaymentMethod.CASH, "", -1, emptySet));

		assert ids.size() == expenses.size() : "Id and expense lists do not match";

		return newId;
	}

	private int indexOf(int inKey)
	{
		assert debugRemoveAt(inKey) : "Invalid ID";
		assert debugRemoveAtsOrder() : "Ids are not in assending order";

		int start = 0;
		int end = ids.size();

		while(end > start)
		{
			final int middle = (start + end) / 2;

			final int middleValue = ids.get(middle);

			if(middleValue == inKey)
			{
				return middle;
			}
			else if(middleValue < inKey)
			{
				start = middle;
			}
			else 
			{
				end = middle;
			}
		}

		return -1;
	}

	private boolean debugRemoveAtsOrder()
	{
		// ids should be in accending order

		for(int i = 1; i < ids.size(); i++)
		{
			if(ids.get(i - 1) > ids.get(i))
			{
				return false;
			}
		}

		return true;
	}

	private boolean debugRemoveAt(int inKey)
	{
		return inKey >= 0;
	}

	private int getNewId()
	{
		final int newId = nextId;
		nextId++;

		assert nextId > newId : "int overflow detected";

		return newId;
	}

	public static ExpenseSystem getCurrent()
	{
		assert current != null : "Returning a null current value";

		return current;
	}

	private Vector<Integer> ids = new Vector<Integer>();
	private Vector<Expense> expenses = new Vector<Expense>();

	private int nextId = 0;

	private static ExpenseSystem current = new ExpenseSystem();
}