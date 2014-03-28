package util;

public class Sanitizer {
	
	public static String sanitize(String toSan)
	{

		for(int i=0; i<toSan.length(); i++)
		{
			int index = toSan.indexOf("'");
			if(index!=-1)
			{
				toSan = toSan.substring(0, index) + "~!?" + toSan.substring(index+1);
			}
			else 
				break;
		}
		
		return toSan;
	}

	public static String desanitize(String toDsan)
	{
		for(int i=0; i<toDsan.length(); i++)
		{
			int index = toDsan.indexOf("~!?");
			if(index!=-1)
			{
				toDsan = toDsan.substring(0, index) + "'" + toDsan.substring(index+3);
			}
			else 
				break;
		}
		
		return toDsan;
	}
	
}
