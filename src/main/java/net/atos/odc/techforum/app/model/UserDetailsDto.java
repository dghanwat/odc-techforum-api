package net.atos.odc.techforum.app.model;

public class UserDetailsDto {

	private String firstName;
	private String lastName;
	private String location = "";
	/**
	 * @return the firstName
	 */
	public String getFirstName() {
		return firstName != null ? firstName.trim() : null ;
	}
	/**
	 * @param firstName the firstName to set
	 */
	public void setFirstName(String firstName) {
		this.firstName = firstName;
	}
	/**
	 * @return the lastName
	 */
	public String getLastName() {
		return lastName != null ? lastName.trim() : null;
	}
	/**
	 * @param lastName the lastName to set
	 */
	public void setLastName(String lastName) {
		this.lastName = lastName;
	}
	/**
	 * @return the location
	 */
	public String getLocation() {
		return location != null ? location.trim() : null;
	}
	/**
	 * @param location the location to set
	 */
	public void setLocation(String location) {
		this.location = this.location + location;
	}
	
	
	
}
