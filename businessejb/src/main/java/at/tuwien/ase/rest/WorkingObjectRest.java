package at.tuwien.ase.rest;

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

import at.tuwien.ase.dao.WorkingObjectDaoInterface;
import at.tuwien.ase.model.User;
import at.tuwien.ase.model.WorkingObject;
import at.tuwien.ase.rest.security.SecurityInterceptor;

@Stateless// please use only stateles sessionbeans
@Path("/WorkingObjectRest")
@Interceptors ({SecurityInterceptor.class})
public class WorkingObjectRest implements WorkingObjectRestI {
	
	ProxyRemover<User> proxyRemover=new ProxyRemover<User>();
	@PersistenceContext 
	Session session;
	@EJB//inject 
	WorkingObjectDaoInterface workingObjectDao ;
	
	@Override
	@GET
	@Path("getItem/{id}")
	@Produces("application/json")
	@RolesAllowed({"SUPERVISOR", "MANAGER"})
	public WorkingObject getItem(@PathParam("id") Long id) throws Exception {
		WorkingObject o = workingObjectDao.get(id);
		return  ProxyRemover.cleanFromProxies(session,o);//clean hibernate stuff
	}
	
	@Override
	@GET
	@Path("listAll/")
	@Produces("application/json")
	@RolesAllowed({"SUPERVISOR", "MANAGER"})
	public List<WorkingObject> listAll() throws Exception {
		
		return ProxyRemover.cleanFromProxies(session,workingObjectDao.listAll());
	}

	@Override
	@POST
	@Path("persist/")
	@Consumes("application/json")
	@RolesAllowed({"SUPERVISOR", "MANAGER"})

	public void createOrUpdateWorkingObject(WorkingObject wo) throws Exception {

		session.saveOrUpdate(wo);
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
		workingObjectDao.delete(id);
		
	}
}
