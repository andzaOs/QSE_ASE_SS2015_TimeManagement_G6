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

import at.tuwien.ase.model.TaskType;

import org.hibernate.Session;
import org.hibernate.Transaction;

import at.tuwien.ase.dao.ResourceDaoInterface;
import at.tuwien.ase.model.Resource;
import at.tuwien.ase.model.TaskType;
import at.tuwien.ase.model.User;
import at.tuwien.ase.rest.security.SecurityInterceptor;

@Stateless// please use only stateles sessionbeans
@Path("/ResourceRest")
@Produces("application/json")
@Interceptors ({SecurityInterceptor.class})
public class ResourceRest implements ResourceRestI {
	ProxyRemover<User> proxyRemover=new ProxyRemover<User>();
	@PersistenceContext 
	Session session;

	@EJB//inject 
	ResourceDaoInterface  resourceDao ;


	@Override
	@GET
	@Path("getItem/{id}")
	public Resource getItem(@PathParam("id") Long id) throws Exception {
		Resource u = resourceDao.get(id);
		return  ProxyRemover.cleanFromProxies(session,u);//clean hibernate stuff
	}
	
	
	@Override
	@GET
	@Path("listAll/")
	public List<Resource> listAll() throws Exception {
		
		return ProxyRemover.cleanFromProxies(session,resourceDao.listAll());
	}
	
	@Override
	@GET
	@Path("listAllForTaskType/{id}")
	@RolesAllowed({"WORKER","SUPERVISOR"})
	public List<Resource> listAllForTaskType(@PathParam("id") Long id) throws Exception {
		List<Resource> l=new ArrayList<Resource>();
		List<Resource> res = resourceDao.listAllForTaskType(id);
		for ( Resource i : res) {
			

			Resource ret = new Resource();
			ret.setCategory(i.getCategory());
			ret.setDescription(i.getDescription());
			ret.setId(i.getId());
			List<TaskType> ttlist=new ArrayList<TaskType>();
			
			for ( TaskType t : i.getTaskTypeList()) {
				TaskType tt = new TaskType();
				tt.setDescription(t.getDescription());
				tt.setExpectedWorkHours(t.getExpectedWorkHours());
				tt.setName(t.getName());
				tt.setTaskNumber(t.getTaskNumber());
				ttlist.add(tt);
			}
			ret.setTaskTypeList(ttlist);
			l.add(ret);
		}
		
//		return ProxyRemover.cleanFromProxies(session,resourceDao.listAllForTaskType(id));
		
		
		return l;
	}
	
	@Override
	@POST
	@Path("persist/")
	@Consumes("application/json")
	public void createOrUpdateResourceWithCategory(Resource resource) throws Exception {
		
		Transaction t = session.beginTransaction();
		
		
		if(resource.getCategory()!=null){
			session.saveOrUpdate(resource.getCategory());
		}
		session.saveOrUpdate(resource);
		
		
		t.commit();
		
	}
	@Override
	@DELETE
	@Path("delete/{id}")
	/**	
	 * Deletes the  entity with the given id
	 * Ie sets the deleted flag to true
	 */
	public void delete(@PathParam("id") Long id) throws Exception {
		resourceDao.delete(id);
		
	}
}