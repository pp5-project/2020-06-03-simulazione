package it.polito.tdp.PremierLeague.model;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.jgrapht.Graphs;
import org.jgrapht.graph.DefaultWeightedEdge;
import org.jgrapht.graph.SimpleDirectedWeightedGraph;

import it.polito.tdp.PremierLeague.db.PremierLeagueDAO;

public class Model {
	private Map<Integer, Player> idMap;
	private PremierLeagueDAO dao;
	private SimpleDirectedWeightedGraph<Player, DefaultWeightedEdge> grafo;
	private Player top;
	private List<Player> percorsoBest;
	private int bestPeso;
	
	public Player getTop() {
		return top;
	}

	public void setTop(Player top) {
		this.top = top;
	}

	public Model(){
		dao=new PremierLeagueDAO();
		idMap=new HashMap<Integer,Player>();
		dao.listAllPlayers(idMap);
					
				}
	
	public void CreaGrafo(double goal) {
		this.grafo=new SimpleDirectedWeightedGraph<>(DefaultWeightedEdge.class);
		//aggiungo vertici
		Graphs.addAllVertices(grafo,dao.getVertici(goal, idMap));
		//aggiungo archi
		for(Arco a:dao.getArchi(idMap)) {
			if(grafo.containsVertex(a.getDue()) && grafo.containsVertex(a.getUno())){
			if(a.getPeso()>0) 
				Graphs.addEdge(grafo, a.getUno(), a.getDue(), a.getPeso());
				else if(a.getPeso()<0) {
					Graphs.addEdge(grafo, a.getDue(), a.getUno(), Math.abs(a.getPeso()));
				}
			}
		}
		}
	
	public List<Battuti> battuti(){
		List<Battuti> result=new LinkedList<Battuti>();
		top=null;
		int max=0;
		
		for(Player p:grafo.vertexSet()) {
		  	if(grafo.outDegreeOf(p)>max) {
			  max=grafo.outDegreeOf(p);
			  top=p;
		     }
		   }
		for(DefaultWeightedEdge e:grafo.outgoingEdgesOf(top)) {
			result.add(new Battuti(Graphs.getOppositeVertex(grafo, e, top), (int) grafo.getEdgeWeight(e)));
		}
		Collections.sort(result);
		return result;
		
	}
	public List<Player> percorso(int K) {
		this.bestPeso=0;
		this.percorsoBest=new ArrayList<>();
		List<Player> parziale=new ArrayList<Player>();
		
		recursive(parziale,new ArrayList<Player>(this.grafo.vertexSet()),K);
		return this.percorsoBest;
	}
	
	public void recursive(List<Player> partial, List<Player> players, int k) {
		if(partial.size() == k) {
			int degree = this.getDegree(partial);
			if(degree > this.bestPeso) {
				percorsoBest = new ArrayList<>(partial);
				bestPeso = degree;
			}
			return;
		}
		
		for(Player p : players) {
			if(!partial.contains(p)) {
				partial.add(p);
				//i "battuti" di p non possono più essere considerati
				List<Player> remainingPlayers = new ArrayList<>(players);
				remainingPlayers.removeAll(Graphs.successorListOf(grafo, p));
				recursive(partial, remainingPlayers, k);
				partial.remove(p);
				
			}
		}
	}
	
	private int getDegree(List<Player> team) {
		int degree = 0;
		int in;
		int out;

		for(Player p : team) {
			in = 0;
			out = 0;
			for(DefaultWeightedEdge edge : this.grafo.incomingEdgesOf(p))
				in += (int) this.grafo.getEdgeWeight(edge);
			
			for(DefaultWeightedEdge edge : grafo.outgoingEdgesOf(p))
				out += (int) grafo.getEdgeWeight(edge);
		
			degree += (out-in);
		}
		return degree;
	}

	public Integer getBestDegree() {
		return this.bestPeso;
	}


	

	public int nVertici() {
		// TODO Auto-generated method stub
		return this.grafo.vertexSet().size();
	}
	public int nArchi() {
		// TODO Auto-generated method stub
		return this.grafo.edgeSet().size();
	}
	
	
	public Set<Player> getVertici(){
		return this.grafo.vertexSet();
	}

}
