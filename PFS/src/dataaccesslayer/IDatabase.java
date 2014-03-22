package dataaccesslayer;

public interface IDatabase 
{
	void open(String dbName);
	void close();
	
	IDatabaseTable getExpenseTable();
	IDatabaseTable getLabelTable();
	IDatabaseTable getPayToTable();
}
