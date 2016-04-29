package at.tuwien.ase.rest;

import java.util.List;

import javax.ejb.Local;

import at.tuwien.ase.model.Resource;

@Local
public interface ResourceRestI {

	/**
	 * Method returns a Resource by providing its id. If an id exists in the
	 * database, a Resource is returned. Otherwise, null is returned. 
	 * 
	 * @param id - id of the Resource
	 * 
	 * @return an entity with the given id or null if no such entity exists.
	 *
	 */
	Resource getItem(Long id) throws Exception;
	
	/**
	 * Method lists all Resource stored in the database.
	 * 
	 * @return a List of all entities from the database or an empty list if no entry exists.
	 *
	 */
	List<Resource> listAll() throws Exception;
	
	/**
	 * Method returns a List of Resource belonging to the Project with the
	 * provided Project id.
	 * 
	 * @param id - TaskType id
	 * 
	 * @return a List of Resource belonging to the TaskType
	 * 
	 * @throws Exception - throws a hibernate exception if no such id exists.
	 */
	List<Resource> listAllForTaskType(Long id) throws Exception;

	/**
	 * Method saves a Resource provided as an input or updates an existing Resource
	 * in the database.
	 * 
	 * @param c - provided Resource
	 * 
	 * @throws Exception - if an object can not be stored or update.
	 *
	 */
	void createOrUpdateResourceWithCategory(Resource resource) throws Exception;

	/**
	 * Method deletes a Resource with the provided id from the database.
	 * 
	 * @param id - id of the Resource
	 * 
	 * @throws Exception - throws a hibernate exception if no such id exists.
	 * 
	 */
	void delete(Long id) throws Exception;

}
