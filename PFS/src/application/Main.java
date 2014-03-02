package application;
import gui.IWindow;
import gui.MinedView;
import gui.ViewExpense;

public class Main 
{
	public static void main(String[] args)
	{
		try 
		{
			IWindow window = new MinedView();
			window.open();
		} 
		catch (Exception e) 
		{
				e.printStackTrace();
		}
	}
}
