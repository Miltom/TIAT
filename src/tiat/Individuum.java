package tiat;

public class Individuum implements Comparable<Individuum>, Cloneable{

	private String h;
	private String d;
	private int hReel;
	private int dReel;
	private double f;
	private double g;
	private int id;
	private int bits;
	private double prozentualVon;
	private double prozentualBis;
	private double gewicht;

	public Individuum(int id, int h, int d, int bits) {
		this.id = id;
		this.hReel = h;
		this.dReel = d;
		this.h = checkZeros(Integer.toBinaryString(h), bits);
		this.d = checkZeros(Integer.toBinaryString(d), bits);
		this.f = fitnessFunction();
		this.g = gAusrechnen();
		this.prozentualBis = 0;
		this.prozentualVon = 0;
		this.gewicht = 0;
	}

	public void auswerten(String h, String d) {
		this.h = h;
		this.d = d;
		this.hReel = getDekodH();
		this.dReel = getDekodD();
		this.f = fitnessFunction();
		this.g = gAusrechnen();
	}

	public void auswerten() {
		this.h = checkZeros(Integer.toBinaryString(hReel), bits);
		this.d = checkZeros(Integer.toBinaryString(dReel), bits);
		this.f = fitnessFunction();
		this.g = gAusrechnen();
	}

	public int getId() {
		return id;
	}

	private double fitnessFunction() {
		return ((Math.PI * (dReel ^ 2)) / 2 + (Math.PI * dReel * hReel));
	}

	private double gAusrechnen() {
		return (Math.PI * (dReel ^ 2) * hReel) / 4;
	}

	private String checkZeros(String bin, int bits) {
		while (bin.length() < bits) {
			bin = "0" + bin;
		}

		return bin;
	}

	public int getDekodH() {
		System.out.println("h: "+h);
		return Integer.parseInt(h, 2);
	}

	public int getDekodD() {
		return Integer.parseInt(d, 2);
	}

	public String getBinDAndH() {
		return d + h;
	}

	public double getG() {
		return g;
	}

	/**
	 * Binäres d
	 * 
	 * @return Binäres d
	 */
	public String getD() {
		return d;
	}

	public void setD(String d) {
		this.d = d;
	}

	public void setH(String h) {
		this.h = h;
	}

	/**
	 * Binäres h
	 * 
	 * @return Binäres h
	 */
	public String getH() {
		return h;
	}

	public double getF() {
		return f;
	}

	public void setProzentualBis(double prozentualBis) {
		this.prozentualBis = prozentualBis;
	}

	public void setProzentualVon(double prozentualVon) {
		this.prozentualVon = prozentualVon;
	}

	public double getProzentualVon() {
		return prozentualVon;
	}

	public double getProzentualBis() {
		return prozentualBis;
	}

	public double getGewicht() {
		return gewicht;
	}

	public void setGewicht(double gewicht) {
		this.gewicht = gewicht;
	}
	
	@Override
	public String toString() {
		return id + ". Individuum:\nh=" + h + " (" + hReel + ")\n" + "d=" + d + " (" + dReel + ")\n" + "f=" + f + "\ng=" + g;
	}

	@Override
	public int compareTo(Individuum o) {
			return ((Integer)getId()).compareTo(((Integer)o.getId()));
	}
	//Defined clone method in Department class.
	@Override
	protected Object clone() throws CloneNotSupportedException {
	    return super.clone();
	}

}