package at.tuwien.ase.test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.fail;
import static org.junit.Assert.assertNull;

import java.util.ArrayList;
import java.util.List;

import javax.ejb.EJB;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;

import org.jboss.arquillian.junit.Arquillian;
import org.jboss.arquillian.transaction.api.annotation.TransactionMode;
import org.jboss.arquillian.transaction.api.annotation.Transactional;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;

import at.tuwien.ase.dao.UserDaoInterface;
import at.tuwien.ase.model.Company;
import at.tuwien.ase.model.Project;
import at.tuwien.ase.model.Task;
import at.tuwien.ase.model.User;
import at.tuwien.ase.model.UserType;
import at.tuwien.ase.model.WorkingObject;

@RunWith(Arquillian.class)
public class UserDaoTest extends TestSuiteDao{
	
	@EJB
	UserDaoInterface userDao;
	
	@Test
	public void getWorkingWorkersForProjectId() throws Exception {

		utx.begin();
		em.joinTransaction();
		assertEquals("database setup error-> database not empty", 0, userDao.listAll().size());

		List<User> list = new ArrayList<User>();
		int size = 10;
		for (int i = 0; i < size; i++) {

			// create company
			Company c = new Company();
			c.setName("Test_company");
			c.setUserList(new ArrayList<User>());
			// create user
			User u = new User();
			u.setUserType(UserType.MANAGER);
			u.setForname("forname");
			u.setLastname("lastname");
			u.setPassword("pass");
			u.setUsername("user");

			c.getUserList().add(u);
			u.setCompany(c);

			// persist
			em.persist(u);
			em.persist(c);
			list.add(u);
			
		}
		utx.commit();

		
		
	}

	@Test
	public void testListAll() throws Exception {

		utx.begin();
		em.joinTransaction();
		assertEquals("database setup error-> database not empty", 0, userDao.listAll().size());

		List<User> list = new ArrayList<User>();
		int size = 10;
		for (int i = 0; i < size; i++) {

			// create company
			Company c = new Company();
			c.setName("Test_company");
			c.setUserList(new ArrayList<User>());
			// create user
			User u = new User();
			u.setUserType(UserType.MANAGER);
			u.setForname("forname");
			u.setLastname("lastname");
			u.setPassword("pass");
			u.setUsername("user");

			c.getUserList().add(u);
			u.setCompany(c);

			// persist
			em.persist(u);
			em.persist(c);
			list.add(u);
			
		}
		
		Project p=new Project();
		
		
		em.persist(p);
		for (int i = 0; i < 3; i++) {//more tasks per worke should still be only one working worker
			Task t = new Task();
			t.setProject(p);
			t.setWorker(list.get(i));
			t.setFinished(false);
			em.persist(t);
		}
		
		for (int i = 0; i < 3; i++) {
			Task t = new Task();
			t.setProject(p);
			t.setWorker(list.get(i));
			t.setFinished(false);
			em.persist(t);
		}
		

		for (int i = 3; i < 6; i++) {//finished null
			Task t = new Task();
			t.setProject(p);
			t.setWorker(list.get(i));
			
			em.persist(t);
		}
		for (int i = 6; i < 10; i++) {//finished null
			Task t = new Task();
			t.setProject(p);
			t.setWorker(list.get(i));
			t.setFinished(true);
			em.persist(t);
		}
		utx.commit();
		assertEquals(6, userDao.getWorkingWorkersForProjectId(p.getId()).size());
		
	}

	@Test
	public void testGetItem() throws Exception {

		utx.begin();
		em.joinTransaction();
		assertEquals("database setup error-> database not empty", 0, userDao.listAll().size());

		// create company
		Company c = new Company();
		c.setName("Test_company");
		c.setUserList(new ArrayList<User>());
		// create user
		User u = new User();
		u.setUserType(UserType.MANAGER);
		u.setForname("forname");
		u.setLastname("lastname");
		u.setPassword("pass");
		u.setUsername("user");
		
		c.getUserList().add(u);
		u.setCompany(c);

		// persist
		em.persist(u);
		em.persist(c);

		utx.commit();
		assertNotNull(u.getId());
		assertNotNull(c.getId());

		// check dao
		User du = userDao.get(u.getId());
		assertNotNull("error  could not load user", du);
		assertEquals("forname", du.getForname());
		assertEquals("lastname", du.getLastname());
		assertEquals("pass", du.getPassword());
		assertEquals("user", du.getUsername());
		assertEquals("Test_company", du.getCompany().getName());
	}
	
	@Test
	public void getByCredentialsSucessful() throws Exception {
		
		utx.begin();
		em.joinTransaction();
		assertEquals("database setup error-> database not empty", 0, userDao.listAll().size());

		// create user
		User u = new User();
		u.setUserType(UserType.MANAGER);
		u.setForname("forname");
		u.setLastname("lastname");
		u.setPassword("pass");
		u.setUsername("user");

		// persist
		em.persist(u);

		utx.commit();
		assertNotNull(u.getId());
	}
	@Test
	public void getByCredentialsDel() throws Exception {
		
		utx.begin();
		em.joinTransaction();
		assertEquals("database setup error-> database not empty", 0, userDao.listAll().size());

		// create user
		User u = new User();
		u.setUserType(UserType.MANAGER);
		u.setForname("forname");
		u.setLastname("lastname");
		u.setPassword("pass");
		u.setUsername("user");
		// persist
		em.persist(u);

		utx.commit();
		assertNotNull(userDao.getByCredentials("user", "pass"));
		userDao.delete(u.getId());
		assertNull(userDao.getByCredentials("user", "pass"));
	}
	@Test
	public void getByCredentialsUnSucessful() throws Exception {
		
		User u = userDao.getByCredentials("xxxx", "yyyy");
		
		assertNull(u);
	}
}