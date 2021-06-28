package it.polito.tdp.PremierLeague.model;

public class Arco {
	private Player uno;
	private Player due;
	private int peso;
	
	public Player getUno() {
		return uno;
	}
	public void setUno(Player uno) {
		this.uno = uno;
	}
	public Player getDue() {
		return due;
	}
	public void setDue(Player due) {
		this.due = due;
	}
	public int getPeso() {
		return peso;
	}
	public void setPeso(int peso) {
		this.peso = peso;
	}
	public Arco(Player uno, Player due, int peso) {
		super();
		this.uno = uno;
		this.due = due;
		this.peso = peso;
	}

}
