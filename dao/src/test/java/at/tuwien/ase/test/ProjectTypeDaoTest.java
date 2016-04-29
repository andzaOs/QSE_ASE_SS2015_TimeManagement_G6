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

import at.tuwien.ase.dao.ProjectDaoInterface;
import at.tuwien.ase.dao.ProjectTypeDaoInterface;
import at.tuwien.ase.model.Project;
import at.tuwien.ase.model.ProjectType;
import at.tuwien.ase.model.User;

@RunWith(Arquillian.class)
public class ProjectTypeDaoTest extends TestSuiteDao {
	private static final String DESC = "DESC";

	@EJB
	ProjectTypeDaoInterface projectTypeDao;

	@Test
	public void testListAll() throws Exception {

		utx.begin();
		em.joinTransaction();

		assertEquals("database setup error-> database not empty", 0, projectTypeDao.listAll().size());

		List<ProjectType> list = new ArrayList<ProjectType>();
		int size = 10;
		for (int i = 0; i < size; i++) {
			// one2one refs (lazy)

			// create Project
			ProjectType p = new ProjectType();
			p.setDescription(DESC);

			// persist

			em.persist(p);
			list.add(p);

		}

		utx.commit();

		List<ProjectType> rList = projectTypeDao.listAll();

		assertEquals(size, rList.size());
		for (int i = 0; i < size; i++) {
			boolean contains = false;
			for (ProjectType ptd : rList) {
				if (ptd.getId().equals(list.get(i).getId())) {
					contains = true;

					assertEquals(DESC, ptd.getDescription());

					break;

				}
			}
			if (!contains) {
				fail("returned list does not cointain all items");
			}

		}

	}
	
	@Test
	public void testDelete() throws Exception{
		utx.begin();
		em.joinTransaction();
		

		assertEquals("database setup error-> database not empty", 0, projectTypeDao.listAll().size());

		List<ProjectType> list = new ArrayList<ProjectType>();
		int size = 10;
		for (int i = 0; i < size; i++) {
			// one2one refs (lazy)

			// create Project
			ProjectType p = new ProjectType();
			p.setDescription(DESC);

			// persist

			em.persist(p);
			list.add(p);

		}

		utx.commit();

		List<ProjectType> rList = projectTypeDao.listAll();

		for (int i = 0; i < size; i++) {
			assertNotNull(projectTypeDao.get(list.get(i).getId()));
			projectTypeDao.delete(list.get(i).getId());
			assertEquals(null, projectTypeDao.get(list.get(i).getId()));
			assertEquals(size - i - 1, projectTypeDao.listAll().size());
		}
	}

	@Test
	public void testGetItem() throws Exception {

		utx.begin();
		em.joinTransaction();

		assertEquals("database setup error-> database not empty", 0, projectTypeDao.listAll().size());

		// one2one refs (lazy)

		// create Project
		ProjectType p = new ProjectType();
		p.setDescription(DESC);

		// persist

		em.persist(p);
		utx.commit();
		assertNotNull(p.getId());
		ProjectType ptd = projectTypeDao.get(p.getId());

		assertEquals(DESC, ptd.getDescription());

		assertEquals(p.getId(), ptd.getId());
	}
}