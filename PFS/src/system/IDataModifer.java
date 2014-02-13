package system;

public interface IDataModifer
{
	int create();
	boolean update(int id, Object data);
	boolean delete(int id);
}
