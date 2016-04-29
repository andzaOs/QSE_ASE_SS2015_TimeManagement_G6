package at.tuwien.ase.dao;

import java.util.List;

import javax.ejb.Local;

import at.tuwien.ase.model.Task;

@Local
public interface TaskDaoInterface {
	
	/**
	 * Method returns a List of Task from the database.
	 * 
	 * @return a List of all entities from the database or an empty list if no entry exists.
	 *
	 */
	List<Task> listAll();

	/**
	 * Method returns a Task having the provided id.
	 * 
	 * @param id - Task id
	 * 
	 * @return an entity with the given id or null if no such entity exists.
	 *
	 */
	Task get(Long id);

	/**
	 * Method returns a List of Task belonging to the approver with the 
	 * provided id. The approver is supervisor.
	 * 
	 * @param id - supervisor id
	 * 
	 * @return a List of Task which have the given approver id.
	 *
	 */
	List<Task> listTasksForApproverId(Long id);

	/**
	 * Method returns a List of Task belonging to the worker with the 
	 * provided id.
	 * 
	 * @param id - worker id
	 * 
	 * @return a List of Task which have the given worker id.
	 *
	 */
	List<Task> listTasksForUserId(Long id);

	/**
	 * Method returns a List of Task belonging to the approver with the 
	 * provided id. Returned are Task which are not approved yet. 
	 * The approver is supervisor.
	 * 
	 * @param id - supervisor id
	 *  
	 * @return a List of Tasks which are not yet approved and 
	 * 			have to by approved by the user with the given id.
	 */
	List<Task> listTasksWhichAreNotApprovedForApproverId(Long id);

	/**
	 * Method deletes a Task with the provided id.
	 * 
	 * @param id - WorkingObject id
     *  
	 * Throws a hibernate exception if no such id exists.
	 */
	void delete(Long id);

	/**
	 * @return list of tasks related to a projectId, The element count  is limited to 10 and the page parameter determines which  page will be returned
	 * @throws Exception 
	 */

	List<Task> listTasksPaged(Long projectId, Integer page);

}
