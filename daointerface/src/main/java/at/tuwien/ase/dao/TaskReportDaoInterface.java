
package at.tuwien.ase.dao;

import java.util.List;

import javax.ejb.Local;

import at.tuwien.ase.model.TaskReport;

@Local
public interface TaskReportDaoInterface {
	
	/**
	 * Method returns a List of TaskReport from the database.
	 * 
	 * @return a List of all entities from the database or an empty list if no entry exists.
	 *
	 */
	List<TaskReport> listAll();
	
	/**
	 * Method returns a TaskReport having the provided id.
	 * 
	 * @param id - WorkingObject id
	 * 
	 * @return an entity with the given id or null if no such entity exists.
	 *
	 */
	TaskReport get(Long id);
	
	/**
	 * Method return a List of TaskReport which have to be approved by the approver
	 * with the provided id. The approver is a supervisor.
	 * 
	 * @param id - supervisor id
	 * 
	 * @return a List of TaskReport which have to be approved by the user with a given id.
	 *
	 */
	List<TaskReport> getReportsToApproveByApproveId(Long id);

	/**
	 * Method approves a List of TaskReport. Sets the APPROVED status for the provided
	 * TaskReport.
	 *  
	 * @param list - a List of TaskReport to set statis to APPROVED
	 *
	 */
	void approveTaskList(List<TaskReport> list);

	/**
	 *Method rejects a TaskReport. Sets the REJECTED status and updates the 
	 *rejection message with the value saved in provided TaskReport.
	 * 
	 * @param report - TaskReport to process
	 *
	 */
	void rejectTaskReport(TaskReport report);

	/**
	 * Method returns a List of TaskReport which are not older than 24 h or
	 * are rejected and belonging to the worker with a provided id.
	 * 
	 * @param id - Worker id
	 * 
	 * @return a List of TaskReports which are not older then 24 hours or  are rejected.
	 *
	 */
	List<TaskReport> getTaskReportsForWorker(Long id);
	  
	/**
	 * Method deletes a TaskReport with the provided id.
	 * 
	 * @param id - TaskReport id
     *  
	 * Throws a hibernate exception if no such id exists.
	 */
	void delete(Long id);
	
	/**
	 * Method return a List of TaskReport from the database for a given
	 * Project id.
	 * 
	 * @param id - Project id
	 * 
	 * @return a List of TaskReport related to a given project
	 * 
	 * @throws Exception - a hibernate exception if no such id exists.
	 *
	 */
	List<TaskReport> getTaskReportsForProject(Long id);

	/**
	 * @param id
	 * @return latest 10 TaskReport forProjectId
	 * @throws Exception
	 */
	List<TaskReport> getLatest10TaskReportForProjectId(Long id);
	/**
	 * 
	 * @param id
	 * @return a list of  TaskReports for a given worker
	 *
	 */
	List<TaskReport> getAllTaskReportsForWorker(Long id);

}

