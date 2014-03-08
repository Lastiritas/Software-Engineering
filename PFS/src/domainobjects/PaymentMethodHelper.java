package domainobjects;

public class PaymentMethodHelper 
{
	public static int toInteger(PaymentMethod method)
	{	
		PaymentMethod[] methods = PaymentMethod.values();
		for(int i = 0; i < methods.length; i++)
		{
			if(methods[i] == method)
			{
				return i;
			}
		}
		
		assert false : "This should be impossible";
		return -1;
	}
	
	public static PaymentMethod toPaymentMethod(int value)
	{
		PaymentMethod[] methods = PaymentMethod.values();
		
		assert value >= 0 && value < methods.length;
		
		return PaymentMethod.values()[value];
	}
}
