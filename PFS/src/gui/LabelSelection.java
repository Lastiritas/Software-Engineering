package gui;

import java.util.ArrayList;

import org.eclipse.swt.widgets.*;
import org.eclipse.swt.*;
import org.eclipse.swt.events.*;

import acceptanceTests.EventLoop;
import acceptanceTests.Register;
import system.Manager;
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
	private boolean madeSelection = false;
	
	public void setStartingSet(IDSet inSet)
	{
		startingSet = inSet;
		
		final int setSize = inSet.getSize();
		
		for(int i = 0; i < setSize; i++)
		{
			selectedLabels.add(new Integer(inSet.getValue(i)));
		}
	}
	
	public LabelSelection()
	{
		Register.newWindow(this);
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
		
		if(EventLoop.isEnabled())
		{
			while (!shell.isDisposed()) 
			{
				if (!display.readAndDispatch()) 
				{
					display.sleep();
				}
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
		shell = new Shell(SWT.SYSTEM_MODAL | SWT.DIALOG_TRIM);
		shell.setSize(590, 465);
		shell.setText("Label Management");
		shell.addListener(SWT.Close, new Listener()
		{
			@Override
			public void handleEvent(Event event)
			{
				if(!madeSelection)
				{
					selectedLabels.clear();
					
					final int totalLabels = startingSet.getSize();
					for(int i = 0; i < totalLabels; i++)
					{
						final int id = startingSet.getValue(i);
						selectedLabels.add(new Integer(id));
					}
				}
			}
		});
		
		Composite composite = new Composite(shell, SWT.NONE);
		composite.setBounds(0, 0, 584, 425);
		
		textSearchLabel = new Text(composite, SWT.BORDER);
		textSearchLabel.setBounds(10, 10, 200, 31);
		
		textSearchPickLabel = new Text(composite, SWT.BORDER);
		textSearchPickLabel.setBounds(374, 10, 200, 31);
		
		Button btnAdd = new Button(composite, SWT.PUSH);
		btnAdd.setBounds(320, 155, 48, 35);
		btnAdd.setText(">>>");
		
		Button btnRemove = new Button(composite, SWT.PUSH);
		btnRemove.setBounds(216, 155, 48, 35);
		btnRemove.setText("<<<");
		
		Button btnNew = new Button(composite, SWT.PUSH);
		btnNew.setBounds(10, 344, 108, 35);
		btnNew.setText("Create Label");
		
		Button btnCancel = new Button(composite, SWT.PUSH);
		btnCancel.setBounds(10, 385, 63, 35);
		btnCancel.setText("Cancel");
		
		Button btnDone = new Button(composite, SWT.PUSH);
		btnDone.setBounds(519, 380, 55, 35);
		btnDone.setText("Done");
		
		choiceTable = new Table(composite, SWT.BORDER | SWT.FULL_SELECTION);
		choiceTable.setBounds(10, 47, 200, 291);
		choiceTable.setHeaderVisible(true);
		choiceTable.setLinesVisible(true);
		
		TableColumn tblclmnId = new TableColumn(choiceTable, SWT.NONE);
		tblclmnId.setWidth(100);
		tblclmnId.setText("ID");
		
		TableColumn tblclmnLabel = new TableColumn(choiceTable, SWT.NONE);
		tblclmnLabel.setWidth(100);
		tblclmnLabel.setText("Label");
		
		pickedTable = new Table(composite, SWT.BORDER | SWT.FULL_SELECTION);
		pickedTable.setBounds(374, 47, 200, 291);
		pickedTable.setLinesVisible(true);
		pickedTable.setHeaderVisible(true);
		
		TableColumn tableColumn = new TableColumn(pickedTable, SWT.NONE);
		tableColumn.setWidth(100);
		tableColumn.setText("ID");
		
		TableColumn tableColumn_1 = new TableColumn(pickedTable, SWT.NONE);
		tableColumn_1.setWidth(100);
		tableColumn_1.setText("Label");
		
		btnDone.addSelectionListener(new SelectionAdapter() 
		{
			@Override
			public void widgetSelected(SelectionEvent e) 
			{
				madeSelection = true;
				shell.close();
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
		
		btnNew.addSelectionListener(new SelectionAdapter() 
		{
			@Override
			public void widgetSelected(SelectionEvent e) 
			{
				LabelCreation createLabel = new LabelCreation();
				String labelName = createLabel.open();
				
				if(labelName != null)
				{
					final Manager labelManager = PFSystem.getCurrent().getLabelSystem();
					
					final int newID = labelManager.create();
					
					domainobjects.Label newLabel = new domainobjects.Label(labelName);
					labelManager.update(newID, newLabel);
					
					refreshList();
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
						
		refreshList();
		refreshPickList();
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
				GUIHelper.addLabelToTable(choiceTable, id);
			}
		}
	}
	
	private void refreshPickList()
	{
		pickedTable.removeAll();
		
		for(int i=0; i <selectedLabels.size(); i++)
		{
			final int id = selectedLabels.get(i).intValue();
			GUIHelper.addLabelToTable(pickedTable, id);
		}
	}
	
	private static void moveLabelFromTableToTable(Table inFrom, Table inTo, int inIndex)
	{
		TableItem item = inFrom.getItem(inIndex);
		Integer id = Integer.parseInt(item.getText(0));
	
		inFrom.remove(inIndex);
		
		GUIHelper.addLabelToTable(inTo, id.intValue());
	}	
}
