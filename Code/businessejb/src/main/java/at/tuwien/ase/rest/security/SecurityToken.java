package at.tuwien.ase.rest.security;

import at.tuwien.ase.model.User;

public class SecurityToken {

	private User user;
	private String sesId;

	public SecurityToken(User u, String sesId) {
		this.setUser(u);
		this.setSesId(sesId);
		
	}

	public String getSesId() {
		return sesId;
	}

	public void setSesId(String sesId) {
		this.sesId = sesId;
	}

	public User getUser() {
		return user;
	}

	public void setUser(User user) {
		this.user = user;
	}

}
