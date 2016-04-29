package at.tuwien.ase;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import javax.ejb.Stateless;
import javax.persistence.PersistenceContext;
import javax.ws.rs.core.Response;

import org.hibernate.Criteria;
import org.hibernate.Query;
import org.hibernate.Session;
import org.hibernate.Transaction;

import at.tuwien.ase.dao.TaskTypeDaoInterface;
import at.tuwien.ase.model.Category;
import at.tuwien.ase.model.Resource;
import at.tuwien.ase.model.TaskType;

@Stateless
public class TaskTypeDaoImpl implements TaskTypeDaoInterface {
	@PersistenceContext
	private Session session;



	@Override
	public List<TaskType> listAll() {
		Query q = session.createQuery("from TaskType as t  left join fetch t.resourceList where (t.deleted!=true or t.deleted is null) ").setResultTransformer(Criteria.DISTINCT_ROOT_ENTITY)  ;//i sugest we use hql where possible
		
		 List<TaskType> ret = q.list();
		 Boolean tr = new Boolean(true);
		for (TaskType tt : ret) {
			List<Resource> del=new ArrayList<Resource>();
			for ( Resource ru : tt.getResourceList()) {
				if(tr.equals(ru.getDeleted())){
					del.add(ru);
				}
			}
			tt.getResourceList().removeAll(del);
			
		}
		
		return ret;
	}

	@Override
	public TaskType get(Long id) {
		Query q = session.createQuery("from TaskType as t left join fetch t.resourceList  where (t.deleted!=true or t.deleted is null) and t.id=?").setResultTransformer(Criteria.DISTINCT_ROOT_ENTITY)  ;//i sugest we use hql where possible
		q.setLong(0, id);
		return (TaskType) q.uniqueResult();
	
	}

	@Override
	public void delete(Long id) {
		Transaction tx = session.beginTransaction();
		TaskType o = (TaskType) session.load(TaskType.class, id);
		o.setDeleted(true);
		session.persist(o);
		tx.commit();
	}

}
