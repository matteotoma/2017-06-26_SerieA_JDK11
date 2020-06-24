package it.polito.tdp.seriea.model;

import java.time.LocalDate;

public class Event implements Comparable<Event>{
	
	public enum EventType{
		PARTITA
	}
	
	private LocalDate time;
	private EventType type;
	private MatchStagione match;
	
	public Event(LocalDate time, EventType type, MatchStagione match) {
		super();
		this.time = time;
		this.type = type;
		this.match = match;
	}

	public LocalDate getTime() {
		return time;
	}

	public void setTime(LocalDate time) {
		this.time = time;
	}

	public EventType getType() {
		return type;
	}

	public void setType(EventType type) {
		this.type = type;
	}

	public MatchStagione getMatch() {
		return match;
	}

	public void setMatch(MatchStagione match) {
		this.match = match;
	}

	@Override
	public int compareTo(Event o) {
		return this.time.compareTo(o.getTime());
	}

}
