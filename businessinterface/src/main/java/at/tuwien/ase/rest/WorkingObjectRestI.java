package at.tuwien.ase.rest;

import java.util.List;

import javax.ejb.Local;

import at.tuwien.ase.model.WorkingObject;

@Local
public interface WorkingObjectRestI {
	
	/**
	 * Method returns a WorkingObject by providing its id. If an id exists in the
	 * database, a WorkingObject is returned. Otherwise, null is returned. 
	 * 
	 * @param id - id of the WorkingObject
	 * 
	 * @return an entity with the given id or null if no such entity exists.
	 *
	 */
	WorkingObject getItem(Long id) throws Exception;

	/**
	 * Method lists all WorkingObject stored in the database.
	 * 
	 * @return a List of all entities from the database or an empty list if no entry exists.
	 *
	 */
	List<WorkingObject> listAll() throws Exception;

	/**
	 * Method saves a WorkingObject provided as an input or updates an existing WorkingObject
	 * in the database.
	 * 
	 * @param c - provided WorkingObject
	 * 
	 * @throws Exception - if an object can not be stored or update.
	 *
	 */
	void createOrUpdateWorkingObject(WorkingObject wo) throws Exception;

	/**
	 * Method deletes a WorkingObject with the provided id from the database.
	 * 
	 * @param id - id of the WorkingObject
	 * 
	 * @throws Exception - throws a hibernate exception if no such id exists.
	 * 
	 */
	void delete(Long id) throws Exception;
}
