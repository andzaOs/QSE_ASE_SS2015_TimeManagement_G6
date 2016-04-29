package at.tuwien.ase;

import java.util.ArrayList;
import java.util.List;

import javax.ejb.Stateless;
import javax.persistence.PersistenceContext;

import org.hibernate.Criteria;
import org.hibernate.Query;
import org.hibernate.Session;
import org.hibernate.Transaction;

import at.tuwien.ase.dao.ProjectDaoInterface;
import at.tuwien.ase.model.Category;
import at.tuwien.ase.model.Company;
import at.tuwien.ase.model.Project;
import at.tuwien.ase.model.TaskType;
import at.tuwien.ase.model.User;

@Stateless
public class ProjectDaoImpl implements ProjectDaoInterface {
	@PersistenceContext
	private Session session;



	@Override
	public List<Project> listAll() {
		Query q = session.createQuery("select p from Project as p left join fetch p.taskTypeList as tl where (p.deleted!=true or p.deleted is null )").setResultTransformer(Criteria.DISTINCT_ROOT_ENTITY)  ;
	
		List<Project> ret= q.list();
		for (Project p : ret) {
			List<Object> del=new ArrayList<Object>();
			for (User u : p.getUserList()) {//hide deleted
			
				if(new Boolean(true).equals(u.getDeleted())){
					del.add(u);
				}
			}
			p.getUserList().removeAll(del);
			del.clear();
			for (TaskType tt : p.getTaskTypeList()) {
			
				if(new Boolean(true).equals(tt.getDeleted())){//prevent nullpointerv
					del.add(tt);
				}
			}
			p.getTaskTypeList().removeAll(del);
		}
		
		return ret;
	}

	@Override
	public Project get(Long id) {
		Query q = session.createQuery("from Project as c left   join fetch c.taskTypeList  where  (c.deleted!=true or c.deleted is null ) and c.id=?");//i sugest we use hql where possible
		List<Object> del=new ArrayList<Object>();
		q.setLong(0, id);
		Project p = (Project) q.uniqueResult();
		if(p==null){
			return null;
		}
		for (User u : p.getUserList()) {//hide deleted
		
			if(new Boolean(true).equals(u.getDeleted())){
				del.add(u);
			}
		}
		p.getUserList().removeAll(del);
		
		for (TaskType tt : p.getTaskTypeList()) {
			
			if(new Boolean(true).equals(tt.getDeleted())){//prevent nullpointerv
				del.add(tt);
			}
		}
		p.getTaskTypeList().removeAll(del);
		
		return p;
	
	}
	@Override
	public void delete(Long id) {
		Transaction tx = session.beginTransaction();
		Project o = (Project) session.load(Project.class, id);
		o.setDeleted(true);
		session.persist(o);
		tx.commit();
	}


}
