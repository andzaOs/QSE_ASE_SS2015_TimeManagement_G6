package at.tuwien.ase.rest;

import java.security.InvalidParameterException;
import java.util.List;

import javax.annotation.security.RolesAllowed;
import javax.ejb.EJB;
import javax.ejb.Stateless;
import javax.interceptor.Interceptors;
import javax.persistence.PersistenceContext;
import javax.ws.rs.Consumes;
import javax.ws.rs.DELETE;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;

import org.hibernate.FlushMode;
import org.hibernate.Session;
import org.hibernate.Transaction;

import at.tuwien.ase.dao.TaskDaoInterface;
import at.tuwien.ase.model.Project;
import at.tuwien.ase.model.Task;
import at.tuwien.ase.model.TaskReport;
import at.tuwien.ase.model.TaskType;
import at.tuwien.ase.model.User;
import at.tuwien.ase.model.WorkingObject;
import at.tuwien.ase.rest.security.SecurityInterceptor;

@Stateless
// please use only stateles sessionbeans
@Path("/TaskRest")
@Interceptors ({SecurityInterceptor.class})
public class TaskRest implements TaskRestI {

	@PersistenceContext
	protected Session session;
	
	
	ProxyRemover<Task> proxyRemover = new ProxyRemover<Task>();
	@EJB
	// inject
	protected TaskDaoInterface taskDao;

	@Override
	@GET
	@Path("getItem/{id}")
	@Produces("application/json")
	@RolesAllowed({"SUPERVISOR", "MANAGER"})
	public Task getItem(@PathParam("id") Long id) throws Exception {
		Task t = taskDao.get(id);
		return (Task) ProxyRemover.cleanFromProxies(session,t);// clean hibernate
															// stuff
	}

	@Override
	@GET
	@Path("listTasksPaged/{projectId}/{page}")
	@Produces("application/json")
	@RolesAllowed({"SUPERVISOR", "MANAGER"})
	public List<Task> listTasksPaged(@PathParam("projectId") Long projectId,@PathParam("page") Integer page) throws Exception {

		return ProxyRemover.cleanFromProxies(session,taskDao.listTasksPaged(projectId,page));
	}
	
	
	@Override
	@GET
	@Path("listAll/")
	@Produces("application/json")
	@RolesAllowed({"SUPERVISOR", "MANAGER"})
	public List<Task> listAll() throws Exception {

		return ProxyRemover.cleanFromProxies(session,taskDao.listAll());
	}

	@Override
	@GET
	@Path("listTasksForUserId/{id}")
	@Produces("application/json")
	@RolesAllowed({"MANAGER", "SUPERVISOR","WORKER"})
	public List<Task> listTasksForUserId(@PathParam("id") Long id) throws Exception {
		session.setFlushMode(FlushMode.MANUAL);//dont let dirty checking  make mess
		List<Task> l = taskDao.listTasksForUserId(id);
		for (Task t : l) {
			for ( TaskReport tt : t.getTaskReportList()) {
				tt.setTask(null);
			}
		}
		return ProxyRemover.cleanFromProxies(session,l);
	}

	@Override
	@GET
	@Path("listTasksWhichAreNotApprovedForApproverId/{id}")
	@Produces("application/json")
	@RolesAllowed({"SUPERVISOR", "MANAGER"})
	public List<Task> listTasksWhichAreNotApprovedForApproverId(@PathParam("id") Long id) throws Exception {
		
		return ProxyRemover.cleanFromProxies(session,taskDao.listTasksWhichAreNotApprovedForApproverId(id));

	}

	@Override
	@GET
	@Path("listTasksForApproverId/{id}")
	@Produces("application/json")
	@RolesAllowed({"SUPERVISOR", "MANAGER"})
	public List<Task> listTasksForApproverId(@PathParam("id") Long id) throws Exception {
		session.setFlushMode(FlushMode.MANUAL);//dont let dirty checking  make mess
		List<Task> l = taskDao.listTasksForApproverId(id);
		
		for (Task task : l) {
			for (TaskReport r  : task.getTaskReportList()) {
				r.setTask(null);
			}
		}
		return ProxyRemover.cleanFromProxies(session,l);
	}

	@Override
	@POST
	@Path("persist/")
	@Consumes("application/json")
	@RolesAllowed({"SUPERVISOR", "MANAGER"})
	public void createOrUpdateTask(Task task) throws Exception {
		
		Transaction t = session.beginTransaction();
		if (task.getApprover() != null) {
			User a = (User) (session.load(User.class, task.getApprover().getId()));
			task.setApprover(a);
		}
		if (task.getProject() != null) {
		Project p = (Project) (session.load(Project.class, task.getProject().getId()));
		task.setProject(p);
		}
		if (task.getWorker() != null) {
			User w = (User) (session.load(User.class, task.getWorker().getId()));
			task.setWorker(w);


		}
		if (task.getTaskType() != null) {
			TaskType tt = (TaskType) (session.load(TaskType.class, task.getTaskType().getId()));
			task.setTaskType(tt);
			

		}else{
			throw new InvalidParameterException("TASK TYPE HAS TO BE SET");
		}
		if (task.getWorkingObject() != null) {
			task.setWorkingObject((WorkingObject) (session.load(WorkingObject.class, task.getWorkingObject().getId())));
		}
		
		
		session.saveOrUpdate(task);//persist(task);
		t.commit();
		
		
	}
	@Override
	@DELETE
	@Path("delete/{id}")
	/**	
	 * Deletes the  entity with the given id
	 * Ie sets the deleted flag to true
	 */

	@RolesAllowed({"SUPERVISOR", "MANAGER"})
	public void delete(@PathParam("id") Long id) throws Exception {
		taskDao.delete(id);
		
	}
}