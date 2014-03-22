package dataaccesslayer;

import java.sql.ResultSet;
import java.sql.SQLWarning;
import java.sql.Statement;
import java.util.Collection;
import java.util.Vector;

import domainobjects.IDHelper;

public abstract class DatabaseTable implements IDatabaseTable
{
	private String tableName;
	private String idName;
	
	private Statement statement;
	
	public DatabaseTable(Statement inStatement, String inIdName, String inTableName)
	{
		statement = inStatement;
		idName = inIdName;
		tableName = inTableName;
	}
		
	@Override
	public int[] getAllIds()
	{
		return getAllIdsWhere("(TRUE)");
	}
	
	@Override
	public int[] getAllIdsWhere(String inWhereClause)
	{
		try
		{
			Collection<Integer> output = new Vector<Integer>();
					
			String cmdString = String.format("Select %s from %s where %s", idName, tableName, inWhereClause);
			ResultSet resultSet = statement.executeQuery(cmdString);
			
			while(resultSet.next())
			{
				int currentId = resultSet.getInt(idName);
				output.add(currentId);
			}
			resultSet.close();
		
			return DatabaseTable.parseIds(output);
		}
		catch(Exception ex)
		{
			System.out.println(DatabaseTable.getError(ex));
			return null;
		}
	}
		
	@Override
	public Object getById(int inId) 
	{
		return getWhere(String.format("%s=%d", idName, inId));
	}

	public Object getWhere(String inWhereClause) 
	{
		try
		{
			String cmdString = String.format("Select * from %s where %s", tableName, inWhereClause);
			ResultSet resultSet = statement.executeQuery(cmdString);
			Object output = convertToObject(resultSet);
			resultSet.close();
		
			return output;
		}
		catch(Exception ex)
		{
			System.out.println(DatabaseTable.getError(ex));
			return null;
		}
	}
	
	protected int getMaxIdForTable()
	{
		try
		{
			String cmdString = String.format("Select MAX(%s) from %s", idName, tableName);
			ResultSet resultSet = statement.executeQuery(cmdString);
			resultSet.next();
			
			int newId = resultSet.getInt(1) + 1;
		
			resultSet.close();
			
			return newId;
		}
		catch(Exception ex)
		{
			System.out.println(DatabaseTable.getError(ex));
			return IDHelper.getInvalidId();
		}
	}
	
	protected abstract Object convertToObject(ResultSet result);

	public abstract int add(Object inNewValue);

	public abstract boolean update(int inId, Object inNewValue);

	public abstract boolean delete(int inId);
	
	protected static String getError(Exception e)
	{
		assert(e != null);
		
		return String.format("*** SQL Error: %s", e.getMessage());
	}
	
	protected static int[] parseIds(Collection<Integer> inIds)
	{
		assert(inIds != null);
		
		int [] ids = new int[inIds.size()];
		
		int i = 0;
		for(Integer id : inIds)
		{
			ids[i] = id.intValue();
			i++;
		}

		assert(i == inIds.size());
		
		return ids;
	}
	
	protected static String checkWarning(Statement st, int updateCount)
	{
		String result = null;
		
		try
		{
			SQLWarning warning = st.getWarnings();
			
			result = warning != null ? warning.getMessage() : null;
		}
		catch (Exception e)
		{
			result = getError(e);
		}
				
		return result;
	}
}
