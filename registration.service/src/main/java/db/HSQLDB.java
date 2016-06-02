package db;

import java.io.FileNotFoundException;
import java.io.InputStream;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.Properties;

import model.User;

public class HSQLDB {
	final private String CREATE = "CREATE TABLE Users (email VARCHAR(255), password VARCHAR(255), is_confirmed BOOLEAN)",
			INSERT = "INSERT INTO Users VALUES('%s', '%s', 'false')",
			UPDATE = "UPDATE Users SET is_confirmed = true WHERE email = '%s' and password = '%s'";
	static Statement statement;

	public static void main(String[] args) throws Exception {
		HSQLDB db = new HSQLDB();
		User u = new User("my_mail2@gmail.com", "katie44");
		db.createUser(u);
		db.confirmUser(u);
		db.printTable();
	}

	public HSQLDB() throws Exception {
		if (statement != null)
			return;
		try {
			Class.forName("org.hsqldb.jdbcDriver");
			Properties p = new Properties();
			String pFile = "config.properties";
			InputStream inputStream = getClass().getClassLoader().getResourceAsStream(pFile);
			if (inputStream != null)
				p.load(inputStream);
			else
				throw new FileNotFoundException("property file '" + pFile + "' not found in the classpath");
			statement = DriverManager.getConnection(p.getProperty("connectionString")).createStatement();
			statement.executeUpdate(CREATE);
		} catch (SQLException e) {
			throw new Exception("Connection wasn't created", e);
		} catch (ClassNotFoundException e) {
			throw new Exception("Driver wasn't found", e);
		}
	}

	public void createUser(User user) throws SQLException {
		statement.executeUpdate(String.format(INSERT, user.getEmail(), user.getPassword()));
	}

	public void confirmUser(User user) throws SQLException {
		statement.executeUpdate(String.format(UPDATE, user.getEmail(), user.getPassword()));
	}

	// TEMP
	private void printTable() throws SQLException {
		ResultSet rs = statement.executeQuery("SELECT * FROM Users");
		while (rs.next()) {
			System.out.println(rs.getString(1) + " " + rs.getString(2) + " " + rs.getBoolean(3));
		}
	}

	protected void finalize() throws SQLException {
		statement.execute("DROP SCHEMA PUBLIC CASCADE");
		statement.getConnection().close();
	}
}
