package gui;

import javax.swing.JPanel;
import javax.swing.JLabel;
import javax.swing.BoxLayout;
import javax.swing.event.ChangeEvent;
import javax.swing.JComboBox;
import javax.swing.DefaultComboBoxModel;
import domainobjects.SimpleDate;

public class DatePicker extends JPanel
{	
	private JPanel yearSubPanel;
	private JLabel yearLabel;
	private JComboBox yearSpinner;
	
	private JPanel monthSubPanel;
	private JLabel monthLabel;
	private JComboBox monthSpinner;
	
	private JPanel daySubPanel;
	private JLabel dayLabel;
	private JComboBox daySpinner;
	
	public DatePicker(SimpleDate inDate) 
	{
		setLayout(new BoxLayout(this, BoxLayout.X_AXIS));
		
		yearSubPanel = new JPanel();
		add(yearSubPanel);
		
		yearLabel = new JLabel("Year");
		yearSubPanel.add(yearLabel);
		
		yearSpinner = new JComboBox();
		yearSpinner.setModel(new DefaultComboBoxModel(new String[] {"2013", "2014", "2015"}));
		yearSubPanel.add(yearSpinner);
		
		monthSubPanel = new JPanel();
		add(monthSubPanel);
		
		monthLabel = new JLabel("Month");
		monthSubPanel.add(monthLabel);
		
		monthSpinner = new JComboBox();
		monthSpinner.setModel(new DefaultComboBoxModel(SimpleDate.Month.values()));
		monthSpinner.setSelectedIndex(0);
		monthSubPanel.add(monthSpinner);
		
		daySubPanel = new JPanel();
		add(daySubPanel);
		
		dayLabel = new JLabel("Day");
		daySubPanel.add(dayLabel);
		
		daySpinner = new JComboBox();
		daySpinner.setModel(new DefaultComboBoxModel(new String[] {"1", "2", "3", "4", "5", "6", "7", "8", "9", "10", "11", "12", "13", "14", "15", "16", "17", "18", "19", "20", "22", "23", "24", "25", "26", "27", "28", "29", "30", "31"}));
		daySpinner.setSelectedIndex(0);
		daySubPanel.add(daySpinner);
		
		setDate(inDate);	// set the date to now
	}
	
	private void onStateChanged(ChangeEvent inEvent)
	{
		// validate the date
		setDate(getDate());
	}
	
	public void setDate(SimpleDate inDate)
	{
		yearSpinner.setSelectedIndex(inDate.getYear() - 2013);
		monthSpinner.setSelectedIndex(inDate.getMonth());
		daySpinner.setSelectedIndex(inDate.getDay());
	}
	
	public SimpleDate getDate()
	{
		SimpleDate date = SimpleDate.Now();
		date.setYear(yearSpinner.getSelectedIndex() + 2013);
		date.setMonth(monthSpinner.getSelectedIndex());
		date.setDay(daySpinner.getSelectedIndex());
	
		return date;
	}
	

}
