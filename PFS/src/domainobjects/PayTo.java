package domainobjects;

public class PayTo 
{	
	public PayTo(String inPayToName, String inBranch)
	{
		payToName = inPayToName;
		branch = inBranch;
	}
	
	public PayTo(String inPayToName)
	{
		payToName = inPayToName;
		branch = "";
	}
	
	public String getPayToName()
	{
		return payToName;
	}
	
	public String getPayToBranch()
	{
		return branch;
	}
	
	private final String payToName;
	private final String branch;
}
