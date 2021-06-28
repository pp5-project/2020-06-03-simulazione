package it.polito.tdp.PremierLeague.model;

public class Battuti implements Comparable<Battuti> {
	Player p;
	int delta;
	public Battuti(Player p, int delta) {
		super();
		this.p = p;
		this.delta = delta;
	}
	public Player getP() {
		return p;
	}
	public void setP(Player p) {
		this.p = p;
	}
	public int getDelta() {
		return delta;
	}
	public void setDelta(int delta) {
		this.delta = delta;
	}
	@Override
	public String toString() {
		return p+"||"+delta+"\n";
	}
	@Override
	public int compareTo(Battuti arg0) {
		// TODO Auto-generated method stub
		return this.delta-arg0.delta;
	}
	

}
