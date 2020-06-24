package it.polito.tdp.seriea.model;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.PriorityQueue;

import it.polito.tdp.seriea.db.SerieADAO;
import it.polito.tdp.seriea.model.Event.EventType;

public class Simulator {
	
	// CODA DELI EVENTI
	private PriorityQueue<Event> queue;
	
	// STATO DEL SISTEMA
	private List<MatchStagione> partite;
	
	// OUTPUT DA CALCOLARE
	private Map<Team, SquadraTifo> squadre;
	
	// PARAMETRI DELLA SIMULAZIONE
	private SerieADAO dao;
	private Integer P;
	
	public void init(Integer stagione, Map<String, Team> idMap) {
		this.queue = new PriorityQueue<>();
		this.dao = new SerieADAO();
		this.P = 10;
		
		this.partite = new ArrayList<>(dao.getMatchStagione(stagione, idMap));
		this.squadre = new HashMap<>();
		
		for(SquadraTifo s: dao.getSquadre(stagione, idMap))
			this.squadre.put(s.getTeam(), s);
		
		for(MatchStagione m: this.partite) {
			Event e = new Event(m.getDate(), EventType.PARTITA, m);
			this.queue.add(e);
		}
	}
	
	public void run() {
		while(!this.queue.isEmpty()) {
			Event e = queue.poll();
			processEvent(e);
		}
	}

	private void processEvent(Event e) {
		switch(e.getType()) {
		case PARTITA:
			SquadraTifo tH = this.squadre.get(e.getMatch().gettH());
			SquadraTifo tA = this.squadre.get(e.getMatch().gettA());
			
			// controllo chi ha più tifosi e nel caso la porb del goal in meno
			if(tH.getTifosi() < tA.getTifosi()) {
				double probGoalInMeno = 1 - (tH.getTifosi() / tA.getTifosi());
				double r = Math.random();
				if(probGoalInMeno > r)
					e.getMatch().decrementogH();
			}
			if(tH.getTifosi() > tA.getTifosi()) {
				double probGoalInMeno = 1 - (tA.getTifosi() / tH.getTifosi());
				double r = Math.random();
				if(probGoalInMeno > r)
					e.getMatch().decrementogA();
			}
			
			// stabilisco il risultato finale e sposto i tifosi
			if(e.getMatch().getgH() > e.getMatch().getgA()) {
				tH.setPunti(3);
				Integer tifosiDaSpostare = this.P * (e.getMatch().getgH() - e.getMatch().getgA());
				tH.incrementoTifosi(tifosiDaSpostare);
				tA.decrementoTifosi(tifosiDaSpostare);
			}
			if(e.getMatch().getgH() < e.getMatch().getgA()) {
				tA.setPunti(3);
				Integer tifosiDaSpostare = this.P * (e.getMatch().getgA() - e.getMatch().getgH());
				tA.incrementoTifosi(tifosiDaSpostare);
				tH.decrementoTifosi(tifosiDaSpostare);
			}
			if(e.getMatch().getgH() == e.getMatch().getgA()) {
				tH.setPunti(1);
				tA.setPunti(1);
			}
			break;
		}
	}

	public List<SquadraTifo> getSquadre() {
		List<SquadraTifo> l = new ArrayList<>(squadre.values());
		return l;
	}
	
}
