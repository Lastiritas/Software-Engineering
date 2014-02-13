package gui;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.SWT;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.widgets.Combo;
import org.eclipse.swt.widgets.List;
import org.eclipse.swt.widgets.Tree;
import org.eclipse.swt.widgets.TreeColumn;
import org.eclipse.swt.widgets.TreeItem;


public class MainWindow {

	protected Shell shell;

	/**
	 * Launch the application.
	 * @param args
	 */
	public static void main(String[] args) {
		try {
			MainWindow window = new MainWindow();
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
		
		//***Place contents inside a panel***//
		shell = new Shell();
		shell.setSize(450, 300);
		shell.setText("PayTo Manager");
		
		Button cancelButton = new Button(shell, SWT.NONE);
		cancelButton.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent e) {
			}
		});
		cancelButton.setBounds(10, 227, 75, 25);
		cancelButton.setText("Cancel");
		
		Button okayButton = new Button(shell, SWT.NONE);
		okayButton.setBounds(349, 227, 75, 25);
		okayButton.setText("Okay");
		
		Button addButton = new Button(shell, SWT.NONE);
		addButton.setBounds(180, 227, 75, 25);
		addButton.setText("+");
		
		Tree tree = new Tree(shell, SWT.BORDER);
		tree.setBounds(12, 10, 412, 211);
		for (int i = 0; i < 5; i++) {
			            TreeItem treeItem = new TreeItem(tree, 0);
			            treeItem.setText("TreeItem" + i);
			            for (int j = 0; j < 5; j++) {
			                TreeItem subTreeItem = new TreeItem(treeItem, SWT.NONE);
			                subTreeItem.setText("SubTreeItem" + j);
			                
			            }
		}

	
}
}
