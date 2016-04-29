package at.tuwien.ase.dao;

import java.util.List;

import javax.ejb.Local;

import at.tuwien.ase.model.Category;
@Local
public interface CategoryDaoInterface {

	/**
	 * Method returns a List of Category from the database.
	 * 
	 * @return a List of all entities from the database or an empty list if no entry exists.
	 *
	 */
	List<Category> listAll(); 
	
	/**
	 * Method returns a Category having the provided id.
	 * 
	 * @param id - Category id
	 * 
	 * @return an entity with the given id or null if no such entity exists.
	 *
	 */
	Category get(Long id);
   
	/**
	 * Method deletes a Category with the provided id.
	 * 
	 * @param id - Category id
     *  
	 * Throws a hibernate exception if no such id exists.
	 */
	void delete(Long id);

}
