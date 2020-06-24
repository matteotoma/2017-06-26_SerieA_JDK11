package it.polito.tdp.seriea.model;

import java.time.LocalDate;

public class MatchStagione {
	
	private Team tH;
	private Team tA;
	private Integer gH;
	private Integer gA;
	private LocalDate date;
	
	public MatchStagione(Team tH, Team tA, Integer gH, Integer gA, LocalDate date) {
		super();
		this.tH = tH;
		this.tA = tA;
		this.gH = gH;
		this.gA = gA;
		this.date = date;
	}

	public Team gettH() {
		return tH;
	}

	public void settH(Team tH) {
		this.tH = tH;
	}

	public Team gettA() {
		return tA;
	}

	public void settA(Team tA) {
		this.tA = tA;
	}

	public Integer getgH() {
		return gH;
	}

	public void incrementogH() {
		this.gH++;
	}
	
	public void decrementogH() {
		if(this.gH == 0)
			return;
		else
			this.gH--;
	}

	public Integer getgA() {
		return gA;
	}

	public void incrementogA() {
		this.gA++;
	}
	
	public void decrementogA() {
		if(this.gA == 0)
			return;
		else
			this.gA--;
	}

	public LocalDate getDate() {
		return date;
	}

	public void setDate(LocalDate date) {
		this.date = date;
	}

}
