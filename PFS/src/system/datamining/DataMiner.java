package system.datamining;

import java.util.ArrayList;

import domainobjects.IDSet;

public final class DataMiner 
{
	public static ArrayList<IDSet> mine(IDSet[] inData, int inMinSup)
	{
		assert inData != null : "Cannot mine on null set collection";
		assert inMinSup >= 0 : "Cannot min with negative min sup";

		final ArrayList<IDSet> domainItems = getDomainItems(inData);

		return mine(inData, inMinSup, domainItems);
	}
	
	private static ArrayList<IDSet> mine(IDSet[] inData, int inMinSup, ArrayList<IDSet> inCandidateList)
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
	 * 		IDSet a : The first of the two sets to be merged
	 * 		IDSet b : The second of the two sets to be merged
	 * 
	 * 	Return 
	 *		false : if the sets are not the same size or if the sets differ more than just the last value.
	 * 		true : if the sets can be merged
	 */
	private static boolean canMerge(IDSet a, IDSet b)
	{
		assert a != null : "Cannot merge null set";
		assert b != null : "Cannot merge null set";

		if(a.getSize() != b.getSize())
		{
			return false;
		}
		
		final int size = a.getSize();	// a and b are the same size

		for(int i = 0; i < size - 1; i++)	// only go up to but not including the last item
		{
			if(a.getValue(i) != b.getValue(i))
			{
				return false;
			}
		}

		return a.getValue(size - 1) != b.getValue(size - 1);	// the last item must be different, you can not merge two sets that are the same
	}
	
	private static boolean isPruned(ArrayList<IDSet> frequentPatterns, IDSet newPattern)
	{
		final ArrayList<IDSet> subSets = getSubSets(newPattern);
		
		for(IDSet subSet : subSets)
		{
			if(!hasSetExactly(frequentPatterns, subSet))	// it must have all the sub sets to not get pruned
			{
				return true;
			}
		}
		
		return false;
	}
	
	private static boolean hasSetExactly(ArrayList<IDSet> inSetArrayList, IDSet inTargetSet)
	{
		for(IDSet set : inSetArrayList)
		{
			if(set.equals(inTargetSet))
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
	private static ArrayList<IDSet> getSubSets(IDSet set)
	{
		assert set != null : "Cannot find subsets of a null set";
		assert set.getSize() > 1 : "Cannot find subsets of a set with fewer than two items";
		
		final int targetSize = set.getSize() - 1;
		
		ArrayList<IDSet> domainItems = getDomainItems(new IDSet[] { set });
		
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
	
	private static ArrayList<IDSet> getDomainItems(IDSet[] inData)
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
	
	private static int countOccurances(IDSet[] inData, IDSet pattern)
	{
		assert inData != null : "Cannot count occurances in a null collection";
		assert pattern != null : "Cannot look for null pattern";

		int occurences = 0;

		for(IDSet set : inData)
		{
			if(set.intersect(pattern).equals(pattern))	// set contains pattern
			{
				occurences++;
			}
		}

		return occurences;
	}
}
