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
		String sql = "SELECT m.HomeTeam AS h, m.AwayTeam a, COUNT(DISTINCT m.match_id) peso " + 
				"FROM matches AS m " + 
				"WHERE m.HomeTeam>m.AwayTeam " + 
				"GROUP BY m.HomeTeam, m.AwayTeam ";
		List<Adiacenza> result = new ArrayList<>();
		Connection conn = DBConnect.getConnection();

		try {
			PreparedStatement st = conn.prepareStatement(sql);
			ResultSet res = st.executeQuery();

			while (res.next()) {
				Team home = idMap.get(res.getString("h"));
				Team away = idMap.get(res.getString("a"));
				if(away!= null && home!=null)
					result.add(new Adiacenza(home, away, res.getInt("peso")));
			}

			conn.close();
			return result;
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			return null;
		}
	}
	
	public List<MatchStagione> getMatches(Integer s, Map<String, Team> idMap){
		String sql = "SELECT m.HomeTeam AS h, m.AwayTeam a, m.FTHG gH, m.FTAG gA, m.Date d " + 
				"FROM matches AS m " + 
				"WHERE m.Season=? " +
				"ORDER BY m.Date ASC ";
		List<MatchStagione> result = new ArrayList<>();
		Connection conn = DBConnect.getConnection();

		try {
			PreparedStatement st = conn.prepareStatement(sql);
			st.setInt(1, s);
			ResultSet res = st.executeQuery();

			while (res.next()) {
				Team home = idMap.get(res.getString("h"));
				Team away = idMap.get(res.getString("a"));
				if(away!= null && home!=null)
					result.add(new MatchStagione(home, away, res.getInt("gH"), res.getInt("gA"), res.getDate("d").toLocalDate()));
			}

			conn.close();
			return result;
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			return null;
		}
	}
	
	public List<SquadraTifo> getSquadre(Integer s, Map<String, Team> idMap){
		String sql = "SELECT DISTINCT m.HomeTeam AS h " + 
				"FROM matches AS m " + 
				"WHERE m.Season=? " + 
				"ORDER BY m.HomeTeam ASC ";
		List<SquadraTifo> result = new ArrayList<>();
		Connection conn = DBConnect.getConnection();

		try {
			PreparedStatement st = conn.prepareStatement(sql);
			st.setInt(1, s);
			ResultSet res = st.executeQuery();

			while (res.next()) {
				Team home = idMap.get(res.getString("h"));
				if(home!=null)
					result.add(new SquadraTifo(home, 1000, 0));
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

