package at.tuwien.ase.rest;

import java.util.ArrayList;
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

import org.hibernate.Session;

import at.tuwien.ase.dao.TaskTypeDaoInterface;
import at.tuwien.ase.model.TaskType;
import at.tuwien.ase.model.User;
import at.tuwien.ase.rest.security.SecurityInterceptor;

@Stateless// please use only stateles sessionbeans
@Path("/TaskTypeRest")
@Interceptors ({SecurityInterceptor.class})
public class TaskTypeRest implements TaskTypeRestI {
	@PersistenceContext 
	Session session;
	ProxyRemover<User> proxyRemover=new ProxyRemover<User>();
	
	@EJB//inject 
	TaskTypeDaoInterface taskTypeDao ;
	
	@Override
	@GET
	@Path("getItem/{id}")
	@Produces("application/json")
	@RolesAllowed({"SUPERVISOR", "MANAGER"})
	public TaskType getItem(@PathParam("id") Long id) throws Exception {
		TaskType u = taskTypeDao.get(id);
		return  (TaskType) ProxyRemover.cleanFromProxies(session,u);//clean hibernate stuff
	}
	
	@Override
	@GET
	@Path("listAll/")
	@Produces("application/json")
	@RolesAllowed({"SUPERVISOR", "MANAGER"})
	public List<TaskType> listAll() throws Exception {

		return ProxyRemover.cleanFromProxies(session,taskTypeDao.listAll());
	}

	@Override
	@POST
	@Path("persist/")
	@Consumes("application/json")
	@RolesAllowed({"SUPERVISOR", "MANAGER"})

	public void createOrUpdateTaskType(TaskType tt) throws Exception {
		session.saveOrUpdate(tt);
	}
	
	@Override
	@DELETE
	@Path("delete/{id}")
	@RolesAllowed({"SUPERVISOR", "MANAGER"})
	/**	
	 * Deletes the  entity with the given id
	 * Ie sets the deleted flag to true
	 */
	public void delete(@PathParam("id") Long id) throws Exception {
		taskTypeDao.delete(id);
		
	}
}