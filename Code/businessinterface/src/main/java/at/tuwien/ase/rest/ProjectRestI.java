package at.tuwien.ase.rest;

import java.util.List;

import javax.ejb.Local;

import at.tuwien.ase.model.Project;

@Local
public interface ProjectRestI {
	
	/**
	 * Method returns a Project by providing its id. If an id exists in the
	 * database, a Project is returned. Otherwise, null is returned. 
	 * 
	 * @param id - id of the Project
	 * 
	 * @return an entity with the given id or null if no such entity exists.
	 *
	 */
	Project getItem(Long id) throws Exception;
	
	/**
	 * Method lists all Project stored in the database.
	 * 
	 * @return a List of all entities from the database or an empty list if no entry exists.
	 *
	 */
	List<Project> listAll() throws Exception;
	
	/**
	 * Method saves a Project provided as an input or updates an existing Project
	 * in the database.
	 * 
	 * @param c - provided Project
	 * 
	 * @throws Exception - if an object can not be stored or update.
	 *
	 */
	void createOrUpdateProject(Project p) throws Exception;
	
	/**
	 * Method deletes a Project with the provided id from the database.
	 * 
	 * @param id - id of the Project
	 * 
	 * @throws Exception - throws a hibernate exception if no such id exists.
	 * 
	 */
	void delete(Long id) throws Exception;
	
	/**
	 * Method returns a project to which a user with the given id is assigned to.
	 * Method returns no data if the user is not assigned to any project.
	 * 
	 * @param id - user id.
	 * 
	 * @return - a project to which the user is assigned.
	 * 
	 * @throws Exception - Throws a hibernate exception if no such id exists.
	 */
	Project getProjectForUser(Long id) throws Exception;
}
