package model;

public class User {
	public User(String email, String password, boolean isConfirmed) {
		_email = email;
		_password = password;
		_isConfirmed = isConfirmed;
	}

	private final String _email, _password;
	private final boolean _isConfirmed;

	public String getEmail() {
		return _email;
	}

	public String getPassword() {
		return _password;
	}

	public boolean isConfirmed() {
		return _isConfirmed;
	}
}