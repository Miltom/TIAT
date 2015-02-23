public class Individuum {

	private String h;
	private String d;
	private int hReel;
	private int dReel;
	private double f;
	private double g;
	private int id;

	public Individuum(int id, int h, int d, int bits) {
		// TODO reel?
		this.id = id;
		this.hReel = h;
		this.dReel = d;
		this.h = checkZeros(Integer.toBinaryString(h), bits);
		this.d = checkZeros(Integer.toBinaryString(d), bits);
		this.f = fitnessFunction();
		this.g = gAusrechnen();
	}

	public int getId() {
		return id;
	}

	private double fitnessFunction() {
		return ((Math.PI * (dReel ^ 2)) / 2 + (Math.PI * dReel * hReel));
	}

	// TODO nebendbedingung überprüfen ob verletzt
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

	@Override
	public String toString() {
		return id + ". Individuum:\nh=" + h + " (" + hReel + ")\n" + "d=" + d + " (" + dReel + ")\n" + "f=" + f + "\ng=" + g;
	}

}