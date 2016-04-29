package at.tuwien.ase.dao;

import java.util.List;

import javax.ejb.Local;

import at.tuwien.ase.model.WorkingObject;

@Local
public interface WorkingObjectDaoInterface {
	
	/**
	 * Method returns a List of WorkingObject from the database.
	 * 
	 * @return a List of all entities from the database or an empty list if no entry exists.
	 *
	 */
	List<WorkingObject> listAll();

	/**
	 * Method returns a WorkingObject having the provided id.
	 * 
	 * @param id - WorkingObject id
	 * 
	 * @return an entity with the given id or null if no such entity exists.
	 *
	 */
	WorkingObject get(Long id);

	/**
	 * Method deletes a WorkingObject with the provided id.
	 * 
	 * @param id - WorkingObject id
     *  
	 * Throws a hibernate exception if no such id exists.
	 */
	 void delete(Long id);
}
