import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JTable;
import javax.swing.JScrollPane;
import javax.swing.ScrollPaneConstants;
import javax.swing.JButton;

import java.util.Date;
import java.awt.BorderLayout;
import java.awt.Component;

import javax.swing.Box;

import java.awt.FlowLayout;

import javax.swing.border.BevelBorder;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.TableModel;
import javax.swing.text.TabExpander;

import net.miginfocom.swing.MigLayout;

import javax.swing.SwingConstants;
import javax.swing.ListSelectionModel;

import java.awt.event.ActionListener;
import java.awt.event.ActionEvent;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

public class ExpensePanel extends JPanel
{
	private final static String[] COLUMN_NAMES = { "ID", "Date", "Pay To", "Amount", "Decription", "Lables" };
	
	private JScrollPane scrollPane;
	private JTable table;
	private JPanel listPanel;
	private JPanel buttonPanel;
	private JButton deleteButton;
	private JButton editButton;
	private JButton addButton;
	private JPanel rightVariablePanel;
	private JPanel leftButtonPanel;
	
	private DefaultTableModel tableModel;
	
	public ExpensePanel() 
	{
		tableModel = new DefaultTableModel()
		{	
			// make our table read only
			public boolean isCellEditable(int row, int column) { return false; }
		};
		
		for(int i = 0; i < COLUMN_NAMES.length; i++)
		{
			tableModel.addColumn(COLUMN_NAMES[i]);
		}
		
		setLayout(new BorderLayout(0, 0));
		
		listPanel = new JPanel();
		add(listPanel);
		
		table = new JTable(tableModel);
		table.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
		table.getTableHeader().setReorderingAllowed(false);
		
		table.getTableHeader().addMouseListener(new MouseAdapter() {
		    @Override
		    public void mouseClicked(MouseEvent e) 
		    {
		    	final int columnIndex = table.columnAtPoint(e.getPoint());
		    	
		    	final int currentOrder = getOrderingForColumn(columnIndex);
		    	
		    	final int newOrder = currentOrder == ACCENDING ? DECENDING : ACCENDING;
		    	
		    	sortTableByColumn(columnIndex, newOrder);	
		    }
		});
		
		scrollPane = new JScrollPane(table);
		listPanel.add(scrollPane);
		scrollPane.setHorizontalScrollBarPolicy(ScrollPaneConstants.HORIZONTAL_SCROLLBAR_NEVER);
		
		buttonPanel = new JPanel();
		add(buttonPanel, BorderLayout.SOUTH);
		buttonPanel.setLayout(new BorderLayout(0, 0));
		
		rightVariablePanel = new JPanel();
		buttonPanel.add(rightVariablePanel, BorderLayout.EAST);
		
		editButton = new JButton("Edit");
		editButton.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) { onEditButtonPressed(e); }
		});
		rightVariablePanel.add(editButton);
		editButton.setHorizontalAlignment(SwingConstants.RIGHT);
		
		addButton = new JButton("+");
		addButton.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) { onNewButtonPressed(e); }
		});
		rightVariablePanel.add(addButton);
		
		leftButtonPanel = new JPanel();
		buttonPanel.add(leftButtonPanel, BorderLayout.WEST);
		
		deleteButton = new JButton("-");
		deleteButton.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) { onDeleteButtonPressed(e); }
		});
		leftButtonPanel.add(deleteButton);
	}
	
	static final int ACCENDING = 1;
	static final int DECENDING = -1;
	static final int NO_ORDER = 0;
	
	private int getOrderingForColumn(int inColumnIndex)
	{	
		boolean isAccending = true;
		boolean isDecending = true;
		
		for(int rowIndex = 1; rowIndex < tableModel.getRowCount(); rowIndex++)
		{
			Comparable a = (Comparable) tableModel.getValueAt(rowIndex - 1, inColumnIndex);
			Comparable b = (Comparable) tableModel.getValueAt(rowIndex, inColumnIndex);
			
			if(a.compareTo(b) > 0)
			{
				isDecending = false;
				break;
			}
		}
		
		for(int rowIndex = 1; rowIndex < tableModel.getRowCount(); rowIndex++)
		{
			Comparable a = (Comparable) tableModel.getValueAt(rowIndex - 1, inColumnIndex);
			Comparable b = (Comparable) tableModel.getValueAt(rowIndex, inColumnIndex);
			
			if(a.compareTo(b) < 0)
			{
				isAccending = false;
				break;
			}
		}
		
		if(isAccending && !isDecending)
		{
			return ACCENDING;
		}
		else if(isDecending && !isAccending)
		{
			return DECENDING;
		}
		else
		{
			return NO_ORDER;
		}
	}
	
	private void sortTableByColumn(int inColumnIndex, int inOrder)
	{
		assert(inColumnIndex >= 0 && inColumnIndex < tableModel.getColumnCount());
		assert(inOrder == ACCENDING || inOrder == DECENDING);
		
		for(int j = 0; j < tableModel.getRowCount(); j++)
		{
			for(int i = 1; i < tableModel.getRowCount() - j; i++)
	    	{
				final int first = i - 1;
				final int second = i;
				
	    		final Comparable rowA = (Comparable) tableModel.getValueAt(first, inColumnIndex);
				final Comparable rowB = (Comparable) tableModel.getValueAt(second, inColumnIndex);
	    		
				if(rowA.compareTo(rowB) != inOrder)
				{
					// swap each column value between the rows
					for(int k = 0; k < tableModel.getColumnCount(); k++)
					{
						Object a = tableModel.getValueAt(first, k);
						Object b = tableModel.getValueAt(second, k);
						
						tableModel.setValueAt(b, first, k);
						tableModel.setValueAt(a, second, k);
					}
				}
	    	}
		}
	}
	
	private void onDeleteButtonPressed(ActionEvent e)
	{
		JOptionPane.showMessageDialog(null, "Delete Selected Entries");
	}
	
	private void onEditButtonPressed(ActionEvent e)
	{
		JOptionPane.showMessageDialog(null, "Edit Selected Entries");
	}
	
	private void onNewButtonPressed(ActionEvent e)
	{
		addExpenseToTable(tableModel.getRowCount());
	}
	
	private void addExpenseToTable(int inExpenseID)
	{
		Date date = new Date();
		String payTo = "A";
		Money money = new Money(100, 50); 
		String description = "This is a bad description";
		String[] labels = {"Food", "School", "Work", "Play"};
		
		
		StringBuilder dateBuilder = new StringBuilder();
		
		dateBuilder.append(date.getMonth());
		dateBuilder.append("/");
		dateBuilder.append(date.getDay());
		dateBuilder.append("/");
		dateBuilder.append(date.getYear());	
		
		StringBuilder labelListBuilder = new StringBuilder();
		for(int i = 0; i < labels.length; i++)
		{
			labelListBuilder.append(labels[i]);
			labelListBuilder.append(", ");
		}
		
		// remove the last ", "
		labelListBuilder.delete(labelListBuilder.length() - 2, labelListBuilder.length() - 1);
		
		String dateString = dateBuilder.toString();
		String payToString = payTo;
		String descriptionString = description;
		String labelsString = labelListBuilder.toString();
		
		tableModel.addRow(new Object[] { new Integer(inExpenseID), dateString, payToString, money, descriptionString, labelsString });		
	}
}
