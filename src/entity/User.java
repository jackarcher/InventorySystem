package entity;

import java.io.Serializable;

/**
 * Refers to all users in the system including administrators and customers
 * 
 * @author Archer
 *
 */
public abstract class User implements Serializable {
	/**
	 * The id of the user
	 */
	private String id;
	/**
	 * The login flag of the user, being true to refers the login status of the
	 * user, false for logout
	 */
	private boolean loginFlag;
	/**
	 * The password of the user
	 */
	private String pwd;

	/**
	 * Initialize a user to contains the given id and password
	 * 
	 * @param id
	 *            The user id
	 * @param pwd
	 *            The user password
	 */
	public User(String id, String pwd) {
		this.id = id;
		this.pwd = pwd;
		loginFlag = false;
	}

	/**
	 * The getter of the id
	 * 
	 * @return The ID of the user
	 */
	public String getId() {
		return id;
	}

	/**
	 * Try to login with the given password
	 * 
	 * @param password
	 *            The password for the user
	 * @return true if password match the record, false if fail
	 */
	public boolean login(String password) {
		if (pwd.equals(password)) {
			loginFlag = true;
		} else {
			loginFlag = false;
		}
		return loginFlag;
	}

	/**
	 * Logout the user
	 * 
	 * @return true if successful, false if not
	 */
	public boolean logout() {
		loginFlag = false;
		return !loginFlag;
	}
}
