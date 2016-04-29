package at.tuwien.ase.test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.fail;

import java.sql.Array;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Map;

import javax.ejb.EJB;

import org.jboss.arquillian.junit.Arquillian;
import org.junit.Test;
import org.junit.runner.RunWith;

import at.tuwien.ase.dao.ProjectDaoInterface;
import at.tuwien.ase.model.Project;
import at.tuwien.ase.model.ProjectType;
import at.tuwien.ase.model.TaskType;
import at.tuwien.ase.model.User;

@RunWith(Arquillian.class)
public class ProjectDaoTest extends TestSuiteDao {
	private static final String NAME = "DESC";

	@EJB
	ProjectDaoInterface projectDao;

	@Test
	public void testDeletedRelatedItems() throws Exception {

		utx.begin();
		em.joinTransaction();

		assertEquals("database setup error-> database not empty", 0, projectDao.listAll().size());

		List<Project> pList = new ArrayList<Project>();
		List<TaskType> ttList = new ArrayList<TaskType>();
		List<User> puList = new ArrayList<User>();

		int size = 10;

		// one2one refs (lazy)
		ProjectType projectType = new ProjectType();
		em.persist(projectType);

		// create Project
		Project p = new Project();
		p.setExpectedWorkHours(10.0);
		p.setName(NAME);
		p.setProjectType(projectType);
		p.setUserList(new HashSet<User>());
		p.setTaskTypeList(new HashSet<TaskType>());
		for (int j = 0; j < size; j++) {
			User u = new User();
			p.getUserList().add(u);
			puList.add(u);
			em.persist(u);
		}
		for (int j = 0; j < size; j++) {
			TaskType tt = new TaskType();
			p.getTaskTypeList().add(tt);
			em.persist(tt);
			ttList.add(tt);
		}
		// persist

		em.persist(p);
		pList.add(p);

		utx.commit();
		
		assertEquals(1, projectDao.listAll().size());
		for (int i = 0; i < size; i++) {
			utx.begin();
			em.joinTransaction();
			User u = em.find(User.class, puList.get(i).getId());
			u.setDeleted(true);
			em.persist(u);
			utx.commit();
			assertEquals(size - i - 1, projectDao.listAll().get(0).getUserList().size());
		}
		for (int i = 0; i < size; i++) {
			utx.begin();
			em.joinTransaction();
			TaskType u = em.find(TaskType.class, ttList.get(i).getId());
			u.setDeleted(true);
			em.persist(u);
			utx.commit();
			assertEquals(size - i - 1, projectDao.listAll().get(0).getTaskTypeList().size());
		}

	}

	@Test
	public void testListAll() throws Exception {

		utx.begin();
		em.joinTransaction();

		assertEquals("database setup error-> database not empty", 0, projectDao.listAll().size());

		List<Project> list = new ArrayList<Project>();
		int size = 10;
		for (int i = 0; i < size; i++) {

			// one2one refs (lazy)
			ProjectType projectType = new ProjectType();
			em.persist(projectType);

			// create Project
			Project p = new Project();
			p.setExpectedWorkHours(10.0);
			p.setName(NAME);
			p.setProjectType(projectType);

			// persist

			em.persist(p);
			list.add(p);

		}
		utx.commit();

		List<Project> rList = projectDao.listAll();

		assertEquals(size, rList.size());
		for (int i = 0; i < size; i++) {
			boolean contains = false;
			for (Project pd : rList) {
				if (pd.getId().equals(list.get(i).getId())) {
					contains = true;
					;

					assertNotNull(pd);

					assertNotNull(pd.getProjectType());

					assertNotNull(pd.getProjectType().getId());
					assertEquals(NAME, pd.getName());
					assertEquals(new Double(10.0), pd.getExpectedWorkHours());

					break;

				}
			}
			if (!contains) {
				fail("returned list does not cointain all items");
			}

		}
		for (int i = 0; i < size; i++) {
			projectDao.delete(list.get(i).getId());

			assertEquals(size - i - 1, projectDao.listAll().size());
		}
	}

	@Test
	public void testGetItem() throws Exception {

		utx.begin();
		em.joinTransaction();

		assertEquals("database setup error-> database not empty", 0, projectDao.listAll().size());
		// one2one refs (lazy)
		ProjectType projectType = new ProjectType();
		em.persist(projectType);

		// create Project
		Project p = new Project();
		p.setExpectedWorkHours(10.0);
		p.setName(NAME);
		p.setProjectType(projectType);
		p.setUserList(new HashSet<User>());
		for (int i = 0; i < 10; i++) {
			User u = new User();
			p.getUserList().add(u);
			em.persist(u);
		}
		
		p.setTaskTypeList(new HashSet<TaskType>());
		for (int i = 0; i < 15; i++) {
			TaskType u = new TaskType();
			p.getTaskTypeList().add(u);
			em.persist(u);
		}
		// persist

		em.persist(p);
		utx.commit();
		assertNotNull(p.getId());
		Project pd = projectDao.get(p.getId());

		assertNotNull(pd);

		assertNotNull(p.getProjectType());

		assertEquals(projectType.getId(), pd.getProjectType().getId());
		assertEquals(NAME, pd.getName());
		assertEquals(new Double(10.0), pd.getExpectedWorkHours());
		assertEquals(p.getId(), pd.getId());
		assertEquals(15, pd.getTaskTypeList().size());
		assertEquals(10, pd.getUserList().size());

		projectDao.delete(p.getId());

		assertEquals(null, projectDao.get(p.getId()));
	}
}