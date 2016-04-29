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

import at.tuwien.ase.dao.ResourceUsageDaoInterface;
import at.tuwien.ase.model.ResourceUsage;
import at.tuwien.ase.model.User;
import at.tuwien.ase.rest.security.SecurityInterceptor;

@Stateless// please use only stateles sessionbeans
@Path("/ResourceUsageRest")
@Interceptors ({SecurityInterceptor.class})
public class ResourceUsageRest implements ResourceUsageRestI {
	
	ProxyRemover<User> proxyRemover=new ProxyRemover<User>();
	@PersistenceContext 
	Session session;
	@EJB//inject 
	ResourceUsageDaoInterface  resourceUsageDao ;
	
	@Override
	@GET
	@Path("getItem/{id}")
	@Produces("application/json")
	@RolesAllowed({"WORKER", "SUPERVISOR", "MANAGER"})

	public ResourceUsage getItem(@PathParam("id") Long id) throws Exception {
		ResourceUsage u = resourceUsageDao.get(id);
		return  ProxyRemover.cleanFromProxies(session,u);//clean hibernate stuff
	}
	
	
	@Override
	@GET
	@Path("listAll/")
	@Produces("application/json")
	@RolesAllowed({"WORKER", "SUPERVISOR", "MANAGER"})
	public List<ResourceUsage> listAll() throws Exception {
		
		return ProxyRemover.cleanFromProxies(session,resourceUsageDao.listAll());
	}


	@Override
	@POST
	@Path("persist/")
	@Consumes("application/json")
	@RolesAllowed({"WORKER", "SUPERVISOR", "MANAGER"})
	public void createOrUpdateResourceUsage(ResourceUsage ru) throws Exception {
		session.saveOrUpdate(ru);
	}
	
	@Override
	@DELETE
	@Path("delete/{id}")
	@RolesAllowed({"WORKER", "SUPERVISOR", "MANAGER"})
	/**	
	 * Deletes the  entity with the given id
	 * Ie sets the deleted flag to true
	 */
	public void delete(@PathParam("id") Long id) throws Exception {
		resourceUsageDao.delete(id);
		
	}
}