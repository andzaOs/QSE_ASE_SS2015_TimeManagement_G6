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

import org.hibernate.FlushMode;
import org.hibernate.Session;

import at.tuwien.ase.dao.ProjectTypeDaoInterface;
import at.tuwien.ase.model.ProjectType;
import at.tuwien.ase.model.Task;
import at.tuwien.ase.model.User;
import at.tuwien.ase.model.WorkingObject;
import at.tuwien.ase.rest.security.SecurityInterceptor;

@Stateless
// please use only stateles sessionbeans
@Path("/ProjectTypeRest")
@Interceptors ({SecurityInterceptor.class})
public class ProjectTypeRest implements ProjectTypeRestI {
	ProxyRemover<User> proxyRemover = new ProxyRemover<User>();
	@PersistenceContext 
	Session session;
	@EJB
	// inject
	ProjectTypeDaoInterface projectTypeDao;

	@Override
	@GET
	@Path("getItem/{id}")
	@Produces("application/json")
	@RolesAllowed({"SUPERVISOR", "MANAGER"})
	public ProjectType getItem(@PathParam("id") Long id) throws Exception {
		
		ProjectType p = projectTypeDao.get(id);
		if(p==null){
			return p;
		}
		session.setFlushMode(FlushMode.MANUAL);//dont let dirty checking  make mess
		for ( WorkingObject w : p.getWorkingObjectList()) {
			w.getProjectTypeList().clear();
		}
		return (ProjectType) ProxyRemover.cleanFromProxies(session,p);// clean hibernate
																// stuff
	}

	@Override
	@GET
	@Path("listAll/")
	@Produces("application/json")
	@RolesAllowed({"SUPERVISOR", "MANAGER"})
	public List<ProjectType> listAll() throws Exception {
		List<ProjectType> l = projectTypeDao.listAll();
		session.setFlushMode(FlushMode.MANUAL);//dont let dirty checking  make mess
		for (ProjectType p : l) {
			for ( WorkingObject w : p.getWorkingObjectList()) {
				w.getProjectTypeList().clear();
			}
		}
		return ProxyRemover.cleanFromProxies(session,l);
	}

	@Override
	@POST
	@Path("persist/")
	@Consumes("application/json")
	@RolesAllowed({"SUPERVISOR", "MANAGER"})
	public void createOrUpdateProjectType(ProjectType pt) throws Exception {
		
		List<WorkingObject> list=new ArrayList<WorkingObject>();
		
		if (pt.getWorkingObjectList()!= null) {
			for (WorkingObject w : pt.getWorkingObjectList()) {
				WorkingObject wd = (WorkingObject) session.load(WorkingObject.class, w.getId());
				list.add(wd);
				
				if(!wd.getProjectTypeList().contains(pt)){
					wd.getProjectTypeList().add(pt);
					session.persist(wd);
				}
			}
			
		}
		
		pt.setWorkingObjectList(list);
		session.saveOrUpdate(pt);
	
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
		projectTypeDao.delete(id);
		
	}
}
