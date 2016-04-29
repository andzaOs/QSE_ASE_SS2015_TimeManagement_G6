package at.tuwien.ase.dao;

import java.util.List;

import javax.ejb.Local;

import at.tuwien.ase.model.ProjectType;

@Local
public interface ProjectTypeDaoInterface {
	
	/**
	 * Method returns a List of ProjectType from the database.
	 * 
	 * @return a List of all entities from the database or an empty list if no entry exists.
	 *
	 */
	List<ProjectType> listAll();
	
	/**
	 * Method returns a ProjectType having the provided id.
	 * 
	 * @param id - ProjectType id
	 * 
	 * @return an entity with the given id or null if no such entity exists.
	 *
	 */
	ProjectType get(Long id);
	 
	/**
	 * Method deletes a ProjectType with the provided id.
	 * 
	 * @param id - ProjectType id
     *  
	 * Throws a hibernate exception if no such id exists.
	 */
	void delete(Long id);
}
