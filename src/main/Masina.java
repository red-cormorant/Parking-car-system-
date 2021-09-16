package main;

public class Masina {

	private int timp; //timpul in care o masina este parcata
	

	public Masina(int timp){

		this.timp = timp;
	}

	/**
	 * un timer pentru masini sa fie parcate in ParcareAuto. Daca time <= 0 atunci
	 *a expirat timpul si functia intoarce  true
	 */
	public boolean timer(){
		timp--;
		
		if(timp <= 0){
			return true;
		}
		
		return false;
	}

	
	/**
	 * Getter si setter pentru timp
	 */
	public int getTimp() {
		return timp;
	}

	public void setTimp(int timp) {
		this.timp = timp;
	}
}
