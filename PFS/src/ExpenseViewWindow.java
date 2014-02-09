import javax.swing.JFrame;
import java.awt.BorderLayout;

public class ExpenseViewWindow extends JFrame 
{
	public ExpenseViewWindow() 
	{
		ExpensePanel panel = new ExpensePanel();
		getContentPane().add(panel, BorderLayout.CENTER);
	}

	public static void main(String[] args)
	{
		ExpenseViewWindow window = new ExpenseViewWindow();
		window.show();
	}
}
