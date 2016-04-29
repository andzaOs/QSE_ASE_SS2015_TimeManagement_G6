package at.tuwien.ase.rest.administration;

public interface AdministrationRestI {

	/**
	 * Helper method for populating the database with an initial set of data.
	 * 
	 * @return - boolean value denoting the outcome of the population process.
	 * 
	 * @throws Exception - if exception occurs during the data persistence.
	 */
	public boolean populateDB() throws Exception;
}
