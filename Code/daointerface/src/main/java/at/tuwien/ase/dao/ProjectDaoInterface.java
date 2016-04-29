package at.tuwien.ase.dao;

import java.util.List;

import javax.ejb.Local;

import at.tuwien.ase.model.Project;

@Local
public interface ProjectDaoInterface {
	
	/**
	 * Method returns a List of Project from the database.
	 * 
	 * @return a List of all entities from the database or an empty list if no entry exists.
	 *
	 */
	List<Project> listAll();
	
	/**
	 * Method returns a Project having the provided id.
	 * 
	 * @param id - Project id
	 * 
	 * @return an entity with the given id or null if no such entity exists.
	 *
	 */
	Project get(Long id);
	  
	/**
	 * Method deletes a Project with the provided id.
	 * 
	 * @param id - Project id
     *  
	 * Throws a hibernate exception if no such id exists.
	 */
	void delete(Long id);
	
}
