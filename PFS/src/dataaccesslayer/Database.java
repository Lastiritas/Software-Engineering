package dataaccesslayer;

import java.sql.Statement;
import java.sql.Connection;
import java.sql.DriverManager;

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
}