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

	public void doWorkWithRekombination(double pm) {
		IndividuumManager ic = new IndividuumManager(5);
		bestIndividuum.clear();
		Vector<Individuum> forRekombine = new Vector<>();
		Vector<Individuum> rang = ic.fillWithRandomNumbers(30, 0, 31);
		rang = ic.auswerten(rang);

		for (int i = 0; i < 30; i++) {
			// Rang-basierte Selektion
			rang = (Vector<Individuum>) ic.getSelects(rang, 30).clone();
			
			// Rekombination
			forRekombine = (Vector<Individuum>) ic.removeRandomInds(rang, 10).clone();
			ic.singlePoint(forRekombine);
			rang.addAll(forRekombine);
			// Mutation und Auswertung, sollte false sein
			rang = (Vector<Individuum>) ic.mutateIndsTestCond(rang, pm).clone();

			// Speicherung des besten Individuum
			bestIndividuum.add(ic.getBestIndividuum(rang));
		}

		addToDataset(bestIndividuum, pm);
	}

	// public void doWorkWithRekombination(double pm) {
	// IndividuumManager ic = new IndividuumManager(5);
	// bestIndividuum.clear();
	// ic.fillWithRandomNumbers(30, 0, 31);
	// ic.auswerten();
	// Vector<Individuum> rang = (Vector<Individuum>) ic.getInds().clone();
	// Vector<Individuum> forRekombine = new Vector<>();
	//
	// for (int i = 0; i < 100; i++) {
	// // Rang-basierte Selektion
	// rang = (Vector<Individuum>) ic.printSelects(rang).clone();
	//
	// // Rekombination mit Singe-Point
	// forRekombine = (Vector<Individuum>) ic.removeRandomInds(rang,
	// 10).clone();
	// ic.singlePoint(forRekombine);
	// rang.addAll(forRekombine);
	//
	// // TODO Nutzen Sie die Rekombination, um die Anzahl der Nachkommen mit 30
	// // Individuen sicherzustellen??
	// // Mutation und Auswertung
	// rang = (Vector<Individuum>) ic.mutateIndsTest(rang, pm, true).clone();
	//
	// // Speicherung des besten Individuum
	// bestIndividuum.add(ic.getBestIndividuum());
	// }
	//
	// addToDataset(bestIndividuum, pm);
	// }

	public void doWork(double pm) {
		IndividuumManager ic = new IndividuumManager(5);
		bestIndividuum.clear();
		Vector<Individuum> rang = ic.fillWithRandomNumbers(30, 0, 31);
		rang = ic.auswerten(rang);

		for (int i = 0; i < 30; i++) {
			// Rang-basierte Selektion
			rang = (Vector<Individuum>) ic.getSelects(rang, 30).clone();

			// Mutation und Auswertung
			rang = (Vector<Individuum>) ic.mutateIndsTest(rang, pm).clone();

			// Speicherung des besten Individuum
			bestIndividuum.add(ic.getBestIndividuum(rang));
		}

		addToDataset(bestIndividuum, pm);
	}

	public void addToDataset(Vector<Individuum> inds, double pm) {
		int i = 1;
		final XYSeries series1 = new XYSeries("Pm=" + pm);

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
