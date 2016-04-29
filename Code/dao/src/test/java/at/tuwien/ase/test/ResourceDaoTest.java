package at.tuwien.ase.test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.fail;

import java.util.ArrayList;
import java.util.List;

import javax.ejb.EJB;
import javax.inject.Inject;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;
import javax.transaction.UserTransaction;

import org.jboss.arquillian.junit.Arquillian;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;

import at.tuwien.ase.dao.ResourceDaoInterface;
import at.tuwien.ase.model.Category;
import at.tuwien.ase.model.Project;
import at.tuwien.ase.model.ProjectType;
import at.tuwien.ase.model.Resource;
import at.tuwien.ase.model.ResourceUsage;
import at.tuwien.ase.model.Task;
import at.tuwien.ase.model.TaskType;
import at.tuwien.ase.model.User;
import at.tuwien.ase.model.WorkingObject;

@RunWith(Arquillian.class)
public class ResourceDaoTest extends TestSuiteDao {
	private static final String DESC = "DESC";

	@EJB
	ResourceDaoInterface resourceDao;

	@Test
	public void testDeletedReferences() throws Exception {

		utx.begin();
		em.joinTransaction();
		List<Resource> list = new ArrayList<Resource>();
		int size = 10;
		TaskType t1 = new TaskType();
		t1.setResourceList(new ArrayList<Resource>());

		// create
		Resource r1 = new Resource();
		r1.setTaskTypeList(new ArrayList<TaskType>());
		Resource r2 = new Resource();
		r2.setTaskTypeList(new ArrayList<TaskType>());

		
		r1.getTaskTypeList().add(t1);
		r2.getTaskTypeList().add(t1);
		
		t1.getResourceList().add(r1);
		t1.getResourceList().add(r2);
		
		em.persist(t1);
		em.persist(r1);
		em.persist(r2);
	
		utx.commit();
		assertNotNull(t1.getId());
		assertEquals(2, resourceDao.listAllForTaskType(t1.getId()).size());
		
		utx.begin();
		em.joinTransaction();
		r1=em.find(Resource.class, r1.getId());
		r1.setDeleted(true);
		em.persist(r1);
		utx.commit();
		assertEquals(1, resourceDao.listAllForTaskType(t1.getId()).size());
		
		utx.begin();
		em.joinTransaction();
		t1=em.find(TaskType.class, t1.getId());
		t1.setDeleted(true);
		em.persist(t1);
		utx.commit();
		assertEquals(0, resourceDao.listAllForTaskType(t1.getId()).size());
		
	}
	@Test
	public void testListAllForTaskType() throws Exception {

		utx.begin();
		em.joinTransaction();

		assertEquals("database setup error-> database not empty", 0, resourceDao.listAll().size());
		List<Resource> list = new ArrayList<Resource>();
		int size = 10;
		TaskType tt = new TaskType();
		tt.setResourceList(new ArrayList<Resource>());
		em.persist(tt);
		for (int i = 0; i < size; i++) {

			// one2one refs (lazy)
			Category c = new Category();
			em.persist(c);
			// create
			Resource r = new Resource();
			r.setTaskTypeList(new ArrayList<TaskType>());
			r.setCategory(c);
			r.setDescription(DESC);
			for (int j = 0; j < 4; j++) {
				TaskType ttj = new TaskType();
				ttj.setResourceList(new ArrayList<Resource>());
				em.persist(ttj);
				ttj.getResourceList().add(r);
				r.getTaskTypeList().add(ttj);
			}
			r.getTaskTypeList().add(tt);
			tt.getResourceList().add(r);
			// persist
			list.add(r);
			em.persist(r);
		}
		utx.commit();

		List<Resource> rList = resourceDao.listAllForTaskType(tt.getId());

		assertEquals(size, rList.size());
		for (int i = 0; i < size; i++) {
			boolean contains = false;
			for (Resource rd : rList) {
				if (rd.getId().equals(list.get(i).getId())) {

					assertEquals(DESC, rd.getDescription());
					assertEquals(list.get(i), resourceDao.get(list.get(i).getId()));
					assertNotNull(rd.getCategory().getId());
					contains = true;
					break;
				}
			}
			if (!contains) {
				fail("returned list does not cointain all items");
			}

		}
	}
	@Test
	public void testGetItem() throws Exception {

		utx.begin();
		em.joinTransaction();

		assertEquals("database setup error-> database not empty", 0, resourceDao.listAll().size());
		List<Resource> list = new ArrayList<Resource>();
		int size = 10;
		for (int i = 0; i < size; i++) {

			// one2one refs (lazy)
			Category c = new Category();
			em.persist(c);
			// create
			Resource r = new Resource();
			r.setCategory(c);
			r.setDescription(DESC);

			// persist
			list.add(r);
			em.persist(r);
		}
		utx.commit();

		List<Resource> rList = resourceDao.listAll();

		assertEquals(size, rList.size());
		for (int i = 0; i < size; i++) {
			boolean contains = false;
			for (Resource rd : rList) {
				if (rd.getId().equals(list.get(i).getId())) {

					assertEquals(DESC, rd.getDescription());
					assertEquals(list.get(i), resourceDao.get(list.get(i).getId()));
					assertNotNull(rd.getCategory().getId());
					contains = true;
					break;
				}
			}
			if (!contains) {
				fail("returned list does not cointain all items");
			}

		}
	}

	@Test
	public void testDelete() throws Exception {
		utx.begin();
		em.joinTransaction();

		assertEquals("database setup error-> database not empty", 0, resourceDao.listAll().size());
		List<Resource> list = new ArrayList<Resource>();
		int size = 10;
		for (int i = 0; i < size; i++) {

			// one2one refs (lazy)
			Category c = new Category();
			em.persist(c);
			// create
			Resource r = new Resource();
			r.setCategory(c);
			r.setDescription(DESC);

			// persist
			list.add(r);
			em.persist(r);
		}
		utx.commit();

		List<Resource> rList = resourceDao.listAll();

		assertEquals(size, rList.size());

		for (int i = 0; i < size; i++) {
			assertNotNull(resourceDao.get(list.get(i).getId()));
			resourceDao.delete(list.get(i).getId());
			assertEquals(null, resourceDao.get(list.get(i).getId()));
			assertEquals(size - i - 1, resourceDao.listAll().size());
		}
	}
}