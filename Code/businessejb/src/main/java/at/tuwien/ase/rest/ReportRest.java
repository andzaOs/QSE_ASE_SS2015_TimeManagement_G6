package at.tuwien.ase.rest;

import javax.ejb.EJB;
import javax.ejb.Stateless;
import javax.interceptor.Interceptors;
import javax.persistence.PersistenceContext;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;

import org.hibernate.Session;

import at.tuwien.ase.dao.ReportDaoInterface;
import at.tuwien.ase.rest.ReportRestI;
import at.tuwien.ase.rest.security.SecurityInterceptor;

@Stateless
@Path("/ReportRest")
@Interceptors ({SecurityInterceptor.class})
public class ReportRest implements  ReportRestI {
	@PersistenceContext 
	Session session;
	@EJB
	private ReportDaoInterface reportDao;
	
	@Override
	@GET
	@Path("/reportTotalHoursForProjectId/{id}")
	@Produces("application/json")
	public Double getTotalHoursForProject(@PathParam("id") Long id) throws Exception {
			return reportDao.getTotalHoursForProject(id);
			
		
	}

	@Override
	@GET
	@Path("/reportTotalTasksPerProjectId/{id}")
	@Produces("application/json")
		
	public String getTotalTasksPerProjectId(@PathParam("id") Long id) throws Exception {
		return reportDao.getTotalTasksPerProjectId(id)+"";
		
	}
	
	@Override
	@GET
	@Path("/reportFinishedTotalTasksPerProjectId/{id}")
	@Produces("application/json")
		
	public String getTotalFinishedTasksPerProjectId(@PathParam("id") Long id) throws Exception {
		return reportDao.getTotalFinishedTasksPerProjectId(id)+"";
		
	}
	
	
	
}

