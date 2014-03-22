package dataaccesslayer;

import java.sql.ResultSet;
import java.sql.Statement;
import java.util.Vector;

import domainobjects.IDHelper;
import domainobjects.PayTo;

public class PayToTable implements IDatabaseTable
{
	private Statement statement = null;
	
	public PayToTable(Statement inStatement)
	{
		statement = inStatement;
	}
	
	@Override
	public int[] getAllIds() 
	{
		return SQLHelper.getAllIdsFrom(statement, "payToID", "PayTo");
	}

	@Override
	public int[] getAllIdsWhere(String inWhereClause) 
	{
		return SQLHelper.getAllIdsFromWhere(statement, "payToID", "PayTo", inWhereClause);
	}

	@Override
	public Object getById(int inId) 
	{
		return getWhere(String.format("payToID=%d",inId));
	}

	@Override
	public Object getWhere(String inWhereClause) 
	{
		assert false : "Payto table does not support get where";
		return null;
	}

	@Override
	public int add(Object inNewValue) 
	{
		assert inNewValue != null;
		assert inNewValue instanceof PayTo;
					
		try
		{
			PayTo payTo = (PayTo)inNewValue;
			
			int nextId = SQLHelper.getMaxIdForTable(statement, "PAYTOID", "PAYTO");
			
			String values = String.format("%d, '%s'", nextId, payTo.getName());
			String cmdString = String.format("Insert into PAYTO (PAYTOID, LOCATION) Values(%s)", values);
			
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
		assert inNewValue instanceof PayTo;
		
		PayTo payTo = (PayTo)inNewValue;
		
		try
		{
			String values = String.format("location='%s'", payTo.getName());
			String where = String.format("where payToID=%d", inId);
			String cmdString = String.format("Update PayTo Set %s %s", values, where);
			
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
		assert false : "Payto table does not support delete";
		return false;
	}

	private PayTo convertToPayTo(ResultSet resultSet)
	{
		try
		{
			resultSet.next();
			String location = resultSet.getString("location");
			
			return new PayTo(location);
		}
		catch(Exception ex)
		{
			System.out.println(SQLHelper.getError(ex));
			return null;
		}
	}	
}
