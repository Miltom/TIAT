package tiat;

import java.util.Vector;

import org.jfree.chart.ChartFactory;
import org.jfree.chart.ChartFrame;
import org.jfree.chart.JFreeChart;
import org.jfree.chart.plot.PlotOrientation;
import org.jfree.data.xy.XYSeries;
import org.jfree.data.xy.XYSeriesCollection;

public class GeneAlg {

	private Vector<Individuum> bestIndividuum;
	private XYSeriesCollection dataset;

	public GeneAlg() {
		this.bestIndividuum = new Vector<>();
		this.dataset = new XYSeriesCollection();
	}
	
	public void doWorkWithRekombination(double pm){
		IndividuumManager ic = new IndividuumManager(5);
		ic.fillWithRandomNumbers(30, 0, 31);
		ic.auswerten();
		Vector<Individuum> rang = ic.getInds();
		Vector<Individuum> forRekombine= new Vector<>();;
		
		for (int i = 0; i < 100; i++) {
			rang = ic.getSelects(rang);
			
			forRekombine = ic.removeRandomInds(rang, 10);
			ic.singlePoint(forRekombine);
			rang.addAll(forRekombine);
			ic.mutateInds(rang, pm);
			ic.auswerten();
			bestIndividuum.add(ic.getBestIndividuum());
		}

		addToDataset(bestIndividuum, pm);
	}

	public void doWork(double pm) {
		IndividuumManager ic = new IndividuumManager(5);
		bestIndividuum.clear();
		ic.fillWithRandomNumbers(30, 0, 31);
		ic.auswerten();
		Vector<Individuum> rang = ic.getInds();

		for (int i = 0; i < 100; i++) {
			rang = ic.getSelects(rang);	if(ic.getBestIndividuum()==null){
				ic.fillWithRandomNumbers(30, 0, 31);
				//TODO falsch
				rang = ic.getInds();
				continue;
			}
			rang = ic.mutateIndsTest(rang, pm);
//			ic.auswerten();
		
			bestIndividuum.add(ic.getBestIndividuum());
			System.out.println(ic.getBestIndividuum());
			System.out.println(i+"-"+rang.size());
			ic.setRangs(rang);
		}

		addToDataset(bestIndividuum, pm);
	}

	
	public void addToDataset(Vector<Individuum> inds, double pm) {
		int i=1;
        final XYSeries series1 = new XYSeries("Pm="+pm);
		
		for (Individuum individuum : inds) {
			series1.add(i, individuum.getF());
			i++;
		}
		
		dataset.addSeries(series1);
	}
	

	public void showOnDiagramm() {
		JFreeChart chart = ChartFactory.createXYLineChart("GA Zylinderproblem", "Generation", "Fitnesswert", dataset, PlotOrientation.VERTICAL, true, true, false);
		ChartFrame frame = new ChartFrame("", chart);
		
		frame.pack();
		frame.setVisible(true);
		frame.setDefaultCloseOperation(ChartFrame.EXIT_ON_CLOSE);
	}
}
