package domainobjects;

public class PaymentMethodHelper 
{
	public static int toInteger(PaymentMethod method)
	{
		switch(method)
		{
		case CASH:	return 0;
		case CREDIT: return 1;
		case DEBIT: return 2;
		case OTHER: return 3;
		}
		
		return -1;
	}
	
	public static PaymentMethod toPaymentMethod(int value)
	{
		switch(value)
		{
		case 0: return PaymentMethod.CASH;
		case 1: return PaymentMethod.CREDIT;
		case 2: return PaymentMethod.DEBIT;
		case 3: return PaymentMethod.OTHER;
		}
		
		return PaymentMethod.OTHER;
	}
}
