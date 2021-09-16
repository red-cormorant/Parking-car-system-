package main;

import java.util.Random;

public class ParcareAuto implements Runnable{

	private final int CAPACITATE_PARCARE = 25; //numarul maxim de locuri disponibile
	private int counter = 0; //numar de masini parcate
	private int numarLocuriLibere = 0; //numarul de locuri de parcare libere
	private boolean activ = false; //variabila folosita pentru a activa intrarea/intrarile in parcare
	
	private Masina masiniParcate[]; //un vector de masini pentru parcare, fiecare masina/element are propriul timp in care este parcata
	private CoadaMasini queues[]; //vector de CoadaMasini pentru fiecare intrare a parcarii
	private Random random = new Random(); //instanta pentru a alege un random mai tarziu
	
	private Status vectorStatus[]; //vector pentru reprezinta un status pentru fiecare loc de parcare
	
	/**
	 * Folosim un ENUM ca status pentru descrierea unui loc de parcare
	 */
	private enum Status{
		OCUPAT,
		LIBER
	}

	/**
	 * Constructor ParcareAuto. Se initializeza un vector de masini de capacitate maxima. Se creeaza un vector pentru fiecare
	 * intrare a parcarii. Se creeaza un vector care reprezinta  statusul fiecarei masini. Se initializeaza acest vector cu LIBER,
	 * adica parcarea e goala.
	 */
	public ParcareAuto(CoadaMasini queues[]){
		this.queues = new CoadaMasini[queues.length];
		
		for(int i = 0; i < queues.length; i++){
			this.queues[i] = queues[i];
		}

		masiniParcate = new Masina[CAPACITATE_PARCARE];
		vectorStatus = new Status[CAPACITATE_PARCARE];
		
		for(int i = 0; i < CAPACITATE_PARCARE; i++){
			vectorStatus[i] = Status.LIBER;
		}
	}

	/**
	 * Getter si setter pentru activ, numarul de masini parcate si numarul de locuri ramase
	 */
	public boolean getActive() {
		return activ;
	}

	public void setActive(boolean activ) {
		this.activ = activ;
	}

	public int getCounter() {
		return counter;
	}

	public void setCounter(int counter) {
		this.counter = counter;
	}

	public int getNumarLocuriLibere() { return numarLocuriLibere; 	}

	public void setNumarLocuriLibere(int numarLocuriLibere) { this.numarLocuriLibere = numarLocuriLibere; 	}

	/**
	 * Se parcheaza masini pe primele locurile disponibile si se actualizeaza vectorul masiniParcate. Se afiseaza
	 * starea sistemului: numarul de masini parcate, pe ce loc s-a parcat, cate locuri mai sunt disponibile.
	 */
	@Override
	public void run() {
		while(activ){ //cat timp intrarea este deschisa sau sistemul accepta parcari
			try {
				Thread.sleep(0); //thread-ul este suspendat pentru a controla thread-ul/thread-urile cu prioritate mare
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
			updateMasini(); //se elibereaza locurile de parcare daca gaseste vectorStatus = OCUPAT
			
			int urmatorulLocLiber = getLocuriLibere(); //cautarea primului loc liber


			if(urmatorulLocLiber != -1) { //daca s-a gasit un loc liber
				int numarMasinaDinCoada = random.nextInt(queues.length); //se ia un numar random din coada de la 0 pana la numarul de masini din coada la un moment de timp
				Masina masina = queues[numarMasinaDinCoada].parcareMasina(); //alegem un numar random si nu primul element din coada
				//pentru ca exista 2 intrari si se blocheaza destul de repede numarul de masini in cozi(devine repetitiv)

				if(masina != null){//if true se parcheaza masina
					vectorStatus[urmatorulLocLiber] = Status.OCUPAT;
					masiniParcate[urmatorulLocLiber] = masina;
					counter++;
					numarLocuriLibere = CAPACITATE_PARCARE - counter;
					System.out.println("Masina a parcat pe locul de parcare: " + (urmatorulLocLiber+1)); //se activeaza senzorul de parcare pentru locul de parcare
				}
			}

			try {
				if(counter == 1)
					System.out.println("Avem o masina parcata; \tAu ramas " + numarLocuriLibere + " locuri de parcare libere" + "\n"); //panoul de afisaj digital
				else if (numarLocuriLibere == 1)
					System.out.println("Sunt " + counter + " masini parcate; \tA ramas un loc de parcare liber" + "\n"); //panoul de afisaj digital
				else
					System.out.println("Sunt " + counter + " masini parcate; \tAu ramas " + numarLocuriLibere + " locuri de parcare libere" + "\n"); //panoul de afisaj digital
				System.out.println("------------------------------------------------"); //s-a realizat o iteratie;
				Thread.sleep(2500); //timpul in care masina parcheaza(cat dureaza drumul de la bariera la parcare).
				// Pentru o valoare mica se parcheaza foarte repede si nu se strang masini in coada, pentru o valoare mare
				// se strang rapid masini in coada
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
		}
	}

	/**
	 * Se returneaza primul loc liber disponibil. Daca toate sunt ocupate, intoarce -1
	 */
	private int getLocuriLibere(){
		for(int i = 0; i < CAPACITATE_PARCARE; i++){
			if(vectorStatus[i] == Status.LIBER){
				return i;
			}
		}
		
		return -1;
	}

	/**
	 * Se actualizeza vectorStatus. Primul de loc parcare care este gasit
	 * ocupat devine liber si astfel masina pleaca de pe locul respectiv.
	 * Se afiseaza locul de parcare ramas liber si situatia locurilor disponibile
	 */
	private void updateMasini(){
		for(int i = 0; i < CAPACITATE_PARCARE; i++){
			if(vectorStatus[i] == Status.OCUPAT){
				if(masiniParcate[i].timer()){
					masiniParcate[i] = null;
					vectorStatus[i] = Status.LIBER;
					counter--;
					numarLocuriLibere = CAPACITATE_PARCARE - counter;
					System.out.println("Masina a plecat de pe locul de parcare: " + (i+1)); //se dezactiveaza led-ul senzorului de parcare pentru locul respectiv
				}
			}
		}
	}


}
