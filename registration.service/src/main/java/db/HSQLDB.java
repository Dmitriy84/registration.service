package db;

import java.io.FileNotFoundException;
import java.io.InputStream;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Properties;

import model.User;

public class HSQLDB {

	Connection connection;

	public static void main(String[] args) throws Exception {
		HSQLDB test = new HSQLDB();
		test.loadDriver();
		test.getConnection();
		test.createTable();
		test.fillTable(new User("my_mail2@gmail.com", "katie44", false));
		test.printTable();
		test.closeConnection();
	}

	private void loadDriver() throws Exception {
		try {
			Class.forName("org.hsqldb.jdbcDriver");
		} catch (ClassNotFoundException e) {
			throw new Exception("Driver wasn't found", e);
		}
	}

	private void getConnection() throws Exception {

		try {
			Properties p = new Properties();
			String pFile = "config.properties";
			InputStream inputStream = getClass().getClassLoader().getResourceAsStream(pFile);
			if (inputStream != null)
				p.load(inputStream);
			else
				throw new FileNotFoundException("property file '" + pFile + "' not found in the classpath");
			connection = DriverManager.getConnection(p.getProperty("connectionString"));
		} catch (SQLException e) {
			throw new Exception("Connection wasn't created", e);
		}
	}

	private void createTable() throws SQLException {
		connection.createStatement()
				.executeUpdate("CREATE TABLE Users (email VARCHAR(255), password VARCHAR(255), is_confirmed BOOLEAN)");
	}

	private void fillTable(User user) throws SQLException {
		connection.createStatement().executeUpdate(String.format("INSERT INTO Users VALUES('%s', '%s', '%s')",
				user.getEmail(), user.getPassword(), user.isConfirmed()));
	}

	private void printTable() throws SQLException {
		ResultSet rs = connection.createStatement().executeQuery("SELECT * FROM Users");
		while (rs.next()) {
			System.out.println(rs.getString(1) + " " + rs.getString(2) + " " + rs.getBoolean(3));
		}
	}

	private void closeConnection() throws SQLException {
		connection.createStatement().execute("DROP SCHEMA PUBLIC CASCADE");
		connection.close();
	}
}
