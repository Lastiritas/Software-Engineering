package system.datamining;

import java.util.ArrayList;
import java.util.Collection;

import domainobjects.IDSet;

public final class DataMiner 
{
	public static ArrayList<IDSet> mine(Collection<IDSet> inData, int inMinSup)
	{
		assert inData != null : "Cannot mine on null set collection";
		assert inMinSup >= 0 : "Cannot min with negative min sup";

		final ArrayList<IDSet> domainItems = getDomainItems(inData);

		return mine(inData, inMinSup, domainItems);
	}
	
	private static ArrayList<IDSet> mine(Collection<IDSet> inData, int inMinSup, Collection<IDSet> inCandidateList)
	{
		assert inData != null : "Cannot mine on null set ArrayList.";
		assert inMinSup >= 0 : "Cannot min with negative min sup";
		assert inCandidateList != null : "Cannot mine with null candidate list";

		ArrayList<IDSet> frequentItems = new ArrayList<IDSet>();
		
		for(IDSet candidate : inCandidateList)
		{
			if(countOccurances(inData, candidate) >= inMinSup)
			{
				frequentItems.add(candidate);
			}
		}

		ArrayList<IDSet> candidateList = new ArrayList<IDSet>();

		for(int itemOneIndex = 0; itemOneIndex < frequentItems.size(); itemOneIndex++)
		{
			final IDSet itemOne = frequentItems.get(itemOneIndex);
			
			for(int itemTwoIndex = itemOneIndex + 1; itemTwoIndex < frequentItems.size(); itemTwoIndex++)
			{
				final IDSet itemTwo = frequentItems.get(itemTwoIndex);
			
				if(canMerge(itemOne, itemTwo))
				{
					candidateList.add(itemOne.union(itemTwo));
				}
			}
		}

		ArrayList<IDSet> prunedList = new ArrayList<IDSet>();

		for(IDSet candidate : candidateList)
		{
			if(!isPruned(frequentItems, candidate))
			{
				prunedList.add(candidate);
			}
		}
		
		if(prunedList.size() > 0)
		{
			ArrayList<IDSet> result = mine(inData, inMinSup, prunedList);

			frequentItems.addAll(result);
		}

		return frequentItems;
	}
	
	/*
	 * Can Merge
	 * 
	 *	Summary
	 *		Check if two sets can be merged together for the next round of mining.
	 * 	Parameters
	 * 		IDSet inSetA : The first of the two sets to be merged
	 * 		IDSet inSetB : The second of the two sets to be merged
	 * 
	 * 	Return 
	 *		false : if the sets are not the same size or if the sets differ more than just the last value.
	 * 		true : if the sets can be merged
	 */
	private static boolean canMerge(IDSet inSetA, IDSet inSetB)
	{
		assert inSetA != null : "Cannot merge null set";
		assert inSetB != null : "Cannot merge null set";

		if(inSetA.getSize() != inSetB.getSize())
		{
			return false;
		}
		
		final int size = inSetA.getSize();	// a and b are the same size

		for(int i = 0; i < size - 1; i++)	// only go up to but not including the last item
		{
			if(inSetA.getValue(i) != inSetB.getValue(i))
			{
				return false;
			}
		}

		return inSetA.getValue(size - 1) != inSetB.getValue(size - 1);	// the last item must be different, you can not merge two sets that are the same
	}
	
	private static boolean isPruned(Collection<IDSet> inFrequentPatterns, IDSet inNewPattern)
	{
		assert inFrequentPatterns != null : "collection of frequent patterns can not be null";
		assert inFrequentPatterns.size() > 0 : "collection of frequent patters must contain id sets";
		assert inNewPattern != null : "new patter must not be null";
		assert inNewPattern.getSize() > 0 : "new patter can not be empty";
		
		final ArrayList<IDSet> subSets = getSubSets(inNewPattern);
		
		for(IDSet subSet : subSets)
		{
			if(!hasSetExactly(inFrequentPatterns, subSet))	// it must have all the sub sets to not get pruned
			{
				return true;
			}
		}
		
		return false;
	}
	
	private static boolean hasSetExactly(Collection<IDSet> inSets, IDSet inTarget)
	{
		assert inSets != null;
		assert inTarget != null;
		
		for(IDSet set : inSets)
		{
			if(set.equals(inTarget))
			{
				return true;
			}
		}
		
		return false;
	}
	
	/*
	 * Get Sub Sets
	 * 
	 *	Summary
	 *		Get all the sub sets of size n - 1 from a set.
	 * 	Parameters
	 * 		IDSet set : The set used to create generate sub sets.
	 * 
	 * 	Return
	 *		A list of all IDSets of size n - 1 that together make the original set.
	 */
	private static ArrayList<IDSet> getSubSets(IDSet inSet)
	{
		assert inSet != null : "Cannot find subsets of a null set";
		assert inSet.getSize() > 1 : "Cannot find subsets of a set with fewer than two items";
		
		final int targetSize = inSet.getSize() - 1;
		
		ArrayList<IDSet> setAsList = new ArrayList<>();
		setAsList.add(inSet);
		
		ArrayList<IDSet> domainItems = getDomainItems(setAsList);
		
		for(int generatingSize = 2; generatingSize <= targetSize; generatingSize++)
		{
			ArrayList<IDSet> newDomainItems = new ArrayList<IDSet>();
		
			for(int itemOneIndex = 0; itemOneIndex < domainItems.size(); itemOneIndex++)
			{
				final IDSet itemOne = domainItems.get(itemOneIndex);
				
				for(int itemTwoIndex = itemOneIndex + 1; itemTwoIndex < domainItems.size(); itemTwoIndex++)
				{
					final IDSet itemTwo = domainItems.get(itemTwoIndex);
				
					if(canMerge(itemOne, itemTwo))
					{
						newDomainItems.add(itemOne.union(itemTwo));
					}
				}
			}
			
			domainItems = newDomainItems;
		}
		
		return domainItems;
	}
	
	private static ArrayList<IDSet> getDomainItems(Collection<IDSet> inData)
	{
		assert inData != null : "Cannot get domain items from null collection";

		IDSet masterList = IDSet.empty();

		for(IDSet set : inData)
		{
			masterList = masterList.union(set);
		}

		ArrayList<IDSet> domainItems = new ArrayList<IDSet>();
		
		for(int i = 0; i < masterList.getSize(); i++)
		{
			domainItems.add(IDSet.createFromValue(masterList.getValue(i)));
		}

		return domainItems;
	}
	
	private static int countOccurances(Collection<IDSet> inData, IDSet inPattern)
	{
		assert inData != null : "Cannot count occurances in a null collection";
		assert inPattern != null : "Cannot look for null pattern";

		int occurences = 0;

		for(IDSet set : inData)
		{
			if(set.intersect(inPattern).equals(inPattern))	// set contains pattern
			{
				occurences++;
			}
		}

		return occurences;
	}
}
