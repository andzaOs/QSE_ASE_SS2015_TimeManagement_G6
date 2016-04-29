package at.tuwien.ase.rest;

import java.util.List;

import javax.ejb.Local;

import at.tuwien.ase.model.ResourceUsage;

@Local
public interface ResourceUsageRestI {

	/**
	 * Method returns a ResourceUsage by providing its id. If an id exists in the
	 * database, a ResourceUsage is returned. Otherwise, null is returned. 
	 * 
	 * @param id - id of the ResourceUsage
	 * 
	 * @return an entity with the given id or null if no such entity exists.
	 *
	 */
	ResourceUsage getItem(Long id) throws Exception;
	
	/**
	 * Method lists all ResourceUsage stored in the database.
	 * 
	 * @return a List of all entities from the database or an empty list if no entry exists.
	 *
	 */
	List<ResourceUsage> listAll() throws Exception;

	/**
	 * Method saves a ResourceUsage provided as an input or updates an existing ResourceUsage
	 * in the database.
	 * 
	 * @param c - provided ResourceUsage
	 * 
	 * @throws Exception - if an object can not be stored or update.
	 *
	 */
	void createOrUpdateResourceUsage(ResourceUsage ru) throws Exception;

	/**
	 * Method deletes a ResourceUsage with the provided id from the database.
	 * 
	 * @param id - id of the ResourceUsage
	 * 
	 * @throws Exception - throws a hibernate exception if no such id exists.
	 * 
	 */
	void delete(Long id) throws Exception;
}
