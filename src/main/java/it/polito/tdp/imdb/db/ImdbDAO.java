package it.polito.tdp.imdb.db;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import it.polito.tdp.imdb.model.Actor;
import it.polito.tdp.imdb.model.Adiacenza;
import it.polito.tdp.imdb.model.Director;
import it.polito.tdp.imdb.model.Movie;

public class ImdbDAO {
	
	public void listAllActors(Map<Integer, Actor> idMap){
		String sql = "SELECT * FROM actors";
		//List<Actor> result = new ArrayList<Actor>();
		Connection conn = DBConnect.getConnection();

		try {
			PreparedStatement st = conn.prepareStatement(sql);
			ResultSet res = st.executeQuery();
			while (res.next()) {

				if(!idMap.containsKey(res.getInt("id"))) {
				Actor actor = new Actor(res.getInt("id"), res.getString("first_name"), res.getString("last_name"),
						res.getString("gender"));
				idMap.put(actor.getId(), actor);
				//result.add(actor);
			}
			}
			conn.close();
			//return result;
			
		} catch (SQLException e) {
			e.printStackTrace();
			//return null;
		}
	}
	
	public List<Movie> listAllMovies(){
		String sql = "SELECT * FROM movies";
		List<Movie> result = new ArrayList<Movie>();
		Connection conn = DBConnect.getConnection();

		try {
			PreparedStatement st = conn.prepareStatement(sql);
			ResultSet res = st.executeQuery();
			while (res.next()) {

				Movie movie = new Movie(res.getInt("id"), res.getString("name"), 
						res.getInt("year"), res.getDouble("rank"));
				
				result.add(movie);
			}
			conn.close();
			return result;
			
		} catch (SQLException e) {
			e.printStackTrace();
			return null;
		}
	}
	
	
	public List<Director> listAllDirectors(){
		String sql = "SELECT * FROM directors";
		List<Director> result = new ArrayList<Director>();
		Connection conn = DBConnect.getConnection();

		try {
			PreparedStatement st = conn.prepareStatement(sql);
			ResultSet res = st.executeQuery();
			while (res.next()) {

				Director director = new Director(res.getInt("id"), res.getString("first_name"), res.getString("last_name"));
				
				result.add(director);
			}
			conn.close();
			return result;
			
		} catch (SQLException e) {
			e.printStackTrace();
			return null;
		}
	}
	
	public List <String> getGeneri(){
		
		String sql = "SELECT DISTINCT g.genre "
				+ "FROM movies_genres g "
				+ "ORDER BY g.genre";
		List<String> result = new ArrayList<>();
		Connection conn = DBConnect.getConnection();

		try {
			PreparedStatement st = conn.prepareStatement(sql);
			ResultSet res = st.executeQuery();
			while (res.next()) {
		
				result.add(res.getString("g.genre"));
			}
			conn.close();
			return result;
			
		} catch (SQLException e) {
			e.printStackTrace();
			return null;
		}
	}
	
	public List <Actor> getVertici(String genere, Map<Integer, Actor> idMap){
		String sql = "SELECT DISTINCT(r.actor_id) "
				+ "FROM movies_genres m , roles r "
				+ "WHERE m.movie_id = r.movie_id AND m.genre=? "
				+"ORDER BY r.actor_id ";
		List<Actor> result = new ArrayList<>();
		Connection conn = DBConnect.getConnection();

		try {
			PreparedStatement st = conn.prepareStatement(sql);
			st.setString(1, genere);
			ResultSet res = st.executeQuery();
			while (res.next()) {
		
				if(idMap.containsKey(res.getInt("r.actor_id"))) {
					result.add(idMap.get(res.getInt("r.actor_id")));
				}
				
			}
			conn.close();
			return result;
			
		} catch (SQLException e) {
			e.printStackTrace();
			return null;
		}
	}
	
	public List <Adiacenza> getAdiacenze( String genere, Map <Integer, Actor> idMap){
		String sql = "SELECT r1.actor_id, r2.actor_id, m1.movie_id, COUNT(m1.movie_id) AS peso "
				+ "FROM movies_genres m1, roles r1, movies_genres m2, roles r2 "
				+ "WHERE m1.movie_id= r1.movie_id AND m2.movie_id=r2.movie_id AND m1.genre=? AND "
				+ "m2.genre= m1.genre AND m1.movie_id=m2.movie_id AND r1.actor_id> r2.actor_id "
				+ "GROUP BY r1.actor_id, r2.actor_id";
		List<Adiacenza> result = new ArrayList<>();
		Connection conn = DBConnect.getConnection();

		try {
			PreparedStatement st = conn.prepareStatement(sql);
			st.setString(1, genere);
			ResultSet res = st.executeQuery();
			while (res.next()) {
		
				if(idMap.containsKey(res.getInt("r1.actor_id")) &&
				   idMap.containsKey(res.getInt("r1.actor_id"))) {
					
					Actor a1 = idMap.get(res.getInt("r1.actor_id"));
					Actor a2 = idMap.get(res.getInt("r2.actor_id"));
					double peso = res.getDouble("peso");
					Adiacenza a = new Adiacenza(a1,a2,peso);
					result.add(a);
				}
				
			}
			conn.close();
			return result;
			
		} catch (SQLException e) {
			e.printStackTrace();
			return null;
		}
	}
	
	
}
