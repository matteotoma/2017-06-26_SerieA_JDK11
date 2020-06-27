package it.polito.tdp.seriea.model;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.jgrapht.Graph;
import org.jgrapht.Graphs;
import org.jgrapht.graph.DefaultWeightedEdge;
import org.jgrapht.graph.SimpleWeightedGraph;

import it.polito.tdp.seriea.db.SerieADAO;

public class Model {
	
	private SerieADAO dao;
	private Graph<Team, DefaultWeightedEdge> grafo;
	private Map<String, Team> idMap;
	private Simulator sim;
	
	public Model() {
		this.dao = new SerieADAO();
		this.idMap = new HashMap<>();
	}
	
	public void creaGrafo() {
		this.grafo = new SimpleWeightedGraph<>(DefaultWeightedEdge.class);
		
		dao.listTeams(idMap);
		for(Adiacenza a: dao.getAdiacenze(idMap)) {
			if(!grafo.containsVertex(a.getT1()))
				grafo.addVertex(a.getT1());
			if(!grafo.containsVertex(a.getT2()))
				grafo.addVertex(a.getT2());
			if(grafo.getEdge(a.getT1(), a.getT2()) == null)
				Graphs.addEdge(grafo, a.getT1(), a.getT2(), a.getPeso());
		}
	}
	
	public List<Adiacenza> getVicini(Team source){
		List<Adiacenza> result = new ArrayList<>();
		for(Team vicino: Graphs.neighborListOf(grafo, source))
			result.add(new Adiacenza(source, vicino, (int) grafo.getEdgeWeight(grafo.getEdge(source, vicino))));
		Collections.sort(result);
		return result;
	}
	
	public void simula(Integer s) {
		this.sim = new Simulator();
		sim.init(dao.getMatches(s, idMap), dao.getSquadre(s, idMap));
		sim.run();
	}
	
	public List<SquadraTifo> getClassifica(){
		List<SquadraTifo> result = new ArrayList<>(sim.getSquadre().values());
		return result;
	}
	
	public List<Season> getSeasons(){
		return dao.listSeasons();
	}
	
	public List<Team> getVertici(){
		List<Team> result = new ArrayList<>(grafo.vertexSet());
		return result;
	}
	
	public int nVertici() {
		return this.grafo.vertexSet().size();
	}
	
	public int nArchi() {
		return grafo.edgeSet().size();
	}

}
