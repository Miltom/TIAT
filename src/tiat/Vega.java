package tiat;

import java.util.Collections;
import java.util.Comparator;
import java.util.Vector;

import org.jfree.chart.ChartFactory;
import org.jfree.chart.ChartFrame;
import org.jfree.chart.JFreeChart;
import org.jfree.chart.plot.PlotOrientation;
import org.jfree.data.xy.XYSeries;
import org.jfree.data.xy.XYSeriesCollection;

public class Vega {

	private IndividuumManager ic;
	private Vector<Individuum> inds;
	private XYSeriesCollection dataset;
	private Vector<Individuum> bestIndividuum;

	public static void main(String[] args) {
		Vega vega = new Vega(2, 30);
	}

	public Vega(int anzahlBlöcke, int anzahlInds) {
		this.dataset = new XYSeriesCollection();
		this.ic = new IndividuumManager(5);
		this.inds = ic.fillWithRandomNumbers(anzahlInds, 0, 31);
		this.bestIndividuum = new Vector<>();
		ic.auswerten(inds);
		
		for (int i = 0; i < 100; i++) {
			Vector<Vector<Individuum>> block = fillBlock(anzahlBlöcke, anzahlInds);
			inds = doFunktion(block, anzahlBlöcke, anzahlInds);
			// TODO überprüfen ob es richtig sortiert
			inds= sort(inds);
			
			try {
				bestIndividuum.add((Individuum) inds.get(0).clone());
			} catch (CloneNotSupportedException e) {
				e.printStackTrace();
			}
		}
		
		showOnDiagramm(bestIndividuum);
	}

	private Vector<Vector<Individuum>> fillBlock(int anzahlBlöcke, int anzahlInds) {
		Vector<Vector<Individuum>> block = new Vector<Vector<Individuum>>();
		// TODO ungerade anzahl
		int proBlock = anzahlInds / anzahlBlöcke;

		Vector<Individuum> vec = null;

		int random;

		for (int i = 0; i < anzahlBlöcke; i++) {
			vec = new Vector<>();

			while (vec.size() < proBlock) {
				random = ic.getRandomInt(0, inds.size());

				if (inds.get(random) != null) {
					vec.add(inds.remove(random));
				}
			}
			block.add(vec);

		}
		Vector<Individuum> vec2 = null;
		for (int i = 0; i < anzahlBlöcke; i++) {

			vec2 = block.get(i);
			for (Individuum ind : vec2) {
				System.out.println(ind.getId());
			}
			System.out.println("----------");
		}
		return block;
	}

	private Vector<Individuum> doFunktion(Vector<Vector<Individuum>> block, int anzahlBlöcke, int anzahlInds) {
		Vector<Individuum> vec1 = getSelectsF1(block.get(0), anzahlInds / anzahlBlöcke);
		IndividuumManager.mutateIndsTest(vec1, 0.01, false);
		
		Vector<Individuum> vec2 = getSelectsF2(block.get(1), anzahlInds / anzahlBlöcke);
		IndividuumManager.mutateIndsTest(vec2, 0.01, false);
		
		inds.clear();
		inds.addAll(vec1);
		inds.addAll(vec2);
		return inds;
	}

	public Vector<Individuum> getSelectsF1(Vector<Individuum> indsParam, int anzahlInds) {
		indsParam = nachFSortieren(indsParam);
		return kuchenDiagramm(indsParam, anzahlInds);
	}
	
	public Vector<Individuum> getSelectsF2(Vector<Individuum> indsParam, int anzahlInds) {
		nachGSortieren(indsParam);
		return kuchenDiagramm(indsParam, anzahlInds);
	}
	
	
	public Vector<Individuum> sort(Vector<Individuum> indsParam) {
		Collections.sort(indsParam, new Comparator<Individuum>() {

			@Override
			public int compare(Individuum o1, Individuum o2) {
				return ((Double) o2.getF()).compareTo((Double) o1.getF())+((Double) o1.getG()).compareTo((Double) o2.getG());
			}
		});

		return indsParam;
	}
	
	
	
	public void addToDataset(Vector<Individuum> inds, double pm) {
	
	}

	public void showOnDiagramm(Vector<Individuum> indsParam) {
		int i = 1;
		final XYSeries series1 = new XYSeries("Verlauf");

		for (Individuum individuum : indsParam) {
			series1.add(i, individuum.getF());
			i++;
		}

		dataset.addSeries(series1);
		
		JFreeChart chart = ChartFactory.createXYLineChart("GA Zylinderproblem", "Generation", "Fitnesswert", dataset, PlotOrientation.VERTICAL, true, true, false);
		ChartFrame frame = new ChartFrame("", chart);

		frame.pack();
		frame.setVisible(true);
		frame.setDefaultCloseOperation(ChartFrame.EXIT_ON_CLOSE);
	}
	
	private  Vector<Individuum> kuchenDiagramm(Vector<Individuum> indsParam, int anzahlInds){

		int summe = IndividuumManager.kleinerGaus(indsParam.size());
		int index = 1;

		// Gewicht berechnen
		for (Individuum ind : indsParam) {
			ind.setGewicht((1.0 / summe) * index);
			index++;
		}

		// Nach der ID sortieren
		Collections.sort(indsParam);

		double lastPos = 0;

		for (Individuum ind : indsParam) {
			ind.setProzentualVon(lastPos);

			ind.setProzentualVon(lastPos);
			lastPos = lastPos + ind.getGewicht();
			ind.setProzentualBis(lastPos);
		}

		Individuum ind = null;
		Vector<Individuum> temp = new Vector<>();

		while (temp.size() < anzahlInds) {
			ind = IndividuumManager.randomIndividuum(indsParam);
			temp.add(ind);
		}

		return temp;
	}

	private Vector<Individuum> nachFSortieren(Vector<Individuum> vec) {

		Collections.sort(vec, new Comparator<Individuum>() {

			@Override
			public int compare(Individuum o1, Individuum o2) {
				return ((Double) o2.getF()).compareTo((Double) o1.getF());
			}
		});

		return vec;
	}

	private Vector<Individuum> nachGSortieren(Vector<Individuum> vec) {

		Collections.sort(vec, new Comparator<Individuum>() {

			@Override
			public int compare(Individuum o1, Individuum o2) {
				return ((Double) o1.getG()).compareTo((Double) o2.getG());
			}
		});

		return vec;
	}

}
