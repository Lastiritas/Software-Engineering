package gui;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JTable;
import javax.swing.JScrollPane;
import javax.swing.ScrollPaneConstants;
import javax.swing.JButton;

import java.util.Arrays;
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

import domainobjects.Expense;
import domainobjects.IDSet;
import domainobjects.SimpleDate;
import system.ExpenseSystem;

import java.awt.event.ActionListener;
import java.awt.event.ActionEvent;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

import javax.swing.JList;
import javax.swing.JTree;

public class ExpensePanel extends JPanel
{
	static final int ACCENDING = 1;
	static final int DECENDING = -1;
	static final int NO_ORDER = 0;
	
	private final static String[] COLUMN_NAMES = { "ID", "Date", "Pay To", "Amount", "Decription", "Lables" };
	
	
	private EditExpensePanel editorPanel;
	
	private JScrollPane scrollPane;
	private JTable table;
	private JPanel listPanel;
	private JPanel buttonPanel;
	private JButton deleteButton;
	private JButton addButton;
	private JPanel rightVariablePanel;
	private JPanel leftButtonPanel;
	
	private DefaultTableModel tableModel;
	private JPanel panel;
	private JButton saveButton;
	private JButton duplicateButton;
	private JPanel panel_1;
	private JPanel panel_2;
	private JPanel panel_3;
	private JPanel panel_4;
	
	public ExpensePanel() 
	{
		
		tableModel = new DefaultTableModel() {	
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
		
		table.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseReleased(MouseEvent inEvent)
			{	
				final int rowIndex = table.getSelectedRow();
				final int id = ((Integer)table.getValueAt(rowIndex, 0)).intValue();
				
				final Expense expense = ExpenseSystem.getCurrent().getExpense(id);
				
				editorPanel.setExpense(expense);
			}
		});
		
		table.getTableHeader().setReorderingAllowed(false);
		
		table.getTableHeader().addMouseListener(new MouseAdapter() {
		    @Override
		    public void mouseReleased(MouseEvent e) 
		    {
		    	final int columnIndex = table.columnAtPoint(e.getPoint());
		    	
		    	if(columnIndex == -1)
				{
					return;
				}
		    	
		    	final int currentOrder = getOrderingForColumn(columnIndex);
		    	
		    	final int newOrder = currentOrder == ACCENDING ? DECENDING : ACCENDING;
		    	
		    	sortTableByColumn(columnIndex, newOrder);	
		    }
		});
		listPanel.setLayout(new BorderLayout(0, 0));
		
		scrollPane = new JScrollPane(table);
		listPanel.add(scrollPane);
		scrollPane.setHorizontalScrollBarPolicy(ScrollPaneConstants.HORIZONTAL_SCROLLBAR_NEVER);
		
		buttonPanel = new JPanel();
		add(buttonPanel, BorderLayout.SOUTH);
		buttonPanel.setLayout(new BorderLayout(0, 0));
		
		rightVariablePanel = new JPanel();
		buttonPanel.add(rightVariablePanel, BorderLayout.EAST);
		
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
		
		panel = new JPanel();
		add(panel, BorderLayout.NORTH);
		panel.setLayout(new BorderLayout(0, 0));
		
		panel_2 = new JPanel();
		panel.add(panel_2, BorderLayout.WEST);
		panel_2.setLayout(new BorderLayout(0, 0));
		editorPanel = new EditExpensePanel();
		panel_2.add(editorPanel, BorderLayout.CENTER);
		
		panel_1 = new JPanel();
		panel_2.add(panel_1, BorderLayout.SOUTH);
		panel_1.setLayout(new BorderLayout(0, 0));
		
		panel_3 = new JPanel();
		panel_1.add(panel_3, BorderLayout.EAST);
		
		saveButton = new JButton("Save");
		saveButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				final int rowIndex = table.getSelectedRow();
				final Expense newExpenseValue = editorPanel.getExpense();
				
				final int expenseID = ((Integer)table.getValueAt(rowIndex, 0)).intValue();
				
				ExpenseSystem.getCurrent().updateExpense(expenseID, newExpenseValue);
				setExpenseForRow(rowIndex, expenseID);
			}
		});
		panel_3.add(saveButton);
		
		panel_4 = new JPanel();
		panel_1.add(panel_4, BorderLayout.WEST);
		
		duplicateButton = new JButton("Duplicate");
		duplicateButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				Expense expense = editorPanel.getExpense();
				
				final int id = ExpenseSystem.getCurrent().newExpense();
				ExpenseSystem.getCurrent().updateExpense(id, expense);
				
				addExpenseToTable(id);
			}
		});
		panel_4.add(duplicateButton);
	}
	
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
		int[] selectedRows = table.getSelectedRows();
		Arrays.sort(selectedRows);	// make sure the rows are in accending order
		
		for(int i = selectedRows.length - 1; i >= 0; i--)
		{
			final int rowIndex = selectedRows[i];
			deleteRow(rowIndex);
		}
	}
	
	private void deleteRow(int inRowIndex)
	{
		final int expenseID = ((Integer)tableModel.getValueAt(inRowIndex, 0)).intValue();
		
		if(ExpenseSystem.getCurrent().deleteExpense(expenseID))
		{
			tableModel.removeRow(inRowIndex);			
		}
		else
		{
			JOptionPane.showMessageDialog(null, "Could not delete expense " + expenseID);
		}
	}
	
	private void onNewButtonPressed(ActionEvent e)
	{
		final int newExpenseID = ExpenseSystem.getCurrent().newExpense();
		
		addExpenseToTable(newExpenseID);
	}
	
	private void addExpenseToTable(int inExpenseID)
	{
		tableModel.addRow(new Object[0]);
		
		setExpenseForRow(tableModel.getRowCount() - 1, inExpenseID);
	}
	
	private void setExpenseForRow(int inRowIndex, int inExpenseID)
	{
		final Expense expense = ExpenseSystem.getCurrent().getExpense(inExpenseID);
		
		if(expense == null)
		{
			return;
		}
		
		SimpleDate date = expense.getDate();
		String payTo = "" + expense.getPayTo();
		Money money = new Money(expense.getDollars(), expense.getCents()); 
		String description = expense.GetDescription();

		IDSet labelIDs = expense.getLabels();
		String[] labels = new String[labelIDs.getSize()];
		for(int i = 0; i < labels.length; i++)
		{
			labels[i] = "" + i;
		}
		
		
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
		
		if(labels.length > 0)
		{
			// remove the last ", "
			labelListBuilder.delete(labelListBuilder.length() - 2, labelListBuilder.length() - 1);
		}
		
		String dateString = dateBuilder.toString();
		String payToString = payTo;
		String descriptionString = description;
		String labelsString = labelListBuilder.toString();
		
		tableModel.setValueAt(new Integer(inExpenseID), inRowIndex, 0);
		tableModel.setValueAt(dateString, inRowIndex, 1);
		tableModel.setValueAt(payToString, inRowIndex, 2);
		tableModel.setValueAt(money, inRowIndex, 3);
		tableModel.setValueAt(descriptionString, inRowIndex, 4);
		tableModel.setValueAt(labelsString, inRowIndex, 5);
	}
}
