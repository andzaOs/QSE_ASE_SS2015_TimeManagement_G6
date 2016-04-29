package at.tuwien.ase.rest;

import javax.ejb.Local;
import javax.ws.rs.core.Response;

import org.jboss.resteasy.plugins.providers.multipart.MultipartFormDataInput;

@Local
public interface FileUploadRestI {

	/**
	 * Method takes a file with task types from the client side, 
	 * parses the data, and stores the data in database.
	 * If a task type already exists in the database, it is updated if there are changes, 
	 * but there are no duplicates inserted.
	 * 
	 * @param input - a file uploaded from a client with .xlsx extension.
	 * 
	 * @return - HTTP status 200 if everything is ok, otherwise HTTP 500 with a corresponding
	 *            error message.
	 */
	public Response uploadTaskTypes(MultipartFormDataInput input);
	
	/**
	 * Method takes a file with working objects from the client side, 
	 * parses the data, and stores the data in database.
	 * If a working object already exists in the database, it is updated if there are changes, 
	 * but there are no duplicates inserted.
	 * 
	 * @param input - a file uploaded from a client with .xlsx extension.
	 * 
	 * @return - HTTP status 200 if everything is ok, otherwise HTTP 500 with a corresponding
	 *            error message.
	 */
	public Response uploadWorkingObjects(MultipartFormDataInput input);
	
	/**
	 * Method takes a file with users from the client side, parses the data, and stores the data in database.
	 * If a user already exists in the database, it is updated if there are changes, but there are no duplicates
	 * inserted.
	 * 
	 * @param input - a file uploaded from a client with .xlsx extension.
	 * 
	 * @return - HTTP status 200 if everything is ok, otherwise HTTP 500 with a corresponding
	 *            error message.
	 */
	public Response uploadUsers(MultipartFormDataInput input);
}
