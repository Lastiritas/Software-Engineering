package dataaccesslayer;

public interface IDatabaseTable 
{
	int[] getAllIds();
	int[] getAllIdsWhere(String inWhereClause);
	
	Object getById(int inId);
	Object getWhere(String inWhereClause);
	
	int add(Object inNewValue);
	boolean update(int inId, Object inNewValue);
	boolean delete(int inId);
}
