package tests.domainobjecttests;

import domainobjects.PaymentMethod;
import domainobjects.PaymentMethodHelper;
import junit.framework.TestCase;

public class PaymentMethodHelperTest extends TestCase
{
	public void test_Get_int_value_for_cash_payment_method()
	{
		PaymentMethod method = PaymentMethod.CASH;
		int expectedResult = 0;
		
		int actualResult = PaymentMethodHelper.toInteger(method);
		
		assertEquals(actualResult, expectedResult);
	}
	
	public void test_Get_int_value_for_debit_payment_method()
	{
		PaymentMethod method = PaymentMethod.DEBIT;
		int expectedResult = 1;
		
		int actualResult = PaymentMethodHelper.toInteger(method);
		
		assertEquals(actualResult, expectedResult);
	}
	
	public void test_Get_int_value_for_credit_payment_method()
	{
		PaymentMethod method = PaymentMethod.CREDIT;
		int expectedResult = 2;
		
		int actualResult = PaymentMethodHelper.toInteger(method);
		
		assertEquals(actualResult, expectedResult);
	}
	
	public void test_Get_int_value_for_other_payment_method()
	{
		PaymentMethod method = PaymentMethod.OTHER;
		int expectedResult = 3;
		
		int actualResult = PaymentMethodHelper.toInteger(method);
		
		assertEquals(actualResult, expectedResult);
	}
	
	public void test_Get_cash_paymentMethod_for_int_value_zero()
	{
		int value = 0;
		PaymentMethod expectedResult = PaymentMethod.CASH;
		
		PaymentMethod actualResult = PaymentMethodHelper.toPaymentMethod(value);
		
		assertEquals(actualResult, expectedResult);
	}
	
	public void test_Get_debit_paymentMethod_for_int_value_one()
	{
		int value = 1;
		PaymentMethod expectedResult = PaymentMethod.DEBIT;
		
		PaymentMethod actualResult = PaymentMethodHelper.toPaymentMethod(value);
		
		assertEquals(actualResult, expectedResult);
	}
	
	public void test_Get_credit_paymentMethod_for_int_value_two()
	{
		int value = 2;
		PaymentMethod expectedResult = PaymentMethod.CREDIT;
		
		PaymentMethod actualResult = PaymentMethodHelper.toPaymentMethod(value);
		
		assertEquals(actualResult, expectedResult);
	}
	
	public void test_Get_other_paymentMethod_for_int_value_three()
	{
		int value = 3;
		PaymentMethod expectedResult = PaymentMethod.OTHER;
		
		PaymentMethod actualResult = PaymentMethodHelper.toPaymentMethod(value);
		
		assertEquals(actualResult, expectedResult);
	}
}