package at.tuwien.ase.rest;

import java.util.List;

import javax.ejb.Local;

import at.tuwien.ase.model.TaskType;

@Local
public interface TaskTypeRestI {

	/**
	 * Method returns a TaskType by providing its id. If an id exists in the
	 * database, a TaskType is returned. Otherwise, null is returned. 
	 * 
	 * @param id - id of the WorkingObject
	 * 
	 * @return an entity with the given id or null if no such entity exists.
	 *
	 */
	TaskType getItem(Long id) throws Exception;

	/**
	 * Method lists all TaskType stored in the database.
	 * 
	 * @return a List of all entities from the database or an empty list if no entry exists.
	 *
	 */
	List<TaskType> listAll() throws Exception;

	/**
	 * Method saves a TaskType provided as an input or updates an existing TaskType
	 * in the database.
	 * 
	 * @param c - provided TaskType
	 * 
	 * @throws Exception - if an object can not be stored or update.
	 *
	 */
	void createOrUpdateTaskType(TaskType tt) throws Exception;

	/**
	 * Method deletes a TaskType with the provided id from the database.
	 * 
	 * @param id - id of the TaskType
	 * 
	 * @throws Exception - throws a hibernate exception if no such id exists.
	 * 
	 */
	void delete(Long id) throws Exception;
}
