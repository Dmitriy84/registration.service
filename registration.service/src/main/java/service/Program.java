package service;

import db.HSQLDB;
import model.User;

public class Program {
	public static void main(String[] args) throws Exception {
		HSQLDB db = new HSQLDB();
		User u = new User("my_mail2@gmail.com", "katie44");
		db.createUser(u);
		db.confirmUser(u);
		db.printUsers();
	}
}