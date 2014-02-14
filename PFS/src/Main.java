import gui.IWindow;
import gui.ViewExpense;

public class Main 
{
	public static void main(String[] args)
	{
		try 
		{
			IWindow window = new ViewExpense();
			window.open();
		} 
		catch (Exception e) 
		{
				e.printStackTrace();
		}
	}
}
