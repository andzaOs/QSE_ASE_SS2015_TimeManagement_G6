package at.tuwien.ase;

import java.io.Serializable;
import java.util.List;

import javax.ejb.Stateless;
import javax.persistence.PersistenceContext;

import org.hibernate.Criteria;
import org.hibernate.Query;
import org.hibernate.Session;
import org.hibernate.Transaction;

import at.tuwien.ase.dao.CompanyDaoInterface;
import at.tuwien.ase.model.Category;
import at.tuwien.ase.model.Company;

@Stateless
public class CompanyDaoImpl implements CompanyDaoInterface {
	@PersistenceContext
	private Session session;



	@Override
	public List<Company> listAll() {
		Query q = session.createQuery("from Company as c left  join  fetch c.userList where (c.deleted!=true or c.deleted is null )");//i sugest we use hql where possible
		q.setResultTransformer(
				Criteria.DISTINCT_ROOT_ENTITY);
		
		return q.list();
	}

	@Override
	public Company get(Long id) {
		Query q = session.createQuery("from Company as c left  join fetch c.userList  where (c.deleted!=true or c.deleted is null ) and c.id=?");//i sugest we use hql where possible
		q.setResultTransformer(
				Criteria.DISTINCT_ROOT_ENTITY);
		
		q.setLong(0, id);
		return (Company) q.uniqueResult();
	
	}

	@Override
	public void delete(Long id) {
		Transaction tx = session.beginTransaction();
		Company o = (Company) session.load(Company.class, id);
		o.setDeleted(true);
		session.persist(o);
		tx.commit();
		
	}

}
