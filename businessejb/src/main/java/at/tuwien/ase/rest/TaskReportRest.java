package at.tuwien.ase.rest;

import java.util.ArrayList;
import java.util.Iterator;
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
import org.hibernate.Transaction;

import at.tuwien.ase.dao.TaskReportDaoInterface;
import at.tuwien.ase.model.Resource;
import at.tuwien.ase.model.ResourceUsage;
import at.tuwien.ase.model.Task;
import at.tuwien.ase.model.TaskReport;
import at.tuwien.ase.rest.TaskReportRestI;
import at.tuwien.ase.rest.security.SecurityInterceptor;
import at.tuwien.ase.model.User;

@Stateless
@Path("/TaskReportRest")
@Interceptors ({SecurityInterceptor.class})
public class TaskReportRest implements TaskReportRestI {

	@PersistenceContext
	protected
	Session session;
	ProxyRemover<User> proxyRemover = new ProxyRemover<User>();

	@EJB
	protected
	// inject
	 TaskReportDaoInterface taskReportDao;
	
	@Override
	@GET
	@Path("getLatest10TaskReportForProjectId/{id}")
	@Produces("application/json")
	@RolesAllowed({"WORKER", "SUPERVISOR", "MANAGER"})
	public List<TaskReport> getLatest10TaskReportForProjectId(@PathParam("id") Long id) throws Exception {
		List<TaskReport> ret = taskReportDao.getLatest10TaskReportForProjectId(id);
		
		
		return ProxyRemover.cleanFromProxies(session,ret);
	}
	@Override
	@GET
	@Path("getItem/{id}")
	@Produces("application/json")
	@RolesAllowed({"WORKER", "SUPERVISOR", "MANAGER"})
	public TaskReport getItem(@PathParam("id") Long id) throws Exception {
		TaskReport tr = taskReportDao.get(id);
		if(tr==null){
			return null;
		}
		for ( ResourceUsage u : tr.getResourceUsageList()) {
				u.setTaskReport(null);
		}
		return (TaskReport) ProxyRemover.cleanFromProxies(session,tr);// clean hibernate
																// stuff
	}

	@Override
	@GET
	@Path("listAll/")
	@Produces("application/json")
	@RolesAllowed({"WORKER", "SUPERVISOR", "MANAGER"})
	public List<TaskReport> listAll() throws Exception {
		List<TaskReport> ret = taskReportDao.listAll();
		
		for (TaskReport i : ret) {
			for ( ResourceUsage u : i.getResourceUsageList()) {
				u.setTaskReport(null);
			}
		}
		return ProxyRemover.cleanFromProxies(session,ret);
	}
	
	@Override
	@GET
	@Path("listAllForWorker/{id}")
	@Produces("application/json")
	@RolesAllowed({"WORKER", "SUPERVISOR", "MANAGER"})
	public List<TaskReport> listAllForWorker(@PathParam("id") Long id) throws Exception {
		
		List<TaskReport> reps = taskReportDao.getAllTaskReportsForWorker(id);
		

		
		return ProxyRemover.cleanFromProxies(session,reps);
	}

	@Override
	@POST
	@Path("persist/")
	@Consumes("application/json")
	/**
	 * creates or updates a task Report
	 * supported relationships are : TASK, ResourceUsage
	 */
	@RolesAllowed({"WORKER", "SUPERVISOR", "MANAGER"})
	public void createOrUpdateTaskReport(TaskReport report) throws Exception {
		Transaction t = session.beginTransaction();
		if (report.getTask() != null) {
			Task task = (Task) session.load(Task.class, report.getTask().getId());

			report.setTask(task);
			session.update(task);
		}
		if (report.getResourceUsageList() != null) {
			System.out.println("ResourceUsage exists - size: " + report.getResourceUsageList().size());
			for (ResourceUsage ru : report.getResourceUsageList()) {
			
				Resource resource = (Resource) session.load(Resource.class, ru.getResource().getId());
				System.out.println("RU id: " + ru.getId());

				ru.setTaskReport(report);
			
				session.saveOrUpdate(ru);
			}
		}
		
		session.saveOrUpdate(report);
		t.commit();
	}

	@Override
	@GET
	@Path("getReportsToApproveByApproveId/{id}")
	@Produces("application/json")
	/**
	 * List taskReports for the approver id which are NEW or REJECTED ie:
	 * select t from TaskReport t join   t.task as tt where ( t.status=? or  t.status=?) and tt.approver.id=?
	 */
	@RolesAllowed({"SUPERVISOR", "MANAGER"})
	public List<TaskReport> getReportsToApproveByApproveId(@PathParam("id") Long id) throws Exception {
		List<TaskReport> tl = taskReportDao.getReportsToApproveByApproveId(id);
		return ProxyRemover.cleanFromProxies(session,tl);// clean hibernate stuff
	}

	@Override
	@POST
	@Path("approve/")
	@Consumes("application/json")
	@RolesAllowed({"SUPERVISOR"})
	public void approveTaskReportList(List<TaskReport> list) throws Exception {
		taskReportDao.approveTaskList(list);

	}
	@Override
	@GET
	@Path("getTaskReportsForWorker/{id}")
	@Produces("application/json")
	@RolesAllowed({"SUPERVISOR","WORKER"})
	public List<TaskReport> getTaskReportsForWorker(@PathParam("id") Long id) throws Exception {
		List<TaskReport> tl = taskReportDao.getTaskReportsForWorker(id);
		 session.setFlushMode(FlushMode.MANUAL);
		for (TaskReport tr : tl) {
			for ( ResourceUsage u : tr.getResourceUsageList()) {
				u.setTaskReport(null);
				if(u.getResource()!=null){
					u.getResource().setResourceUsageList(null);
				}
			}
			tr.getTask().setTaskReportList(null);
		
		}
		
		return ProxyRemover.cleanFromProxies(session,tl);// clean hibernate stuff
	}
	@Override
	@GET
	@Path("getTaskReportsForProject/{id}")
	@Produces("application/json")
	@RolesAllowed({"SUPERVISOR", "MANAGER"})

	public List<TaskReport> getTaskReportsForProject(@PathParam("id") Long id) throws Exception {
		List<TaskReport> tl = taskReportDao.getTaskReportsForProject(id);
		return ProxyRemover.cleanFromProxies(session,tl);// clean hibernate stuff
	}


	@Override
	@POST
	@Path("reject/")
	@Consumes("application/json")
	@RolesAllowed({"SUPERVISOR", "MANAGER"})
	public void rejectReport(TaskReport report) throws Exception {
		
		taskReportDao.rejectTaskReport(report);
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
		taskReportDao.delete(id);
		
	}
}
