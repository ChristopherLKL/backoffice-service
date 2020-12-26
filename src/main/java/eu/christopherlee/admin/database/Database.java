package eu.christopherlee.admin.database;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

public abstract class Database {
	private static final Log log = LogFactory.getLog(Database.class);
	public static String doSelect(String champs, String table, String condition) {
		assert champs != null;
		assert table != null;

		String sql = "SELECT " + champs + " FROM " + table;
		if (condition != null) {
			sql += " WHERE " + condition;
		}

		log.info(sql);
		return sql;
	}

	public static String doInsert(String table, String champs, String valeurs) {
		assert table != null;
		assert valeurs != null;

		String sql = "INSERT INTO " + table;
		if (champs != null) {
			sql += " (" + champs + ")";
		}
		sql += " VALUES (" + valeurs + ")";

		log.info(sql);
		return sql;
	}

	public static String doUpdate(String table, String champsValeur,
			String condition) {
		assert table != null;
		assert champsValeur != null;
		assert condition != null;

		String sql = "UPDATE " + table + " SET " + champsValeur + " WHERE "
				+ condition;

		log.info(sql);
		return sql;
	}

	public static String doDelete(String table, String condition) {
		assert table != null;
		assert condition != null;

		String sql = "DELETE FROM " + table + " WHERE " + condition;

		log.info(sql);
		return sql;
	}
}
