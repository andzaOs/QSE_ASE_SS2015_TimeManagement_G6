package at.tuwien.ase.rest.test;

import static com.jayway.restassured.RestAssured.expect;
import static com.jayway.restassured.RestAssured.given;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

import java.util.HashSet;
import java.util.Set;

import javax.ejb.EJB;

import org.jboss.arquillian.junit.Arquillian;
import org.junit.Test;
import org.junit.runner.RunWith;

import at.tuwien.ase.dao.ProjectDaoInterface;
import at.tuwien.ase.dao.UserDaoInterface;
import at.tuwien.ase.model.Project;
import at.tuwien.ase.model.Resource;
import at.tuwien.ase.model.User;
import at.tuwien.ase.model.UserType;

@RunWith(Arquillian.class)
public class ProjectUserTest extends TestSuiteRest {

	@EJB
	UserDaoInterface userDao;
	

	@EJB
	ProjectDaoInterface projectDao;
	@Test
	public void testDel() throws Exception {

		utx.begin();
		em.joinTransaction();

		// create 
		Project c = new Project();

		// persist

		em.persist(c);

		assertNotNull(c.getId());

		utx.commit();
		given().cookie(getManagerCookie()).expect().statusCode(204)
		.when()
				.delete("http://localhost:8080/web/rest/ProjectRest/delete/" + c.getId());	
		
		given().cookie(getManagerCookie()).expect().statusCode(204)
		.when()
				.get("http://localhost:8080/web/rest/ProjectRest/getItem/" + c.getId());	
	}
	@Test
	public void testSuccessful() throws Exception{
		
		utx.begin();
		em.joinTransaction();

		assertEquals("database setup error-> database not empty", 0, projectDao.listAll().size());
		
		User u = new User();
		u.setUserType(UserType.MANAGER);
		u.setForname("forname");
		u.setLastname("lastname");
		u.setPassword("pass");
		u.setUsername("user");
		
		em.persist(u);
		
		Set<User> users = new HashSet<User>();
		users.add(u);
		
		// create Project
		Project p = new Project();
		p.setExpectedWorkHours(10.0);
		p.setName("Test Project");
		p.setUserList(users);
		
		// persist
		em.persist(p);
		utx.commit();
		
		assertNotNull(p.getId());
		Project pd = projectDao.get(p.getId());
		
		assertNotNull(u.getId());
		User ud = userDao.get(u.getId());
		
		assertNotNull(pd);
		assertNotNull(ud);
	
		given().cookie(getManagerCookie()).expect().statusCode(200).when().get("http://localhost:8080/web/rest/ProjectRest/getProjectItem/" + ud.getId());
	
	}
	
	@Test
	public void testUnSuccessful() throws Exception{
		
		utx.begin();
		em.joinTransaction();

		assertEquals("database setup error-> database not empty", 0, projectDao.listAll().size());
		
		User u = new User();
		u.setUserType(UserType.MANAGER);
		u.setForname("forname");
		u.setLastname("lastname");
		u.setPassword("pass");
		u.setUsername("user");
		
		em.persist(u);
		
		Set<User> users = new HashSet<User>();
		users.add(u);
		
		// create Project
		Project p = new Project();
		p.setExpectedWorkHours(10.0);
		p.setName("Test Project");
		p.setUserList(users);
		
		// persist
		em.persist(p);
		utx.commit();
		
		assertNotNull(p.getId());
		Project pd = projectDao.get(p.getId());
		
		assertNotNull(u.getId());
		User ud = userDao.get(u.getId());
		
		assertNotNull(pd);
		assertNotNull(ud);
	
		given().cookie(getManagerCookie()).expect().statusCode(204).when().get("http://localhost:8080/web/rest/ProjectRest/getProjectItem/" + (ud.getId() + 1));
	}
}
