package db.mysql;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.sql.Statement;



public class MySQLTableCreation {
	//this class can reset tables whenever data in db is messed up
	//run as Java application to reset db schema
	public static void main(String[] args) {
		try {
			Class.forName("com.mysql.cj.jdbc.Driver").newInstance();
			//java sql connection
			Connection conn = null;
			
			//1.connect to mysql
			try {
				System.out.println("Connecting to \n" + MySQLDBUtil.URL);
				
				conn = DriverManager.getConnection(MySQLDBUtil.URL);
			} catch (SQLException e) {
				System.out.println("SQLEXCEPTION: " + e.getMessage());
				System.out.println("SQLState "+ e.getSQLState());
				System.out.println("VendorError "+ e.getErrorCode());
			}
			if(conn == null) {
				return;
			}
			//2. after connected to DB, need to drop old tables if there is one
			Statement stmt = conn.createStatement();
			
			String sql = "DROP TABLE IF EXISTS history";
			stmt.executeUpdate(sql);
			
			sql = "DROP TABLE IF EXISTS categories";
			stmt.executeUpdate(sql);
			
			sql = "DROP TABLE IF EXISTS items";
			stmt.executeUpdate(sql);
			
			sql = "DROP TABLE IF EXISTS users";
			stmt.executeUpdate(sql);
			
			//3. create table
			sql = "CREATE TABLE items " + "(item_id VARCHAR(255) NOT NULL, " + " name VARCHAR(255), " + "rating FLOAT,"
					+ "address VARCHAR(255), " + "image_url VARCHAR(255), " + "url VARCHAR(255), " + "distance FLOAT, "
					+ " PRIMARY KEY ( item_id ))";
			stmt.executeUpdate(sql);

			sql = "CREATE TABLE categories " + "(item_id VARCHAR(255) NOT NULL, " + " category VARCHAR(255) NOT NULL, "
					+ " PRIMARY KEY ( item_id, category), " + "FOREIGN KEY (item_id) REFERENCES items(item_id))";
			stmt.executeUpdate(sql);

			sql = "CREATE TABLE users " + "(user_id VARCHAR(255) NOT NULL, " + " password VARCHAR(255) NOT NULL, "
					+ " first_name VARCHAR(255), last_name VARCHAR(255), " + " PRIMARY KEY ( user_id ))";
			stmt.executeUpdate(sql);

			sql = "CREATE TABLE history " + "(user_id VARCHAR(255) NOT NULL , " + " item_id VARCHAR(255) NOT NULL, "
					+ "last_favor_time timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP, " + " PRIMARY KEY (user_id, item_id),"
					+ "FOREIGN KEY (item_id) REFERENCES items(item_id),"
					+ "FOREIGN KEY (user_id) REFERENCES users(user_id))";
			stmt.executeUpdate(sql);
			
			//4. insert data
			//create a fake user
			sql = "INSERT INTO users " + "VALUES (\"item\", \"0cc10b11b6152cceea5899140edc95a1\", \"John\", \"Smith\")";
			System.out.println("Executing query: \n" + sql);
			stmt.executeUpdate(sql);
			
			System.out.println("Import is done successfully");
		} catch (Exception e) {
			System.out.println(e.getMessage());
		}
	}
}
