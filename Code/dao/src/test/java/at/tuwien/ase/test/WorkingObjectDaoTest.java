package at.tuwien.ase.test;

import static org.junit.Assert.*;

import java.util.ArrayList;
import java.util.List;

import javax.ejb.EJB;

import org.jboss.arquillian.junit.Arquillian;
import org.junit.Test;
import org.junit.runner.RunWith;

import at.tuwien.ase.dao.WorkingObjectDaoInterface;
import at.tuwien.ase.model.Category;
import at.tuwien.ase.model.WorkingObject;

@RunWith(Arquillian.class)
public class WorkingObjectDaoTest extends TestSuiteDao {

	private static final String DESC = "DESC";

	@EJB
	WorkingObjectDaoInterface workingObjectDao;
	@Test
	public void testDeleted() throws Exception {


		utx.begin();
		em.joinTransaction();

		assertEquals("database setup error-> database not empty", 0, workingObjectDao.listAll().size());
		
		List<WorkingObject> list=new ArrayList<WorkingObject>();
		int size=10;
		for (int i = 0; i < size; i++) {

			// create
			WorkingObject w = new WorkingObject();
			w.setDescription(DESC);

			// persist

			em.persist(w);
			list.add(w);
			assertNotNull(w.getId());
		}
		utx.commit();
		List<WorkingObject> rList = workingObjectDao.listAll();
		
		assertEquals(size, rList.size());
		
		for (int i = 0; i < size; i++) {
			workingObjectDao.delete(list.get(i).getId());
			assertEquals(size-i-1, workingObjectDao.listAll().size());
		}

	}
	@Test
	public void testlistAll() throws Exception {

		utx.begin();
		em.joinTransaction();

		assertEquals("database setup error-> database not empty", 0, workingObjectDao.listAll().size());
		
		List<WorkingObject> list=new ArrayList<WorkingObject>();
		int size=10;
		for (int i = 0; i < size; i++) {

			// create
			WorkingObject w = new WorkingObject();
			w.setDescription(DESC);

			// persist

			em.persist(w);
			list.add(w);
			assertNotNull(w.getId());
		}
		utx.commit();
		List<WorkingObject> rList = workingObjectDao.listAll();
		
		assertEquals(size, rList.size());
		for (int i = 0; i < size; i++) {
			boolean contains = false;
			for (WorkingObject w : rList) {
				if(w.getId().equals(list.get(i).getId())){
					contains=true;
					assertNotNull(w.getDescription());
					break;
						
				}
			}
			if(!contains){
				fail("returned list does not cointain all items");
			}
			
		}
		
	}

	@Test
	public void testGetItem() throws Exception {

		utx.begin();
		em.joinTransaction();

		assertEquals("database setup error-> database not empty", 0, workingObjectDao.listAll().size());

		// create
		WorkingObject w = new WorkingObject();
		w.setDescription(DESC);

		// persist

		em.persist(w);

		assertNotNull(w.getId());

		utx.commit();

		WorkingObject wd = workingObjectDao.get(w.getId());
		assertEquals(DESC, wd.getDescription());
	}
}