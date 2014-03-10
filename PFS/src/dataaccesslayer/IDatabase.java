package dataaccesslayer;

import domainobjects.Expense;
import domainobjects.Label;
import domainobjects.PayTo;

public interface IDatabase 
{
	void open(String dbName);
	void close();
	
	Expense getExpenseByID(int inId);
	Expense getExpenseWhere(String whereClause);
	int[] getAllExpenseIDs();
	int[] getAllExpenseIDsWhere(String whereClause);
	int addExpense(Expense inNewValue);
	boolean updateExpense(int inId, Expense inNewValue);
	boolean deleteExpense(int inId);
	
	Label getLabelByID(int inId);
	int[] getAllLabelIDs();
	int addLabel(Label inNewValue);
	boolean updateLabel(int inId, Label inNewValue);
	
	PayTo getPayToByID(int inId);
	int[] getAllPayToIDs();
	int addPayTo(PayTo inNewValue);
	boolean updatePayTo(int inId, PayTo inNewValue);
}
