package dataaccesslayer;

import java.sql.ResultSet;
import java.sql.Statement;
import java.util.Vector;

import util.Sanitizer;
import domainobjects.Expense;
import domainobjects.IDHelper;
import domainobjects.IDSet;
import domainobjects.Money;
import domainobjects.PaymentMethod;
import domainobjects.PaymentMethodHelper;
import domainobjects.SimpleDate;

public class ExpenseTable extends DatabaseTable
{
	private Statement statement;

	public ExpenseTable(Statement inStatement)
	{
		super(inStatement, "expenseID", "Expenses");
		statement = inStatement;
	}
	
	@Override
	public int add(Object inNewValue) 
	{
		assert inNewValue instanceof Expense;
						
		try
		{
			Expense expense = (Expense)inNewValue;
			
			int nextId = getMaxIdForTable();
			
			String values = String.format("%d, %d, %d, %d, '%s', %d", 
								nextId, 
								expense.getDate().toInteger(), 
								expense.getAmount().getTotalCents(), 
								PaymentMethodHelper.toInteger(expense.getPaymentMethod()), 
								Sanitizer.sanitize(expense.getDescription()), 
								expense.getPayTo());
						
			String cmdString = String.format("Insert into Expenses Values(%s)", values);
			
			int insertedSuccessful = statement.executeUpdate(cmdString);
			
			String result = DatabaseTable.checkWarning(statement, insertedSuccessful);
			
			assert result == null;
			
			//Now add or update the labels to the ExpenseLabel table
			insertedSuccessful = updateExpenseLabel(expense.getLabels(), nextId);
			
			return insertedSuccessful == 1 ? nextId : IDHelper.getInvalidId();
		}
		catch(Exception ex)
		{
			System.out.println(DatabaseTable.getError(ex));
			return IDHelper.getInvalidId();
		}
	}

	@Override
	public boolean update(int inId, Object inNewValue) 
	{
		assert IDHelper.isIdValid(inId);
		assert inNewValue != null;
		assert inNewValue instanceof Expense;		
		
		Expense expense = (Expense)inNewValue;
		
		try
		{
			String values = String.format("date=%d, cents=%d, paymentMethod=%d, description='%s', payTo=%d", 
								expense.getDate().toInteger(), 
								expense.getAmount().getTotalCents(), 
								PaymentMethodHelper.toInteger(expense.getPaymentMethod()),
								Sanitizer.sanitize(expense.getDescription()), 
								expense.getPayTo());
			
			String where = String.format("where expenseID=%d", inId);
			String cmdString = String.format("Update Expenses Set %s %s", values, where);
			int successful = statement.executeUpdate(cmdString);
			String result = DatabaseTable.checkWarning(statement, successful);
			assert result == null;
			
			//Now add or update the labels to the ExpenseLabel table
			successful = updateExpenseLabel(expense.getLabels(), inId);
		
			return successful == 1;
		}
		catch(Exception ex)
		{
			System.out.println(DatabaseTable.getError(ex));
			return false;
		}
	}

	@Override
	public boolean delete(int inId) 
	{	
		try
		{		
			deleteFromExpenseLabelWhere(String.format("where expenseID=%d", inId));
		
			String cmdString = String.format("Delete from Expenses where expenseID=%d", inId);
			int successful = statement.executeUpdate(cmdString);
			String result = DatabaseTable.checkWarning(statement, successful);
			assert result == null;
			
			return successful == 1;
		}
		catch(Exception ex)
		{
			System.out.println(DatabaseTable.getError(ex));
			return false;
		}
	}
	
	private void deleteFromExpenseLabelWhere(String whereClause)
	{		
		try
		{
			String cmdString = String.format("Delete from ExpenseLabels %s", whereClause);
			statement.executeUpdate(cmdString);
		}
		catch(Exception ex)
		{
			System.out.println(DatabaseTable.getError(ex));
		}
	}
	
	private int updateExpenseLabel(IDSet labels, int expenseId)
	{
		String values;
		String whereClause;
		String result = null;
		int currentLabel = 0;
		int insertedSuccessful = 0;
		final int size = labels.getSize();
		IDSet labelsPreviouslyInserted;
		IDSet expenseLabelsToInsert;
		IDSet expenseLabelsToDelete;
		
		try
		{
			if(size ==0)
			{
				insertedSuccessful=1;
			}
			else
			{
				labelsPreviouslyInserted = getExpenseLabelsByExpenseID(expenseId);
				expenseLabelsToInsert = labels.difference(labelsPreviouslyInserted);
				expenseLabelsToDelete = labelsPreviouslyInserted.difference(labels);
				
				while(expenseLabelsToDelete.getSize() > currentLabel)
				{
					whereClause = String.format("where expenseID=%d AND labelID=%d", expenseId, expenseLabelsToDelete.getValue(currentLabel));
					deleteFromExpenseLabelWhere(whereClause);
					currentLabel++;
				}
				
				currentLabel = 0;
				
				while(expenseLabelsToInsert.getSize() > currentLabel)
				{
					values = String.format("%d, %d", expenseId, expenseLabelsToInsert.getValue(currentLabel));
					String cmdString = String.format("Insert into ExpenseLabels Values(%s)", values);
					insertedSuccessful = statement.executeUpdate(cmdString);
					result = DatabaseTable.checkWarning(statement, insertedSuccessful);
					currentLabel++;
					assert result == null;
				}
				
				if(expenseLabelsToInsert.getSize() == 0){
					insertedSuccessful = 1;
				}
			}
		}
		catch (Exception ex)
		{
			System.out.println(DatabaseTable.getError(ex));
		}
		
		return insertedSuccessful;//returns one if it was successful
	}
	
	private IDSet getExpenseLabelsByExpenseID(int expenseId)
	{
		Vector<Integer> labelIds = new Vector<Integer>();
		
		try
		{
			String where = String.format("where expenseID=%d", expenseId);
			String cmdString = String.format("Select labelID from ExpenseLabels %s", where);
			ResultSet resultSet = statement.executeQuery(cmdString);
			
			while(resultSet.next())
			{
				int currentId = resultSet.getInt("labelID");
				labelIds.add(currentId);
			}
			resultSet.close();
		}
		catch(Exception ex)
		{
			System.out.println(DatabaseTable.getError(ex));
		}
		
		return IDSet.createFromArray(DatabaseTable.parseIds(labelIds));
	}
	
	@Override
	protected Object convertToObject(ResultSet result) 
	{
		int expenseId;
		SimpleDate date;
		int cents;
		PaymentMethod paymentMethod;
		String description;
		int payTo;
		IDSet labels;
		
		Expense expense = null;
		Money money;
		
		try
		{
			result.next();
			expenseId = result.getInt("expenseId");
			date = SimpleDate.parseDate(result.getInt("date"));
			cents = result.getInt("cents");
			paymentMethod = PaymentMethodHelper.toPaymentMethod(result.getInt("paymentMethod"));
			description = Sanitizer.desanitize(result.getString("description"));
			payTo = result.getInt("payTo");
			labels = getExpenseLabelsByExpenseID(expenseId);
			
			money = new Money(cents/100, cents%100);
			
			expense = new Expense(date, money, paymentMethod, description, payTo, labels);
		}
		catch(Exception ex)
		{
			System.out.println(DatabaseTable.getError(ex));
		}
		
		return expense;
	}
}
