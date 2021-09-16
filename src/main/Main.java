package main;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class Main {
		
	public static void main(String args[]){
		CoadaMasini intrariParcare[] = new CoadaMasini[2]; //se initializeaza cele 2 intrari ale parcarii
		

		
		intrariParcare[0] = new CoadaMasini("Nivel 1");  //prima intrare a parcarii
		intrariParcare[0].setActive(true); //se activeaza intrarea
		intrariParcare[1] = new CoadaMasini("Nivel 2"); //a doua intrare a parcarii
		intrariParcare[1].setActive(true); //se activeaza intrarea

		
		ParcareAuto parcareAuto = new ParcareAuto(intrariParcare);
		parcareAuto.setActive(true); //se activeaza parcarea, este disponibila pentru soferi


		ExecutorService executor = Executors.newFixedThreadPool(3); //maxim 3 thread-uri care pot rula simultan
		
		executor.execute(intrariParcare[0]); //un thread pentru nivelul 1
		executor.execute(intrariParcare[1]); //un thread pentru nivelul 2
		executor.execute(parcareAuto); //un thread pentru parcare

	}
}
