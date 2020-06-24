package it.polito.tdp.seriea.model;

public class SquadraTifo {
	
	private Team team;
	private Integer tifosi;
	private Integer punti;
	
	public SquadraTifo(Team team, Integer tifosi, Integer punti) {
		super();
		this.team = team;
		this.tifosi = tifosi;
		this.punti = punti;
	}
	
	public Team getTeam() {
		return team;
	}
	
	public void setTeam(Team team) {
		this.team = team;
	}
	
	public Integer getTifosi() {
		return tifosi;
	}
	
	public void incrementoTifosi(Integer tifosi) {
		this.tifosi += tifosi;
	}
	
	public void decrementoTifosi(Integer t) {
		this.tifosi = this.tifosi - t;
	}
	
	public Integer getPunti() {
		return punti;
	}
	
	public void setPunti(Integer punti) {
		this.punti += punti;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((team == null) ? 0 : team.hashCode());
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		SquadraTifo other = (SquadraTifo) obj;
		if (team == null) {
			if (other.team != null)
				return false;
		} else if (!team.equals(other.team))
			return false;
		return true;
	}
	
}
