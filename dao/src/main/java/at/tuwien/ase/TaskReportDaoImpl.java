package at.tuwien.ase;

import java.util.ArrayList;
import java.util.List;

import javax.ejb.Stateless;
import javax.persistence.PersistenceContext;

import org.hibernate.Criteria;
import org.hibernate.Query;
import org.hibernate.Session;
import org.hibernate.Transaction;

import at.tuwien.ase.dao.TaskReportDaoInterface;
import at.tuwien.ase.model.Category;
import at.tuwien.ase.model.ResourceUsage;
import at.tuwien.ase.model.TaskReport;
import at.tuwien.ase.model.TaskReportStatus;

@Stateless
public class TaskReportDaoImpl implements TaskReportDaoInterface {
	@PersistenceContext
	private Session session;

	
	@Override
	public List<TaskReport> listAll() {
		Query q = session.createQuery("from TaskReport as tr left join fetch tr.resourceUsageList where (tr.deleted!=true or tr.deleted is null) ").setResultTransformer(Criteria.DISTINCT_ROOT_ENTITY); // sugest

		List<TaskReport> ret = q.list();
		Boolean val = new Boolean(true);
		for (TaskReport tr : ret) {
			List<ResourceUsage> del = new ArrayList<ResourceUsage>();
			for (ResourceUsage ru : tr.getResourceUsageList()) {
				if (val.equals(ru.getDeleted())) {
					del.add(ru);
				}
			}
			tr.getResourceUsageList().removeAll(del);
		}
		return ret;
	}

	@Override
	public TaskReport get(Long id) {
		Query q = session.createQuery("from TaskReport as tr left join fetch tr.resourceUsageList where tr.id=? and (tr.deleted!=true or tr.deleted is null) ").setResultTransformer(
				Criteria.DISTINCT_ROOT_ENTITY);// i sugest we use hql
		q.setLong(0, id);
		TaskReport ret = (TaskReport) q.uniqueResult();
		if(ret==null){
			return null;
		}
		Boolean val = new Boolean(true);
		if (ret.getResourceUsageList() != null) {
			List<ResourceUsage> del = new ArrayList<ResourceUsage>();
			for (ResourceUsage ru : ret.getResourceUsageList()) {
				if (val.equals(ru.getDeleted())) {
					del.add(ru);
				}
			}
			ret.getResourceUsageList().removeAll(del);
		}
		return ret;
	}

	@Override
	public List<TaskReport> getReportsToApproveByApproveId(Long id) {
		Query q = session.createQuery("select t from TaskReport t join   t.task as tt where ( t.status=? or  t.status=?) and tt.approver.id=? and  (t.deleted!=true or t.deleted is null)");
		q.setParameter(0, TaskReportStatus.NEW);
		q.setResultTransformer(
				Criteria.DISTINCT_ROOT_ENTITY);
		q.setParameter(1, TaskReportStatus.REJECTED);
		q.setParameter(2, id);
		return q.list();
	}

	@Override
	public void approveTaskList(List<TaskReport> list) {
		if (list.size() > 0) {
			Transaction tx = session.beginTransaction();
			String q = "update TaskReport set status=? where id=?";
			for (int i = 1; i < list.size(); i++) {
				q += "or id=?";
			}
			Query qu = session.createQuery(q);
			qu.setParameter(0, TaskReportStatus.APPROVED);
			int i = 1;

			for (TaskReport t : list) {
				qu.setParameter(i, t.getId());
				i++;
			}
			qu.executeUpdate();
			tx.commit();

		}
	}

	@Override
	public void rejectTaskReport(TaskReport report) {
		Transaction tx = session.beginTransaction();
		TaskReport tr = (TaskReport) session.load(TaskReport.class, report.getId());
		tr.setStatus(TaskReportStatus.REJECTED);
		tr.setRejectMessage(report.getRejectMessage());
		session.persist(tr);
		tx.commit();
	}

	@Override
	public List<TaskReport> getTaskReportsForWorker(Long id) {
		// and del
		Query q = session
				.createQuery("from TaskReport as t left join fetch t.resourceUsageList as rl left join fetch rl.resource  where (DATEDIFF('hour',t.end, CURRENT_TIMESTAMP() )  <24 or t.status=?) and t.task.worker.id=? and  (t.deleted!=true or t.deleted is null)");
		q.setResultTransformer(
				Criteria.DISTINCT_ROOT_ENTITY);
		q.setParameter(0, TaskReportStatus.REJECTED);
		q.setLong(1, id);
		return q.list();

	}

	@Override
	public void delete(Long id) {
		Transaction tx = session.beginTransaction();
		TaskReport o = (TaskReport) session.load(TaskReport.class, id);
		o.setDeleted(true);
		session.persist(o);
		tx.commit();

	}

	@Override
	public List<TaskReport> getTaskReportsForProject(Long id) {
		// and del
		Query q = session
				.createQuery("select tr from Task as t join t.taskReportList as tr where t.project.id=? and (tr.deleted!=true or tr.deleted is null)");
		q.setResultTransformer(
				Criteria.DISTINCT_ROOT_ENTITY);
		
		q.setLong(0, id);
		return q.list();

	}

	@Override
	public List<TaskReport> getLatest10TaskReportForProjectId(Long id) {
		Query q = session.createQuery(" from TaskReport as tr  where (tr.deleted!=true or tr.deleted is null)and tr.begin is not null order by tr.begin  "); 
		q.setMaxResults(10);
		List<TaskReport> ret = q.list();
		return ret;
	}

	@Override
	public List<TaskReport> getAllTaskReportsForWorker(Long id) {
		// and del
		Query q = session
				.createQuery("from TaskReport as t  left join fetch t.resourceUsageList where   t.task.worker.id=? and  (t.deleted!=true or t.deleted is null)");
		q.setResultTransformer(
				Criteria.DISTINCT_ROOT_ENTITY);
		
		q.setLong(0, id);
		return q.list();
	}
}
