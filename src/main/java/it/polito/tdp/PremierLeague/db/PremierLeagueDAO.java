package it.polito.tdp.PremierLeague.db;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import it.polito.tdp.PremierLeague.model.Action;
import it.polito.tdp.PremierLeague.model.Arco;
import it.polito.tdp.PremierLeague.model.Player;

public class PremierLeagueDAO {
	
	public void listAllPlayers(Map<Integer, Player> idMap){
		String sql = "SELECT * FROM Players";
		
		Connection conn = DBConnect.getConnection();

		try {
			PreparedStatement st = conn.prepareStatement(sql);
			ResultSet res = st.executeQuery();
			while (res.next()) {
				if(!idMap.containsKey(res.getInt("PlayerID"))) {
				Player player = new Player(res.getInt("PlayerID"), res.getString("Name"));
				
				idMap.put(player.getPlayerID(), player);
			}
			conn.close();
			}}
			
		catch (SQLException e) {
			e.printStackTrace();
			
		}
	}
	
	public List<Action> listAllActions(){
		String sql = "SELECT * FROM Actions";
		List<Action> result = new ArrayList<Action>();
		Connection conn = DBConnect.getConnection();

		try {
			PreparedStatement st = conn.prepareStatement(sql);
			ResultSet res = st.executeQuery();
			while (res.next()) {

				Action action = new Action(res.getInt("PlayerID"),res.getInt("MatchID"),res.getInt("TeamID"),res.getInt("Starts"),res.getInt("Goals"),
						res.getInt("TimePlayed"),res.getInt("RedCards"),res.getInt("YellowCards"),res.getInt("TotalSuccessfulPassesAll"),res.getInt("totalUnsuccessfulPassesAll"),
						res.getInt("Assists"),res.getInt("TotalFoulsConceded"),res.getInt("Offsides"));
				
				result.add(action);
			}
			conn.close();
			return result;
			
		} catch (SQLException e) {
			e.printStackTrace();
			return null;
		}
	}
	
	public List<Player> getVertici(double goal, Map<Integer, Player> idMap){
		String sql = "SELECT DISTINCT a.PlayerID, AVG(a.Goals) AS s "
				+ "FROM actions a "
				+ "GROUP BY a.PlayerID "
				+ "HAVING s>? "
				+ "";
		List<Player> result = new ArrayList<Player>();
		Connection conn = DBConnect.getConnection();

		try {
			PreparedStatement st = conn.prepareStatement(sql);
			st.setDouble(1, goal);
			ResultSet res = st.executeQuery();
			while (res.next()) {
				result.add(idMap.get(res.getInt("a.PlayerID")));								
							}
			conn.close();
			return result;
			
		} catch (SQLException e) {
			e.printStackTrace();
			return null;
		}
	}
	
	public List<Arco> getArchi(Map<Integer, Player> idMap){
		String sql = "SELECT  A1.PlayerID AS p1, A2.PlayerID AS p2, (SUM(A1.TimePlayed) - SUM(A2.TimePlayed)) AS peso "
				+ "FROM Actions A1, Actions A2 "
				+ "WHERE A1.TeamID != A2.TeamID "
				+ "AND A1.MatchID = A2.MatchID "
				+ "AND A1.starts = 1 AND A2.starts = 1 "
				+ "AND A1.PlayerID > A2.PlayerID "
				+ "GROUP BY A1.PlayerID, A2.PlayerID "
				+ "";
		List<Arco> result = new ArrayList<Arco>();
		Connection conn = DBConnect.getConnection();

		try {
			PreparedStatement st = conn.prepareStatement(sql);
			
			ResultSet res = st.executeQuery();
			while (res.next()) {
				result.add(new Arco(idMap.get(res.getInt("p1")), idMap.get(res.getInt("p2")), res.getInt("peso")));								
							}
			conn.close();
			return result;
			
		} catch (SQLException e) {
			e.printStackTrace();
			return null;
		}
	}
}
