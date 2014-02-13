
public class StringMatch {

	/**
	 * String matching algorithm
	 * The algorithm looks for a match and returns its position
	 *  in the original string once it finds it, so if I send in 
	 *  THESIS and search for IS then it will return 4 and
	 *  consider it a match
	 */
	 public static int match(String toFind, String search)
	 {
		 int next[] = new int[search.length()];
		 int n = toFind.length(), m=search.length();
		 int i=1, j=0;
		 
		//init next
		 next[0]=0;
		 do {
			 if(j==0 || search.substring(i, i+1).equalsIgnoreCase(search.substring(j, j+1)))
			 {
				 i++;
				 j++;
				 next[i]=j;
			 }
			 else
				 j=next[j];
		 } while (i<=m) ;
		 
		 //do search
		 i=1;
		 j=1;
		 do{
			 if( j==0 || toFind.substring(i, i+1).equalsIgnoreCase(search.substring(j, j+1)))
			 {
				 i++;
				 j++;
			 }
			 else
				 j = next[j];
		 } while (j<=m & i<=n) ;
		 
		 //return
		 if(j>m)
			 return (i-j);
		 else
			 return -1;
	 }
}
