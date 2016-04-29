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

import at.tuwien.ase.dao.CompanyDaoInterface;
import at.tuwien.ase.model.Category;
import at.tuwien.ase.model.Company;
import at.tuwien.ase.model.User;

@RunWith(Arquillian.class)
public class CompanyDaoTest extends TestSuiteDao {
	private static final String COMPANY_NAME = "Test_company";

	@EJB
	CompanyDaoInterface companyDao;
	@Test
	public void testDeleted() throws Exception {

		utx.begin();
		em.joinTransaction();

		assertEquals("database setup error-> database not empty", 0, companyDao.listAll().size());

		List<Company> list = new ArrayList<Company>();
		int size = 10;
		for (int i = 0; i < size; i++) {

			// create company
			Company c = new Company();
			c.setName(COMPANY_NAME);
			c.setUserList(new ArrayList<User>());

			// persist

			em.persist(c);

			assertNotNull(c.getId());
			list.add(c);

		}
		utx.commit();

		List<Company> rList = companyDao.listAll();

		assertEquals(size, rList.size());
		
		for (int i = 0; i < size; i++) {
			companyDao.delete(list.get(i).getId());
		
			assertEquals(size-i-1, companyDao.listAll().size());
		}
	}
	@Test
	public void testListAll() throws Exception {

		utx.begin();
		em.joinTransaction();

		assertEquals("database setup error-> database not empty", 0, companyDao.listAll().size());

		List<Company> list = new ArrayList<Company>();
		int size = 10;
		for (int i = 0; i < size; i++) {

			// create company
			Company c = new Company();
			c.setName(COMPANY_NAME);
			c.setUserList(new ArrayList<User>());

			// persist

			em.persist(c);

			assertNotNull(c.getId());
			list.add(c);

		}
		utx.commit();

		List<Company> rList = companyDao.listAll();

		assertEquals(size, rList.size());
		for (int i = 0; i < size; i++) {
			boolean contains = false;
			for (Company cd : rList) {
				if (cd.getId().equals(list.get(i).getId())) {
					contains = true;
					assertEquals(COMPANY_NAME, cd.getName());
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

		assertEquals("database setup error-> database not empty", 0, companyDao.listAll().size());

		// create company
		Company c = new Company();
		c.setName(COMPANY_NAME);
		c.setUserList(new ArrayList<User>());

		// persist

		em.persist(c);

		assertNotNull(c.getId());

		utx.commit();

		Company cd = companyDao.get(c.getId());
		assertEquals(COMPANY_NAME, cd.getName());
		
		
		companyDao.delete(c.getId());
		
		assertEquals(null, companyDao.get(c.getId()));
	}
}