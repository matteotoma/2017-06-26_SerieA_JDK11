package it.polito.tdp.seriea.model;

import java.util.ArrayList;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.PriorityQueue;

import it.polito.tdp.seriea.model.Event.EventType;

public class Simulator {
	
	// CODA DEGLI EVENTI
	private PriorityQueue<Event> queue;
	
	// PARAMETRI DELLA SIMULAZIONE
	private Integer P;
	
	// STATO DEL SISTEMA
	private List<MatchStagione> matches;
	
	// OUTPUT DA CALCOLARE
	private Map<Team, SquadraTifo> squadre;
	
	public void init(List<MatchStagione> m, List<SquadraTifo> s) {
		this.queue = new PriorityQueue<>();
		this.P = 10;
		this.matches = new ArrayList<>(m);
		this.squadre = new HashMap<>();
		
		for(SquadraTifo sT: s)
			this.squadre.put(sT.getTeam(), sT);
		
		for(MatchStagione match: matches) {
			Event e = new Event(match.getDate(), EventType.PARTITA, match);
			queue.add(e);
		}
	}
	
	public void run() {
		while(!this.queue.isEmpty()) {
			Event e = queue.poll();
			System.out.println(e.getMatch());
			processEvent(e);
		}
	}

	private void processEvent(Event e) {
		switch(e.getType()) {
		case PARTITA:
			Team home = e.getMatch().gettH();
			Team away = e.getMatch().gettA();
			
			// controllo chi ha fatto pià goal e modifico il risultato
			if(squadre.get(home).getTifosi() < squadre.get(away).getTifosi()) {
				double probGoalMeno = 1 - (squadre.get(home).getTifosi() / squadre.get(away).getTifosi());
				if(probGoalMeno > Math.random())
					e.getMatch().decrementogH();
			}
			if(squadre.get(away).getTifosi() < squadre.get(home).getTifosi()) {
				double probGoalMeno = 1 - (squadre.get(away).getTifosi() / squadre.get(home).getTifosi());
				if(probGoalMeno > Math.random())
					e.getMatch().decrementogA();
			}
			
			// controllo chi ha vinto e sposto i tifosi
			if(e.getMatch().getgH() > e.getMatch().getgA()) {
				squadre.get(home).setPunti(3);
				double percentualeTifosi = (double) P * (e.getMatch().getgH() - e.getMatch().getgA()) / 100;
				int tifosiSpostano = (int) (percentualeTifosi * squadre.get(away).getTifosi());
				System.out.println(tifosiSpostano+ " "+percentualeTifosi);
				squadre.get(home).incrementoTifosi(tifosiSpostano);
				squadre.get(away).decrementoTifosi(tifosiSpostano);
			}
			else if(e.getMatch().getgH() < e.getMatch().getgA()) {
				squadre.get(away).setPunti(3);
				double percentualeTifosi = (double) (e.getMatch().getgA() - e.getMatch().getgH()) * P / 100;
				int tifosiSpostano = (int) (percentualeTifosi * squadre.get(home).getTifosi());
				System.out.println(tifosiSpostano+ " "+percentualeTifosi);
				squadre.get(away).incrementoTifosi(tifosiSpostano);
				squadre.get(home).decrementoTifosi(tifosiSpostano);
			}
			else {
				squadre.get(home).setPunti(1);
				squadre.get(away).setPunti(1);
			}
			
			break;
		}
	}

	public Map<Team, SquadraTifo> getSquadre() {
		return squadre;
	}
	
}
