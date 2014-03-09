package domainobjects;

public class IDHelper 
{
	public static int getInvalidId()
	{
		return MIN_ALLOWABLE_ID - 1;	// the value one less than the min value is invalid
	}
	
	public static boolean isIdValid(int inId)
	{
		return inId >= MIN_ALLOWABLE_ID;
	}
	
	private static final int MIN_ALLOWABLE_ID = 1;
}
