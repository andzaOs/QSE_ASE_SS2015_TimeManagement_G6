package at.tuwien.ase;

import java.util.List;

import javax.ejb.Stateless;
import javax.persistence.PersistenceContext;

import org.hibernate.Query;
import org.hibernate.Session;

import at.tuwien.ase.dao.WorkingObjectDaoInterface;
import at.tuwien.ase.model.Category;
import at.tuwien.ase.model.WorkingObject;

@Stateless
public class WorkingObjectDaoImpl implements WorkingObjectDaoInterface {
	@PersistenceContext
	private Session session;



	@Override
	public List<WorkingObject> listAll() {
		Query q = session.createQuery("from WorkingObject  as c where c.deleted!=true or c.deleted is null");//i sugest we use hql where possible
		
		return q.list();
	}

	@Override
	public WorkingObject get(Long id) {
		Query q = session.createQuery("from WorkingObject as c where (c.deleted!=true or c.deleted is null ) and c.id=?");//i sugest we use hql where possible
		q.setLong(0, id);
		return (WorkingObject) q.uniqueResult();
	
	}
	@Override
	public void delete(Long id) {
		WorkingObject o = (WorkingObject) session.load(WorkingObject.class, id);
		o.setDeleted(true);
		session.persist(o);
		
	}

}
