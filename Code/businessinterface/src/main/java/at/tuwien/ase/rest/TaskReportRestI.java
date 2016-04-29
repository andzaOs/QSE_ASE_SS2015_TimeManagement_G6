package at.tuwien.ase.rest;

import java.util.List;

import javax.ejb.Local;

import at.tuwien.ase.model.TaskReport;
@Local
public interface TaskReportRestI {

	/**
	 * Method returns a TaskReport by providing its id. If an id exists in the
	 * database, a TaskReport is returned. Otherwise, null is returned. 
	 * 
	 * @param id - id of the TaskReport
	 * 
	 * @return an entity with the given id or null if no such entity exists.
	 *
	 */
	TaskReport getItem(Long id) throws Exception;

	/**
	 * Method lists all TaskReport stored in the database.
	 * 
	 * @return list of all entities from the database or an empty list if no entry exists.
	 *
	 */
	List<TaskReport> listAll() throws Exception;

	/**
	 * Method saves a TaskReport provided as an input or updates an existing TaskReport
	 * in the database.
	 * 
	 * @param c - provided TaskReport
	 * 
	 * @throws Exception - if an object can not be stored or update.
	 *
	 */
	void createOrUpdateTaskReport(TaskReport report) throws Exception;

	/**
	 * Method returns a List of TaskReport which have to be approved by the
	 * approver, i.e., User, with the provided id. The User is a supervisor.
	 * 
	 * @param id - supervisor id
	 * 
	 * @return a List of TaskReport which have to be approved by the supervisor
	 * 			with the provided id.
	 * 
	 * @throws Exception - throws a hibernate exception if no such id exists.
	 * 
	 */
	List<TaskReport> getReportsToApproveByApproveId(Long id) throws Exception;
	
	/**
	 * Method returns a List of TaskReport belonging to a User with provided
	 * id. The user is a worker.
	 * 
	 * @param id - worker id
	 * 
	 * @return a List of TaskReport belonging to the worker with provided id.
	 * 
	 * @throws Exception - throws a hibernate exception if no such id exists.
	 * 
	 */
	List<TaskReport> listAllForWorker(Long id) throws Exception;

	/**
	 * Method which receives a List of TaskReport to set to approved in the 
	 * database.
	 * 
	 * @param report - List of TaskReport
	 * 
	 * @throws Exception - throws a hibernate exception if a TaskReport does
	 * 						not exist in the database.
	 * 
	 */
	void approveTaskReportList(List<TaskReport> report) throws Exception;

	/**
	 * Method sets the status of received TaskReport in the database to rejected.
	 * 
	 * @param report - TaskReport
	 * @throws Exception - throws a hibernate exception if a TaskReport does
	 * 						not exist in the database.
	 * 
	 */
	void rejectReport(TaskReport report) throws Exception;

	/**
	 * Method returns a List of TaskReport for a worker with provided id.
	 * 
	 * @param id - worker id 
	 * 
	 * @return a List of TaskReport belonging to the worker with provided id.
	 * 
	 * @throws Exception - throws a hibernate exception if no such id exists.
	 * 
	 */
	List<TaskReport> getTaskReportsForWorker(Long id) throws Exception;

	/**
	 * Method deletes a WorkingObject with the provided id from the database.
	 * 
	 * @param id - id of the WorkingObject
	 * 
	 * @throws Exception - throws a hibernate exception if no such id exists.
	 * 
	 */
	void delete(Long id) throws Exception;

	/**
	 * Method returns a List of TaskReport which belong to the Project with the
	 * provided id.
	 * 
	 * @param id - Project id
	 * 
	 * @return a list of TaskReport related to a given Project
	 * 
	 * @throws Exception - throws a hibernate exception if no such id exists.
	 *
	 */
	List<TaskReport> getTaskReportsForProject(Long id) throws Exception;

/**
 * @param id
 * @return latest 10 TaskReport forProjectId
 * @throws Exception
 */
List<TaskReport> getLatest10TaskReportForProjectId(Long id) throws Exception;

}
