package registration.db;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Properties;

public class HSQLDB {

	Connection connection;

	public static void main(String[] args) throws IOException {
		HSQLDB test = new HSQLDB();
		if (!test.loadDriver())
			return;
		if (!test.getConnection())
			return;
		test.createTable();
		test.fillTable("my_mail2@gmail.com", "katie44", false);
		test.printTable();
		test.closeConnection();
	}

	private boolean loadDriver() {
		try {
			Class.forName("org.hsqldb.jdbcDriver");
		} catch (ClassNotFoundException e) {
			System.out.println("Драйвер не найден");
			e.printStackTrace();
			return false;
		}
		return true;
	}

	private boolean getConnection() throws IOException {

		try {
			Properties p = new Properties();
			String pFile = "config.properties";
			InputStream inputStream = getClass().getClassLoader().getResourceAsStream(pFile);
			if (inputStream != null)
				p.load(inputStream);
			else
				throw new FileNotFoundException("property file '" + pFile + "' not found in the classpath");
			connection = DriverManager.getConnection(p.getProperty("connectionString"), p.getProperty("user"),
					p.getProperty("password"));
		} catch (SQLException e) {
			System.out.println("Connection wasn't created");
			e.printStackTrace();
			return false;
		}
		return true;
	}

	private void createTable() {
		try {
			connection.createStatement().executeUpdate(
					"CREATE TABLE Users (email VARCHAR(255), password VARCHAR(255), is_confirmed BOOLEAN)");
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}

	private void fillTable(String email, String password, boolean confirmed) {
		try {
			connection.createStatement().executeUpdate(
					String.format("INSERT INTO Users VALUES('%s', '%s', '%s')", email, password, confirmed));
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}

	private void printTable() {
		try {
			ResultSet rs = connection.createStatement().executeQuery("SELECT * FROM Users");
			while (rs.next()) {
				System.out.println(rs.getString(1) + " " + rs.getString(2) + " " + rs.getBoolean(3));
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}

	private void closeConnection() {
		try {
			connection.createStatement().execute("DROP SCHEMA PUBLIC CASCADE");
			connection.close();
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}
}
