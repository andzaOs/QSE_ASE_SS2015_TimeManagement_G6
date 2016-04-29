
package at.tuwien.ase.rest;

import java.util.HashSet;
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
import org.hibernate.Transaction;

import at.tuwien.ase.dao.ProjectDaoInterface;
import at.tuwien.ase.model.Project;
import at.tuwien.ase.model.ProjectType;
import at.tuwien.ase.model.TaskType;
import at.tuwien.ase.model.User;
import at.tuwien.ase.rest.security.SecurityInterceptor;

@Stateless
// please use only stateles sessionbeans
@Path("/ProjectRest")
@Interceptors({SecurityInterceptor.class})
public class ProjectRest implements ProjectRestI {

	ProxyRemover<User> proxyRemover = new ProxyRemover<User>();
	@PersistenceContext
	Session session;
	@EJB
	// inject
	ProjectDaoInterface projectDao;

	@Override
	@GET
	@Path("getItem/{id}")
	@Produces("application/json")
	@RolesAllowed({"SUPERVISOR", "MANAGER"})
	public Project getItem(@PathParam("id") Long id) throws Exception {
		Project u = projectDao.get(id);
		return ProxyRemover.cleanFromProxies(session,u);// clean hibernate stuff
	}

	@Override
	@GET
	@Path("listAll/")
	@Produces("application/json")
	@RolesAllowed({"SUPERVISOR", "MANAGER"})
	public List<Project> listAll() throws Exception {
		List<Project> l = projectDao.listAll();
		return ProxyRemover.cleanFromProxies(session,l);
	}

	@Override
	@POST
	@Path("persist/")
	@Consumes("application/json")
    @RolesAllowed({"SUPERVISOR", "MANAGER"})
	public void createOrUpdateProject(Project p) throws Exception {
		
		
		Transaction tx = session.beginTransaction();
		
		if(p.getProjectType()!=null){
		
			ProjectType t = (ProjectType) session.load(ProjectType.class, p.getProjectType().getId());
		
		}
  
		HashSet<TaskType> ttSet = new HashSet<TaskType>();
		if(p.getTaskTypeList()!=null){
			
			for ( TaskType tt : p.getTaskTypeList()) {
				TaskType t = (TaskType) session.load(TaskType.class,tt.getId());
				ttSet.add(t);
			
			}
		}
		p.setTaskTypeList(ttSet);
		
		HashSet<User> uSet = new HashSet<User>();
		if(p.getUserList()!=null){
			
			for ( User u : p.getUserList()) {
				User t = (User) session.load(User.class,u.getId());
				
				uSet.add(t);
			}
		}
		p.setUserList(uSet);
		
		
		
		session.saveOrUpdate(p);
		tx.commit();
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
		projectDao.delete(id);
		
	}
	
	@Override
	@GET
	@Path("getProjectItem/{id}")
	@Produces("application/json")
	@RolesAllowed("SUPERVISOR")
	public Project getProjectForUser(@PathParam("id") Long id) throws Exception {
		
		Project p = null;
		
		for (Project proj : projectDao.listAll()) {
			for (User u : proj.getUserList()) {
				
				if(u.getId().longValue() == id.longValue()) {
					
					p = proj;
					break;
				}
			}
		}

		return ProxyRemover.cleanFromProxies(session,p);//clean hibernate stuff
	}

}
