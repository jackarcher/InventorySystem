package entity;

import java.io.Serializable;

/**
 * Reference an administrator in the system.
 * 
 * @author Archer
 *
 */
public class Administrator extends User implements Serializable {

	/**
	 * The name of administrator
	 */
	private String name;

	/**
	 * Constructor with the least 2 params
	 * 
	 * @param id
	 *            The user id
	 * @param pwd
	 *            The password
	 */
	public Administrator(String id, String pwd) {
		super(id, pwd);
	}

	/**
	 * Constructor with the all 3 params
	 * 
	 * @param id
	 *            The user id
	 * @param pwd
	 *            The password
	 * @param name
	 *            The administrator's name
	 */
	public Administrator(String id, String pwd, String name) {
		super(id, pwd);
		this.name = name;
	}

	/**
	 * The getter for name attribute.
	 * 
	 * @return
	 */
	public String getName() {
		return name;
	}

	/**
	 * The setter for name attribut.
	 * 
	 * @param name
	 */
	public void setName(String name) {
		this.name = name;
	}

}
