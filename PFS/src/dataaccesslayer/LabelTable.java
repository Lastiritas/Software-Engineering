package dataaccesslayer;

import java.sql.ResultSet;
import java.sql.Statement;
import java.util.Vector;

import domainobjects.IDHelper;
import domainobjects.Label;

public class LabelTable implements IDatabaseTable
{
	private Statement statement = null;
	
	public LabelTable(Statement inStatement)
	{
		statement = inStatement;
	}
	
	@Override
	public int[] getAllIds() 
	{
		return SQLHelper.getAllIdsFrom(statement, "labelID", "Label");
	}

	@Override
	public int[] getAllIdsWhere(String inWhereClause) 
	{	
		return SQLHelper.getAllIdsFromWhere(statement, "labelID", "Label", inWhereClause);
	}

	@Override
	public Object getById(int inId) 
	{
		return getWhere(String.format("labelID=%d",inId));
	}

	@Override
	public Object getWhere(String inWhereClause) 
	{
		assert false : "Label table does not support get where";
		return null;
	}

	@Override
	public int add(Object inNewValue) 
	{
		assert inNewValue != null;
		assert inNewValue instanceof Label;
				
		try
		{
			Label label = (Label)inNewValue;
			
			int nextId = SQLHelper.getMaxIdForTable(statement, "labelID", "Label");
			
			String values = String.format("%d, '%s'", nextId, label.getName());
			String cmdString = String.format("Insert into Label Values(%s)", values);
			
			int insertedSuccessful = statement.executeUpdate(cmdString);
			String result = SQLHelper.checkWarning(statement, insertedSuccessful);
			
			assert result == null;
			
			return insertedSuccessful == 1 ? nextId : IDHelper.getInvalidId();
		}
		catch(Exception ex)
		{
			System.out.println(SQLHelper.getError(ex));
			return IDHelper.getInvalidId();
		}
	}

	@Override
	public boolean update(int inId, Object inNewValue) 
	{
		assert IDHelper.isIdValid(inId);
		assert inNewValue != null;
		assert inNewValue instanceof Label;		
		
		Label label = (Label)inNewValue;
		
		try
		{
			String values = String.format("name='%s'", label.getName());
			String where = String.format("where labelID=%d", inId);
			String cmdString = String.format("Update Label Set %s %s", values, where);
	
			int successful = statement.executeUpdate(cmdString);
			
			String result = SQLHelper.checkWarning(statement, successful);
			
			assert result == null;
			
			return successful == 1;
		}
		catch(Exception ex)
		{
			System.out.println(SQLHelper.getError(ex));
			return false;
		}
	}

	@Override
	public boolean delete(int inId) 
	{
		assert false : "Label table does not support delete";
		return false;
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
			System.out.println(SQLHelper.getError(ex));
		}
		
		return label;
	}
}
