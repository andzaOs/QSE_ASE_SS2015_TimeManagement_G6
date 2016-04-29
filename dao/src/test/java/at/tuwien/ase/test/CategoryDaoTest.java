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

import at.tuwien.ase.dao.CategoryDaoInterface;
import at.tuwien.ase.model.Category;
import at.tuwien.ase.model.User;

@RunWith(Arquillian.class)
public class CategoryDaoTest extends TestSuiteDao {

	private static final String NAME = "DESC";

	@EJB
	CategoryDaoInterface categoryObjectDao;
	@Test
	public void testDeleted() throws Exception {

		utx.begin();
		em.joinTransaction();

		assertEquals("database setup error-> database not empty", 0, categoryObjectDao.listAll().size());

		List<Category> list = new ArrayList<Category>();
		int size = 10;
		for (int i = 0; i < size; i++) {

			// create
			Category c = new Category();
			c.setName(NAME);

			// persist

			em.persist(c);

			assertNotNull(c.getId());
			list.add(c);

		}
		utx.commit();
		List<Category> rList = categoryObjectDao.listAll();

		assertEquals(size, rList.size());
		
		for (int i = 0; i < size; i++) {
			categoryObjectDao.delete(list.get(i).getId());
			assertEquals(size-i-1, categoryObjectDao.listAll().size());
		}

	}

	@Test
	public void testListAkk() throws Exception {

		utx.begin();
		em.joinTransaction();

		assertEquals("database setup error-> database not empty", 0, categoryObjectDao.listAll().size());

		List<Category> list = new ArrayList<Category>();
		int size = 10;
		for (int i = 0; i < size; i++) {

			// create
			Category c = new Category();
			c.setName(NAME);

			// persist

			em.persist(c);

			assertNotNull(c.getId());
			list.add(c);

		}
		utx.commit();
		List<Category> rList = categoryObjectDao.listAll();

		assertEquals(size, rList.size());
		for (int i = 0; i < size; i++) {
			boolean contains = false;
			for (Category wd : rList) {
				if (wd.getId().equals(list.get(i).getId())) {
					contains = true;
					assertEquals(NAME, wd.getName());
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

		assertEquals("database setup error-> database not empty", 0, categoryObjectDao.listAll().size());

		// create
		Category c = new Category();
		c.setName(NAME);

		// persist

		em.persist(c);

		assertNotNull(c.getId());

		utx.commit();

		Category wd = categoryObjectDao.get(c.getId());
		assertEquals(NAME, wd.getName());
		

		categoryObjectDao.delete(c.getId());

		
		assertEquals(null, categoryObjectDao.get(c.getId()));
	}
}