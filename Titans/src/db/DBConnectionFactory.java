package db;

import db.mongodb.MongoDBConnection;
import db.mysql.MySQLConnection;

public class DBConnectionFactory {
	//this could change based on the pipeline
	private static final String DEFAULT_DB = "MYSQL";
	
	//create a DBConnection based on the given db type
	public static DBConnection getDBConnection(String db) {
		switch(db) {
		case "MYSQL":
			return new MySQLConnection();
		case "MONGO":
			return new MongoDBConnection();
		default:
			throw new IllegalArgumentException("Invalid db "+db);
		}
	}
	public static DBConnection getDBConnection() {
		return getDBConnection(DEFAULT_DB);
	}
}
