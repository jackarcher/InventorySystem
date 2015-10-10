package entity;

import java.io.Serializable;

public class Administrator extends User implements Serializable {

	private String name;

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public Administrator(String id, String pwd) {
		super(id, pwd);
	}

	public Administrator(String id, String pwd, String name) {
		super(id, pwd);
		this.name = name;
	}

}
