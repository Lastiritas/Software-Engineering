
public class StringMatch {

	/**
	 * String matching algorithm
	 * The algorithm looks for a match and returns true once it finds match
	 * accepts string and its pattern to look for
	 */
	 public static boolean match(String search, String toFind)
	 {
		 if (search.length() == 0)
			 return false;
		 search = search.toLowerCase();
		 toFind = toFind.toLowerCase();
		 
		 int n=search.length(), m=toFind.length();
		 int next[] = new int[m];
		 int j=0;
		 
		 for(int i = 1; i < m; i++) 
		 {
			 while (j > 0 && toFind.charAt(j) != toFind.charAt(i))  
				 j = next[j - 1];
			 
			 if(toFind.charAt(j) == toFind.charAt(i)) 
				 j++; 
			 
			 next[i] = j;
	    }

		 
		j = 0;
		
		for (int i = 0; i < n; i++) 
		{
		      while (j > 0 && toFind.charAt(j) != search.charAt(i))
		    	  j = next[j - 1];
		      
		      if (toFind.charAt(j) == search.charAt(i))
		    	  j++;
		      
		      if (j == m) 
		        return true;
		}
		return false;
		 
		
	 }
}
