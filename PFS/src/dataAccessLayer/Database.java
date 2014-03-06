package dataAccessLayer;

import java.sql.Statement;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.DriverManager;
import java.sql.SQLWarning;
import java.util.Vector;

import domainobjects.Expense;
import domainobjects.IDSet;
import domainobjects.Label;
import domainobjects.Money;
import domainobjects.PayTo;
import domainobjects.PaymentMethod;
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
			url = "jdbc:hsqldb:database/" + dbName;
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
		assert inId > 0;
		return getExpenseWhere("expenseID="+inId);
	}
	
	public Expense getExpenseWhere(String whereClause)
	{
		assert whereClause != null;
		Expense expense = null;
		
		try
		{
			cmdString = "Select * from Expenses where "+ whereClause;
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
			paymentMethod = PaymentMethod.values()[resultSet.getInt("paymentMethod")];
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
			cmdString = "Select expenseID from Expenses where " + whereClause;
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
		int newId = 0;
		
		try
		{
			values = expenseId
					+", "+ inNewValue.getDate().toInteger() 
					+", "+inNewValue.getAmount().getTotalCents()
					+", " + inNewValue.getPaymentMethod().ordinal()
					+", '" + inNewValue.getDescription()
					+"', " + inNewValue.getPayTo();
			cmdString = "Insert into Expenses " + "Values(" + values + ")";
			insertedSuccessful = statement.executeUpdate(cmdString);
			result = checkWarning(statement, insertedSuccessful);
			assert result == null;
			
			//Now add the labels to the ExpenseLabel table
			insertedSuccessful = addExpenseLabel(labels, expenseId);
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
		assert inId > 0;
		assert inNewValue != null;
		
		String values;
		String where;
		String result = null;
		int successful = 0;
		
		try
		{
			values = "date=" + inNewValue.getDate().toInteger() 
					+", cents=" + inNewValue.getAmount().getTotalCents()
					+", paymentMethod=" + inNewValue.getPaymentMethod().ordinal()
					+", description='" + inNewValue.getDescription()
					+"', payTo=" + inNewValue.getPayTo();
			where = "where expenseID=" + inId;
			cmdString = "Update Expenses " + " Set " + values + " " + where;
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
	
	public boolean deleteExpense(int inId)
	{
		assert inId > 0;
		
		String result = null;
		int successful = 0;
		
		try
		{
			deleteFromExpenseLabel(inId);
			cmdString = "Delete from Expenses where expenseID=" + inId;
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
		assert inId > 0;
		
		String result = null;
		int successful = 0;
		
		try
		{
			cmdString = "Delete from ExpenseLabels where expenseID=" + inId;
			successful = statement.executeUpdate(cmdString);
			result = checkWarning(statement, successful);
			assert result == null;
		}
		catch(Exception ex)
		{
			System.out.println(processSQLError(ex));
		}
	}
	
	private int addExpenseLabel(IDSet labels, int expenseId)
	{
		String values;
		String result = null;
		int currentLabel = 0;
		int insertedSuccessful = 0;
		final int size = labels.getSize();
		
		try
		{
			if(size ==0)
			{
				insertedSuccessful=1;
			}
			else
			{
				while(labels.getSize() > currentLabel)
				{
					values = expenseId + ", " + labels.getValue(currentLabel);
					cmdString = "Insert into ExpenseLabels " + " Values(" + values + ")";
					insertedSuccessful = statement.executeUpdate(cmdString);
					result = checkWarning(statement, insertedSuccessful);
					currentLabel++;
					assert result == null;
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
		String where;
		int currentId;
		
		try
		{
			where = "where expenseID=" + expenseId;
			cmdString = "Select labelID from ExpenseLabels " + where;
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
		
		return IDSet.createFromArray(parseIds(labelIds));
	}
	
	public Label getLabelByID(int inId)
	{
		assert inId > 0;
		Label label = null;
		
		try
		{
			cmdString = "Select * from Label where labelID="+inId;
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
		int newId = 0;
		int labelId = getNextLabelId();
		
		try
		{
			values = labelId + ", '"+inNewValue.getLabelName() + "'";
			cmdString = "Insert into Label " + " Values(" + values + ")";
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
		assert inId > 0;
		assert inNewValue != null;
		
		String values;
		String where;
		String result = null;
		int successful = 0;
		
		try
		{
			values = "name='" + inNewValue.getLabelName() + "'";
			where = "where labelID=" + inId;
			cmdString = "Update Label " + " Set " + values + " " + where;
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
		assert inId > 0;
		PayTo payTo = null;
		
		try
		{
			cmdString = "Select * from PayTo where payToID="+inId;
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
		String branch;
		
		try
		{
			resultSet.next();
			location = resultSet.getString("location");
			branch = resultSet.getString("branch");
			
			payTo = new PayTo(location, branch);
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
		int newId = 0;
		int payToId = getNextPayToId();
		
		try
		{
			values = payToId 
					+ ", '" + inNewValue.getPayToName()
					+ "', '"+ inNewValue.getPayToBranch() + "'";
			cmdString = "Insert into PayTo " + " Values(" + values + ")";
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
		assert inId > 0;
		assert inNewValue != null;
		
		String values;
		String where;
		String result = null;
		int successful = 0;
		
		try
		{
			values = "location='" + inNewValue.getPayToName() 
					+ "', branch='" + inNewValue.getPayToBranch() + "'";
			where = "where payToID=" + inId;
			cmdString = "Update PayTo " + " Set " + values + " " + where;
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
		int expenseId = 0;
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
		int labelId = 0;
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
		int payToId = 0;
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
			result = "Tuple not inserted correctly.";
		}
		return result;
	}
}