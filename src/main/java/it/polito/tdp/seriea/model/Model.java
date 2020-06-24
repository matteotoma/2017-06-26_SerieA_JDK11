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
	
	private Graph<Team, DefaultWeightedEdge> grafo;
	private SerieADAO dao;
	private Map<String, Team> idMap;
	private Simulator sim;
	
	public Model() {
		this.dao = new SerieADAO();
		this.idMap = new HashMap<>();
	}
	
	public void creaGrafo() {
		this.grafo = new SimpleWeightedGraph<>(DefaultWeightedEdge.class);
		
		// aggiungo i vertici
		Graphs.addAllVertices(grafo, dao.listTeams(idMap));
		
		// aggiungo gli archi
		for(Adiacenza a: dao.getAdiacenze(idMap)) {
			if(!grafo.containsEdge(grafo.getEdge(a.getT1(), a.getT2())))
				Graphs.addEdge(grafo, a.getT1(), a.getT2(), a.getPeso());
			else {
				int pesoNuovo = (int) grafo.getEdgeWeight(grafo.getEdge(a.getT1(), a.getT2())) + a.getPeso();
				grafo.setEdgeWeight(grafo.getEdge(a.getT1(), a.getT2()), pesoNuovo);
			}
		}
	}
	
	public List<Adiacenza> getVicini(Team t){
		List<Adiacenza> list = new ArrayList<>();
		for(Team vicino: Graphs.neighborListOf(grafo, t))
			list.add(new Adiacenza(t, vicino, (int) grafo.getEdgeWeight(grafo.getEdge(t, vicino))));
		Collections.sort(list);
		return list;
	}
	
	public List<Season> getSeasons(){
		return dao.listSeasons();
	}
	
	public int Narchi() {
		return grafo.edgeSet().size();
	}
	
	public int Nvertici() {
		return grafo.vertexSet().size();
	}

	public List<Team> getVertici() {
		List<Team> list = new ArrayList<>(grafo.vertexSet());
		return list;
	}

	public void simula(Season s) {
		this.sim = new Simulator();
		sim.init(s.getSeason(), idMap);
		sim.run();
	}
	
	public List<SquadraTifo> getSquadre(){
		return sim.getSquadre();
	}

}
