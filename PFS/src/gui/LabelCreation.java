package gui;

import org.eclipse.swt.*;
import org.eclipse.swt.widgets.*;
import org.eclipse.swt.events.*;

import acceptanceTests.EventLoop;
import acceptanceTests.Register;
import system.PFSystem;
import domainobjects.*;

public class LabelCreation implements IWindow
{
	protected Shell shell;
	protected Table existingLabel;
	private Text textNewLabel;

	private Composite composite;
	
	private Button btnDone;
	private Button btnCancel;
	
	/**
	 * Open the window.
	 * 
	 */

	public void open()
	{
		Display display =Display.getDefault();
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
	}
	
	
	/**
	 * Create contents of the window.
	 * @wbp.parser.entryPoint
	 */
	protected void createContents()
	{
		shell = new Shell(SWT.SYSTEM_MODAL | SWT.DIALOG_TRIM);
		shell.setSize(316, 328);
		shell.setText("Label Creation");
		
		composite = new Composite(shell, SWT.NONE);
		composite.setBounds(0, 0, 310, 288);
		
		textNewLabel = new Text(composite, SWT.BORDER);
		textNewLabel.setBounds(10, 10, 290, 31);
		textNewLabel.addModifyListener(new ModifyListener() {
			public void modifyText(ModifyEvent arg0)
			{
				refreshList();
				
				final String text = textNewLabel.getText();
				if(text.length()>0)
				{
					GUIHelper.filterTable(existingLabel,text);
				}
			}
		});
		
		btnCancel = new Button(composite, SWT.NONE);
		btnCancel.setBounds(10, 253, 75, 25);
		btnCancel.setText("Cancel");
		btnCancel.addSelectionListener(new SelectionAdapter() 
		{
			@Override
			public void widgetSelected(SelectionEvent e)
			{
				shell.close();
			}
		});
		
		btnDone = new Button(composite, SWT.NONE);
		btnDone.setBounds(225, 253, 75, 25);
		btnDone.setText("Done");
		btnDone.addSelectionListener(new SelectionAdapter() 
		{
			@Override
			public void widgetSelected(SelectionEvent e)
			{
				String temp = textNewLabel.getText();
				boolean exist = false;
				final int labelIds = PFSystem.getCurrent().getLabelSystem().getAllIDs().getSize();
				domainobjects.Label label;
				
				for(int i=0; i<labelIds; i++)
				{
					label = (domainobjects.Label)PFSystem.getCurrent().getLabelSystem().getDataByID(i+1);
				
					if(label.getName().equalsIgnoreCase(temp))
					{
						exist=true;
						break;
					}
				}
				
				if(!exist)
				{
					final int newLabelID = PFSystem.getCurrent().getLabelSystem().create();
					PFSystem.getCurrent().getLabelSystem().update(newLabelID, new domainobjects.Label(temp));
				}
				
				shell.close();
			}
		});
		
		existingLabel = new Table(composite, SWT.BORDER);
		existingLabel.setBounds(10, 47, 290, 200);
		existingLabel.setHeaderVisible(true);
		existingLabel.setLinesVisible(true);
		existingLabel.addSelectionListener(new SelectionAdapter() 
		{
			@Override
			public void widgetSelected(SelectionEvent arg0)
			{
				if(existingLabel.getSelectionCount() == 0)
				{
					return;
				}
				
				textNewLabel.setText(existingLabel.getSelection()[0].getText(1)); 
			}
		});
		
		TableColumn tblclmnId = new TableColumn(existingLabel,SWT.NONE);
		tblclmnId.setWidth(100);
		tblclmnId.setText("ID");
		
		TableColumn tblclmnLabel = new TableColumn(existingLabel, SWT.NONE);
		tblclmnLabel.setWidth(100);
		tblclmnLabel.setText("Label");
		
		refreshList();
	}
	
	private void refreshList()
	{
		existingLabel.removeAll();
		
		IDSet labels = PFSystem.getCurrent().getLabelSystem().getAllIDs();

		GUIHelper.addLabelsToTable(existingLabel, labels);
	}
}
