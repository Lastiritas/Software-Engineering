package gui;

import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Table;
import org.eclipse.swt.widgets.TableColumn;
import org.eclipse.swt.SWT;
import org.eclipse.swt.widgets.Text;
import org.eclipse.swt.events.ModifyEvent;
import org.eclipse.swt.events.ModifyListener;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;

import domainobjects.IDSet;
import domainobjects.PayTo;
import system.PFSystem;

import org.eclipse.swt.widgets.Composite;

import acceptanceTests.EventLoop;
import acceptanceTests.Register;

public class PayToCreation implements IWindow 
{

	protected Shell shell;
	protected Table existingPayto;
	private Text nameField;
	private Composite composite;
	private Button okayButton;
	
	/**
	 * Open the window.
	 * @wbp.parser.entryPoint
	 */
	public void open() 
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
	}

	/**
	 * Create contents of the window.
	 */
	protected void createContents() 
	{
		shell = new Shell(SWT.SYSTEM_MODAL | SWT.DIALOG_TRIM);
		shell.setSize(444, 327);
		shell.setText("PayTo Creation");
		
		composite = new Composite(shell, SWT.NONE);
		composite.setBounds(0, 0, 444, 305);
		
		Button cancelButton = new Button(composite, SWT.NONE);
		cancelButton.setBounds(10, 260, 63, 35);
		cancelButton.addSelectionListener(new SelectionAdapter() 
		{
			@Override
			public void widgetSelected(SelectionEvent arg0) 
			{
				shell.close();
			}
		});
		cancelButton.setText("Cancel");
		
		okayButton = new Button(composite, SWT.NONE);
		okayButton.setBounds(381, 260, 53, 35);
		okayButton.addSelectionListener(new SelectionAdapter() 
		{
			@Override
			public void widgetSelected(SelectionEvent arg0) 
			{
				String tempName = nameField.getText();
				boolean exist = false;
				IDSet ids;
				
				if(!tempName.isEmpty())
				{
					ids = PFSystem.getCurrent().getPayToSystem().getAllIDs();
					
					for(int i = 0; i < ids.getSize(); i++)
					{
						int id = ids.getValue(i);
						PayTo payto = (PayTo)PFSystem.getCurrent().getPayToSystem().getDataByID(id);
						
						if(payto.getName().equalsIgnoreCase(tempName))
						{
							exist = true;
							break;
						}
					}
					
					if(!exist)
					{
						final int newPaytoID = PFSystem.getCurrent().getPayToSystem().create();
						PFSystem.getCurrent().getPayToSystem().update(newPaytoID, new PayTo(tempName));
					}
				}
				shell.close();
			}
		});
		okayButton.setText("Okay");
		
		nameField = new Text(composite, SWT.BORDER);
		nameField.setBounds(10, 10, 424, 31);
		nameField.addModifyListener(new ModifyListener() {
			public void modifyText(ModifyEvent arg0)
			{
				refreshList();
				
				final String text = nameField.getText();
				if(text.length()>0)
				{
					GUIHelper.filterTable(existingPayto,text);
				}
			}
		});
		nameField.setMessage("Name");
		
		existingPayto = new Table(composite, SWT.BORDER);
		existingPayto.setBounds(10, 47, 424, 207);
		existingPayto.setHeaderVisible(true);
		existingPayto.setLinesVisible(true);
		
		TableColumn tblclmnId = new TableColumn(existingPayto,SWT.NONE);
		tblclmnId.setWidth(100);
		tblclmnId.setText("ID");
		
		TableColumn tblclmnLabel = new TableColumn(existingPayto, SWT.NONE);
		tblclmnLabel.setWidth(100);
		tblclmnLabel.setText("Pay To");
		
		refreshList();
	}
	
	private void refreshList()
	{
		existingPayto.removeAll();
		
		IDSet paytos = PFSystem.getCurrent().getPayToSystem().getAllIDs();
		
		GUIHelper.addPayTosToTable(existingPayto, paytos);
		
	}
}
