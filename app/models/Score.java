package models;

import java.sql.Timestamp;

import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Version;

import play.db.ebean.Model;

@Entity
public class Score extends Model {

	private static final long serialVersionUID = 1L;

	@Id
	public Long id;
	public String name;
	public Long score;

	@Version
	public Timestamp lastUpdate;

	public Score(String name, Long score) {
		this.name = name;
		this.score = score;
	}
	
	public static Finder<Long, Score> find = new Finder<Long, Score>(
			Long.class, Score.class);

}
