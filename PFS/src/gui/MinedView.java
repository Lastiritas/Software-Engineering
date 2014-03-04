package gui;

import java.util.Collection;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.swt.SWT;
import org.eclipse.swt.widgets.Event;
import org.eclipse.swt.widgets.Listener;
import org.eclipse.swt.widgets.List;
import org.eclipse.swt.widgets.Label;
import domainobjects.IDSet;
import system.PFSystem;
import org.eclipse.swt.widgets.Scale;
import org.eclipse.swt.widgets.Composite;

public class MinedView implements IDialog 
{
	private Shell shell;
	private List tree;
	private Scale scale;
	private Label scaleAmountLabel;
	
	protected int currID;
	private Composite composite_1;
	
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
		shell.setSize(565, 365);
		shell.setText("Mined Data View");
		shell.setLayout(null);
		
		tree = new List(shell, SWT.BORDER | SWT.V_SCROLL);
		tree.setBounds(10, 10, 545, 260);
		
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
		
		tree.removeAll();
		
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
			
			tree.add(builder.toString());
		}
	}
}
