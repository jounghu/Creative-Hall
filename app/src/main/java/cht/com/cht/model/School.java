package cht.com.cht.model;
/**
 *
 * id
 * school_name
 * school_address
 * @author Administrator
 *
 */
public class School {
	private int id;
	private String school_name;
	private String school_address;
	
	public School() {

	}
	
	public School(int id, String school_name, String school_address) {
		super();
		this.id = id;
		this.school_name = school_name;
		this.school_address = school_address;
	}

	public int getId() {
		return id;
	}
	public void setId(int id) {
		this.id = id;
	}
	public String getSchool_name() {
		return school_name;
	}
	public void setSchool_name(String school_name) {
		this.school_name = school_name;
	}
	public String getSchool_address() {
		return school_address;
	}
	public void setSchool_address(String school_address) {
		this.school_address = school_address;
	}

	@Override
	public String toString() {
		return "School [id=" + id + ", school_name=" + school_name
				+ ", school_address=" + school_address + "]";
	}
	
}
