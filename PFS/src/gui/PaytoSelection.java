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

import system.PFSystem;
import domainobjects.IDSet;
import domainobjects.PayTo;


public class PaytoSelection implements IDialog
{
	private Shell shell;
	private Tree tree;
	
	private String output = null;
	
	/**
	 * Open the window.
	 */
	public Object open() 
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
		
		return getPayToID();
	}

	private int getPayToID()
	{		
		final IDSet payToIDs = PFSystem.getCurrent().getPayToSystem().getAllIDs();
		
		for(int i = 0; i < payToIDs.getSize(); i++)
		{
			final int id = payToIDs.getValue(i);
			
			final PayTo payTo = (PayTo)PFSystem.getCurrent().getPayToSystem().getDataByID(id);
			
			String payToString = payTo.getPayToName() + ", " + payTo.getPayToBranch();
			
			if(payToString.equals(output))
			{
				return id;
			}
		}
		
		return -1;
	}
	
	/**
	 * Create contents of the window.
	 * @wbp.parser.entryPoint
	 */
	protected void createContents() 
	{
		
		//***Place contents inside a panel***//
		shell = new Shell(SWT.APPLICATION_MODAL);
		shell.setSize(450, 300);
		shell.setText("PayTo Manager");
		
		tree = new Tree(shell, SWT.BORDER);
		tree.addSelectionListener(new SelectionAdapter() 
		{
			@Override
			public void widgetSelected(SelectionEvent arg0) 
			{
				TreeItem selection = tree.getSelection()[0];	
				
				if(selection.getParentItem() == null)
				{
					return;
				}
				
				output = selection.getParentItem().getText() + ", " + selection.getText();
			}
		});
		tree.setBounds(12, 10, 426, 211);

		populateList(tree);
		
		Button cancelButton = new Button(shell, SWT.NONE);
		cancelButton.addSelectionListener(new SelectionAdapter() 
		{
			@Override
			public void widgetSelected(SelectionEvent e) 
			{
				shell.close();
			}
		});
		cancelButton.setBounds(10, 227, 75, 25);
		cancelButton.setText("Cancel");
		
		Button okayButton = new Button(shell, SWT.NONE);
		okayButton.addSelectionListener(new SelectionAdapter() 
		{
			@Override
			public void widgetSelected(SelectionEvent arg0) 
			{
				shell.close();
			}
		});
		okayButton.setBounds(363, 227, 75, 25);
		okayButton.setText("Okay");
		
		Button addButton = new Button(shell, SWT.NONE);
		addButton.addSelectionListener(new SelectionAdapter() 
		{
			@Override
			public void widgetSelected(SelectionEvent arg0) 
			{
				IWindow window = new PayToCreation();
				window.open();
				tree.removeAll();
				populateList(tree);
				
			}
		});
		addButton.setBounds(185, 227, 75, 25);
		addButton.setText("+");
		
	}
	
	private void populateList(Tree tree)
	{
		IDSet payToIDs = PFSystem.getCurrent().getPayToSystem().getAllIDs();
		
		int totalTrees = 0;
		TreeItem[] trees = new TreeItem[payToIDs.getSize()];
				
		for(int i = 0; i < payToIDs.getSize(); i++)
		{
			final int id = payToIDs.getValue(i);
			final PayTo payto = (PayTo)PFSystem.getCurrent().getPayToSystem().getDataByID(id);
			
			boolean placed = false;
			
			for(int j = 0; j < totalTrees; j++)
			{
				if(trees[j].getText().equals(payto.getPayToName()))
				{
					TreeItem subTreeItem = new TreeItem(trees[j], SWT.NONE);
					subTreeItem.setText(payto.getPayToBranch());
					placed = true;
					break;
				}
			}
			
			if(!placed)
			{
				TreeItem treeItem = new TreeItem(tree, 0);
				treeItem.setText(payto.getPayToName());
				
				trees[totalTrees] = treeItem;
				totalTrees++;
				
				TreeItem subTreeItem = new TreeItem(treeItem, SWT.NONE);
				subTreeItem.setText(payto.getPayToBranch());
			}
		}
	}
}
