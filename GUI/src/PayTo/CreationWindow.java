import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.SWT;
import org.eclipse.swt.widgets.Text;
import org.eclipse.swt.widgets.Tree;
import org.eclipse.swt.widgets.List;


public class CreationWindow {

	protected Shell shell;
	private Text paytoInput;
	private Text text;

	/**
	 * Launch the application.
	 * @param args
	 */
	public static void main(String[] args) {
		try {
			CreationWindow window = new CreationWindow();
			window.open();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	/**
	 * Open the window.
	 */
	public void open() {
		Display display = Display.getDefault();
		createContents();
		shell.open();
		shell.layout();
		while (!shell.isDisposed()) {
			if (!display.readAndDispatch()) {
				display.sleep();
			}
		}
	}

	/**
	 * Create contents of the window.
	 */
	protected void createContents() {
		shell = new Shell();
		shell.setSize(450, 300);
		shell.setText("PayTo Creation");
		
		Button cancelButton = new Button(shell, SWT.NONE);
		cancelButton.setBounds(10, 227, 75, 25);
		cancelButton.setText("Cancel");
		
		Button okayButton = new Button(shell, SWT.NONE);
		okayButton.setBounds(349, 227, 75, 25);
		okayButton.setText("Okay");
		
		paytoInput = new Text(shell, SWT.BORDER);
		paytoInput.setMessage("Name");
		paytoInput.setBounds(10, 10, 199, 21);
		
		text = new Text(shell, SWT.BORDER);
		text.setMessage("Location");
		text.setBounds(225, 10, 199, 21);
		
		List list = new List(shell, SWT.BORDER);
		list.setBounds(10, 37, 199, 184);
		
		List list_1 = new List(shell, SWT.BORDER);
		list_1.setBounds(225, 37, 199, 184);

	}
}
