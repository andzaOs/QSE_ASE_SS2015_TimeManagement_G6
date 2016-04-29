package at.tuwien.ase;

import java.io.Serializable;
import java.util.List;

import javax.ejb.Stateless;
import javax.persistence.PersistenceContext;

import org.hibernate.Criteria;
import org.hibernate.Query;
import org.hibernate.Session;
import org.hibernate.Transaction;

import at.tuwien.ase.dao.ProjectTypeDaoInterface;
import at.tuwien.ase.model.Category;
import at.tuwien.ase.model.ProjectType;
import at.tuwien.ase.model.TaskType;

@Stateless
public class ProjectTypeDaoImpl implements ProjectTypeDaoInterface {
	@PersistenceContext
	private Session session;



	@Override
	public List<ProjectType> listAll() {
		Query q = session.createQuery("from ProjectType as c left  join  fetch c.workingObjectList where (c.deleted!=true or c.deleted is null )" );//i sugest we use hql where possible
		q.setResultTransformer(
				Criteria.DISTINCT_ROOT_ENTITY);
		return q.list();
	}

	@Override
	public ProjectType get(Long id) {
		Query q = session.createQuery("from ProjectType as c left  join fetch c.workingObjectList where  (c.deleted!=true or c.deleted is null ) and c.id=?" );//i sugest we use hql where possible
		q.setResultTransformer(
				Criteria.DISTINCT_ROOT_ENTITY);
		q.setLong(0, id);
		return (ProjectType) q.uniqueResult();
	}
	@Override
	public void delete(Long id) {
		Transaction tx = session.beginTransaction();
		ProjectType o = (ProjectType) session.load(ProjectType.class, id);
		o.setDeleted(true);
		session.persist(o);
		tx.commit();
		
	}

}
