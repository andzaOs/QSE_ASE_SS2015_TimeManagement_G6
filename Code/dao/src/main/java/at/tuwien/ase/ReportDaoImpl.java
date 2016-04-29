package at.tuwien.ase;

import java.util.List;

import javax.ejb.Stateless;
import javax.persistence.PersistenceContext;

import org.hibernate.Criteria;
import org.hibernate.Query;
import org.hibernate.Session;

import at.tuwien.ase.dao.ProjectDaoInterface;
import at.tuwien.ase.dao.ReportDaoInterface;
import at.tuwien.ase.model.Category;
import at.tuwien.ase.model.Project;
import at.tuwien.ase.model.TaskReportStatus;

@Stateless
public class ReportDaoImpl implements ReportDaoInterface {
	@PersistenceContext
	private Session session;

	@Override
	public Double getTotalHoursForProject(Long id) {
		Query q = session.createQuery("select sum(datedIFF ('minute',t.begin, t.end)) from TaskReport t where t.task.project.id=? and t.status=? and  (t.deleted!=true or t.deleted is null )");
		q.setLong(0, id);
		q.setParameter(1, TaskReportStatus.APPROVED);
		Long r=(Long) q.uniqueResult();
		if(r==null){
			return 0.0;
		}
		return r/60.0;
	
	}

	@Override
	public Long getTotalTasksPerProjectId(Long id) {
		Query q = session.createQuery("select count(t) from Task as t where  (t.deleted!=true or t.deleted is null ) and t.project.id=? ");
		q.setLong(0, id);
	
		return (Long) q.uniqueResult();
	}

	@Override
	public Long getTotalFinishedTasksPerProjectId(Long id) {
		Query q = session.createQuery("select count(t) from Task as t where  (t.deleted!=true or t.deleted is null ) and t.project.id=? and t.finished=true ");
		q.setLong(0, id);
		
	
		return (Long) q.uniqueResult();
	}



}
