
package at.tuwien.ase.dao;

import java.util.List;

import javax.ejb.Local;

import at.tuwien.ase.model.Resource;

@Local
public interface ResourceDaoInterface {
	
	/**
	 * Method returns a List of Resource from the database.
	 * 
	 * @return a List of all entities from the database or an empty list if no entry exists.
	 *
	 */
	List<Resource> listAll();
	
	/**
	 * Method returns a Resource having the provided id.
	 * 
	 * @param id - Resource id
	 * 
	 * @return an entity with the given id or null if no such entity exists.
	 *
	 */
	Resource get(Long id);
	
	/**
	 * Method returns a List of Resource for a TaskType with the provided id.
	 * 
	 * @param id - TaskType id
	 * 
	 * @return a List of Resource for the provided TaskType id
	 */
	List<Resource> listAllForTaskType(Long id);
	  
	/**
	 * Method deletes a Resource with the provided id.
	 * 
	 * @param id - Resource id
     *  
	 * Throws a hibernate exception if no such id exists.
	 */
	void delete(Long id);
}
