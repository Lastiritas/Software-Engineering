package gui;

import java.text.DecimalFormat;

import org.eclipse.swt.SWT;
import org.eclipse.swt.graphics.Color;
import org.eclipse.swt.graphics.Font;
import org.eclipse.swt.layout.FillLayout;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.swt.widgets.TabFolder;
import org.eclipse.swt.widgets.TabItem;
import org.swtchart.Chart;
import org.swtchart.IAxis;
import org.swtchart.IAxisTick;
import org.swtchart.IBarSeries;
import org.swtchart.ILegend;
import org.swtchart.ISeries.SeriesType;
import org.swtchart.ISeriesLabel;

import domainobjects.IDSet;
import util.XAxis;

public class ExpenseTrends 
{
	private IDSet expenseIDs;
	private Shell shell;
	private ChartHelper chartHelper;
	
	//Settings for charts
	private Color colorTitle;
	private Color backgroundColor;
	private Color colorBlack;
	private Color colorBars;
	private Font fontTitle;
	private Font fontAxis;
	private Font fontAxisValues;
	private Font fontForLocations;
	
	
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
		shell.setSize(700, 670);
		shell.setLayout(null);
		
		TabFolder tabFolder = new TabFolder(shell, SWT.NONE);
		tabFolder.setBounds(0, 0, 700, 640);
		
		//Color and font for titles
		colorTitle = new Color(Display.getDefault(), 139, 69, 19);
		backgroundColor = new Color(Display.getDefault(), 255, 255, 224);
		colorBlack = new Color(Display.getDefault(), 0, 0, 0);
		colorBars = new Color(Display.getDefault(), 80, 240, 180);
		
		fontTitle = new Font(Display.getDefault(), "Times New Romans", 16, SWT.BOLD);
		fontAxis = new Font(Display.getDefault(), "Times New Romans", 10, SWT.BOLD);
		fontAxisValues = new Font(Display.getDefault(), "Times New Romans", 8, SWT.NORMAL);
		fontForLocations = new Font(Display.getDefault(), "Times New Romans", 6, SWT.NORMAL);
		
		createTabs(tabFolder);
	}
	
	private void createTabs(TabFolder tabFolder)
	{
		//Payment Method tab
		TabItem tbtmChartOne = new TabItem(tabFolder, SWT.NONE);
		tbtmChartOne.setText("Expenses by Payment Method");
		
		Composite composite = new Composite(tabFolder, SWT.NONE);
		composite.setLayout(new FillLayout());
		chartByPaymentMethod(composite);
		
		tbtmChartOne.setControl(composite);
		
		//Month tab
		TabItem tbtmChartTwo = new TabItem(tabFolder, SWT.NONE);
		tbtmChartTwo.setText("Expenses by Month");
		
		Composite composite2 = new Composite(tabFolder, SWT.NONE);
		composite2.setLayout(new FillLayout());
		chartByMonth(composite2);
		
		tbtmChartTwo.setControl(composite2);
		
		//Location tab
		TabItem tbtmChartThree = new TabItem(tabFolder, SWT.NONE);
		tbtmChartThree.setText("Expenses by Location");
		
		Composite composite3 = new Composite(tabFolder, SWT.NONE);
		composite3.setLayout(new FillLayout());
		chartByLocation(composite3, XAxis.LOCATION);
		
		tbtmChartThree.setControl(composite3);
		
		//Location Branch tab
		TabItem tbtmChartFour = new TabItem(tabFolder, SWT.NONE);
		tbtmChartFour.setText("Expenses by Location/Branch");
		
		Composite composite4 = new Composite(tabFolder, SWT.NONE);
		composite4.setLayout(new FillLayout());
		chartByLocation(composite4, XAxis.LOCATION_BRANCH);
		
		tbtmChartFour.setControl(composite4);
	}
	
	public void chartByPaymentMethod(Composite parent)
	{
		Chart chart = setChartProperties(parent);
		IAxis xAxis = chart.getAxisSet().getXAxis(0);
		IAxis yAxis = chart.getAxisSet().getYAxis(0);
		
		//Set titles
		chart.getTitle().setText("Expense Trends by Payment Method");
		xAxis.getTitle().setText("Payment Method");
		yAxis.getTitle().setText("Amount");
		
		int[] xAxisValues = {0, 1, 2, 3};
		double[] ySeries = chartHelper.getYAxisIntValues(xAxisValues, XAxis.PAYMENT_METHOD);
		
		//Create bar series
		IBarSeries barSeries = (IBarSeries) chart.getSeriesSet().createSeries(SeriesType.BAR,  "bar series");
		barSeries.setYSeries(ySeries);
		
		//Set color
		barSeries.setBarColor(colorBars);
		
		//Set labels
		ISeriesLabel seriesLabel = barSeries.getLabel();
		seriesLabel.setFormat("$#,###,###.##");
		seriesLabel.setVisible(true);
		
		//Set Categories
		xAxis.setCategorySeries(new String[] {"Cash", "Debit", "Credit", "Other"});
		xAxis.enableCategory(true);
		
		//Set Legend
		ILegend legend = chart.getLegend();
		legend.setVisible(false);
		
		//Set the orientation of chart
		chart.setOrientation(SWT.HORIZONTAL);
		
		//Adjust the axis range
		chart.getAxisSet().adjustRange();
	}
	
	public void chartByMonth(Composite parent)
	{
		Chart chart = setChartProperties(parent);
		IAxis xAxis = chart.getAxisSet().getXAxis(0);
		IAxis yAxis = chart.getAxisSet().getYAxis(0);
		
		//Set titles
		chart.getTitle().setText("Expense Trends by Month");
		xAxis.getTitle().setText("Months");
		yAxis.getTitle().setText("Amount");
		
		int[] xAxisValues = chartHelper.getXAxisIntValues(XAxis.DATES);
		double[] ySeries = chartHelper.getYAxisIntValues(xAxisValues, XAxis.DATES);
		
		//Create bar series
		IBarSeries barSeries = (IBarSeries) chart.getSeriesSet().createSeries(SeriesType.BAR,  "bar series");
		barSeries.setYSeries(ySeries);
		
		//Set color
		barSeries.setBarColor(colorBars);
		
		//Set labels
		ISeriesLabel seriesLabel = barSeries.getLabel();
		seriesLabel.setFormat("$#,###,###.##");
		seriesLabel.setVisible(true);
		
		//Set Categories
		String[] xAxisStrings = chartHelper.createCategoriesForDates(xAxisValues);
		xAxis.setCategorySeries(xAxisStrings);
		xAxis.enableCategory(true);
		
		//Set Legend
		ILegend legend = chart.getLegend();
		legend.setVisible(false);
		
		//Set the orientation of chart
		chart.setOrientation(SWT.HORIZONTAL);
		
		//Adjust the axis range
		chart.getAxisSet().adjustRange();
	}
	
	public void chartByLocation(Composite parent, XAxis axisType)
	{
		Chart chart = setChartProperties(parent);
		IAxis xAxis = chart.getAxisSet().getXAxis(0);
		IAxis yAxis = chart.getAxisSet().getYAxis(0);
		IAxisTick xTick = xAxis.getTick();
		
		//Set titles
		chart.getTitle().setText("Expense Trends by Location");
		xAxis.getTitle().setText("Location");
		yAxis.getTitle().setText("Amount");
		
		//Override the font for locations
		xTick.setFont(fontForLocations);
		
		String[] xAxisValues;
		double[] ySeries;
		
		if(axisType == XAxis.LOCATION)
		{
			xAxisValues= chartHelper.getXAxisStringValues(XAxis.LOCATION);
			ySeries = chartHelper.getYAxisStringValues(xAxisValues, XAxis.LOCATION);
		}
		else
		{
			xAxisValues= chartHelper.getXAxisStringValues(XAxis.LOCATION_BRANCH);
			ySeries = chartHelper.getYAxisStringValues(xAxisValues, XAxis.LOCATION_BRANCH);
		}
		//Create bar series
		IBarSeries barSeries = (IBarSeries) chart.getSeriesSet().createSeries(SeriesType.BAR,  "bar series");
		barSeries.setYSeries(ySeries);
		
		//Set color
		barSeries.setBarColor(colorBars);
		
		//Set labels
		ISeriesLabel seriesLabel = barSeries.getLabel();
		seriesLabel.setFormat("$#,###,###.##");
		Font font2 = new Font(Display.getDefault(), "Times New Romans", 8, SWT.NORMAL);
		seriesLabel.setFont(font2);
		seriesLabel.setVisible(true);
		
		yAxis.enableLogScale(true);
		
		//Set Categories
		xAxis.setCategorySeries(xAxisValues);
		xAxis.enableCategory(true);
		
		//Set legend
		ILegend legend = chart.getLegend();
		legend.setVisible(false);
		
		//Set the orientation of chart
		chart.setOrientation(SWT.VERTICAL);
		
		//Adjust the axis range
		chart.getAxisSet().adjustRange();
	}
	
	private Chart setChartProperties(Composite parent)
	{
		Chart chart = new Chart(parent, SWT.NONE);
		IAxis xAxis = chart.getAxisSet().getXAxis(0);
		IAxis yAxis = chart.getAxisSet().getYAxis(0);
		IAxisTick xTick = xAxis.getTick();
		IAxisTick yTick = yAxis.getTick();
		
		chart.getTitle().setForeground(colorTitle);
		xAxis.getTitle().setForeground(colorTitle);
		yAxis.getTitle().setForeground(colorTitle);
		
		chart.getTitle().setFont(fontTitle);
		xAxis.getTitle().setFont(fontAxis);
		yAxis.getTitle().setFont(fontAxis);
		
		chart.setBackground(backgroundColor);
		chart.setBackgroundInPlotArea(backgroundColor);
		
		yTick.setFormat(new DecimalFormat("$ ##,###.##"));
		yTick.setFont(fontAxisValues);
		yTick.setForeground(colorBlack);
		xTick.setFont(fontAxisValues);
		xTick.setForeground(colorBlack);
		
		return chart;
	}
}
