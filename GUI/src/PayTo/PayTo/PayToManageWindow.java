package GUI.src.PayTo;

import java.util.ArrayList;

import org.eclipse.swt.widgets.*;
import org.eclipse.swt.SWT;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;



import dataAccessLayer.*;
import domainobjects.PayTo;

public class PayToManageWindow 
{
	Shell shell;
	PayTo selectedPayTo;
	
	public PayToManageWindow()
	{
		selectedPayTo = null;
	}

	public void open()
	{
		Display display = Display.getDefault();
		createContents();
		shell.open();
		shell.layout();
		while (!shell.isDisposed())
		{
			if (!display.readAndDispatch())
			{
				display.sleep();
			}
		}
	}

	private void createContents() 
	{
		
		//***Place contents inside a panel***//
		
		shell = new Shell();
		shell.setSize(450, 300);
		shell.setText("PayTo Manager");
		
		Button cancelButton = new Button(shell, SWT.NONE);
		cancelButton.addSelectionListener(new SelectionAdapter()
		{
			@Override
			public void widgetSelected(SelectionEvent e)
			{
				//open previous window in flow
				shell.dispose();
			}
		});
		cancelButton.setBounds(10, 227, 75, 25);
		cancelButton.setText("Cancel");
		
		Button okayButton = new Button(shell, SWT.NONE);
		okayButton.addSelectionListener(new SelectionAdapter()
		{
			@Override
			public void widgetSelected(SelectionEvent e)
			{
				//pass selectedPayTo to previous window
				shell.dispose();
			}
		});
		okayButton.setBounds(349, 227, 75, 25);
		okayButton.setText("Okay");
		
		Button addButton = new Button(shell, SWT.NONE);
		addButton.addSelectionListener(new SelectionAdapter()
		{
			@Override
			public void widgetSelected(SelectionEvent e)
			{
				//open creationWindow
				shell.dispose();
			}
		});
		addButton.setBounds(180, 227, 75, 25);
		addButton.setText("+");
		
		Tree tree = populateTree();
		tree.addSelectionListener(new SelectionAdapter() 
		{
			public void widgetSelected(SelectionEvent e) 
			{
				TreeItem selectedItem = (TreeItem) e.item;
				if(selectedItem.getParentItem() == null)
				{
					selectedPayTo = new PayTo(selectedItem.getText());
				}
				else
				{
					selectedPayTo = new PayTo((selectedItem.getParentItem().getText()),selectedItem.getText());				
				}
			}
		});
	}

	private Tree populateTree()
	{
		//Create Tree
		
		Tree tree = new Tree(shell, SWT.BORDER);
		tree.setBounds(12, 10, 412, 211);
			
		StubDatabase database = new StubDatabase();
		int[] payTos = database.getAllPayToIDs();
		
		PayTo currPT = new PayTo("null");
		ArrayList<String> nameList = new ArrayList<String>();
		String currName = null;
		
		for (int i = 0; i < payTos.length; i++)
		{
			currPT = database.getPayToByID(payTos[i]);
			currName = currPT.getPayToName();
				            
			if (nameList.contains(currName) == false)
			{
				nameList.add(currName);
				TreeItem treeItem = new TreeItem(tree, 0);
				treeItem.setText(currPT.getPayToName());
				            	
				for (int j = 0; j < payTos.length; j++)
				{
					currPT = database.getPayToByID(payTos[j]);
					if (currPT.getPayToName().equals(currName))
					{
						TreeItem subTreeItem = new TreeItem(treeItem, SWT.NONE);
						subTreeItem.setText(currPT.getPayToBranch());
					}
				}
			}
				            
		}

		return tree;
	}
}
