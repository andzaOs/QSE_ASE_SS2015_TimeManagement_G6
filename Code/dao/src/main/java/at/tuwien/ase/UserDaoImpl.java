package at.tuwien.ase;

import java.util.List;

import javax.ejb.Stateless;
import javax.persistence.PersistenceContext;

import org.hibernate.Criteria;
import org.hibernate.Query;
import org.hibernate.Session;
import org.hibernate.Transaction;

import at.tuwien.ase.dao.UserDaoInterface;
import at.tuwien.ase.model.Category;
import at.tuwien.ase.model.User;

@Stateless
public class UserDaoImpl implements UserDaoInterface {
	@PersistenceContext
	private Session session;

	@Override
	public List<User> listAll() {
		Query q = session.createQuery("from User as c where c.deleted!=true or c.deleted is null");
		
		return q.list();
	}

	

	@Override
	public User get(Long id) {
		
		Query q = session.createQuery("from User as c where (c.deleted!=true or c.deleted is null) and c.id=?	");//i sugest we use hql where possible
		q.setParameter(0, id);
		return (User) q.uniqueResult();
	
	}

	@Override
	public User getByCredentials(String username, String password) {
		
		User u = null;
		
		Query q = session.createQuery("from User  where username = ? and password = ? and (deleted!=true or deleted is null)");
		q.setParameter(0, username);
		q.setParameter(1, password);
		
		u = (User)q.uniqueResult();
		
		return u;
	}
	@Override
	public void delete(Long id) {
		Transaction tx = session.beginTransaction();
		User o = (User) session.load(User.class, id);
		o.setDeleted(true);
		session.persist(o);
		tx.commit();
		
	}



	@Override
	public List<User> getWorkingWorkersForProjectId(Long id) {
		Query q = session.createQuery("select w from Task as  t join t.worker as w where (t.deleted!=true or t.deleted is null) and( t.finished!=true or t.finished is null) and (w.deleted!=true or w.deleted is null) and t.project.id=?");//i sugest we use hql where possible
		q.setResultTransformer(
				Criteria.DISTINCT_ROOT_ENTITY);
		q.setLong(0, id);
		return q.list();
	}
}
