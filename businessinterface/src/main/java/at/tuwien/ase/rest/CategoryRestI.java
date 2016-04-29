package at.tuwien.ase.rest;

import java.util.List;

import javax.ejb.Local;

import at.tuwien.ase.model.Category;

@Local
public interface CategoryRestI {
	
	/**
	 * Method returns a Category by providing its id. If an id exists in the
	 * database, a Category is returned. Otherwise, null is returned. 
	 * 
	 * @param id - id of the Category
	 * 
	 * @return an entity with the given id or null if no such entity exists.
	 *
	 */
	Category getItem(Long id) throws Exception;

	/**
	 * Method lists all Category stored in the database.
	 * 
	 * @return a List of all entities from the database or an empty list if no entry exists.
	 *
	 */
	List<Category> listAll() throws Exception;

	/**
	 * Method saves a Category provided as an input or updates an existing Category
	 * in the database.
	 * 
	 * @param c - provided WorkingObject
	 * 
	 * @throws Exception - if an object can not be stored or update.
	 *
	 */
	void createOrUpdateCategory(Category c) throws Exception;

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
