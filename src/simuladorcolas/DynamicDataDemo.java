package simuladorcolas;

import java.awt.BorderLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JButton;
import javax.swing.JPanel;

import org.jfree.chart.ChartFactory;
import org.jfree.chart.ChartPanel;
import org.jfree.chart.JFreeChart;
import org.jfree.chart.axis.ValueAxis;
import org.jfree.chart.plot.XYPlot;
import org.jfree.data.time.Millisecond;
import org.jfree.data.time.TimeSeries;
import org.jfree.data.time.TimeSeriesCollection;
import org.jfree.data.xy.XYDataset;
import org.jfree.ui.ApplicationFrame;
import org.jfree.ui.RefineryUtilities;

@SuppressWarnings("serial")
public class DynamicDataDemo extends ApplicationFrame {

	/** The time series data. */
	private static TimeSeries series;

	/** The most recent value added. */
	private double lastValue = 100.0;

	/**
	 * Constructs a new demonstration application.
	 * 
	 * @param title
	 *            the frame title.
	 */
	public DynamicDataDemo(final String title) {

		super(title);
		this.series = new TimeSeries("Largo de la cola", Millisecond.class);
		final TimeSeriesCollection dataset = new TimeSeriesCollection(
				this.series);
		final JFreeChart chart = createChart(dataset);

		final ChartPanel chartPanel = new ChartPanel(chart);

		final JPanel content = new JPanel(new BorderLayout());
		content.add(chartPanel);
		chartPanel.setPreferredSize(new java.awt.Dimension(500, 270));
		setContentPane(content);

	}

	/**
	 * Creates a sample chart.
	 * 
	 * @param dataset
	 *            the dataset.
	 * 
	 * @return A sample chart.
	 */
	private JFreeChart createChart(final XYDataset dataset) {
		final JFreeChart result = ChartFactory.createTimeSeriesChart(
				"Largo de la cola", "Time", "Value", dataset, true, true,
				false);
		final XYPlot plot = result.getXYPlot();
		ValueAxis axis = plot.getDomainAxis();
		axis.setAutoRange(true);
		axis.setFixedAutoRange(60000.0); // 60 seconds
		axis = plot.getRangeAxis();
		axis.setRange(0.0, 30);
		return result;
	}

	/**
	 * Starting point for the demonstration application.
	 * 
	 * @param args
	 *            ignored.
	 */
	public static void main(final String[] args) {

		final long start = System.currentTimeMillis();
		final SimuladorColas simulador = new SimuladorColas();
		simulador.start();
		final DynamicDataDemo demo = new DynamicDataDemo("Dynamic Data Demo");
		demo.pack();
		RefineryUtilities.centerFrameOnScreen(demo);
		demo.setVisible(true);
		Thread t = new Thread(new Runnable() {

			@Override
			public void run() {
				while (true) {
					try {
						Thread.sleep(500);
					} catch (InterruptedException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
					long now = System.currentTimeMillis();
					series.add(new Millisecond(), simulador.getQueueLength());
					demo.repaint();
				}
			}
		});
		t.start();

	}
}
