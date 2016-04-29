package at.tuwien.ase.rest;

import javax.ejb.Local;

@Local
public interface ReportRestI {

	/**
	 * Method returns the total number of hours associated with the
	 * Project with provided id.
	 * 
	 * @param id - Project id 
	 * 
	 * @return total number of hours.
	 * 
	 * @throws Exception - throws a hibernate exception if no such id exists.
	 */
	Double getTotalHoursForProject(Long id) throws Exception;

	/**
	 * Method returns the number of Task included in the Project with provided
	 * id.
	 * 
	 * @param id - id of the project to by analyzed
	 * 
	 * @return count of tasks included in project with the given id.
	 * 
	 * @throws Exception - throws a hibernate exception if no such id exists.
	 *
	 */
	String getTotalTasksPerProjectId(Long id) throws Exception;
	
	/**
	 * Method returns the number of finished Task included in the Project 
	 * with provided id.
	 * 
	 * @param id - id of the project to by analyzed
	 * 
	 * @return count of finished tasks included in project.
	 * 
	 * @throws Exception - throws a hibernate exception if no such id exists.
	 *
	 */
	String getTotalFinishedTasksPerProjectId(Long id) throws Exception;

}
