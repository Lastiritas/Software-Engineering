package gui;

import java.text.DecimalFormat;

import org.eclipse.swt.SWT;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.graphics.Color;
import org.eclipse.swt.graphics.Font;
import org.eclipse.swt.graphics.Point;
import org.eclipse.swt.layout.FillLayout;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Menu;
import org.eclipse.swt.widgets.MenuItem;
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
import acceptanceTests.Register;
import acceptanceTests.EventLoop;

public class ExpenseTrends 
{
	private IDSet expenseIDs;
	private Shell shell;
	private ChartHelper chartHelper;
	private Menu menu;
	private MenuItem mntmNewSubmenu;
	private Menu menu_1;
	private MenuItem mntmExit;
	private TabItem tbtmChartOne;
	private TabItem tbtmChartTwo;
	private TabItem tbtmChartThree;
	private TabItem tbtmChartFour;
	
	//Settings for charts
	private Color colorTitle;
	private Color backgroundColor;
	private Color colorBlack;
	private Color colorBars;
	private Font fontTitle;
	private Font fontAxis;
	private Font fontAxisValues;
	private Font fontForLabels;
	
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
		Register.newWindow(this);
		chartHelper = new ChartHelper(expenseIDs);
		
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
	
	public void createContents()
	{
		shell = new Shell(SWT.SYSTEM_MODAL | SWT.DIALOG_TRIM);
		shell.setText("Expenses");
		shell.setSize(700, 690);
		shell.setLayout(null);
		
		menu = new Menu(shell, SWT.BAR);
		menu.setLocation(new Point(0, 0));
		shell.setMenuBar(menu);
		
		mntmNewSubmenu = new MenuItem(menu, SWT.CASCADE);
		mntmNewSubmenu.setText("File");
		
		menu_1 = new Menu(mntmNewSubmenu);
		mntmNewSubmenu.setMenu(menu_1);
		
		mntmExit = new MenuItem(menu_1, SWT.NONE);
		mntmExit.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent arg0) {
				shell.close();
			}
		});
		mntmExit.setText("Exit");
		
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
		fontForLabels = new Font(Display.getDefault(), "Times New Romans", 8, SWT.NORMAL);
		
		createTabs(tabFolder);
	}
	
	private void createTabs(TabFolder tabFolder)
	{
		//Payment Method tab
		tbtmChartOne = new TabItem(tabFolder, SWT.NONE);
		tbtmChartOne.setText("Expenses by Payment Method");
		
		Composite composite = new Composite(tabFolder, SWT.NONE);
		composite.setLayout(new FillLayout());
		chartByPaymentMethod(composite);
		
		tbtmChartOne.setControl(composite);
		
		//Month tab
		tbtmChartTwo = new TabItem(tabFolder, SWT.NONE);
		tbtmChartTwo.setText("Expenses by Month");
		
		Composite composite2 = new Composite(tabFolder, SWT.NONE);
		composite2.setLayout(new FillLayout());
		chartByMonth(composite2);
		
		tbtmChartTwo.setControl(composite2);
		
		//Location tab
		tbtmChartThree = new TabItem(tabFolder, SWT.NONE);
		tbtmChartThree.setText("Expenses by Location");
		
		Composite composite3 = new Composite(tabFolder, SWT.NONE);
		composite3.setLayout(new FillLayout());
		chartByLocation(composite3);
		
		tbtmChartThree.setControl(composite3);
		
		//Labels tab
		tbtmChartFour = new TabItem(tabFolder, SWT.NONE);
		tbtmChartFour.setText("Expenses by Labels");
		
		Composite composite4 = new Composite(tabFolder, SWT.NONE);
		composite4.setLayout(new FillLayout());
		chartByLabels(composite4);
		
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
	
	public void chartByLocation(Composite parent)
	{
		Chart chart = setChartProperties(parent);
		IAxis xAxis = chart.getAxisSet().getXAxis(0);
		IAxis yAxis = chart.getAxisSet().getYAxis(0);
		
		//Set titles
		chart.getTitle().setText("Expense Trends by Location");
		xAxis.getTitle().setText("Location");
		yAxis.getTitle().setText("Amount");
		
		String[] xAxisValues= chartHelper.getXAxisStringValues(XAxis.LOCATION);
		double[] ySeries = chartHelper.getYAxisStringValues(xAxisValues, XAxis.LOCATION);
		
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
		
		//This line will create an exception if a payTo has no associated amount ($0)
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
	
	public void chartByLabels(Composite parent)
	{
		Chart chart = setChartProperties(parent);
		IAxis xAxis = chart.getAxisSet().getXAxis(0);
		IAxis yAxis = chart.getAxisSet().getYAxis(0);
		IAxisTick xTick = xAxis.getTick();
		
		//Set titles
		chart.getTitle().setText("Expense Trends by Labels");
		xAxis.getTitle().setText("Labels");
		yAxis.getTitle().setText("Amount");
		
		//Override the font for labels
		xTick.setFont(fontForLabels);
		
		String[] xAxisValues= chartHelper.getXAxisStringValues(XAxis.LABELS);
		double[] ySeries = chartHelper.getYAxisStringValues(xAxisValues, XAxis.LABELS);
		
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
