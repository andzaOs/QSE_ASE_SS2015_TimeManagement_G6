
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

import at.tuwien.ase.dao.ProjectDaoInterface;
import at.tuwien.ase.dao.UserDaoInterface;
import at.tuwien.ase.model.Project;
import at.tuwien.ase.model.User;
import at.tuwien.ase.rest.security.SecurityInterceptor;

@Stateless// please use only stateles sessionbeans
@Path("/UserRest")
@Interceptors ({SecurityInterceptor.class})
public class UserRest implements UserRestI {
	
	ProxyRemover<User> proxyRemover=new ProxyRemover<User>();
	@PersistenceContext 
	Session session;
	@EJB//inject 
	UserDaoInterface userDao;
	
	@EJB
	ProjectDaoInterface projectDao;
	
	@Override
	@GET
	@Path("getItem/{id}")
	@Produces("application/json")
	@RolesAllowed({"SUPERVISOR", "MANAGER"})
	public User getItem(@PathParam("id") Long id) throws Exception {
		User u = userDao.get(id);
		return  (User) ProxyRemover.cleanFromProxies(session,u);//clean hibernate stuff
	}
	
	@Override
	@GET
	@Path("getWorkingWorkersForProjectId/{id}")
	@Produces("application/json")
	@RolesAllowed({"SUPERVISOR", "MANAGER"})

	public List<User> getWorkingWorkersForProjectId(@PathParam("id") Long id) throws Exception {
		
		return ProxyRemover.cleanFromProxies(session,userDao.getWorkingWorkersForProjectId(id));
	}

	@Override
	@GET
	@Path("listAll/")
	@Produces("application/json")
	@RolesAllowed({"SUPERVISOR", "MANAGER"})
	public List<User> listAll() throws Exception {
		
		return ProxyRemover.cleanFromProxies(session,userDao.listAll());
	}


	@Override
	@POST
	@Path("persist/")
	@Consumes("application/json")
	@RolesAllowed({"SUPERVISOR", "MANAGER"})

	public void createOrUpdateUser(User u) throws Exception {
		
		if(u.getId()==null){

			session.save(u);
		}else{
			User olduser = (User) session.load(User.class, u.getId());
			u.setPassword(olduser.getPassword());
			session.merge(u);
		}
	
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
		userDao.delete(id);
		
	}
	@Override
	@POST
	@Path("updatePassword")
	@RolesAllowed({"SUPERVISOR", "MANAGER"})
	public void updateUserPassword(User u) throws Exception {
		User user = (User) session.load(User.class, u.getId());
		user.setPassword(u.getPassword());
		session.persist(user);
	}
}
