package tiat;

import java.util.Random;
import java.util.Vector;

public class IndividuumManager {

	private Vector<Individuum> inds;
	private Vector<Individuum> indsRang;
	private int bits;
	private int min;
	private int max;

	public IndividuumManager(int bits) {
		this.inds = new Vector<>();
		this.indsRang = new Vector<Individuum>();
		this.bits = bits;
	}

	public void auswerten() {
		for (int i = 0; i < inds.size(); i++) {
			inds.get(i).auswerten();
		}
	}

	public void fillWithRandomNumbers(int countOfInds, int min, int max) {
		this.min = min;
		this.max = max;

		for (int i = 0; i < countOfInds; i++) {
			inds.add(new Individuum(i, getRandomInt(min, max), getRandomInt(min, max), bits));
		}
	}

	public Individuum createIndividuum(int id) {
		return new Individuum(id, getRandomInt(min, max), getRandomInt(min, max), bits);
	}

	public boolean passedCondition(int id) {
		return inds.get(id).getG() >= 300;
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

	public Vector<Individuum> getSelects() {
		indsRang.clear();

		for (int i = 0; i < inds.size(); i++) {
			if (passedCondition(i)) {
				sortAndAdd(inds.get(i));
			}
		}

		if (indsRang.size() < 30) {
			fillRang();
		}

		// TODO Kuchen, prozentuel rechnen etc
		return indsRang;
	}

	private void kuchendiagramm() {

	}

	/**
	 * Vergleichen und sortiertes einfügen
	 * 
	 * @param ind
	 *            Individuum
	 */
	private void sortAndAdd(Individuum ind) {
		int placeCount = 0;

		while (placeCount < indsRang.size() && ind.getF() > indsRang.get(placeCount).getF()) {
			placeCount++;
		}

		indsRang.add(placeCount, ind);
	}

	private void fillRang() {
		int counter = 31;

		while (indsRang.size() < 30) {
			Individuum id = createIndividuum(counter);

			if (passedCondition(id)) {
				sortAndAdd(id);
			}

			counter++;
		}
	}

	public void printSelects() {
		getSelects();
		int counter = 1;

		System.out.println("-------------------------------------");
		System.out.println("Selektion Rang");

		for (Individuum ind : indsRang) {
			System.out.println(counter + ". g:" + ind.getG() + ", f: " + ind.getF() + " (id=" + ind.getId() + ")");
			counter++;
		}
	}

	public void print() {
		for (int i = 0; i < inds.size(); i++) {
			System.out.println(inds.get(i).toString());
			System.out.println("d+h->" + inds.get(i).getBinDAndH());
			System.out.println("Deko. d: " + inds.get(i).getDekodD());
			System.out.println("Deko. h: " + inds.get(i).getDekodH());
			System.out.println("\n");
		}

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

	public void mutateInds(double pm) {
		System.out.println("-------------------------------------------------------");
		System.out.println("Mutation: pm = " + pm);
		String first = "", second = "";
		
		for (int i = 0; i < inds.size(); i++) {
			first = mutation(pm, inds.get(i).getD());
			second = mutation(pm, inds.get(i).getH());
			// TODO evlt eleganter
			inds.get(i).auswerten(first, second);
		}

		System.out.println("end mutation");
		System.out.println("-------------------------------------------------------");
	}

	public String mutation(double pm, String binaer) {
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

		return null;
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
	private String mutationString(String binaer, int index) {
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

	public void singlePoint(int index) {
		singlePoint(inds.get(index));
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

	public Individuum getBestIndividuum() {
		//TODO nach der verbesserung von selektion rang: hier getlast anstatt 0
		return indsRang.get(0);
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
			}
		}

		return indsRandom;
	}

}