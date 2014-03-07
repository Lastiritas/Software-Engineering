package gui;

import java.util.ArrayList;

import org.eclipse.swt.widgets.*;
import org.eclipse.swt.*;
import org.eclipse.swt.events.*;

import system.LabelManagement;
import system.PFSystem;
import util.StringMatch;
import domainobjects.*;

public class LabelSelection implements IDialog
{
	protected Shell shell;
	private Text textSearchLabel;
	private Text textSearchPickLabel;
	protected final int LIST_OPTIONS = SWT.MULTI | SWT.BORDER |SWT.V_SCROLL;
	
	private Table choiceTable;
	private Table pickedTable;
	
	private IDSet startingSet = IDSet.empty();
	private ArrayList<Integer> selectedLabels = new ArrayList<Integer>();
	
	public void setStartingSet(IDSet inSet)
	{
		startingSet = inSet;
		
		final int setSize = inSet.getSize();
		
		for(int i = 0; i < setSize; i++)
		{
			selectedLabels.add(new Integer(inSet.getValue(i)));
		}
	}
	
	/**
	 * @wbp.parser.entryPoint
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
		
		final int[] array = new int[selectedLabels.size()];
		for(int i = 0; i < array.length; i++)
		{
			array[i] = selectedLabels.get(i).intValue();
		}
		
		return IDSet.createFromArray(array);
	}
	
	/**
	 * Create contents of the window.
	 */
	protected void createContents() 
	{
		shell = new Shell(SWT.APPLICATION_MODAL);
		shell.setSize(590, 465);
		shell.setText("Label Management");
		
		textSearchLabel = new Text(shell, SWT.BORDER);
		textSearchLabel.addModifyListener(new ModifyListener() {
			public void modifyText(ModifyEvent arg0) {
				refreshList();
				
				final String text = textSearchLabel.getText();
				if(text.length() > 0)
				{
					filterTable(choiceTable, text);
				}
			}
		});
		textSearchLabel.setBounds(20, 20, 200, 22);
		
		textSearchPickLabel = new Text(shell, SWT.BORDER);
		textSearchPickLabel.addModifyListener(new ModifyListener() {
			public void modifyText(ModifyEvent arg0) {
				refreshPickList();
				
				final String text = textSearchPickLabel.getText();
				if(text.length() > 0)
				{
					filterTable(pickedTable, text);
				}
			}
		});
		textSearchPickLabel.setBounds(355, 20, 200, 22);
		
		Button btnAdd = new Button(shell, SWT.PUSH);
		btnAdd.setText(">>>");
		btnAdd.setBounds(250, 100, 75, 25);
		
		Button btnRemove = new Button(shell, SWT.PUSH);
		btnRemove.setText("<<<");
		btnRemove.setBounds(250, 130, 75, 25);
		
		Button btnNew = new Button(shell, SWT.PUSH);
		btnNew.setText("Create Label");
		btnNew.setBounds(65, 316, 110, 25);
		
		Button btnCancel = new Button(shell, SWT.PUSH);
		btnCancel.setText("Cancel");
		btnCancel.setBounds(20, 392, 75, 25);
		
		Button btnDone = new Button(shell, SWT.PUSH);
		btnDone.setText("Done");
		btnDone.setBounds(480,392, 75, 25);
		
		choiceTable = new Table(shell, SWT.BORDER | SWT.FULL_SELECTION);
		choiceTable.setBounds(20, 48, 200, 262);
		choiceTable.setHeaderVisible(true);
		choiceTable.setLinesVisible(true);
		
		TableColumn tblclmnId = new TableColumn(choiceTable, SWT.NONE);
		tblclmnId.setWidth(100);
		tblclmnId.setText("ID");
		
		TableColumn tblclmnLabel = new TableColumn(choiceTable, SWT.NONE);
		tblclmnLabel.setWidth(100);
		tblclmnLabel.setText("Label");
		
		pickedTable = new Table(shell, SWT.BORDER | SWT.FULL_SELECTION);
		pickedTable.setLinesVisible(true);
		pickedTable.setHeaderVisible(true);
		pickedTable.setBounds(355, 48, 200, 262);
		
		TableColumn tableColumn = new TableColumn(pickedTable, SWT.NONE);
		tableColumn.setWidth(100);
		tableColumn.setText("ID");
		
		TableColumn tableColumn_1 = new TableColumn(pickedTable, SWT.NONE);
		tableColumn_1.setWidth(100);
		tableColumn_1.setText("Label");
						
		refreshList();
		refreshPickList();
		
		btnAdd.addSelectionListener(new SelectionAdapter() 
		{
			@Override
			public void widgetSelected(SelectionEvent e) 
			{
				final int selectedIndex = choiceTable.getSelectionIndex();
				
				if(selectedIndex >= 0)
				{
					final int id = Integer.parseInt(choiceTable.getItem(selectedIndex).getText(0));
					selectedLabels.add(new Integer(id));
					
					moveLabelFromTableToTable(choiceTable, pickedTable, selectedIndex);
				}
			}
		});
		
		btnRemove.addSelectionListener(new SelectionAdapter() 
		{
			@Override
			public void widgetSelected(SelectionEvent e) 
			{
				final int selectedIndex = pickedTable.getSelectionIndex();
				
				if(selectedIndex >= 0)
				{
					final int id = Integer.parseInt(pickedTable.getItem(selectedIndex).getText(0));
					selectedLabels.remove(new Integer(id));
					
					moveLabelFromTableToTable(pickedTable, choiceTable, selectedIndex);
				}
			}
		});
		
		btnNew.addSelectionListener(new SelectionAdapter() 
		{
			@Override
			public void widgetSelected(SelectionEvent e) 
			{
				LabelCreation createLabel = new LabelCreation();
				String labelName = createLabel.open();
				
				if(labelName != null)
				{
					final LabelManagement labelManager = PFSystem.getCurrent().getLabelSystem();
					
					final int newID = labelManager.create();
					
					domainobjects.Label newLabel = new domainobjects.Label(labelName);
					labelManager.update(newID, newLabel);
					
					refreshList();
				}
			}
		});
		
		btnCancel.addSelectionListener( new SelectionAdapter() 
		{
			public void widgetSelected(SelectionEvent e) 
			{
				selectedLabels.clear();
				
				final int totalLabels = startingSet.getSize();
				for(int i = 0; i < totalLabels; i++)
				{
					final int id = startingSet.getValue(i);
					selectedLabels.add(new Integer(id));
				}
				
				shell.close();
			}
		});
		
		btnDone.addSelectionListener(new SelectionAdapter() 
		{
			@Override
			public void widgetSelected(SelectionEvent e) 
			{
				shell.close();
			}
		});
	}
	
	private static void filterTable(Table inTable, String inFilterText)
	{					
		assert(inTable != null);
		assert(inFilterText != null);
		assert(inFilterText.length() > 0);
		
		final int originalTableSize = inTable.getItemCount();
			
		for(int i = originalTableSize - 1; i >= 0; i--)
		{
			final String label = inTable.getItem(i).getText(1);
					
			if(!StringMatch.match(label, inFilterText))
			{
				inTable.remove(i);	
			}
		}
	}
	
	private void refreshList()
	{
		choiceTable.removeAll();
		
		IDSet labels = PFSystem.getCurrent().getLabelSystem().getAllIDs();
		
		for(int i = 0; i < labels.getSize(); i++)
		{	
			final int id = labels.getValue(i);
			
			if(!selectedLabels.contains(new Integer(id)))
			{
				addLabelToTable(choiceTable, id);
			}
		}
	}
	
	private void refreshPickList()
	{
		pickedTable.removeAll();
		
		for(int i=0; i <selectedLabels.size(); i++)
		{
			final int id = selectedLabels.get(i).intValue();
			addLabelToTable(pickedTable, id);
		}
	}
	
	private static void moveLabelFromTableToTable(Table inFrom, Table inTo, int inIndex)
	{
		TableItem item = inFrom.getItem(inIndex);
		Integer id = Integer.parseInt(item.getText(0));
	
		inFrom.remove(inIndex);
		
		addLabelToTable(inTo, id.intValue());
	}
	
	private static void addLabelToTable(Table inTable, int inId)
	{
		domainobjects.Label label = (domainobjects.Label)PFSystem.getCurrent().getLabelSystem().getDataByID(inId);
		
		TableItem tableItem= new TableItem(inTable, SWT.NONE);
		tableItem.setText(0, "" + inId);
		tableItem.setText(1, label.getLabelName());
	}
}
