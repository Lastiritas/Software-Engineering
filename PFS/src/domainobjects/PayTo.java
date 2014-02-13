package domainobjects;

public class PayTo 
{
	public PayTo(int inID, String inPayToName, String inBranch)
	{
		id = inID;
		payToName = inPayToName;
		branch = inBranch;
	}
	
	public PayTo(String inPayToName, String inBranch)
	{
		id = 0;
		payToName = inPayToName;
		branch = inBranch;
	}
	
	public int getPayToID()
	{
		return id;
	}
	
	public String getPayToName()
	{
		return payToName;
	}
	
	public String getPayToBranch()
	{
		return branch;
	}
	
	private int id;
	private final String payToName;
	private final String branch;
}
