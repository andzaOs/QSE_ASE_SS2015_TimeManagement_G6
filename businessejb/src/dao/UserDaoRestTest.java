package at.tuwien.ase.dao;

import static org.junit.Assert.*;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import javax.ejb.EJB;
import javax.persistence.EntityManager;
import javax.persistence.EntityTransaction;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;

import org.hibernate.Hibernate;
import org.jboss.arquillian.container.test.api.Deployment;
import org.jboss.arquillian.junit.Arquillian;
import org.jboss.arquillian.transaction.api.annotation.TransactionMode;
import org.jboss.arquillian.transaction.api.annotation.Transactional;
import org.jboss.shrinkwrap.api.ShrinkWrap;
import org.jboss.shrinkwrap.api.asset.EmptyAsset;
import org.jboss.shrinkwrap.api.spec.EnterpriseArchive;
import org.jboss.shrinkwrap.api.spec.WebArchive;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;

import com.jayway.restassured.internal.ResponseSpecificationImpl;
import com.jayway.restassured.response.Response;
import com.jayway.restassured.specification.ResponseSpecification;

import static com.jayway.restassured.RestAssured.*;
import static com.jayway.restassured.matcher.RestAssuredMatchers.*;
import static org.hamcrest.Matchers.*;
import at.tuwien.ase.dao.time.UserDaoInterface;
import at.tuwien.ase.model.Company;
import at.tuwien.ase.model.Task;
import at.tuwien.ase.model.User;
import at.tuwien.ase.model.UserType;

@RunWith(Arquillian.class)
@Transactional(TransactionMode.ROLLBACK)
public class UserDaoRestTest {
	@PersistenceContext(name = "AseDataSource")
	EntityManager em;

	@EJB
	UserDaoInterface userDao;

	@Before
	public void clearDatabese() throws Exception {

		try {

			System.out.println("Dumping old records...");
			// Disable referential integrity using SET REFERENTIAL_INTEGRITY
			// FALSE
			String s = "SET REFERENTIAL_INTEGRITY FALSE;";

			// Get the list of all tables using SHOW TABLES
			Query q = em.createNativeQuery("SELECT    TABLE_NAME   FROM INFORMATION_SCHEMA.TABLES where TABLE_SCHEMA  ='PUBLIC'");

			// Delete the data from each table using TRUNCATE TABLE tableName
			List<String> res = q.getResultList();
			for (String t : res) {
				s += "TRUNCATE TABLE " + t + ";";

			}
			em.createNativeQuery(s).executeUpdate();

		} catch (Exception e) {
			throw e;
		} finally {

			// Enable referential integrity using
			em.createNativeQuery("SET REFERENTIAL_INTEGRITY TRUE"); // lets do
																	// this
																	// allways
		}
	}

	@Deployment
	public static EnterpriseArchive createDeployment() {
		EnterpriseArchive ear = ShrinkWrap.createFromZipFile(EnterpriseArchive.class, new File("../build/libs/ASE.ear"));
		WebArchive war = ear.getAsType(WebArchive.class, "/web.war");

		war.addClass(UserDaoRestTest.class).addAsManifestResource(EmptyAsset.INSTANCE, "beans.xml");
		return ear;
	}

	@Test
	public void testGetItem() throws Exception {

		assertEquals("database setup error-> database not empty", 0, userDao.listAllUsers().size());

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
		
		assertNotNull(u.getId());
		assertNotNull(c.getId());

		// check dao
		User du = userDao.get(u.getId());
		assertNotNull("error  could not load user", du);
		assertEquals("forname", du.getForname());
		assertEquals("lastname", du.getLastname());
		assertEquals("pass", du.getPassword());
		assertEquals("user", du.getUsername());

		// check rest

		expect().statusCode(200)
		.body("name", equalTo("klaus"))
		.when().get("http://localhost:8080/web/rest/UserRest/getItem/1");

	}
}