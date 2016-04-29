package at.tuwien.ase.test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.fail;

import java.util.ArrayList;
import java.util.List;

import javax.ejb.EJB;

import org.jboss.arquillian.junit.Arquillian;
import org.junit.Test;
import org.junit.runner.RunWith;

import at.tuwien.ase.dao.ResourceDaoInterface;
import at.tuwien.ase.dao.TaskTypeDaoInterface;
import at.tuwien.ase.model.Resource;
import at.tuwien.ase.model.TaskType;

@RunWith(Arquillian.class)
public class TaskTypeDaoTest extends TestSuiteDao {
	private static final String NAME = "NAME";

	private static final String TASKNR = "1.1.1.1";
	@EJB
	ResourceDaoInterface ruDao;
	@EJB
	TaskTypeDaoInterface taskTypeDao;

	@Test
	public void testGetItemDel() throws Exception {
		String desc = "desc";

		utx.begin();
		em.joinTransaction();
		assertEquals("database setup error-> database not empty", 0, taskTypeDao.listAll().size());

		// create taskTypeDao
		TaskType t = new TaskType();
		t.setDescription(desc);
		t.setExpectedWorkHours(10.0);
		t.setName(NAME);
		t.setTaskNumber(TASKNR);

		em.persist(t);
		utx.commit();

		TaskType td = taskTypeDao.get(t.getId());

		assertEquals(t.getId(), td.getId());

		taskTypeDao.delete(td.getId());
		assertEquals(null, taskTypeDao.get(t.getId()));
		

	}
	@Test
	public void testListAllDel() throws Exception {
		
		String desc = "desc";

		utx.begin();
		em.joinTransaction();
		assertEquals("database setup error-> database not empty", 0, taskTypeDao.listAll().size());

		List<TaskType> list = new ArrayList<TaskType>();
		int size = 10;
	// create taskTypeDao
			TaskType t = new TaskType();
			t.setDescription(desc);
			t.setExpectedWorkHours(10.0);
			t.setName(NAME);
			t.setTaskNumber(TASKNR);
			Resource r = new Resource();
			Resource r1 = new Resource();
			Resource r2 = new Resource();
			t.setResourceList(new ArrayList<Resource>());
			t.getResourceList().add(r);
			t.getResourceList().add(r1);
			t.getResourceList().add(r2);
			
			
			r.setTaskTypeList(new ArrayList<TaskType>());
			r1.setTaskTypeList(new ArrayList<TaskType>());
			r2.setTaskTypeList(new ArrayList<TaskType>());
			
			r.getTaskTypeList().add(t);
			r1.getTaskTypeList().add(t);
			r2.getTaskTypeList().add(t);
			em.persist(r);
			em.persist(r1);
			em.persist(r2);
			list.add(t);
			em.persist(t);
		
		utx.commit();

		List<TaskType> rList = taskTypeDao.listAll();
		assertEquals(1, rList.size());
		TaskType tt = rList.get(0);
		int i =1;
		for ( Resource rl : tt.getResourceList()) {
			ruDao.delete(rl.getId());
			assertEquals(3-i,taskTypeDao.listAll().get(0).getResourceList().size());
			i++;
		}
		
		taskTypeDao.delete(tt.getId());
		assertEquals(0, taskTypeDao.listAll().size());
	}

	@Test
	public void testListAll() throws Exception {
		
		String desc = "desc";

		utx.begin();
		em.joinTransaction();
		assertEquals("database setup error-> database not empty", 0, taskTypeDao.listAll().size());

		List<TaskType> list = new ArrayList<TaskType>();
		int size = 10;
		for (int i = 0; i < size; i++) {

			// create taskTypeDao
			TaskType t = new TaskType();
			t.setDescription(desc);
			t.setExpectedWorkHours(10.0);
			t.setName(NAME);
			t.setTaskNumber(TASKNR);
			Resource r = new Resource();
			Resource r1 = new Resource();
			Resource r2 = new Resource();
			t.setResourceList(new ArrayList<Resource>());
			t.getResourceList().add(r);
			t.getResourceList().add(r1);
			t.getResourceList().add(r2);
			
			
			r.setTaskTypeList(new ArrayList<TaskType>());
			r1.setTaskTypeList(new ArrayList<TaskType>());
			r2.setTaskTypeList(new ArrayList<TaskType>());
			
			r.getTaskTypeList().add(t);
			r1.getTaskTypeList().add(t);
			r2.getTaskTypeList().add(t);
			em.persist(r);
			em.persist(r1);
			em.persist(r2);
			list.add(t);
			em.persist(t);
		}
		utx.commit();

		List<TaskType> rList = taskTypeDao.listAll();
		assertEquals(size, rList.size());
		for (int i = 0; i < size; i++) {
			boolean contains = false;
			for (TaskType td : rList) {
				if (td.getId().equals(list.get(i).getId())) {
					contains = true;
					assertEquals(desc, td.getDescription());
					assertEquals(NAME, td.getName());
					assertEquals(TASKNR, td.getTaskNumber());
					assertEquals(3, td.getResourceList().size());
					assertEquals((Double) 10.0, td.getExpectedWorkHours());
				}
			}

			if(!contains){
				fail("returned list does not cointain all items");
			}
		}
		
	}


	@Test
	public void testGetItem() throws Exception {
		String desc = "desc";

		utx.begin();
		em.joinTransaction();
		assertEquals("database setup error-> database not empty", 0, taskTypeDao.listAll().size());

		// create taskTypeDao
		TaskType t = new TaskType();
		t.setDescription(desc);
		t.setExpectedWorkHours(10.0);
		t.setName(NAME);
		t.setTaskNumber(TASKNR);

		em.persist(t);
		utx.commit();

		TaskType td = taskTypeDao.get(t.getId());

		assertEquals(t.getId(), td.getId());
		assertEquals(desc, td.getDescription());
		assertEquals(NAME, td.getName());
		assertEquals(TASKNR, td.getTaskNumber());
		assertEquals((Double) 10.0, td.getExpectedWorkHours());

	}
}