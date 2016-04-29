package at.tuwien.ase.rest;

import java.util.List;

import javax.ejb.Local;

import at.tuwien.ase.model.ProjectType;

@Local
public interface ProjectTypeRestI {

	/**
	 * Method returns a ProjectType by providing its id. If an id exists in the
	 * database, a ProjectType is returned. Otherwise, null is returned. 
	 * 
	 * @param id - id of the ProjectType
	 * 
	 * @return an entity with the given id or null if no such entity exists.
	 *
	 */
	ProjectType getItem(Long id) throws Exception;
	
	/**
	 * Method lists all ProjectType stored in the database.
	 * 
	 * @return a List of all entities from the database or an empty list if no entry exists.
	 *
	 */
	List<ProjectType> listAll() throws Exception;

	/**
	 * Method saves a ProjectType provided as an input or updates an existing ProjectType
	 * in the database.
	 * 
	 * @param c - provided ProjectType
	 * 
	 * @throws Exception - if an object can not be stored or update.
	 *
	 */
	void createOrUpdateProjectType(ProjectType pt) throws Exception;

	/**
	 * Method deletes a ProjectType with the provided id from the database.
	 * 
	 * @param id - id of the ProjectType
	 * 
	 * @throws Exception - throws a hibernate exception if no such id exists.
	 * 
	 */
	void delete(Long id) throws Exception;
}
