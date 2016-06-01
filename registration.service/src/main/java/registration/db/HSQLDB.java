package registration.db;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

public class HSQLDB {

	Connection connection;

	public static void main(String[] args) {

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

	private boolean getConnection() {

		try {
			String path = "mypath/";
			String dbname = "mydb";
			String connectionString = "jdbc:hsqldb:file:" + path + dbname;
			String login = "joe";
			String password = "password";
			connection = DriverManager.getConnection(connectionString, login, password);

		} catch (SQLException e) {
			System.out.println("Соединение не создано");
			e.printStackTrace();
			return false;
		}
		return true;
	}

	private void createTable() {
		try {
			Statement statement = connection.createStatement();
			String sql = "CREATE TABLE Users (email VARCHAR(255), password VARCHAR(255), is_confirmed BOOLEAN)";
			statement.executeUpdate(sql);
		} catch (SQLException e) {

		}
	}

	private void fillTable(String email, String password, boolean confirmed) {
		Statement statement;
		try {
			statement = connection.createStatement();
			String sql = String.format("INSERT INTO Users VALUES('%s', '%s', '%s')", email, password, confirmed);
			statement.executeUpdate(sql);
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}

	private void printTable() {
		Statement statement;
		try {
			statement = connection.createStatement();
			String sql = "SELECT * FROM Users";
			ResultSet resultSet = statement.executeQuery(sql);
			while (resultSet.next()) {
				System.out
						.println(resultSet.getString(1) + " " + resultSet.getString(2) + " " + resultSet.getBoolean(3));
			}

		} catch (SQLException e) {
			e.printStackTrace();
		}
	}

	private void closeConnection() {

		Statement statement;
		try {
			statement = connection.createStatement();
			String sql = "SHUTDOWN";
			statement.execute(sql);

		} catch (SQLException e) {
			e.printStackTrace();
		}
	}
}
