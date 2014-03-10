package tests.domainobject;

import domainobjects.Money;
import junit.framework.TestCase;

public class MoneyTest extends TestCase
{
	public void test_Create_and_get_a_money_object()
	{
		int actualDollars;
		int actualCents;
		int expectedDollars = 50;
		int expectedCents = 25;
		
		Money money = new Money(expectedDollars, expectedCents);
		
		actualDollars = money.getDollars();
		actualCents = money.getCents();
		
		assertEquals(actualDollars, expectedDollars);
		assertEquals(actualCents, expectedCents);
	}
	
	public void test_Compare_a_lower_money_to_a_higher_money_should_return_minus_one()
	{
		int dollars = 50;
		int dollarsDouble = 100;
		int cents = 25;
		int expectedResult = -1;
		int actualResult;
		
		Money moneyOne = new Money(dollars, cents);
		Money moneyTwo = new Money(dollarsDouble, cents);
		
		actualResult = moneyOne.compareTo(moneyTwo);
		
		assertEquals(actualResult, expectedResult);
	}
	
	public void test_Compare_a_higher_money_to_a_lower_money_should_return_one()
	{
		int dollars = 50;
		int dollarsLess = 25;
		int cents = 25;
		int expectedResult = 1;
		int actualResult;
		
		Money moneyOne = new Money(dollars, cents);
		Money moneyTwo = new Money(dollarsLess, cents);
		
		actualResult = moneyOne.compareTo(moneyTwo);
		
		assertEquals(actualResult, expectedResult);
	}
	
	public void test_Compare_same_money_objects_should_return_zero()
	{
		int dollars = 50;
		int cents = 25;
		int expectedResult = 0;
		int actualResult;
		
		Money moneyOne = new Money(dollars, cents);
		Money moneyTwo = new Money(dollars, cents);
		
		actualResult = moneyOne.compareTo(moneyTwo);
		
		assertEquals(actualResult, expectedResult);
	}
	
	public void test_Get_total_cents()
	{
		int dollars = 50;
		int cents = 25;
		int expectedCents = dollars * 100 + cents;
		int actualCents;
		
		Money money = new Money(dollars, cents);
		
		actualCents = money.getTotalCents();
		
		assertEquals(actualCents, expectedCents);
	}
	
	public void test_Comparing_same_money_objects_should_return_true()
	{
		int dollars = 50;
		int cents = 25;
		boolean actualResult;
		
		Money moneyOne = new Money(dollars, cents);
		Money moneyTwo = new Money(dollars, cents);
		
		actualResult = moneyOne.equals(moneyTwo);
		
		assertTrue(actualResult);
	}
	
	public void test_Comparing_different_money_objects_should_return_false()
	{
		int dollars = 50;
		int differentDollars = 24;
		int cents = 25;
		boolean actualResult;
		
		Money moneyOne = new Money(dollars, cents);
		Money moneyTwo = new Money(differentDollars, cents);
		
		actualResult = moneyOne.equals(moneyTwo);
		
		assertFalse(actualResult);
	}
	
	public void test_ToString_method()
	{
		int dollars = 50;
		int cents = 25;
		String expectedResult = "$50.25";
		String actualResult;
		
		Money money = new Money(dollars, cents);
		
		actualResult = money.toString();
		
		assertEquals(actualResult, expectedResult);
	}
	
	public void test_Create_a_new_money_object_from_string()
	{
		int dollars = 50;
		int cents = 25;
		String parameter = "$50.25";
		
		Money expectedMoney = new Money(dollars, cents);
		Money actualMoney = Money.fromString(parameter);
		
		assertTrue(expectedMoney.equals(actualMoney));
	}
}