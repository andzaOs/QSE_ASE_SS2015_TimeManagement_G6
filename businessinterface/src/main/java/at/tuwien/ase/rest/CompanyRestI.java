package at.tuwien.ase.rest;

import java.util.List;

import javax.ejb.Local;

import at.tuwien.ase.model.Company;

@Local
public interface CompanyRestI {

	/**
	 * Method returns a Company by providing its id. If an id exists in the
	 * database, a Company is returned. Otherwise, null is returned. 
	 * 
	 * @param id - id of the Company
	 * 
	 * @return an entity with the given id or null if no such entity exists.
	 *
	 */
	Company getItem(Long id) throws Exception;

	/**
	 * Method lists all Company stored in the database.
	 * 
	 * @return a List of all entities from the database or an empty list if no entry exists.
	 *
	 */
	List<Company> listAll() throws Exception;
	
	/**
	 * Method saves a Company provided as an input or updates an existing Company
	 * in the database.
	 * 
	 * @param c - provided Company
	 * 
	 * @throws Exception - if an object can not be stored or update.
	 *
	 */
	void createOrUpdateCompany(Company c) throws Exception;

	/**
	 * Method deletes a Company with the provided id from the database.
	 * 
	 * @param id - id of the Company
	 * 
	 * @throws Exception - throws a hibernate exception if no such id exists.
	 * 
	 */
	void delete(Long id) throws Exception;

}
