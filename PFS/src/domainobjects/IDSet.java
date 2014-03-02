package domainobjects;

import java.util.Arrays;

public class IDSet
{
	private IDSet(int[] inValues)
	{
		assert inValues != null;

		// we are safe during a direct assignment because this constructor should only be
		// called from CreateFromArray which creates the array
		values = inValues;
	}
	
	public IDSet union(IDSet inSet)
	{
		assert inSet != null;

		int[] newSet = union(values, inSet.values);
		return createFromArray(newSet);
	}

	public IDSet intersect(IDSet inSet)
	{
		assert inSet != null;

		int[] newSet = intersect(values, inSet.values);
		return createFromArray(newSet);
	}

	public boolean contains(int inValue)
	{
		// binary search

		int start = 0;
		int end = values.length - 1;

		while(end >= start)
		{
			assert(start >= 0 && start < values.length);
			assert(end >= 0 && end < values.length);
			
			final int middle = (start + end) / 2;

			final int middleValue = values[middle];

			if(middleValue == inValue)
			{
				return true;
			}
			else if(middleValue < inValue)
			{
				start = middle + 1;
			}
			else	// middle > inValue
			{
				end = middle - 1;
			}
		}

		return false;
	}

	public boolean equals(IDSet inOtherSet)
	{
		assert inOtherSet != null;

		final int size = getSize();

		if(inOtherSet.getSize() != size)
		{
			return false;
		}

		for(int i = 0; i < size; i++)
		{
			if(inOtherSet.getValue(i) != getValue(i))
			{
				return false;
			}
		}

		return true;
	}

	public int getSize()
	{
		return values.length;
	}

	public int getValue(int inIndex)
	{
		assert inIndex >= 0 && inIndex < values.length;

		return values[inIndex];
	}

	public String toString()
	{
		if(values.length == 0)
		{
			return "< >";
		}
		
		StringBuilder output = new StringBuilder();

		output.append("<");
		
		for(int i = 0; i < values.length; i++)
		{
			output.append(values[i]);
			output.append(", ");
		}

		output.replace(output.length() - 2, output.length() - 1, ">");
		
		return output.toString();
	}

	public void copyToArray(int[] outArray)
	{
		assert outArray.length >= values.length;
		System.arraycopy(values, 0, outArray, 0, values.length);
	}

	private final int[] values;

	public static IDSet createFromArray(int[] inValues)
	{
		assert inValues != null;

		int[] values = new int[inValues.length];
		System.arraycopy(inValues, 0, values, 0, inValues.length);

		int[] set = intersect(values, values);	// intersect with itself to ensure that only one value is kept values 

		return new IDSet(set);
	}

	public static IDSet createFromValue(int value)
	{
		int[] values = { value };
		return createFromArray(values);
	}
	
	private static int[] union(int[] inSet0, int[] inSet1)
	{
		assert inSet0 != null;
		assert inSet1 != null;

		final int totalCount = inSet0.length + inSet1.length;

		int[] totalCollection = new int[totalCount];
		System.arraycopy(inSet0, 0, totalCollection, 0, inSet0.length);
		System.arraycopy(inSet1, 0, totalCollection, inSet0.length, inSet1.length);
		
		Arrays.sort(totalCollection);

		int[] uniqueCollection = new int[totalCount];
		int uniqueCount = 0;

		// add the first value because it is garenteed to be unqiue
		uniqueCollection[uniqueCount] = totalCollection[uniqueCount];
		uniqueCount++;

		// only add a value if it is different from the previous value
		for(int i = 1; i < totalCollection.length; i++)
		{
			final int index0 = i - 1;
			final int index1 = i;

			if(totalCollection[index0] != totalCollection[index1])
			{
				uniqueCollection[uniqueCount] = totalCollection[index1];
				uniqueCount++;
			}
		}

		int[] output = new int[uniqueCount];
		System.arraycopy(uniqueCollection, 0, output, 0, uniqueCount);

		return output;
	}

	private static int[] intersect(int[] inSet0, int[] inSet1)
	{
		assert inSet0 != null;
		assert inSet1 != null;

		final int totalCount = inSet0.length + inSet1.length;

		int[] totalCollection = new int[totalCount];
		System.arraycopy(inSet0, 0, totalCollection, 0, inSet0.length);
		System.arraycopy(inSet1, 0, totalCollection, inSet0.length, inSet1.length);
		
		Arrays.sort(totalCollection);

		int[] similarCollection = new int[totalCount];
		int similarCount = 0;

		for(int start = 0; start < totalCollection.length; start++)
		{
			int length = 1;

			for(int end = start + 1; end < totalCollection.length && totalCollection[start] == totalCollection[end]; end++)
			{
				length++;
			}

			if(length > 1)
			{
				similarCollection[similarCount] = totalCollection[start];
				similarCount++;

				start += (length - 1);	// jump to the end of the block, minus one because start will be incremented at the head of the loop
			}
		}

		int[] output = new int[similarCount];
		System.arraycopy(similarCollection, 0, output, 0, similarCount);

		return output;
	}
	
	public static IDSet empty()
	{
		return emptySet;
	}
	
	private static int[] emptyData = new int[0];
	private static IDSet emptySet = new IDSet(emptyData);
}