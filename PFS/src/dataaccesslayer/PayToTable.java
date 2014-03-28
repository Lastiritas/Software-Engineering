package dataaccesslayer;

import java.sql.ResultSet;
import java.sql.Statement;

import util.Sanitizer;
import domainobjects.IDHelper;
import domainobjects.PayTo;

public class PayToTable extends DatabaseTable
{
	private Statement statement = null;
	
	public PayToTable(Statement inStatement)
	{
		super(inStatement, "payToID", "PayTo");
		statement = inStatement;
	}
	
	@Override
	public int add(Object inNewValue) 
	{
		assert inNewValue != null;
		assert inNewValue instanceof PayTo;
					
		try
		{
			PayTo payTo = (PayTo)inNewValue;
			
			int nextId = getMaxIdForTable();
			
			String values = String.format("%d, '%s'", nextId, Sanitizer.sanitize(payTo.getName()));
			String cmdString = String.format("Insert into PAYTO (PAYTOID, LOCATION) Values(%s)", values);
			
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
		assert inNewValue instanceof PayTo;
		
		PayTo payTo = (PayTo)inNewValue;
		
		try
		{
			String values = String.format("location='%s'", Sanitizer.sanitize(payTo.getName()));
			String where = String.format("where payToID=%d", inId);
			String cmdString = String.format("Update PayTo Set %s %s", values, where);
			
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
		assert false : "Payto table does not support delete";
		return false;
	}

	@Override
	protected Object convertToObject(ResultSet result) 
	{
		try
		{
			result.next();
			String location = Sanitizer.desanitize(result.getString("location"));
			
			return new PayTo(location);
		}
		catch(Exception ex)
		{
			System.out.println(DatabaseTable.getError(ex));
			return null;
		}
	}	
}
