package at.tuwien.ase.dao;

import javax.ejb.Local;

@Local
public interface ReportDaoInterface {
	
	/**
	 * Method returns the total number of hours for the Project
	 * with the provided id.
	 * 
	 * @param id the id of the project to by analyzed
	 * 
	 * @return sum  of reported  hours included in project
	 * 
	 * @throws Exception - a hibernate exception if no such id exists.
	 *
	 */
	Double getTotalHoursForProject(Long id);
	
	/**
	 * Method return the number of tasks in the Project
	 * with the provided id.
	 * 
	 * @param id the id of the project to by analyzed
	 * 
	 * @return count of tasks included in project
	 * 
	 * @throws Exception - a hibernate exception if no such id exists.
	 *
	 */
	Long getTotalTasksPerProjectId(Long id);
	
	/**
	 * Method return the number of finished tasks in the Project
	 * with the provided id.
	 * 
	 * @param id the id of the project to by analyzed
	 * 
	 * @return count of finished tasks included in project
	 * 
	 * @throws Exception - a hibernate exception if no such id exists.
	 *
	 */
	Long getTotalFinishedTasksPerProjectId(Long id);	
}