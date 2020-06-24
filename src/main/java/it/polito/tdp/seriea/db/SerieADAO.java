package it.polito.tdp.seriea.db;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import it.polito.tdp.seriea.model.Adiacenza;
import it.polito.tdp.seriea.model.MatchStagione;
import it.polito.tdp.seriea.model.Season;
import it.polito.tdp.seriea.model.SquadraTifo;
import it.polito.tdp.seriea.model.Team;

public class SerieADAO {

	public List<Season> listSeasons() {
		String sql = "SELECT season, description FROM seasons";
		List<Season> result = new ArrayList<>();
		Connection conn = DBConnect.getConnection();

		try {
			PreparedStatement st = conn.prepareStatement(sql);
			ResultSet res = st.executeQuery();

			while (res.next()) {
				result.add(new Season(res.getInt("season"), res.getString("description")));
			}

			conn.close();
			return result;

		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			return null;
		}
	}

	public List<Team> listTeams(Map<String, Team> idMap) {
		String sql = "SELECT team FROM teams";
		List<Team> result = new ArrayList<>();
		Connection conn = DBConnect.getConnection();

		try {
			PreparedStatement st = conn.prepareStatement(sql);
			ResultSet res = st.executeQuery();

			while (res.next()) {
				if(!idMap.containsKey("team")) {
					Team t = new Team(res.getString("team"));
					result.add(t);
					idMap.put(t.getTeam(), t);
				}
			}

			conn.close();
			return result;
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			return null;
		}
	}
	
	public List<Adiacenza> getAdiacenze(Map<String, Team> idMap){
		String sql = "SELECT m.HomeTeam AS t1, m.AwayTeam t2, COUNT(*) peso " + 
				"FROM matches AS m " + 
				"GROUP BY m.HomeTeam, m.AwayTeam ";
		List<Adiacenza> result = new ArrayList<>();
		Connection conn = DBConnect.getConnection();

		try {
			PreparedStatement st = conn.prepareStatement(sql);
			ResultSet res = st.executeQuery();

			while (res.next()) {
				Team t1 = idMap.get(res.getString("t1"));
				Team t2 = idMap.get(res.getString("t2"));
				if(t1!=null && t2!=null) {
					Adiacenza a = new Adiacenza(t1, t2, res.getInt("peso"));
					result.add(a);
				}
			}

			conn.close();
			return result;
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			return null;
		}
	}
	
	public List<MatchStagione> getMatchStagione(Integer stagione, Map<String, Team> idMap){
		String sql = "SELECT m.HomeTeam AS t1, m.AwayTeam t2, m.FTHG gH, m.FTAG gA, m.Date d " + 
				"FROM matches AS m " + 
				"WHERE m.Season=? " + 
				"ORDER BY m.Date ASC ";
		List<MatchStagione> result = new ArrayList<>();
		Connection conn = DBConnect.getConnection();

		try {
			PreparedStatement st = conn.prepareStatement(sql);
			st.setInt(1, stagione);
			ResultSet res = st.executeQuery();

			while (res.next()) {
				Team t1 = idMap.get(res.getString("t1"));
				Team t2 = idMap.get(res.getString("t2"));
				if(t1!=null && t2!=null) {
					MatchStagione a = new MatchStagione(t1, t2, res.getInt("gH"), res.getInt("gA"), res.getDate("d").toLocalDate());
					result.add(a);
				}
			}

			conn.close();
			return result;
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			return null;
		}
	}
	
	public List<SquadraTifo> getSquadre(Integer stagione, Map<String, Team> idMap){
		String sql = "SELECT m.HomeTeam AS t1, m.AwayTeam AS t2 " + 
				"FROM matches AS m " + 
				"WHERE m.Season=? ";
		List<SquadraTifo> result = new ArrayList<>();
		List<Team> teams = new ArrayList<>();
		Connection conn = DBConnect.getConnection();

		try {
			PreparedStatement st = conn.prepareStatement(sql);
			st.setInt(1, stagione);
			ResultSet res = st.executeQuery();

			while (res.next()) {
				Team t1 = idMap.get(res.getString("t1"));
				Team t2 = idMap.get(res.getString("t2"));
				if(t1!=null && t2!=null) {
					SquadraTifo s;
					SquadraTifo s2;
					if(!teams.contains(t1)) {
						s = new SquadraTifo(t1, 1000, 0);
						teams.add(t1);
						result.add(s);						
					}
					if(!teams.contains(t2)) {
						s2 = new SquadraTifo(t2, 1000, 0);
						teams.add(t2);
						result.add(s2);
					}
				}
			}

			conn.close();
			return result;
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			return null;
		}
	}

}

