package at.tuwien.ase.rest;

import java.util.List;

import javax.annotation.security.RolesAllowed;
import javax.ejb.EJB;
import javax.ejb.Stateless;
import javax.interceptor.Interceptors;
import javax.persistence.PersistenceContext;
import javax.ws.rs.Consumes;
import javax.ws.rs.GET;
import javax.ws.rs.DELETE;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;

import org.hibernate.Session;

import at.tuwien.ase.dao.CategoryDaoInterface;
import at.tuwien.ase.model.Category;
import at.tuwien.ase.model.User;
import at.tuwien.ase.rest.security.SecurityInterceptor;

@Stateless
// please use only stateles sessionbeans
@Path("/CategoryRest")
@Interceptors ({SecurityInterceptor.class})
public class CategoryRest implements CategoryRestI {
	ProxyRemover<User> proxyRemover = new ProxyRemover<User>();
	@PersistenceContext 
	Session session;
	@EJB
	// inject
	
	CategoryDaoInterface categoryDao;

	@Override
	@GET
	@Path("getItem/{id}")
	@Produces("application/json")
	@RolesAllowed({"MANAGER","SUPERVISOR"})
	public Category getItem(@PathParam("id") Long id) throws Exception {
		Category c = categoryDao.get(id);
		return ProxyRemover.cleanFromProxies(session,c);// clean hibernate stuff
	}

	@Override
	@GET
	@Path("listAll/")
	@Produces("application/json")
	@RolesAllowed({"MANAGER","SUPERVISOR"})
	public List<Category> listAll() throws Exception {

		return ProxyRemover.cleanFromProxies(session,categoryDao.listAll());
	}

	@Override
	@POST
	@Path("persist/")
	@Consumes("application/json")
	@RolesAllowed({"SUPERVISOR", "MANAGER"})
	public void createOrUpdateCategory(Category c) throws Exception {

		session.saveOrUpdate(c);
		
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
		categoryDao.delete(id);
		
	}

}
