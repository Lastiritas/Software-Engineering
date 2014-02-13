package domainobjects;

public class Expense
{
	public Expense(SimpleDate inDate, int inTotalCents, PaymentMethod inMethod, String inDescription, int inPayTo, IDSet inLabels)
	{
		date = inDate;
		totalCents = inTotalCents;
		method = inMethod;
		description = inDescription;
		payTo = inPayTo;
		labels = inLabels;
	}

	public SimpleDate getDate()
	{
		return date;
	}

	public int getDollars()
	{
		return totalCents / CENTS_IN_DOLLAR;
	}

	public int getCents()
	{
		return totalCents % CENTS_IN_DOLLAR;
	}

	public int getTotalAmountInCents()
	{
		return totalCents;
	}

	public PaymentMethod getPaymentMethod()
	{
		return method;
	}

	public String getDescription()
	{
		return description;	// string data is immutable, no need to copy
	}

	public int getPayTo()
	{
		return payTo;
	}

	public IDSet getLabels()
	{
		return labels;
	}

	public String toString()
	{
		StringBuilder output = new StringBuilder();

		output.append(date);
		output.append(" | ");

		output.append(totalCents);
		output.append(" | ");

		output.append(method);
		output.append(" | ");

		output.append(description);
		
		return output.toString();
	}

	private final SimpleDate date;
	private final int totalCents;
	private final PaymentMethod method;
	private final String description;
	private final int payTo;
	private final IDSet labels;

	private static final int CENTS_IN_DOLLAR = 100;
}