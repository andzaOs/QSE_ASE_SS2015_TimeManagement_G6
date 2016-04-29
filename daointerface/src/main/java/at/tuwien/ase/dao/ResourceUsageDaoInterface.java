package at.tuwien.ase.dao;

import java.util.List;

import javax.ejb.Local;

import at.tuwien.ase.model.ResourceUsage;

@Local
public interface ResourceUsageDaoInterface {
	
	/**
	 * Method returns a List of ResourceUsage from the database.
	 * 
	 * @return a List of all entities from the database or an empty list if no entry exists.
	 *
	 */
	List<ResourceUsage> listAll();
	
	/**
	 * Method returns a ResourceUsage having the provided id.
	 * 
	 * @param id - ResourceUsage id
	 * 
	 * @return an entity with the given id or null if no such entity exists.
	 *
	 */
	ResourceUsage get(Long id);
	  
	/**
	 * Method deletes a ResourceUsage with the provided id.
	 * 
	 * @param id - ResourceUsage id
     *  
	 * Throws a hibernate exception if no such id exists.
	 */
	void delete(Long id);
}
