package main;

import java.util.Random;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

public class CoadaMasini implements Runnable{

	private final int NR_MAXIM_MASINI_COADA = 7; //capacitate maxima in coada
	private int counter = 0; //cate masini sunt prezente in coada
	private boolean activ = false; //initilizam intrarea in coada ca fiind interzisa
	
	private String nume; //nume pentru o intrare in coada
	private Masina coada[]; //vector de masini care asteapta in coada
	private Random random = new Random(); //instanta pentru a genera un numar ranodm
	private Lock lock = new ReentrantLock(true); //sincronizarea thread-urilor.

	/**
	 *Constructorul CoadaMasinii care atribuie un nume unei intrari si initializeaza vectorul coada cu nr maxim permis
	 */
	public CoadaMasini(String nume){
		this.nume = nume;
		
		coada = new Masina[NR_MAXIM_MASINI_COADA];
	}


	/**
	 * Getter si setter pentru nume, activ, counter
	 */
	public String getName() {
		return nume;
	}

	private void setName(String name) {
		this.nume = name;
	}

	public boolean getActive() {
		return activ;
	}

	public void setActive(boolean active) {
		this.activ = active;
	}
	public int getCount() {
		return counter;
	}
	public void setCount(int count) {
		this.counter = count;
	}

	/**
	 * se adauga masini in coada, se stabileste timpul de parcare pentru fiecare si se stabileste timpul necesar pentru adaugarea
	 * unei noi masini in coada, timp dat de Thread.sleep()
	 */
	@Override
	public void run(){
		while(activ){
			if (counter < NR_MAXIM_MASINI_COADA - 1){
				int i = random.nextInt(20) + 25; //ne asiguram ca masina sta parcata cel putin 25 de secunde unde adaugam
				//un numar random [0,19]
				
				coada[counter] = new Masina(i); //masina urmeaza sa fie parcata cu timpul i
				counter++; //crestem numarul de masini aflate in coada
			}
			
			try {
				System.out.println(nume + ": " + counter);
				int i = random.nextInt(1000) + 5000;
				
				Thread.sleep(i); //suspendam thread-ul pentru aproape 6 secunde, o masina se poate aseza in coada dupa 6 secunde
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
		}
	}
	
	/**
	 * In aceasta metoda se realizeaza sincronizarea thread-urilor. Daca sunt masini in coada, Se ridica bariera, eliminam masina din coada
	 */
	public synchronized Masina parcareMasina(){
		lock.lock(); //alocam resurse unui singur thread pentru parcare
		
		try{
			if(counter > 0) {
				counter--; //se ridica bariera si masina are acces in parcare
				
				return coada[0]; //se ia prima masina din coada
			}
			
			return null; //daca nu se afla o masina in coada, parcareMasina intoarce un obiect gol
		}finally{
			lock.unlock(); //eliberam aceea resursa pentru a lasa locul unui alt thread
		}
	}


}
