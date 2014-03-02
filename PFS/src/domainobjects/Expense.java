package domainobjects;

public class Expense
{
	public Expense(SimpleDate inDate, Money inAmount, PaymentMethod inMethod, String inDescription, int inPayTo, IDSet inLabels)
	{
		date = inDate;
		amount = inAmount;
		method = inMethod;
		description = inDescription;
		payTo = inPayTo;
		labels = inLabels;
	}

	public SimpleDate getDate()
	{
		return date;
	}

	public Money getAmount()
	{
		return amount;
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

		output.append(amount);
		output.append(" | ");

		output.append(method);
		output.append(" | ");

		output.append(description);
		
		return output.toString();
	}

	private final SimpleDate date;
	private final Money amount;
	private final PaymentMethod method;
	private final String description;
	private final int payTo;
	private final IDSet labels;

	private static final int CENTS_IN_DOLLAR = 100;
}