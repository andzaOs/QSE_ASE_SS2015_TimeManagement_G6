package at.tuwien.ase;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import javax.ejb.Stateless;
import javax.persistence.PersistenceContext;

import org.hibernate.Criteria;
import org.hibernate.Query;
import org.hibernate.Session;
import org.hibernate.Transaction;

import at.tuwien.ase.dao.TaskDaoInterface;
import at.tuwien.ase.model.Category;
import at.tuwien.ase.model.Task;
import at.tuwien.ase.model.TaskReport;
import at.tuwien.ase.model.TaskReportStatus;

@Stateless
public class TaskDaoImpl implements TaskDaoInterface {
	@PersistenceContext
	private Session session;

	@Override
	public List<Task> listAll() {
		Query q = session.createQuery("from Task  as c where c.deleted!=true or c.deleted is null");//i sugest we use hql where possible
	
		return q.list();
	}

	@Override
	public Task get(Long id) {
		Query q = session.createQuery("from Task  as c where (c.deleted!=true or c.deleted is null) and c.id=?");//i sugest we use hql where possible
		q.setLong(0, id);
		return (Task) q.uniqueResult();
	
	}

	@Override
	public List<Task> listTasksForApproverId(Long id) {
		Query q = session.createQuery("from Task as t left join fetch t.taskReportList where t.approver.id=? and  (t.deleted!=true or t.deleted is null)");
		q.setLong(0, id);
		return q.list();
	}

	@Override
	public List<Task> listTasksForUserId(Long id) {
		Query q = session.createQuery("from Task as t left join fetch t.taskReportList  where t.worker.id=?  and  (t.deleted!=true or t.deleted is null)");
		q.setResultTransformer(Criteria.DISTINCT_ROOT_ENTITY)  ;
		q.setLong(0, id);
		
		return q.list();
	}


	@Override
	public List<Task> listTasksWhichAreNotApprovedForApproverId(Long id) {//LIST ONLY  TaskReportList==empty or not all tasks approved
		Query q = session.createQuery("select t from Task as t left  join t.taskReportList as tr  "
				+ "where t.approver.id=? and (tr.status=? or tr.status=? or tr IS EMPTY) and (t.deleted!=true or t.deleted is null)");
		q.setResultTransformer(Criteria.DISTINCT_ROOT_ENTITY)  ;
		q.setLong(0, id);
		q.setParameter(1, TaskReportStatus.NEW);
		
		q.setParameter(2, TaskReportStatus.REJECTED);
		List<Task> r = q.list();
		return r;
	}
	@Override
	public void delete(Long id) {
		Transaction tx = session.beginTransaction();
		Task o = (Task) session.load(Task.class, id);
		o.setDeleted(true);
		session.persist(o);
		tx.commit();
	}

	@Override
	public List<Task> listTasksPaged(Long projectId,Integer page) {
		Query q = session.createQuery("from Task  as c where (c.deleted!=true or c.deleted is null ) and c.project.id=? order by c.id desc");//i sugest we use hql where possible
		q.setMaxResults(10);
		q.setFirstResult(10*page);
		q.setLong(0, projectId);
		return q.list();
	}
	

}
