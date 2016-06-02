package model;

public class User {
	public User(String email, String password) {
		_email = email;
		_password = password;
	}

	private final String _email, _password;

	public String getEmail() {
		return _email;
	}

	public String getPassword() {
		return _password;
	}
}