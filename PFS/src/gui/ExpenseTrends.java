package gui;

import org.eclipse.swt.SWT;
import org.eclipse.swt.layout.FillLayout;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.TabFolder;
import org.eclipse.swt.widgets.TabItem;
import org.eclipse.swt.widgets.Text;
import org.swtchart.Chart;
import org.swtchart.IAxis;
import org.swtchart.IBarSeries;
import org.swtchart.ISeries.SeriesType;

import domainobjects.IDSet;
import util.XAxis;

public class ExpenseTrends 
{
	private IDSet expenseIDs;
	
	private Shell shell;
	private Composite chartOne;
	private Composite chartTwo;
	private ChartHelper chartHelper;
	//private static final double[] ySeries = {0.2, 1.1, 1.9, 2.3, 1.8, 1.5, 1.8, 2.6, 2.9, 3.2};
	private final double[] ySeriesTwo = {1.5, 1.8, 2.6, 2.9}; 
	
	//By payment method, By months and By year, by location, by labels, based on day
	
	public void setExpenseIDs(IDSet ids)
	{
		expenseIDs = ids;
	}
	
	/**
	 * Open the window.
	 * @wbp.parser.entryPoint
	 * 
	 */
	public void open()
	{
		Display display = Display.getDefault();
		chartHelper = new ChartHelper(expenseIDs);
		
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
	
	public void createContents()
	{
		shell = new Shell(SWT.SYSTEM_MODAL | SWT.DIALOG_TRIM);
		shell.setText("Expenses");
		shell.setSize(742, 607);
		shell.setLayout(null);
		
		TabFolder tabFolder = new TabFolder(shell, SWT.NONE);
		tabFolder.setBounds(0, 0, 736, 578);
		
		TabItem tbtmChartOne = new TabItem(tabFolder, SWT.NONE);
		tbtmChartOne.setText("Chart One");
		
		Composite composite = new Composite(tabFolder, SWT.NONE);
		composite.setLayout(new FillLayout());
		chartByPaymentMethod(composite);
		
		tbtmChartOne.setControl(composite);
		
		TabItem tbtmChartTwo = new TabItem(tabFolder, SWT.NONE);
		tbtmChartTwo.setText("Chart Two");
		
		Composite composite2 = new Composite(tabFolder, SWT.NONE);
		composite2.setLayout(new FillLayout());
		createSecondChart(composite2);
		
		tbtmChartTwo.setControl(composite2);
	}
	
	public void chartByPaymentMethod(Composite parent)
	{
		Chart chart = new Chart(parent, SWT.NONE);
		
		//set titles
		chart.getTitle().setText("Expense Trends");
		chart.getAxisSet().getXAxis(0).getTitle().setText("Payment Method");
		chart.getAxisSet().getYAxis(0).getTitle().setText("Amount");
		
		int[] xAxisValues = {0, 1, 2, 3};
		double[] ySeries = chartHelper.getYAxisValues(xAxisValues, XAxis.PAYMENT_METHOD);
		
		//create bar series
		IBarSeries barSeries = (IBarSeries) chart.getSeriesSet().createSeries(SeriesType.BAR,  "bar series");
		barSeries.setYSeries(ySeries);
		
		//Set Categories
		IAxis xAxis = chart.getAxisSet().getXAxis(0);
		xAxis.setCategorySeries(new String[] {"Cash", "Debit", "Credit", "Other"});
		xAxis.enableCategory(true);
		
		//Set the orientation of chart
		chart.setOrientation(SWT.HORIZONTAL);
		
		//adjust the axis range
		chart.getAxisSet().adjustRange();
	}
	
	//By month and year
	public void chartByMonthAndYear(Composite parent)
	{
		
	}
	
	public void createSecondChart(Composite parent)
	{
		Chart chart = new Chart(parent, SWT.NONE);
		
		//set titles
		chart.getTitle().setText("Expense Trends");
		chart.getAxisSet().getXAxis(0).getTitle().setText("Data points");
		chart.getAxisSet().getYAxis(0).getTitle().setText("Frequency");
		
		//create bar series
		IBarSeries barSeries = (IBarSeries) chart.getSeriesSet().createSeries(SeriesType.BAR,  "bar series");
		barSeries.setYSeries(ySeriesTwo);
		
		//Set Categories
		IAxis xAxis = chart.getAxisSet().getXAxis(0);
		xAxis.setCategorySeries(new String[] {"July", "Aug", "Sept", "Oct"});
		xAxis.enableCategory(true);
		
		//Set the orientation of chart
		chart.setOrientation(SWT.HORIZONTAL);
		
		//adjust the axis range
		chart.getAxisSet().adjustRange();
	}
}
