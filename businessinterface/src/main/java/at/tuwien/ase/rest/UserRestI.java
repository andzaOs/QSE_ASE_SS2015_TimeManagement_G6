package at.tuwien.ase.rest;

import java.util.List;

import javax.ejb.Local;

import at.tuwien.ase.model.User;

@Local
public interface UserRestI {
	
	/**
	 * Method returns a User by providing its id. If an id exists in the
	 * database, a User is returned. Otherwise, null is returned. 
	 * 
	 * @param id - id of the User
	 * 
	 * @return an entity with the given id or null if no such entity exists.
	 *
	 */
	User getItem(Long id) throws Exception;

	/**
	 * Method lists all Users stored in the database.
	 * 
	 * @return a List of all entities from the database or an empty list if no entry exists.
	 *
	 */
	List<User> listAll() throws Exception;
	
	/**
	 * Method saves a User provided as an input or updates an existing User
	 * in the database. The password is not changed if the user already exists,
	 * use updateUserPassword instead.
	 * 
	 * @param c - provided User
	 * 
	 * @throws Exception - if an object can not be stored or update.
	 *
	 */
	void createOrUpdateUser(User u) throws Exception;

	
	/**
	 * Method changes the password of an existing user.
	 * 
	 * @param u - provided User
	 * 
	 * @throws Exception - if an object can not be modified.
	 *
	 */
	void updateUserPassword(User u) throws Exception;

	/**
	 * Method deletes a User with the provided id from the database.
	 * 
	 * @param id - id of the User
	 * 
	 * @throws Exception - throws a hibernate exception if no such id exists.
	 * 
	 */
	void delete(Long id) throws Exception;
	/**
	 * @return list of workers which have assigned a task which is not finished
	 *
	 */
	List<User> getWorkingWorkersForProjectId(Long id) throws Exception;
}
