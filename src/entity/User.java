package entity;

import java.io.Serializable;

/**
 * refers to all users in the system including admins and customers
 * 
 * @author Archer
 *
 */
public abstract class User implements Serializable {
	private String id;
	private String pwd;
	private boolean loginFlag;

	public User(String id, String pwd) {
		this.id = id;
		this.pwd = pwd;
		loginFlag = false;
	}

	public String getId() {
		return id;
	}

	public boolean login(String password) {
		if (pwd.equals(password)) {
			loginFlag = true;
		} else {
			loginFlag = false;
		}
		return loginFlag;
	}

	public boolean logout() {
		loginFlag = false;
		return !loginFlag;
	}
}
