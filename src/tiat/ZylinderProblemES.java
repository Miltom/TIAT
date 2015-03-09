package tiat;

import java.util.Vector;

import org.jfree.chart.ChartFactory;
import org.jfree.chart.ChartFrame;
import org.jfree.chart.JFreeChart;
import org.jfree.chart.plot.PlotOrientation;
import org.jfree.data.xy.XYSeries;
import org.jfree.data.xy.XYSeriesCollection;

public class ZylinderProblemES {

	private IndividuumManager ic;
	private Vector<Individuum> inds;
	private XYSeriesCollection dataset;
	private Vector<Individuum> bestIndividuum;

	public ZylinderProblemES(int anzahlInds, int lives) {
		int bits = 5;
		this.dataset = new XYSeriesCollection();
		this.ic = new IndividuumManager(bits);
		this.inds = ic.fillWithRandomNumbers(anzahlInds, 0, 31, lives);
		this.bestIndividuum = new Vector<>();
		ic.auswerten(inds);

		for (int i = 0; i < 100; i++) {

			// *** Objektparameter ****
			// Durchschnittliche Rekombination
			Individuum newInd = durchschnittlicheRekomb(ic.randomInds(inds, 3), bits);
			inds.add(newInd);

			// 'Isotropische' Mutation
			inds = ic.isotropischeMutation(inds, 0.1);
			inds = ic.getSelects(inds, 49);

			// *** Strategieparameter ****
			newInd = disrketeRekomb(ic.randomInds(inds, 3), bits, 15);
			inds.add(newInd);

			// 'Nicht-Isotropische' Mutation
			inds = ic.nichtIsotropischeMutation(inds, 0.1);
			inds = ic.getSelects(inds, 49);

			try {
				bestIndividuum.add((Individuum) inds.get(0).clone());
			} catch (CloneNotSupportedException e) {
				e.printStackTrace();
			}

			ic.reducingLives(inds, lives);
		}

		showOnDiagramm(bestIndividuum);
	}

	private Individuum disrketeRekomb(Vector<Individuum> randomInds, int bits, int livesCounter) {
		StringBuilder newHZahl = new StringBuilder();

		int length = new String("" + randomInds.get(0).getHReel()).length();
		int randomInd = 0;

		String ind1 = new String("" + randomInds.get(0).getHReel());
		String ind2 = new String("" + randomInds.get(1).getHReel());
		String ind3 = new String("" + randomInds.get(2).getHReel());

		for (int i = 0; i < length; i++) {
			randomInd = IndividuumManager.getRandomInt(0, 3);

			try {
				switch (randomInd) {
				case 0:
					newHZahl.append(ind1.charAt(i));
					break;
				case 1:
					newHZahl.append(ind2.charAt(i));
					break;
				case 2:
					newHZahl.append(ind3.charAt(i));
					break;
				}
			} catch (StringIndexOutOfBoundsException e) {
				newHZahl.append(ind1.charAt(i));
			}
		}

		StringBuilder newDZahl = new StringBuilder();

		length = new String("" + randomInds.get(0).getDReel()).length();

		ind1 = new String("" + randomInds.get(0).getDReel());
		ind2 = new String("" + randomInds.get(1).getDReel());
		ind3 = new String("" + randomInds.get(2).getDReel());

		for (int i = 0; i < length; i++) {
			randomInd = IndividuumManager.getRandomInt(0, 3);

			switch (randomInd) {
			case 0:
				newDZahl.append(ind1.charAt(i));
				break;
			case 1:
				newDZahl.append(ind2.charAt(i));
				break;
			case 2:
				newDZahl.append(ind3.charAt(i));
				break;
			}
		}

		Individuum newInd = new Individuum(livesCounter, -1, Integer.parseInt(newDZahl.toString()), Integer.parseInt(newHZahl.toString()), bits);
		newInd.auswerten();
		return newInd;
	}

	public void showOnDiagramm(Vector<Individuum> indsParam) {
		int i = 1;
		final XYSeries series1 = new XYSeries("Verlauf");

		for (Individuum individuum : indsParam) {
			series1.add(i, individuum.getF());
			i++;
		}

		dataset.addSeries(series1);

		JFreeChart chart = ChartFactory.createXYLineChart("ES Zylinderproblem", "Generation", "Fitnesswert", dataset, PlotOrientation.VERTICAL, true, true, false);
		ChartFrame frame = new ChartFrame("", chart);

		frame.pack();
		frame.setVisible(true);
		frame.setDefaultCloseOperation(ChartFrame.EXIT_ON_CLOSE);
	}

	public Individuum durchschnittlicheRekomb(Vector<Individuum> inds, int bits) {
		Individuum ind1 = inds.get(0);
		Individuum ind2 = inds.get(1);
		Individuum ind3 = inds.get(2);

		int hReel = (ind1.getHReel() + ind2.getHReel() + ind3.getHReel()) / 3;
		int dReel = (ind1.getDReel() + ind2.getDReel() + ind3.getDReel()) / 3;
		Individuum newInd = new Individuum(-1, hReel, dReel, bits);
		newInd.auswerten();
		return newInd;
	}

	public static void main(String[] args) {
		new ZylinderProblemES(7, 15);
	}
}
