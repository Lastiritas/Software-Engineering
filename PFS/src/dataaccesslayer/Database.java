package dataaccesslayer;

import java.sql.Statement;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.DriverManager;
import java.sql.SQLWarning;
import java.util.Vector;

import domainobjects.Expense;
import domainobjects.IDHelper;
import domainobjects.IDSet;
import domainobjects.Label;
import domainobjects.Money;
import domainobjects.PayTo;
import domainobjects.PaymentMethod;
import domainobjects.PaymentMethodHelper;
import domainobjects.SimpleDate;

public class Database implements IDatabase
{
	private Statement statement;
	private Connection c1;
	private ResultSet resltSet;
	
	private String dbName;
	private String dbType;
	private String cmdString;
	
	public Database(String dbName)
	{
		this.dbName = dbName;
	}
	
	public void open(String dbName)
	{
		String url;
		try
		{
			dbType = "HSQL";
			Class.forName("org.hsqldb.jdbcDriver").newInstance();
			url = String.format("jdbc:hsqldb:database/%s", dbName);
			c1 = DriverManager.getConnection(url, "SA", "");
			statement = c1.createStatement();
		}
		catch (Exception e)
		{
			processSQLError(e);
		}
		System.out.println("Opened " +dbType +" database " +dbName);
	}
	
	public void close()
	{
		try
		{	// commit all changes to the database
			cmdString = "shutdown compact";
			resltSet = statement.executeQuery(cmdString);
			c1.close();
		}
		catch (Exception ex)
		{
			System.out.println(processSQLError(ex));
		}
		System.out.println("Closed " +dbType +" database " +dbName);
	}
	
	public Expense getExpenseByID(int inId)
	{
		assert IDHelper.isIdValid(inId);
		return getExpenseWhere(String.format("expenseID=%d",inId));
	}
	
	public Expense getExpenseWhere(String whereClause)
	{
		assert whereClause != null;
		Expense expense = null;
		
		try
		{
			cmdString = String.format("Select * from Expenses where %s", whereClause);
			resltSet = statement.executeQuery(cmdString);
			expense = convertToExpense(resltSet);
			resltSet.close();
		}
		catch(Exception ex)
		{
			System.out.println(processSQLError(ex));
		}
		
		return expense;
	}
	
	private Expense convertToExpense(ResultSet resultSet)
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
			resultSet.next();
			expenseId = resultSet.getInt("expenseId");
			date = SimpleDate.parseDate(resultSet.getInt("date"));
			cents = resultSet.getInt("cents");
			paymentMethod = PaymentMethodHelper.toPaymentMethod(resultSet.getInt("paymentMethod"));
			description = resultSet.getString("description");
			payTo = resultSet.getInt("payTo");
			labels = getExpenseLabelsByExpenseID(expenseId);
			
			money = new Money(cents/100, cents%100);
			
			expense = new Expense(date, money, paymentMethod, description, payTo, labels);
		}
		catch(Exception ex)
		{
			System.out.println(processSQLError(ex));
		}
		
		return expense;
	}
	
	public int[] getAllExpenseIDs()
	{
		Vector<Integer> expenseIds = new Vector<Integer>();
		int currentId;
		
		try
		{
			cmdString = "Select expenseID from Expenses";
			resltSet = statement.executeQuery(cmdString);
			
			while(resltSet.next())
			{
				currentId = resltSet.getInt("expenseID");
				expenseIds.add(currentId);
			}
			resltSet.close();
		}
		catch(Exception ex)
		{
			System.out.println(processSQLError(ex));
		}
		
		return parseIds(expenseIds);
	}
	
	public int[] getAllExpenseIDsWhere(String whereClause)
	{
		assert whereClause != null;
		Vector<Integer> expenseIds = new Vector<Integer>();
		int currentId;
		
		try
		{
			cmdString = String.format("Select expenseID from Expenses where %s", whereClause);
			resltSet = statement.executeQuery(cmdString);
			
			while(resltSet.next())
			{
				currentId = resltSet.getInt("expenseID");
				expenseIds.add(currentId);
			}
			resltSet.close();
		}
		catch(Exception ex)
		{
			System.out.println(processSQLError(ex));
		}
		
		return parseIds(expenseIds);
	}
	
	public int addExpense(Expense inNewValue)
	{
		assert inNewValue != null;
		
		String values;
		String result = null;
		int insertedSuccessful = 0;
		IDSet labels = inNewValue.getLabels();
		int expenseId = getNextExpenseId();
		int newId = IDHelper.getInvalidId();
		
		try
		{
			values = String.format("%d, %d, %d, %d, '%s', %d", expenseId, 
					inNewValue.getDate().toInteger(), inNewValue.getAmount().getTotalCents(), 
					PaymentMethodHelper.toInteger(inNewValue.getPaymentMethod()), inNewValue.getDescription(), 
					inNewValue.getPayTo());
			cmdString = String.format("Insert into Expenses Values(%s)", values);
			insertedSuccessful = statement.executeUpdate(cmdString);
			result = checkWarning(statement, insertedSuccessful);
			assert result == null;
			
			//Now add or update the labels to the ExpenseLabel table
			insertedSuccessful = updateExpenseLabel(labels, expenseId);
		}
		catch(Exception ex)
		{
			System.out.println(processSQLError(ex));
		}
		
		if(insertedSuccessful == 1)
			newId = expenseId;
		
		return newId;
	}
	
	
	public boolean updateExpense(int inId, Expense inNewValue)
	{
		assert IDHelper.isIdValid(inId);
		assert inNewValue != null;
		
		String values;
		String where;
		String result = null;
		IDSet labels = inNewValue.getLabels();
		int successful = 0;
		
		try
		{
			values = String.format("date=%d, cents=%d, paymentMethod=%d, description='%s', payTo=%d", 
					inNewValue.getDate().toInteger(), inNewValue.getAmount().getTotalCents(), 
					PaymentMethodHelper.toInteger(inNewValue.getPaymentMethod()), inNewValue.getDescription(), 
					inNewValue.getPayTo());
			where = String.format("where expenseID=%d", inId);
			cmdString = String.format("Update Expenses Set %s %s", values, where);
			successful = statement.executeUpdate(cmdString);
			result = checkWarning(statement, successful);
			assert result == null;
			
			//Now add or update the labels to the ExpenseLabel table
			successful = updateExpenseLabel(labels, inId);
		}
		catch(Exception ex)
		{
			System.out.println(processSQLError(ex));
		}
		
		return successful == 1;
	}
	
	public boolean deleteExpense(int inId)
	{
		assert IDHelper.isIdValid(inId);
		
		String result = null;
		int successful = 0;
		
		try
		{
			deleteFromExpenseLabel(inId);
			cmdString = String.format("Delete from Expenses where expenseID=%d", inId);
			successful = statement.executeUpdate(cmdString);
			result = checkWarning(statement, successful);
			assert result == null;
		}
		catch(Exception ex)
		{
			System.out.println(processSQLError(ex));
		}
		
		return successful == 1;
	}
	
	private void deleteFromExpenseLabel(int inId)
	{
		assert IDHelper.isIdValid(inId);
		deleteFromExpenseLabelWhere(String.format("where expenseID=%d", inId));
	}
	
	private void deleteFromExpenseLabelWhere(String whereClause)
	{
		assert whereClause != null;
		
		String result = null;
		int successful = 0;
		
		try
		{
			cmdString = String.format("Delete from ExpenseLabels %s", whereClause);
			successful = statement.executeUpdate(cmdString);
			result = checkWarning(statement, successful);
			assert result == null;
		}
		catch(Exception ex)
		{
			System.out.println(processSQLError(ex));
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
					cmdString = String.format("Insert into ExpenseLabels Values(%s)", values);
					insertedSuccessful = statement.executeUpdate(cmdString);
					result = checkWarning(statement, insertedSuccessful);
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
			System.out.println(processSQLError(ex));
		}
		
		return insertedSuccessful;//returns one if it was successful
	}
	
	private IDSet getExpenseLabelsByExpenseID(int expenseId)
	{
		Vector<Integer> labelIds = new Vector<Integer>();
		
		try
		{
			String where = String.format("where expenseID=%d", expenseId);
			cmdString = String.format("Select labelID from ExpenseLabels %s", where);
			resltSet = statement.executeQuery(cmdString);
			
			while(resltSet.next())
			{
				int currentId = resltSet.getInt("labelID");
				labelIds.add(currentId);
			}
			resltSet.close();
		}
		catch(Exception ex)
		{
			System.out.println(processSQLError(ex));
		}
		
		return IDSet.createFromArray(parseIds(labelIds));
	}
	
	public Label getLabelByID(int inId)
	{
		assert IDHelper.isIdValid(inId);
		Label label = null;
		
		try
		{
			cmdString = String.format("Select * from Label where labelID=%d", inId);
			resltSet = statement.executeQuery(cmdString);
			label = convertToLabel(resltSet);
			resltSet.close();
		}
		catch(Exception e)
		{
			processSQLError(e);
		}
		
		return label;
	}
	
	private Label convertToLabel(ResultSet resultSet)
	{
		Label label = null;
		String name;
		
		try
		{
			resultSet.next();
			name = resultSet.getString("name");
			label = new Label(name);
		}
		catch(Exception ex)
		{
			System.out.println(processSQLError(ex));
		}
		
		return label;
	}
	
	public int[] getAllLabelIDs()
	{
		Vector<Integer> labelIds = new Vector<Integer>();
		int currentId;
		
		try
		{
			cmdString = "Select labelID from Label";
			resltSet = statement.executeQuery(cmdString);
			
			while(resltSet.next())
			{
				currentId = resltSet.getInt("labelID");
				labelIds.add(currentId);
			}
			resltSet.close();
		}
		catch(Exception ex)
		{
			System.out.println(processSQLError(ex));
		}
		
		return parseIds(labelIds);
	}
	
	public int addLabel(Label inNewValue)
	{
		assert inNewValue != null;
		
		String values;
		String result = null;
		int insertedSuccessful = 0;
		int newId = IDHelper.getInvalidId();
		int labelId = getNextLabelId();
		
		try
		{
			values = String.format("%d, '%s'", labelId, inNewValue.getLabelName());
			cmdString = String.format("Insert into Label Values(%s)", values);
			insertedSuccessful = statement.executeUpdate(cmdString);
			result = checkWarning(statement, insertedSuccessful);
			assert result == null;
		}
		catch(Exception ex)
		{
			System.out.println(processSQLError(ex));
		}
		
		if(insertedSuccessful == 1)
			newId = labelId;
		
		return newId;
	}
	
	public boolean updateLabel(int inId, Label inNewValue)
	{
		assert IDHelper.isIdValid(inId);
		assert inNewValue != null;
		
		String values;
		String where;
		String result = null;
		int successful = 0;
		
		try
		{
			values = String.format("name='%s'", inNewValue.getLabelName());
			where = String.format("where labelID=%d", inId);
			cmdString = String.format("Update Label Set %s %s", values, where);
			successful = statement.executeUpdate(cmdString);
			result = checkWarning(statement, successful);
			assert result == null;
		}
		catch(Exception ex)
		{
			System.out.println(processSQLError(ex));
		}
		
		return successful == 1;
	}
	
	public PayTo getPayToByID(int inId)
	{
		assert IDHelper.isIdValid(inId);
		PayTo payTo = null;
		
		try
		{
			cmdString = String.format("Select * from PayTo where payToID=%d", inId);
			resltSet = statement.executeQuery(cmdString);
			payTo = convertToPayTo(resltSet);
			resltSet.close();
		}
		catch(Exception ex)
		{
			System.out.println(processSQLError(ex));
		}
		
		return payTo;
	}
	
	private PayTo convertToPayTo(ResultSet resultSet)
	{
		PayTo payTo = null;
		String location;
		
		try
		{
			resultSet.next();
			location = resultSet.getString("location");
			
			payTo = new PayTo(location);
		}
		catch(Exception ex)
		{
			System.out.println(processSQLError(ex));
		}
		
		return payTo;
	}
	
	public int[] getAllPayToIDs()
	{
		Vector<Integer> payToIds = new Vector<Integer>();
		int currentId;
		
		try
		{
			cmdString = "Select payToID from PayTo";
			resltSet = statement.executeQuery(cmdString);
			
			while(resltSet.next())
			{
				currentId = resltSet.getInt("payToID");
				payToIds.add(currentId);
			}
			resltSet.close();
		}
		catch(Exception ex)
		{
			System.out.println(processSQLError(ex));
		}
		
		return parseIds(payToIds);
	}
	
	public int addPayTo(PayTo inNewValue)
	{
		assert inNewValue != null;
		
		String values;
		String result = null;
		int insertedSuccessful = 0;
		int newId = IDHelper.getInvalidId();
		int payToId = getNextPayToId();
		
		try
		{
			values = String.format("%d, '%s', '%s'", payToId, inNewValue.getName(), "No one needs me");
			cmdString = String.format("Insert into PayTo Values(%s)", values);
			insertedSuccessful = statement.executeUpdate(cmdString);
			result = checkWarning(statement, insertedSuccessful);
			assert result == null;
		}
		catch(Exception ex)
		{
			System.out.println(processSQLError(ex));
		}
		
		if(insertedSuccessful == 1)
			newId = payToId;
		
		return newId;
	}
	
	public boolean updatePayTo(int inId, PayTo inNewValue)
	{
		assert IDHelper.isIdValid(inId);
		assert inNewValue != null;
		
		String values;
		String where;
		String result = null;
		int successful = 0;
		
		try
		{
			values = String.format("location='%s', branch='%s'", inNewValue.getName(), "no one needs me");
			where = String.format("where payToID=%d", inId);
			cmdString = String.format("Update PayTo Set %s %s", values, where);
			successful = statement.executeUpdate(cmdString);
			result = checkWarning(statement, successful);
			assert result == null;
		}
		catch(Exception ex)
		{
			System.out.println(processSQLError(ex));
		}
		
		return successful == 1;
	}
	
	private int getNextExpenseId()
	{
		int expenseId = IDHelper.getInvalidId();
		try
		{
			cmdString = "Select MAX(expenseID) from Expenses";
			resltSet = statement.executeQuery(cmdString);
			resltSet.next();
			expenseId = resltSet.getInt(1);
			expenseId++;
			resltSet.close();
		}
		catch(Exception ex)
		{
			System.out.println(processSQLError(ex));
		}
		
		return expenseId;
	}
	
	private int getNextLabelId()
	{
		int labelId = IDHelper.getInvalidId();
		try
		{
			cmdString = "Select MAX(labelID) from Label";
			resltSet = statement.executeQuery(cmdString);
			resltSet.next();
			labelId = resltSet.getInt(1);
			labelId++;
			System.out.println(labelId);
			resltSet.close();
		}
		catch(Exception ex)
		{
			System.out.println(processSQLError(ex));
		}
		
		return labelId;
	}
	
	private int getNextPayToId()
	{
		int payToId = IDHelper.getInvalidId();
		try
		{
			cmdString = "Select MAX(payToID) from PayTo";
			resltSet = statement.executeQuery(cmdString);
			resltSet.next();
			payToId = resltSet.getInt(1);
			payToId++;
			System.out.println(payToId);
			resltSet.close();
		}
		catch(Exception ex)
		{
			System.out.println(processSQLError(ex));
		}
		
		return payToId;
	}
	
	private int[] parseIds(Vector<Integer> inIds)
	{
		int [] ids = new int[inIds.size()];
		
		for(int i=0; i < ids.length; i++)
		{
			ids[i] = inIds.get(i).intValue();
		}
		
		return ids;
	}
	
	private String processSQLError(Exception e)
	{
		String result;
		result = "*** SQL Error: " + e.getMessage();
		//e.printStackTrace();
		return result;
	}
	
	private String checkWarning(Statement st, int updateCount)
	{
		String result = null;
		try
		{
			SQLWarning warning = st.getWarnings();
			if (warning != null)
			{
				result = warning.getMessage();
			}
		}
		catch (Exception e)
		{
			result = processSQLError(e);
		}
		if (updateCount != 1)
		{
			result = "Tuple not inserted or deleted correctly.";
		}
		return result;
	}
}