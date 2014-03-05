package gui;

import java.util.Collection;

import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.swt.SWT;
import org.eclipse.swt.widgets.Event;
import org.eclipse.swt.widgets.Listener;
import org.eclipse.swt.widgets.List;
import org.eclipse.swt.widgets.Label;

import domainobjects.ExpenseFilter;
import domainobjects.IDSet;
import domainobjects.SetOperation;
import system.PFSystem;

import org.eclipse.swt.widgets.Scale;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;

public class MinedView implements IDialog 
{
	private Shell shell;
	private List list;
	private Scale scale;
	private Label scaleAmountLabel;
	
	protected int currID;
	private Composite composite_1;
	private Button btnCancel;
	private Button btnView;
	
	private ExpenseFilter outputFilter = null;
	
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
		
		return null;	// will return a filter
	}

	/**
	 * Create contents of the window.
	 * @wbp.parser.entryPoint
	 */
	protected void createContents() 
	{
		shell = new Shell();
		shell.setSize(565, 406);
		shell.setText("Mined Data View");
		shell.setLayout(null);
		
		list = new List(shell, SWT.BORDER | SWT.V_SCROLL);
		list.setBounds(10, 10, 545, 260);
		
		composite_1 = new Composite(shell, SWT.NONE);
		composite_1.setBounds(10, 289, 545, 44);
		composite_1.setLayout(null);
		
		scale = new Scale(composite_1, SWT.NONE);
		scale.setBounds(10, 10, 472, 24);
		scale.addListener(SWT.Selection, new Listener() {
			public void handleEvent(Event event) {
				scaleAmountLabel.setText(scale.getSelection() + "%");
				refreshList();
			}
		});
		scale.setMinimum(1);
		scale.setSelection(50);
		
		scaleAmountLabel = new Label(composite_1, SWT.NONE);
		scaleAmountLabel.setBounds(488, 10, 47, 15);
		scaleAmountLabel.setAlignment(SWT.RIGHT);
		scaleAmountLabel.setText(scale.getSelection() + "%");
		
		btnCancel = new Button(shell, SWT.NONE);
		btnCancel.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent arg0) {
				closeWithReturn(null);
			}
		});
		btnCancel.setBounds(10, 346, 94, 28);
		btnCancel.setText("Cancel");
		
		btnView = new Button(shell, SWT.NONE);
		btnView.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent arg0) {
				closeWithReturn(generateFilterFromGUI());
			}
		});
		btnView.setBounds(461, 346, 94, 28);
		btnView.setText("View");
		
		refreshList();
	}
	
	private void refreshList()
	{
		final int scaleValue = scale.getSelection();
		final double frequency = (double)scaleValue / 100.0;
		
		updateList(frequency);
	}
	
	private void updateList(double inFrequency)
	{
		Collection<IDSet> frequentSets = PFSystem.getCurrent().getAllFrequentLabelCombinations(inFrequency);
		
		list.removeAll();
		
		for(IDSet set : frequentSets)
		{
			StringBuilder builder = new StringBuilder();
			
			for(int i = 0; i < set.getSize(); i++)
			{
				final int id = set.getValue(i);
				final domainobjects.Label label = (domainobjects.Label)PFSystem.getCurrent().getLabelSystem().getDataByID(id);
				
				assert(label != null);
				
				builder.append(label.getLabelName());
				builder.append(", ");
			}
			
			list.add(builder.toString());
		}
	}
	
	private ExpenseFilter generateFilterFromGUI()
	{
		ExpenseFilter output = new ExpenseFilter();
		
		IDSet set = IDSet.empty();	// need to generate the set values
		
		output.assignLabels(set, SetOperation.INTERSECTION);
		
		return output;
	}
	
	private void closeWithReturn(ExpenseFilter filter)
	{
		assert filter == null || filter != outputFilter;	// do not set the filter to itself
		
		outputFilter = filter;
		shell.close();
	}
}
