package tiat;
public class Main {

	public static void main(String[] args) {
//		IndividuumManager ic = new IndividuumManager( 5);
//		ic.fillWithRandomNumbers(30, 0, 31);
//		ic.printSelects(ic.getInds());
//		ic.print();
////		ic.singlePoint(2);
//		ic.printSelects();
//		ic.mutateInds(IndividuumManager.getRandomDouble());
		
		GeneAlg alg = new GeneAlg();
		alg.doWorkWithRekombination(0.01);
//		alg.doWork(0.1);
//		alg.doWork(0.01);
//		alg.doWork(0.005);
//		alg.doWork(0.02);
//		alg.doWork(0.2);
		alg.showOnDiagramm();
	}
	
	

}
