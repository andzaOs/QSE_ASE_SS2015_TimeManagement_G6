package at.tuwien.ase.dao;

import java.util.List;

import javax.ejb.Local;

import at.tuwien.ase.model.User;

@Local
public interface UserDaoInterface {
	
	/**
	 * Method returns a List of User from the database.
	 * 
	 * @return list of all entities from the database or an empty list if no entry exists.
	 *
	 */
	List<User> listAll();
	
	/**
	 * Method returns a User having the provided id. 
	 * 
	 * @param id - User id
	 * 
	 * @return an entity with the given id or null if no such entity exists.
	 *
	 */
	User get(Long id);

	/**
	 * Method returns an User object from the database if the user with given credentials exists.
	 * Otherwise, the method returns null.
	 * 
	 * @param username - username assigned to the user during the creation.
	 * 
	 * @param password - password assigned to the user during the creation.
	 * 
	 * @return user object if the user with given credentials exists, null otherwise.
	 */
	User getByCredentials(String username, String password);
	  
	/**
	 * Method deletes a User with the provided id.
     * 
     * @param id - User id
     * 
     * Throws a hibernate exception if no such id exists.
     */
	void delete(Long id);
	/**
	 * @return list of workers which have assigned a task which is not finished
	 *
	 */
	List<User> getWorkingWorkersForProjectId(Long id);
}
