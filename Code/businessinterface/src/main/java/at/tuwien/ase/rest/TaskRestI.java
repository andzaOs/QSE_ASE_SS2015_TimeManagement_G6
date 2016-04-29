package at.tuwien.ase.rest;

import java.util.List;

import javax.ejb.Local;

import at.tuwien.ase.model.Task;

@Local
public interface TaskRestI {
	
	/**
	 * Method returns a Task by providing its id. If an id exists in the
	 * database, a Task is returned. Otherwise, null is returned. 
	 * 
	 * @param id - id of the WorkingObject
	 * 
	 * @return an entity with the given id or null if no such entity exists.
	 *
	 */
	Task getItem(Long id) throws Exception;
	
	/**
	 * Method lists all Task stored in the database.
	 * 
	 * @return a List of all entities from the database or an empty list if no entry exists.
	 *
	 */
	List<Task> listAll() throws Exception;
	
	/**
	 * Method saves a Task provided as an input or updates an existing Task
	 * in the database.
	 * 
	 * @param c - provided Task
	 * 
	 * @throws Exception - if an object can not be stored or update.
	 *
	 */
	void createOrUpdateTask(Task task) throws Exception;

	/**
	 * Method returns a List of Task assigned to the User with the provided
	 * id. The User is a supervisor who gets a List of Task to assign to the
	 * workers.
	 * 
	 * @param id - supervisor id
	 * 
	 * @return a List of tasks which have the given approver (supervisor) id.
	 *
	 */
	List<Task> listTasksForApproverId(Long id) throws Exception;
	
	/**
	 * Method returns a List of Tasks assigned to the User with the provided
	 * id. The User is a worker who gets a List of assigned Task.
	 * 
	 * @param id - worker id
	 * 
	 * @return a List of tasks which have the given worker id.
	 * 
	 */
	List<Task> listTasksForUserId(Long id) throws Exception;

	/**
	 * Method returns a List of Tasks assigned to the User with the provided
	 * id. The User is a supervisor who gets a List of Task which are not
	 * yet approved.
	 * 
	 * @param id - supervisor id
	 * 
	 * @return a List of tasks which are not yet approved and 
	 * 			which have the given approver (supervisor) id. 
	 * 
	 */
	List<Task> listTasksWhichAreNotApprovedForApproverId(Long id) throws Exception;

	/**
	 * Method deletes a Task with the provided id from the database.
	 * 
	 * @param id - id of the Task
	 * 
	 * @throws Exception - throws a hibernate exception if no such id exists.
	 * 
	 */
	void delete(Long id) throws Exception;

	/**
	 * @return list of tasks related to a projectId, The element count  is limited to 10 and the page parameter determines which  page will be returned
	 * @throws Exception 
	 */

	List<Task> listTasksPaged(Long projectId, Integer page) throws Exception;

}
