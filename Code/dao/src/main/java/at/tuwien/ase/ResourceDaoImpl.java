package at.tuwien.ase;

import java.io.Serializable;
import java.util.List;

import javax.ejb.Stateless;
import javax.persistence.PersistenceContext;

import org.hibernate.Query;
import org.hibernate.Session;
import org.hibernate.Transaction;

import at.tuwien.ase.dao.ResourceDaoInterface;
import at.tuwien.ase.model.Category;
import at.tuwien.ase.model.ProjectType;
import at.tuwien.ase.model.Resource;

@Stateless
public class ResourceDaoImpl implements ResourceDaoInterface {
	@PersistenceContext
	private Session session;



	@Override
	public List<Resource> listAll() {
		Query q = session.createQuery("from Resource as c where (c.deleted!=true or c.deleted is null )");//i sugest we use hql where possible
		
		return q.list();
	}

	@Override
	public Resource get(Long id) {
		Query q = session.createQuery("from Resource as c where (c.deleted!=true or c.deleted is null )and id=? )");//i sugest we use hql where possible
		q.setLong(0, id);
		return (Resource) q.uniqueResult();
		
	
	}

	@Override
	public List<Resource> listAllForTaskType(Long id) {
		
		Query q = session.createQuery("from Resource as p left join fetch p.taskTypeList as pu where  (p.deleted!=true or p.deleted is null ) and (pu.deleted!=true or pu.deleted is null )and pu.id = ?");
		q.setParameter(0, id);
		
		return q.list();
	}
	@Override
	public void delete(Long id) {
		Transaction tx = session.beginTransaction();
		Resource o = (Resource) session.load(Resource.class, id);
		o.setDeleted(true);
		session.persist(o);
		tx.commit();
	}
}
