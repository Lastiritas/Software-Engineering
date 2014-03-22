package dataaccesslayer;

import java.sql.ResultSet;
import java.sql.Statement;

import domainobjects.IDHelper;
import domainobjects.Label;

public class LabelTable extends DatabaseTable
{
	private Statement statement = null;
	
	public LabelTable(Statement inStatement)
	{
		super(inStatement, "labelID", "Label");
		statement = inStatement;
	}
	
	@Override
	public int add(Object inNewValue) 
	{
		assert inNewValue != null;
		assert inNewValue instanceof Label;
				
		try
		{
			Label label = (Label)inNewValue;
			
			int nextId = getMaxIdForTable();
			
			String values = String.format("%d, '%s'", nextId, label.getName());
			String cmdString = String.format("Insert into Label Values(%s)", values);
			
			int insertedSuccessful = statement.executeUpdate(cmdString);
			String result = DatabaseTable.checkWarning(statement, insertedSuccessful);
			
			assert result == null;
			
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
		assert inNewValue instanceof Label;		
		
		Label label = (Label)inNewValue;
		
		try
		{
			String values = String.format("name='%s'", label.getName());
			String where = String.format("where labelID=%d", inId);
			String cmdString = String.format("Update Label Set %s %s", values, where);
	
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

	@Override
	public boolean delete(int inId) 
	{
		assert false : "Label table does not support delete";
		return false;
	}
	
	@Override
	protected Object convertToObject(ResultSet result) 
	{	
		try
		{
			result.next();
	
			String name = result.getString("name");
						
			return new Label(name);
		}
		catch(Exception ex)
		{
			System.out.println(DatabaseTable.getError(ex));
			return null;
		}
	}
}
