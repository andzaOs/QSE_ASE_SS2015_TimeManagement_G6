package at.tuwien.ase.rest;

import javax.ws.rs.core.Response;

public interface LogingRestI {

	 /**
	 * Method returns an User object from the database if the User with given credentials exists.
	 * Otherwise, the method returns null.
	 * 
	 * @param username - username assigned to the user during the creation.
	 * 
	 * @param password - password assigned to the user during the creation.
	 * 
	 * @return - user object if the user with given credentials exists, null otherwise.
	 * 
	 */
	public Response login(String username, String password) throws Exception;
}
