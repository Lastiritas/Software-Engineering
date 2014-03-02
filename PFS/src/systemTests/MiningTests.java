package systemTests;

import java.util.Collection;

import system.datamining.DataMiner;
import domainobjects.IDSet;

public class MiningTests 
{
	public static void main(String[] args)
	{

		//IDSet[] sets = generateInputData(new double[] {1.0, 0.8, 0.6, 0.3}, 4);
		
		IDSet[] sets = {
				IDSet.createFromArray(new int[] { 0, 1 }),
				IDSet.createFromArray(new int[] { 0, 2, 3 }),
				IDSet.createFromArray(new int[] { 0, 2 }),
				IDSet.createFromArray(new int[] { 0, 1 }),
		};

		System.out.println("Sets: ");
		for(IDSet set : sets)
		{
			System.out.println(set);
		}
		
		for(int i = 1; i <= 4; i++)
		{
			System.out.println("Min Sup = " + i);
			Collection<IDSet> freqPatterns = DataMiner.mine(sets, i);
			
			for(IDSet set : freqPatterns)
			{
				System.out.println(set);
			}
		}
	}
	
	private static IDSet[] generateInputData(double[] factors, int totalTransactions)
	{
		IDSet[] sets = new IDSet[totalTransactions];
		
		for(int i = 0; i <sets.length; i++)
		{
			sets[i] = IDSet.empty();
			
			for(int j = 0; j < factors.length; j++)
			{
				if(factors[j] >= Math.random())
				{
					sets[i] = sets[i].union(IDSet.createFromValue(j));
				}
			}
		}
		
		return sets;
	}
}
