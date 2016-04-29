package at.tuwien.ase.rest.test;

import static com.jayway.restassured.RestAssured.expect;
import static com.jayway.restassured.RestAssured.given;
import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.Matchers.isOneOf;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;

import java.util.ArrayList;
import java.util.List;

import javax.ejb.EJB;

import org.jboss.arquillian.junit.Arquillian;
import org.junit.Test;
import org.junit.runner.RunWith;

import at.tuwien.ase.dao.UserDaoInterface;
import at.tuwien.ase.model.Company;
import at.tuwien.ase.model.Project;
import at.tuwien.ase.model.ProjectType;
import at.tuwien.ase.model.User;
import at.tuwien.ase.model.UserType;
import at.tuwien.ase.model.WorkingObject;
import at.tuwien.ase.rest.ProxyRemover;

import com.jayway.restassured.RestAssured;
import com.jayway.restassured.parsing.Parser;
import com.jayway.restassured.specification.ResponseSpecification;

@RunWith(Arquillian.class)
public class UserRestTest extends TestSuiteRest {
	private static final String COMPANY_NAME = "Test_company";

	@EJB
	UserDaoInterface userDao;
	@Test
	public void getItemAccessTest() throws Exception{
		
		testRolesAuth("http://localhost:8080/web/rest/UserRest/getItem/1", UserType.SUPERVISOR, UserType.MANAGER);
	}

	@Test
	public void testDel() throws Exception {

		utx.begin();
		em.joinTransaction();

		// create 
		User c = new User();

		// persist

		em.persist(c);

		assertNotNull(c.getId());

		utx.commit();
		given().cookie(getManagerCookie()).expect().statusCode(204)
		.when()
				.delete("http://localhost:8080/web/rest/UserRest/delete/" + c.getId());	
		
		given().cookie(getManagerCookie()).expect().statusCode(204)
		.when()
				.get("http://localhost:8080/web/rest/UserRest/getItem/" + c.getId());	
	}
	@Test
	public void testUserPersist() throws Exception {

		User u = new User();
		u.setUserType(UserType.MANAGER);
		u.setForname("forname");
		u.setLastname("lastname");
		u.setPassword("pass");
		u.setUsername("user");
		
		given().cookie(getManagerCookie()).contentType("application/json").body(u).expect().statusCode(204).when().post("http://localhost:8080/web/rest/UserRest/persist/");
		List<User> list = userDao.listAll();
		assertEquals(1, list.size());
		
		User user = list.get(0);
		assertNotNull(user.getId());
		assertEquals(u.getCompany(), user.getCompany());
		assertEquals(u.getDeleted(), user.getDeleted());
		assertEquals(u.getForname(), user.getForname());
		//create  should also  save the password

		assertEquals(u.getPassword(), user.getPassword());
		assertEquals(u.getLastname(), user.getLastname());
		assertEquals(u.getUsername(), user.getUsername());
		
		
		
		//change pass 
		User userChangePass = new User();
		userChangePass.setId(user.getId());
		userChangePass.setUserType(UserType.MANAGER);
		userChangePass.setForname("forname");
		userChangePass.setLastname("lastname");
		userChangePass.setUsername("user");
		userChangePass.setPassword("THis is not the way you shoud be able to save a password");
		
		given().cookie(getManagerCookie()).contentType("application/json").body(userChangePass).expect().statusCode(204).when().post("http://localhost:8080/web/rest/UserRest/persist/");
		 list = userDao.listAll();
		assertEquals(1, list.size());
		 User userUpdated = list.get(0);
		 assertEquals(u.getPassword(), userUpdated.getPassword());
		
	}
	@Test
	public void testUpdateUserPassword() throws Exception {
		utx.begin();
		em.joinTransaction();

		Company c = new Company();
		c.setName(COMPANY_NAME);
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
		
		User user=new User();
		user.setUserType(UserType.SUPERVISOR);
		user.setForname("sadsaf");
		user.setLastname("sfdfsdf");
		user.setPassword("NEWPASS");
		user.setUsername("3r35gfg");
		user.setId(u.getId());
		given().cookie(getManagerCookie()).contentType("application/json").body(user).expect().statusCode(204).when().post("http://localhost:8080/web/rest/UserRest/updatePassword/");
		User dbUser = userDao.get(u.getId());
		assertEquals(user.getPassword(), dbUser.getPassword());
		assertEquals(u.getCompany(), dbUser.getCompany());
		assertEquals(u.getDeleted(), dbUser.getDeleted());
		assertEquals(u.getForname(), dbUser.getForname());

		assertEquals(u.getLastname(), dbUser.getLastname());
		assertEquals(u.getUsername(), dbUser.getUsername());
		
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
		given().cookie(getManagerCookie()).expect().statusCode(204).when().delete("http://localhost:8080/web/rest/UserRest/delete/" + u.getId());
		assertNull(userDao.getByCredentials("user", "pass"));
	}
	@Test
	public void testListALl() throws Exception {

		utx.begin();
		em.joinTransaction();

		assertEquals("database setup error-> database not empty", 0, userDao.listAll().size());
		int size = 10;
		Integer[] idArray = new Integer[size];
		for (int i = 0; i < size; i++) {
			// create company
			Company c = new Company();
			c.setName(COMPANY_NAME);
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

			idArray[i] = u.getId().intValue();
		}
		utx.commit();
		// check rest
		RestAssured.defaultParser = Parser.JSON;
		ResponseSpecification r = given().cookie(getManagerCookie()).expect().statusCode(200);

		for (int i = 0; i < size; i++) {

			r.body("[" + i + "].id", isOneOf(idArray)).body("[" + i + "].company.name", equalTo(COMPANY_NAME)).body("[" + i + "].forname", equalTo("forname"));
		}

		r.when().get("http://localhost:8080/web/rest/UserRest/listAll/");

	}

	@Test
	public void testGetItem() throws Exception {

		utx.begin();
		em.joinTransaction();

		assertEquals("database setup error-> database not empty", 0, userDao.listAll().size());

		// create company
		Company c = new Company();
		c.setName(COMPANY_NAME);
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

		utx.commit();
		// check rest
		given().cookie(getManagerCookie()).expect().statusCode(200).body("id", equalTo(u.getId().intValue())).body("company.name", equalTo(COMPANY_NAME)).body("password", equalTo(null)).body("forname", equalTo("forname")).when()
				.get("http://localhost:8080/web/rest/UserRest/getItem/" + u.getId());

	}
}