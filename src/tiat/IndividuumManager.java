package tiat;

import java.util.Collections;
import java.util.Comparator;
import java.util.Random;
import java.util.Vector;

public class IndividuumManager {

	private int bits;
	private int min;
	private int max;

	public IndividuumManager(int bits) {
		this.bits = bits;
	}

	public Vector<Individuum> auswerten(Vector<Individuum> inds) {
		for (int i = 0; i < inds.size(); i++) {
			inds.get(i).auswerten();
		}
		
		return inds;
	}

	public Vector<Individuum> fillWithRandomNumbers(int countOfInds, int min, int max) {
		this.min = min;
		this.max = max;
		Vector<Individuum> inds = new Vector<>();

		for (int i = 0; i < countOfInds; i++) {
			inds.add(new Individuum(i, getRandomInt(min, max), getRandomInt(min, max), bits));
		}
		
		return inds;
	}
	
	
	public Vector<Individuum> fillWithRandomNumbers(int countOfInds, int min, int max, int liveCounter) {
		this.min = min;
		this.max = max;
		Vector<Individuum> inds = new Vector<>();

		for (int i = 0; i < countOfInds; i++) {
			inds.add(new Individuum(liveCounter, i, getRandomInt(min, max), getRandomInt(min, max), bits));
		}
		
		return inds;
	}

	public Individuum createIndividuum(int id) {
		return new Individuum(id, getRandomInt(min, max), getRandomInt(min, max), bits);
	}

	public boolean passedCondition(Individuum ind) {
		return ind.getG() >= 300;
	}

	public static int getRandomInt(int min, int max) {
		return (int) (Math.random() * (max - min));
	}

	public static double getRandomDouble() {
		return (Math.random());
	}

	public Vector<Individuum> getSelects(Vector<Individuum> vecInds, int count) {

		for (int i = 0; i < vecInds.size(); i++) {
			if (!passedCondition(vecInds.get(i))) {
				vecInds.remove(i);
				i--;
			}
		}

		if (vecInds.size() == 0) {
			return null;
		}

		vecInds=nachFSortieren(vecInds);
		Vector<Individuum> newInds = kuchendiagramm(vecInds, count);

		return (Vector<Individuum>) newInds.clone();
	}

	public static int kleinerGaus(int zahl) {
		return (zahl * (zahl + 1)) / 2;
	}

	private Vector<Individuum> kuchendiagramm(Vector<Individuum> indsParam, int count) {
		int summe = kleinerGaus(indsParam.size());
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

		while(temp.size()<count){
			ind = randomIndividuum(indsParam);

			if (passedCondition(ind)) {
				temp.add(ind);
			}

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

	public static Individuum randomIndividuum(Vector<Individuum> vec) {

		double p = getRandomDouble();
		Individuum newInd = null;

		for (Individuum ind : vec) {
			if (ind.getProzentualBis() >= p) {

				try {
					newInd = (Individuum) ind.clone();
					newInd.setNew(true);
					return newInd;
				} catch (CloneNotSupportedException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}

			}
		}

		return null;

	}

	public Vector<Individuum> printSelects(Vector<Individuum> vecInds, int count) {
		getSelects(vecInds, count);
		int counter = 1;

		System.out.println("-------------------------------------");
		System.out.println("Selektion Rang");

		for (Individuum ind : vecInds) {
			System.out.println(counter + ". g:" + ind.getG() + ", f: " + ind.getF() + " (id=" + ind.getId() + ")");
			counter++;
		}
		return vecInds;
	}

	public void mutateInds(Vector<Individuum> vecInds, double pm) {
		System.out.println("-------------------------------------------------------");
		System.out.println("Mutation: pm = " + pm);

		for (int i = 0; i < vecInds.size(); i++) {
			mutation(pm, vecInds.get(i).getD());
			mutation(pm, vecInds.get(i).getH());
		}

		System.out.println("end mutation");
		System.out.println("-------------------------------------------------------");
	}

	public Vector<Individuum> isotropischeMutation(Vector<Individuum> vecInds, double pm){
		return mutateIndsTest(vecInds, pm);
	}

	public Vector<Individuum> nichtIsotropischeMutation(Vector<Individuum> vecInds, double pm){
		return mutateIndsTest(vecInds, pm);
	}
	
	public static Vector<Individuum> mutateIndsTestCond(Vector<Individuum> vecInds, double pm) {
		System.out.println("-------------------------------------------------------");
		System.out.println("Mutation: pm = " + pm);
		String first = "", second = "";

		for (int i = 0; i < vecInds.size(); i++) {

			do {
				first = mutation(pm, vecInds.get(i).getD());
				second = mutation(pm, vecInds.get(i).getH());
				vecInds.get(i).auswerten(first, second);
			} while (vecInds.get(i).getG() < 300);
		}

		System.out.println("end mutation");
		System.out.println("-------------------------------------------------------");
		return (Vector<Individuum>) vecInds.clone();
	}
	
	
	public static Vector<Individuum> mutateIndsTest(Vector<Individuum> vecInds, double pm) {
		System.out.println("-------------------------------------------------------");
		System.out.println("Mutation: pm = " + pm);
		String first = "", second = "";

		for (int i = 0; i < vecInds.size(); i++) {

				first = mutation(pm, vecInds.get(i).getD());
				second = mutation(pm, vecInds.get(i).getH());
				vecInds.get(i).auswerten(first, second);
		}

		System.out.println("end mutation");
		System.out.println("-------------------------------------------------------");
		return (Vector<Individuum>) vecInds.clone();
	}

	public static String mutation(double pm, String binaer) {
		String oldString = binaer;
		System.out.println("String " + binaer + ":");
		double binPm;

		for (int i = 0; i < binaer.length(); i++) {
			binPm = getRandomDouble();
			System.out.println("	" + i + ": " + binPm + " < " + pm + "->" + (binPm < pm));

			if (binPm < pm) {
				System.out.println("		old: " + binaer);
				binaer = mutationString(binaer, i);
				System.out.println("		new: " + binaer);
			}
		}

		System.out.println("Mutation ends for " + oldString + ": " + binaer + "\n");

		return binaer;
	}

	/**
	 * Mutation: 1 to 0 and 0 to 1
	 * 
	 * @param binaer
	 *            the strig which should be mutated
	 * @param index
	 *            the index of the char from the string
	 * @return the new string
	 */
	public static String mutationString(String binaer, int index) {
		char[] binaerChars = binaer.toCharArray();

		if (binaerChars[index] == '0') {
			binaerChars[index] = '1';
		} else if (binaerChars[index] == '1') {
			binaerChars[index] = '0';
		}

		return new String(binaerChars);
	}

	public void singlePoint(Vector<Individuum> vecInds) {
		for (Individuum individuum : vecInds) {
			singlePoint(individuum);
		}
	}

	public void singlePoint(Individuum ind) {
		Random rand = new Random();
		int singlePoint = rand.nextInt(bits);

		String first = ind.getD().substring(0, singlePoint) + ind.getH().substring(singlePoint);
		String second = ind.getH().substring(0, singlePoint) + ind.getD().substring(singlePoint);

		// TODO evlt elegant lösen
		ind.auswerten(first, second);

		System.out.println("----------------------");
		System.out.println("Singlepoint: " + singlePoint);

		System.out.println("d: " + ind.getD());
		System.out.println("h: " + ind.getH());

		System.out.println("d: " + ind.getD().substring(0, singlePoint) + "|" + ind.getD().substring(singlePoint));
		System.out.println("h: " + ind.getH().substring(0, singlePoint) + "|" + ind.getH().substring(singlePoint));

		System.out.println("\nRekombination:");
		System.out.println(first);
		System.out.println(second);
		System.out.println("----------------------");
	}

	public Individuum getBestIndividuum(Vector<Individuum> indsParam) {

		indsParam = nachFSortieren(indsParam);

		if (indsParam.size() == 0) {
			return null;
		}

		return indsParam.get(indsParam.size() - 1);
	}

	public Vector<Individuum> randomInds(Vector<Individuum> indsParam, int anzahl) {
		int counter = 0;
		Vector<Individuum> indsRandom = new Vector<Individuum>();
		Individuum ind = null;

		while (counter < anzahl) {
			try {
				System.out.println(indsParam.size());
				ind = (Individuum) indsParam.get(getRandomInt(0, indsParam.size())).clone();
			} catch (CloneNotSupportedException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}

			if (!indsRandom.contains(ind)) {
				indsRandom.add(ind);
				counter++;
			}
		}

		return indsRandom;
	}
	
	public Vector<Individuum> removeRandomInds(Vector<Individuum> indsParam, int anzahl) {
		int counter = 0;
		Vector<Individuum> indsRandom = new Vector<Individuum>();
		Individuum ind = null;

		while (counter < anzahl) {
			ind = indsParam.get(getRandomInt(1, indsParam.size()));

			if (!indsRandom.contains(ind)) {
				indsParam.remove(ind);
				indsRandom.add(ind);
				counter++;
			}
		}

		return indsRandom;
	}

	public Vector<Individuum> reducingLives(Vector<Individuum> indsParam, int lives){
		for (int i = 0; i < indsParam.size(); i++) {
			indsParam.get(i).reducingLives();
			
			if(indsParam.get(i).getLiveCounter()<1){
				indsParam.remove(i);
				indsParam.addAll(fillWithRandomNumbers(1, 0, 31));
			}
		}
		
		return indsParam;
	}
	
}
