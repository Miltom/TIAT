public class Main {

	public static void main(String[] args) {
		IndividuumManager ic = new IndividuumManager(30, 5);
		ic.fillWithRandomNumbers(0, 31);
		ic.print();
		ic.singlePoint(2);
		ic.printSelects();
		ic.mutateInds(IndividuumManager.getRandomDouble());
	}

}
