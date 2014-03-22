package dataaccesslayer;

import java.sql.Statement;
import java.sql.Connection;
import java.sql.DriverManager;

import domainobjects.Expense;
import domainobjects.Label;
import domainobjects.PayTo;

public class Database implements IDatabase
{
	private Statement statement;
	private Connection c1;
	
	private String dbName;
	private String dbType;
	
	private IDatabaseTable expenseTable;
	private IDatabaseTable labelTable;
	private IDatabaseTable payToTable;
	
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
			
			expenseTable = new ExpenseTable(statement);
			labelTable = new LabelTable(statement);
			payToTable = new PayToTable(statement);
		}
		catch (Exception ex)
		{
			System.out.println(SQLHelper.getError(ex));
		}
		System.out.println("Opened " +dbType +" database " +dbName);
	}
	
	public void close()
	{
		try
		{	// commit all changes to the database
			statement.executeQuery("shutdown compact");
			
			c1.close();
		}
		catch (Exception ex)
		{
			System.out.println(SQLHelper.getError(ex));
		}
		
		System.out.println("Closed " +dbType +" database " +dbName);
	}

	@Override
	public Expense getExpenseByID(int inId) 
	{
		return (Expense)expenseTable.getById(inId);
	}

	@Override
	public Expense getExpenseWhere(String whereClause) 
	{
		return (Expense)expenseTable.getWhere(whereClause);
	}

	@Override
	public int[] getAllExpenseIDs() 
	{
		return expenseTable.getAllIds();
	}

	@Override
	public int[] getAllExpenseIDsWhere(String whereClause) 
	{
		return expenseTable.getAllIdsWhere(whereClause);
	}

	@Override
	public int addExpense(Expense inNewValue) 
	{
		return expenseTable.add(inNewValue);
	}

	@Override
	public boolean updateExpense(int inId, Expense inNewValue) 
	{
		return expenseTable.update(inId, inNewValue);
	}

	@Override
	public boolean deleteExpense(int inId) 
	{
		return expenseTable.delete(inId);
	}

	@Override
	public Label getLabelByID(int inId) 
	{
		return (Label)labelTable.getById(inId);
	}

	@Override
	public int[] getAllLabelIDs() 
	{
		return labelTable.getAllIds();
	}

	@Override
	public int addLabel(Label inNewValue) 
	{
		return labelTable.add(inNewValue);
	}

	@Override
	public boolean updateLabel(int inId, Label inNewValue) 
	{
		return labelTable.update(inId, inNewValue);
	}

	@Override
	public PayTo getPayToByID(int inId) 
	{
		return (PayTo)payToTable.getById(inId);
	}

	@Override
	public int[] getAllPayToIDs() 
	{
		return payToTable.getAllIds();
	}

	@Override
	public int addPayTo(PayTo inNewValue) 
	{
		return payToTable.add(inNewValue);
	}

	@Override
	public boolean updatePayTo(int inId, PayTo inNewValue) 
	{
		return payToTable.update(inId, inNewValue);
	}
}