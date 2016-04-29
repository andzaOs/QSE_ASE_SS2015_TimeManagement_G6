package at.tuwien.ase.rest.test;

import static com.jayway.restassured.RestAssured.expect;
import static com.jayway.restassured.RestAssured.given;
import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.Matchers.isOneOf;
import static org.hamcrest.Matchers.notNullValue;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

import javax.ejb.EJB;

import org.jboss.arquillian.junit.Arquillian;
import org.junit.Test;
import org.junit.runner.RunWith;

import at.tuwien.ase.dao.ProjectDaoInterface;
import at.tuwien.ase.dao.UserDaoInterface;
import at.tuwien.ase.model.Project;
import at.tuwien.ase.model.ProjectType;
import at.tuwien.ase.model.TaskType;
import at.tuwien.ase.model.User;
import at.tuwien.ase.model.UserType;

import com.jayway.restassured.specification.ResponseSpecification;




@RunWith(Arquillian.class)
public class ProjectRestTest extends TestSuiteRest{
	private static final String NAME = "DESC";


	@EJB
	ProjectDaoInterface projectDao;
	
	@Test
	public void getItemAccessTest() throws Exception{
		
		testRolesAuth("http://localhost:8080/web/rest/ProjectRest/getItem/1",UserType.SUPERVISOR);
	}

	@EJB
	UserDaoInterface userDao;
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
	public void testPersistError() throws Exception {//tests for a bug
		utx.begin();
		em.joinTransaction();
		Project p = new Project();
		em.persist(p);
		utx.commit();
		assertNotNull(p.getId());
		String b = "{\"name\":\"Hauptbahnhof Wien Change\",\"expectedWorkHours\":\"1321312321\",\"projectType\":null,\"taskTypeList\":[],\"userList\":[],\"id\":"+p.getId()+"}";
	
		given().cookie(getManagerCookie()).given().contentType("application/json").body(b.getBytes()).expect().statusCode(204).when().post("http://localhost:8080/web/rest/ProjectRest/persist/");
		assertEquals(1, projectDao.listAll().size());
	}
	
	@Test
	public void testPersistNToMany() throws Exception {

		String description = "";
		String username="username";
		assertEquals("database setup error-> database not empty", 0, projectDao.listAll().size());
	
		utx.begin();

		HashSet<TaskType> ttSetd = new HashSet<TaskType>();

		HashSet<User> uSetd = new HashSet<User>();
		for (int i = 0; i < 10; i++) {
			TaskType tt = new TaskType();
			tt.setDescription(description);
			em.persist(tt);
			ttSetd.add(tt);
			
		}
		
		
		for (int i = 0; i < 10; i++) {
			User u = new User();
			em.persist(u);
			u.setUsername(username);
			uSetd.add(u);
		}
		em.joinTransaction();
		
		utx.commit();
		
	
		Project p = new Project();
		p.setUserList(uSetd);
		p.setTaskTypeList(ttSetd);
		
	
		given().cookie(getManagerCookie()).contentType("application/json").body(p).expect().statusCode(204).when().post("http://localhost:8080/web/rest/ProjectRest/persist/");
		List<Project> l = projectDao.listAll();
		for (Project project : l) {
			System.out.println(project.getId());
		}
		assertEquals(1, l.size());
		
		Project pd = l.get(0);
		
		
		
		Set<User> uSet = pd.getUserList();
		assertEquals(10, uSet.size());
		
		for (User u : uSet) {
			assertEquals(username, u.getUsername());
		}
		Set<TaskType> ttSet = pd.getTaskTypeList();		
		assertEquals(10, ttSet.size());
		for (TaskType tt : ttSet) {
			assertEquals(description, tt.getDescription());
		}
	}
	
	@Test
	public void testPersistNToOne() throws Exception {

		String description = "";
		Double h = 10.0;
		assertEquals("database setup error-> database not empty", 0, projectDao.listAll().size());
	
		utx.begin();
		
		em.joinTransaction();
		
		ProjectType type = new ProjectType();
		type.setDescription(description);
		em.persist(type);
		utx.commit();
		
		assertNotNull(type.getId());
		Project p = new Project();

		p.setExpectedWorkHours(h);
		p.setName(NAME);
		p.setProjectType(type);
		
	
		given().cookie(getManagerCookie()).contentType("application/json").body(p).expect().statusCode(204).when().post("http://localhost:8080/web/rest/ProjectRest/persist/");
		List<Project> l = projectDao.listAll();
		assertEquals(1, l.size());

		Project pd = l.get(0);
		assertEquals(NAME, pd.getName());
		assertEquals(h, pd.getExpectedWorkHours());
		assertNotNull(pd.getProjectType());
		assertEquals(description, pd.getProjectType().getDescription());
		
	}
	
	@Test
	public void testListAlls() throws Exception {

		utx.begin();
		em.joinTransaction();

		assertEquals("database setup error-> database not empty", 0, projectDao.listAll().size());
		int size = 10;
		Integer[] idArray = new Integer[size];
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
			idArray[i] = p.getId().intValue();
		}
		utx.commit();
		ResponseSpecification r = given().cookie(getManagerCookie()).expect().statusCode(200);

		for (int i = 0; i < size; i++) {
			r.body("[" + i + "].id", isOneOf(idArray))
			.body("[" + i + "].projectType.id", notNullValue())
			.body("[" + i + "].name", equalTo(NAME));
		
		}

		r.when().get("http://localhost:8080/web/rest/ProjectRest/listAll/");
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
		
		// persist

		em.persist(p);
		utx.commit();
		assertNotNull(p.getId());
		Project pd = projectDao.get(p.getId());
		
		
		
		assertNotNull(pd);
	
		given().cookie(getManagerCookie()).expect().statusCode(200).body("id", equalTo(pd.getId().intValue()))
		.body("projectType.id", equalTo(projectType.getId().intValue()))
		.body("name", equalTo(NAME))
		.when()
				.get("http://localhost:8080/web/rest/ProjectRest/getItem/" + pd.getId());
	}
}