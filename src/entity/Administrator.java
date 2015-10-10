package entity;

public class Administrator extends User {

	private String name;

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public Administrator(String id, String pwd) {
		super(id, pwd);
		// TODO Auto-generated constructor stub
	}

	public Administrator(String id, String pwd, String name) {
		super(id, pwd);
		this.name = name;
	}

}
