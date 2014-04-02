package gui;

import java.util.Collection;

import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.swt.SWT;
import org.eclipse.swt.widgets.Event;
import org.eclipse.swt.widgets.Listener;
import org.eclipse.swt.widgets.Menu;
import org.eclipse.swt.widgets.MenuItem;
import org.eclipse.swt.widgets.Table;
import org.eclipse.swt.widgets.TableColumn;
import org.eclipse.swt.widgets.TableItem;

import domainobjects.ExpenseFilter;
import domainobjects.IDSet;
import domainobjects.INamed;
import system.GroupedCollection;
import system.IDataReader;
import system.PFSystem;

import org.eclipse.swt.widgets.Scale;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.graphics.Point;
import org.eclipse.swt.widgets.Group;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;

import acceptanceTests.EventLoop;
import acceptanceTests.Register;

public class PerDayWindow implements IDialog 
{
	private Shell shell;
	private Menu menu;
	private Menu menu_1;
	private MenuItem mntmNewSubmenu;
	private MenuItem mntmExit;
	
	private Table table;
	private Scale thresholdSlider;
		
	private ExpenseFilter outputFilter = new ExpenseFilter();
	private Composite composite;
	private TableColumn tblclmnSunday;
	private TableColumn tblclmnMonday;
	private TableColumn tblclmnTuesday;
	private TableColumn tblclmnWednesday;
	private TableColumn tblclmnThursday;
	private TableColumn tblclmnFriday;
	private TableColumn tblclmnSaturday;
	
	private Button lablesPerDayRadio;
	private Button locationsPerDayRadio;
	
	/**
	 * Open the window.
	 */
	public Object open() 
	{
		Display display = Display.getDefault();
		Register.newWindow(this);
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
		
		return outputFilter;
	}

	/**
	 * Create contents of the window.
	 * @wbp.parser.entryPoint
	 */
	protected void createContents() 
	{
		shell = new Shell(SWT.SYSTEM_MODAL | SWT.DIALOG_TRIM);
		shell.setSize(811, 577);
		shell.setMinimumSize(new Point(800, 570));
		shell.setText("Day View");
		shell.setLayout(null);
		
		menu = new Menu(shell, SWT.BAR);
		menu.setLocation(new Point(0, 0));
		shell.setMenuBar(menu);
		
		mntmNewSubmenu = new MenuItem(menu, SWT.CASCADE);
		mntmNewSubmenu.setText("File");
		
		menu_1 = new Menu(mntmNewSubmenu);
		mntmNewSubmenu.setMenu(menu_1);
		
		mntmExit = new MenuItem(menu_1, SWT.NONE);
		mntmExit.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent arg0) {
				shell.close();
			}
		});
		mntmExit.setText("Exit");
		
		composite = new Composite(shell, SWT.NONE);
		composite.setBounds(0, 0, 805, 528);
		
		Composite composite_1 = new Composite(composite, SWT.NONE);
		composite_1.setBounds(10, 449, 488, 69);
		composite_1.setLayout(null);
		
		thresholdSlider = new Scale(composite_1, SWT.NONE);
		thresholdSlider.setBounds(10, 10, 468, 49);
		thresholdSlider.addListener(SWT.Selection, new Listener() {
			public void handleEvent(Event event) {
				refreshList();
			}
		});
		thresholdSlider.setMinimum(1);
		thresholdSlider.setSelection(50);
		
		table = new Table(composite, SWT.BORDER | SWT.FULL_SELECTION);
		table.setBounds(10, 10, 785, 433);
		table.setHeaderVisible(true);
		table.setLinesVisible(true);
		
		tblclmnSunday = new TableColumn(table, SWT.NONE);
		tblclmnSunday.setWidth(100);
		tblclmnSunday.setText("Sunday");
		
		tblclmnMonday = new TableColumn(table, SWT.NONE);
		tblclmnMonday.setWidth(100);
		tblclmnMonday.setText("Monday");
		
		tblclmnTuesday = new TableColumn(table, SWT.NONE);
		tblclmnTuesday.setWidth(100);
		tblclmnTuesday.setText("Tuesday");
		
		tblclmnWednesday = new TableColumn(table, SWT.NONE);
		tblclmnWednesday.setWidth(100);
		tblclmnWednesday.setText("Wednesday");
		
		tblclmnThursday = new TableColumn(table, SWT.NONE);
		tblclmnThursday.setWidth(100);
		tblclmnThursday.setText("Thursday");
		
		tblclmnFriday = new TableColumn(table, SWT.NONE);
		tblclmnFriday.setWidth(100);
		tblclmnFriday.setText("Friday");
		
		tblclmnSaturday = new TableColumn(table, SWT.NONE);
		tblclmnSaturday.setWidth(100);
		tblclmnSaturday.setText("Saturday");
		
		Group grpShow = new Group(composite, SWT.NONE);
		grpShow.setText("Show");
		grpShow.setBounds(504, 449, 291, 69);
		
		lablesPerDayRadio = new Button(grpShow, SWT.RADIO);
		lablesPerDayRadio.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent arg0) {
				refreshList();
			}
		});
		lablesPerDayRadio.setSelection(true);
		lablesPerDayRadio.setBounds(10, 27, 100, 18);
		lablesPerDayRadio.setText("Labels Per Day");
		
		locationsPerDayRadio = new Button(grpShow, SWT.RADIO);
		locationsPerDayRadio.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent arg0) {
				refreshList();
			}
		});
		locationsPerDayRadio.setText("Locations Per Day");
		locationsPerDayRadio.setBounds(164, 27, 117, 18);
		
		refreshList();
	}
	
	private void refreshList()
	{
		final int scaleValue = thresholdSlider.getSelection();
		final double frequency = (double)scaleValue / 100.0;
		
		updateList(frequency);
	}
	
	private void updateList(double inFrequency)
	{
		table.clearAll();	// use clear to avoid needing to create all new items
		
		PFSystem system = PFSystem.getCurrent();
		
		GroupedCollection[] collections = null;
		IDataReader dataSource = null;
		
		if(locationsPerDayRadio.getSelection())
		{
			collections = system.getFrequentPlacesForDayOfWeek(inFrequency);
			dataSource = system.getPayToSystem();
		}
		else
		{
			collections = system.getFrequentLabelsForDayOfWeek(inFrequency);
			dataSource = system.getLabelSystem();
		}
		
		int requiredRows = 0;
		
		// generate all the needed rows
		for(GroupedCollection collection : collections)
		{
			Collection<IDSet> sets = collection.getAll();
			
			int myRequirement = 0;
			
			for(IDSet set : sets)
			{
				myRequirement += set.getSize();
			}
			
			requiredRows = Math.max(myRequirement, requiredRows);
		}
				
		while(table.getItemCount() < requiredRows)
		{
			new TableItem(table, SWT.NONE);
		}
		
		while(table.getItemCount() > requiredRows)
		{
			table.remove(0);
		}
		
		// fill in the table (all the needed items should have already been created)
		for(int collectionIndex = 0; collectionIndex < collections.length; collectionIndex++)
		{			
			Collection<IDSet> sets = collections[collectionIndex].getAll();
			
			assert sets.size() == 1;
			
			for(IDSet set : sets)
			{
				for(int itemIndex = 0; itemIndex < set.getSize(); itemIndex++)
				{
					int id = set.getValue(itemIndex);
					INamed item = (INamed)dataSource.getDataByID(id);
				
					assert(item != null);
				
					// the collection index maps to the column index
					table.getItem(itemIndex).setText(collectionIndex, item.getName());				
				}
			}
		}
	}
}
