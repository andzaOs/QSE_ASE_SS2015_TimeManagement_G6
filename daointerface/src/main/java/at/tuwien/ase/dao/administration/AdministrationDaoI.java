package at.tuwien.ase.dao.administration;

import javax.ejb.Local;

@Local
public interface AdministrationDaoI {

	/**
	 * Method populates the database with the initial set of data extracted
	 * from the documentation provided by PORR.
	 * 
	 * @return boolean valued denoting the success od data import.
	 */
	public boolean populateDB();
}
