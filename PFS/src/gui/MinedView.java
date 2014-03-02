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

public class MinedView implements IWindow 
{
	private Shell shell;
	private List tree;
	private Scale scale;
	private Label scaleAmountLabel;
	
	protected int currID;
	
	/**
	 * Open the window.
	 */
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

	/**
	 * Create contents of the window.
	 * @wbp.parser.entryPoint
	 */
	protected void createContents() 
	{
		shell = new Shell();
		shell.setSize(565, 365);
		shell.setText("Mined Data View");
		
		tree = new List(shell, SWT.BORDER);
		tree.setBounds(10, 10, 545, 289);
		
		scale = new Scale(shell, SWT.NONE);
		scale.addListener(SWT.Selection, new Listener() {
			public void handleEvent(Event event) {
				scaleAmountLabel.setText(scale.getSelection() + "%");
				refreshList();
			}
		});
		scale.setSelection(50);
		scale.setBounds(10, 305, 497, 28);
		
		scaleAmountLabel = new Label(shell, SWT.NONE);
		scaleAmountLabel.setAlignment(SWT.RIGHT);
		scaleAmountLabel.setBounds(513, 305, 42, 28);
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
			tree.add(set.toString());
		}
	}
}
