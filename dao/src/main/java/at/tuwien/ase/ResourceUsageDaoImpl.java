package at.tuwien.ase;

import java.util.List;

import javax.ejb.Stateless;
import javax.persistence.PersistenceContext;

import org.hibernate.Query;
import org.hibernate.Session;
import org.hibernate.Transaction;

import at.tuwien.ase.dao.ResourceUsageDaoInterface;
import at.tuwien.ase.model.Category;
import at.tuwien.ase.model.ResourceUsage;

@Stateless
public class ResourceUsageDaoImpl implements ResourceUsageDaoInterface {
	@PersistenceContext
	private Session session;



	@Override
	public List<ResourceUsage> listAll() {
		Query q = session.createQuery("from ResourceUsage as c where c.deleted!=true or c.deleted is null");//i sugest we use hql where possible
		
		return q.list();
	}

	@Override
	public ResourceUsage get(Long id) {
		Query q = session.createQuery("from ResourceUsage as c where (c.deleted!=true or c.deleted is null) and c.id =?");//i sugest we use hql where possible
		q.setLong(0, id);  
		return (ResourceUsage) q.uniqueResult();
		
	
	}

	@Override
	public void delete(Long id) {
		Transaction tx = session.beginTransaction();
		ResourceUsage o = (ResourceUsage) session.load(ResourceUsage.class, id);
		o.setDeleted(true);
		session.persist(o);
		tx.commit();
		
	}
}
