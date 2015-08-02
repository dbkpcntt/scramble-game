package models;

import java.sql.Timestamp;

import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Version;

import play.db.ebean.Model;

/**
 * This is Word model
 * 
 * @author phuongdbk
 * @version 1.0
 * @since 2015-07-28
 */
@Entity
public class Word extends Model {

	private static final long serialVersionUID = 1L;

	@Id
	public String word;

	public static Finder<Long, Word> find = new Finder<Long, Word>(
			Long.class, Word.class);

}
