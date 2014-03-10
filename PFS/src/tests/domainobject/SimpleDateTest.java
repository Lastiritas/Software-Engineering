package tests.domainobject;

import domainobjects.SimpleDate;
import junit.framework.TestCase;

public class SimpleDateTest extends TestCase
{
	public void test_Create_and_get_a_simpleDate_object()
	{
		int actualDay;
		int actualMonth;
		int actualYear;
		final int expectedDay = 24;
		final int expectedMonth = 12;
		final int expectedYear = 2014;
		String stringExpected = String.format("%d/%d/%d", expectedDay, expectedMonth, expectedYear);
		
		SimpleDate date = SimpleDate.Now();
		date.setDate(stringExpected);
		
		actualDay = date.getDay();
		actualMonth = date.getMonth();
		actualYear = date.getYear();
		
		assertEquals(actualDay, expectedDay);
		assertEquals(actualMonth, expectedMonth);
		assertEquals(actualYear, expectedYear);
	}
	
	public void test_Set_simple_date_by_element_and_get_it_back()
	{
		int actualDay;
		int actualMonth;
		int actualYear;
		final int expectedDay = 24;
		final int expectedMonth = 12;
		final int expectedYear = 2014;
		
		SimpleDate date = SimpleDate.Now();
		date.setDay(expectedDay);
		date.setMonth(expectedMonth);
		date.setYear(expectedYear);
		
		actualDay = date.getDay();
		actualMonth = date.getMonth();
		actualYear = date.getYear();
		
		assertEquals(actualDay, expectedDay);
		assertEquals(actualMonth, expectedMonth);
		assertEquals(actualYear, expectedYear);
	}
	
	public void test_comparing_an_older_date_with_a_recent_one_should_return_minus_one()
	{
		int actualResult;
		final int expectedResult = -1;
		final String olderDate = "12/12/1990";
		final String newDate = "3/8/2014";
		
		SimpleDate olderSimpleDate = SimpleDate.Now();
		olderSimpleDate.setDate(olderDate);
		
		SimpleDate newSimpleDate = SimpleDate.Now();
		newSimpleDate.setDate(newDate);
		
		actualResult = olderSimpleDate.compareTo(newSimpleDate);
		
		assertEquals(actualResult, expectedResult);
	}
	
	public void test_comparing_a_new_date_with_an_older_date_should_return_one()
	{
		int actualResult;
		final int expectedResult = 1;
		final String olderDate = "3/8/2014";
		final String newDate = "12/12/1990";
		
		SimpleDate olderSimpleDate = SimpleDate.Now();
		olderSimpleDate.setDate(olderDate);
		
		SimpleDate newSimpleDate = SimpleDate.Now();
		newSimpleDate.setDate(newDate);
		
		actualResult = olderSimpleDate.compareTo(newSimpleDate);
		
		assertEquals(actualResult, expectedResult);
	}
	
	public void test_comparing_two_similar_simple_dates_should_return_zero()
	{
		int actualResult;
		final int expectedResult = 0;
		final String dateString = "3/8/2014";
		
		SimpleDate dateOne = SimpleDate.Now();
		dateOne.setDate(dateString);
		
		SimpleDate dateTwo = SimpleDate.Now();
		dateTwo.setDate(dateString);
		
		actualResult = dateOne.compareTo(dateTwo);
		
		assertEquals(actualResult, expectedResult);
	}
	
	public void test_comparing_two_similar_simple_dates_should_return_true()
	{
		final String dateString = "3/8/2014";
		
		SimpleDate dateOne = SimpleDate.Now();
		dateOne.setDate(dateString);
		
		SimpleDate dateTwo = SimpleDate.Now();
		dateTwo.setDate(dateString);
		
		assertTrue(dateOne.equals(dateTwo));
	}
	
	public void test_comparing_two_different_simple_dates_should_return_false()
	{
		final String dateString = "3/8/2014";
		final String different = "3/9/2013";
		
		SimpleDate dateOne = SimpleDate.Now();
		dateOne.setDate(dateString);
		
		SimpleDate dateTwo = SimpleDate.Now();
		dateTwo.setDate(different);
		
		assertFalse(dateOne.equals(dateTwo));
	}
	
	public void test_parse_a_simple_date_to_integer()
	{
		int actualResult;
		final int expectedResult = 20140803;
		final String stringDate = "3/8/2014";
		
		SimpleDate dateOne = SimpleDate.Now();
		dateOne.setDate(stringDate);
		
		actualResult = dateOne.toInteger();
		
		assertEquals(actualResult, expectedResult);
	}
	
	public void test_parse_from_int_to_SimpleDate_object()
	{
		SimpleDate actualResult;
		final int value = 20141203;
		final String stringDate = "3/12/2014";
		
		SimpleDate expectedResult = SimpleDate.Now();
		expectedResult.setDate(stringDate);
		
		actualResult = SimpleDate.parseDate(value);
		
		assertEquals(actualResult.toInteger(), expectedResult.toInteger());
	}
	
	public void test_ToString_method()
	{
		final String expectedResult = "03/08/2014";
		String actualResult;
		
		SimpleDate dateOne = SimpleDate.Now();
		dateOne.setDate(expectedResult);
		
		actualResult = dateOne.toString();
		
		assertEquals(actualResult, expectedResult);
	}
}