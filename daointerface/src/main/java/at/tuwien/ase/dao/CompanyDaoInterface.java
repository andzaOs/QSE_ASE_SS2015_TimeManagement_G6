package at.tuwien.ase.dao;

import java.util.List;

import javax.ejb.Local;

import at.tuwien.ase.model.Company;

@Local
public interface CompanyDaoInterface {
	
	/**
	 * Method returns a List of Company from the database.
	 * 
	 * @return a List of all entities from the database or an empty list if no entry exists.
	 *
	 */
	List<Company> listAll();
	
	/**
	 * Method returns a Company having the provided id.
	 * 
	 * @param id - Company id
	 * 
	 * @return an entity with the given id or null if no such entity exists.
	 *
	 */
	Company get(Long id);
	  
	/**
	 * Method deletes a Company with the provided id.
	 * 
	 * @param id - Company id
     *  
	 * Throws a hibernate exception if no such id exists.
	 */
	void delete(Long id);
	
}

