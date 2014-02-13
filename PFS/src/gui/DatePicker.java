package gui;

import javax.swing.JPanel;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JSpinner;
import javax.swing.BoxLayout;
import javax.swing.SpinnerModel;
import javax.swing.event.ChangeListener;

import java.awt.BorderLayout;
import java.util.Date;
import javax.swing.event.ChangeEvent;
import java.awt.FlowLayout;

public class DatePicker extends JPanel
{	
	private JPanel yearSubPanel;
	private JLabel yearLabel;
	private JSpinner yearSpinner;
	
	private JPanel monthSubPanel;
	private JLabel monthLabel;
	private JSpinner monthSpinner;
	
	private JPanel daySubPanel;
	private JLabel dayLabel;
	private JSpinner daySpinner;
	
	public DatePicker(Date inDate) 
	{
		setLayout(new BoxLayout(this, BoxLayout.X_AXIS));
		
		yearSubPanel = new JPanel();
		add(yearSubPanel);
		
		yearLabel = new JLabel("Year");
		yearSubPanel.add(yearLabel);
		
		yearSpinner = new JSpinner();
		yearSpinner.addChangeListener(new ChangeListener() {
			public void stateChanged(ChangeEvent inEvent) {
				onStateChanged(inEvent);
			}
		});
		yearSubPanel.add(yearSpinner);
		
		monthSubPanel = new JPanel();
		add(monthSubPanel);
		
		monthLabel = new JLabel("Month");
		monthSubPanel.add(monthLabel);
		
		monthSpinner = new JSpinner();
		monthSubPanel.add(monthSpinner);
		
		daySubPanel = new JPanel();
		add(daySubPanel);
		
		dayLabel = new JLabel("Day");
		daySubPanel.add(dayLabel);
		
		daySpinner = new JSpinner();
		daySubPanel.add(daySpinner);
		
		setDate(inDate);	// set the date to now
	}
	
	private void onStateChanged(ChangeEvent inEvent)
	{
		// validate the date
	}
	
	private void setDate(Date inDate)
	{
		yearSpinner.setValue(inDate.getYear());
		monthSpinner.setValue(inDate.getMonth());
		daySpinner.setValue(inDate.getDay());
	}
	
	public Date getDate()
	{
		Date date = new Date();
		date.setYear(((Integer)yearSpinner.getValue()).intValue());
		date.setMonth(((Integer)monthSpinner.getValue()).intValue());
		date.setDate(((Integer)daySpinner.getValue()).intValue());
	
		return date;
	}
}
