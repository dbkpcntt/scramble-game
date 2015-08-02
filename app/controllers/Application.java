package controllers;

import static play.data.Form.form;
import static play.libs.Json.toJson;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

import models.Score;
import models.Word;
import play.data.DynamicForm;
import play.data.Form;
import play.db.ebean.Model;
import play.mvc.Controller;
import play.mvc.Result;
import views.html.processWord;

public class Application extends Controller {

	/**
	 * This method is used to get the index Page which I have set to
	 * processWord.scala.html
	 * 
	 * 
	 * @return Result This method returns createAndList.scala.html
	 */
	public static Result index() {
		Form<Word> WordForm = form(Word.class);
		return ok(processWord.render(WordForm));
	}

	/**
	 * This method is used to get a random seed word from Database
	 * with length provided
	 * 
	 * @return Result This method returns data in JSON format
	 * @throws SQLException 
	 */
	public static Result seedWord() throws SQLException {
		@SuppressWarnings("unchecked")
		DynamicForm df = play.data.Form.form().bindFromRequest();

	    String length = df.get("length");
		String sql = "SELECT WORD FROM WORD_LIST WHERE LENGTH(WORD) = " + length 
					+ " ORDER BY RAND() LIMIT 1";

		String seed = "";
		Connection conn = play.db.DB.getConnection();
		try {
		    Statement stmt = conn.createStatement();
	        
		    try {
		    	 ResultSet rs = stmt.executeQuery(sql);
		    	 while (rs.next()) {
		             seed = rs.getString("word");
		    	 }
		    } finally {
		        stmt.close();
		    }
		} finally {
		    conn.close();
		}
		
		return ok(seed);
	}
	
	/**
	 * This method is used to get Word data from database
	 * 
	 * 
	 * @return Result This method returns data in JSON format
	 * @throws SQLException 
	 */
	public static Result checkWord() throws SQLException {
		@SuppressWarnings("unchecked")
		DynamicForm df = play.data.Form.form().bindFromRequest();

	    String word = df.get("word");
		String sql = "SELECT WORD FROM WORD_LIST WHERE WORD = '" + word + "'";
		String existed = "0";
		Connection conn = play.db.DB.getConnection();
		try {
		    Statement stmt = conn.createStatement();
	        
		    try {
		    	 ResultSet rs = stmt.executeQuery(sql);
		    	 while (rs.next()) {
		             String w = rs.getString("word");
		             existed = "1";
		    	 }
		    } finally {
		        stmt.close();
		    }
		} finally {
		    conn.close();
		}
		
		return ok(existed);
	}
	
	public static Result saveScore() throws SQLException {
		DynamicForm df = play.data.Form.form().bindFromRequest();

	    String name = df.get("name");
	    String score = df.get("score");
	    String done = "0";
		String sql = "INSERT INTO SCORE (NAME, SCORE) VALUES ('" + name + "', " + score + ")";

		Connection conn = play.db.DB.getConnection();
		try {
		    Statement stmt = conn.createStatement();
	        
		    try {
		    	int rs = stmt.executeUpdate(sql);
		    	if (rs > 0) {
		    		done = "1";
		    	}
		    } finally {
		        stmt.close();
		    }
		} finally {
		    conn.close();
		}

		return(ok(done));
	}

	public static Result getScores() throws SQLException {
		@SuppressWarnings("unchecked")

		String sql = " SELECT * FROM SCORE ORDER BY SCORE DESC LIMIT 5";
		List<Score> scores = new ArrayList<Score>();	
		StringBuilder returnJson = new StringBuilder("{\"score\": ");
		Connection conn = play.db.DB.getConnection();
		try {
		    Statement stmt = conn.createStatement();
	        returnJson.append("[");
		    try {
		    	ResultSet resultset = stmt.executeQuery(sql);
		    	while(resultset.next()){
					returnJson.append("{\"name\": ").append("\"").append(resultset.getString("name")).append("\", ");
					returnJson.append("\"score\": ").append("\"").append(resultset.getLong("score")).append("\"} ");
					if (!resultset.isLast()) {
						returnJson.append(", ");
					}
				}

		    } finally {
		        stmt.close();
		    }
		    returnJson.append("]}");
		} finally {
		    conn.close();
		}
		return ok(returnJson.toString());
	}

}

