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

import org.hibernate.FlushMode;
import org.hibernate.Session;

import at.tuwien.ase.dao.CompanyDaoInterface;
import at.tuwien.ase.model.Company;
import at.tuwien.ase.model.User;
import at.tuwien.ase.rest.security.SecurityInterceptor;

@Stateless
// please use only stateles sessionbeans
@Path("/CompanyRest")
@Interceptors ({SecurityInterceptor.class})
public class CompanyRest implements CompanyRestI {
	
	ProxyRemover<User> proxyRemover = new ProxyRemover<User>();
	@PersistenceContext 
	Session session;
	@EJB
	// inject
	CompanyDaoInterface companyDao;

	@Override
	@GET
	@Path("getItem/{id}")
	@Produces("application/json")
	@RolesAllowed({"SUPERVISOR", "MANAGER"})
	public Company getItem(@PathParam("id") Long id) throws Exception {
		Company u = companyDao.get(id);
		session.setFlushMode(FlushMode.MANUAL);//dont let dirty checking  make mess
		for ( User c : u.getUserList()) {
			c.setCompany(null);
		}
		return ProxyRemover.cleanFromProxies(session,u);// clean hibernate stuff
	}

	@Override
	@GET
	@Path("listAll/")
	@Produces("application/json")
	@RolesAllowed({"SUPERVISOR", "MANAGER"})
	public List<Company> listAll() throws Exception {
		session.setFlushMode(FlushMode.MANUAL);//dont let dirty checking  make mess
		
		List<Company> ret = companyDao.listAll();
		for (Company u : ret) {
			for ( User c : u.getUserList()) {
				c.setCompany(null);
			}
		}
		
		return ProxyRemover.cleanFromProxies(session,ret);
	}

	@Override
	@POST
	@Path("persist/")
	@Consumes("application/json")
	@RolesAllowed({"SUPERVISOR", "MANAGER"})
	public void createOrUpdateCompany(Company c) throws Exception {
		
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
		companyDao.delete(id);
		
	}
}
