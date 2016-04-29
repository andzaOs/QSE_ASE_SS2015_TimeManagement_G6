package at.tuwien.ase.rest.test;

import static com.jayway.restassured.RestAssured.expect;
import static com.jayway.restassured.RestAssured.given;
import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.Matchers.isOneOf;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

import javax.ejb.EJB;

import at.tuwien.ase.model.UserType;
import org.jboss.arquillian.junit.Arquillian;
import org.junit.Test;
import org.junit.runner.RunWith;

import at.tuwien.ase.dao.ProjectTypeDaoInterface;
import at.tuwien.ase.model.Project;
import at.tuwien.ase.model.ProjectType;

import com.jayway.restassured.specification.ResponseSpecification;


@RunWith(Arquillian.class)
public class ProjectTypeRestTest extends TestSuiteRest {
	private static final String DESC = "DESC";

	
	@EJB
	ProjectTypeDaoInterface projectTypeDao;
	@Test
	public void getItemAccessTest() throws Exception{
		
		testRolesAuth("http://localhost:8080/web/rest/ProjectTypeRest/getItem/1", UserType.SUPERVISOR, UserType.MANAGER);
	}

	@Test
	public void testDel() throws Exception {

		utx.begin();
		em.joinTransaction();

		// create 
		ProjectType c = new ProjectType();

		// persist

		em.persist(c);

		assertNotNull(c.getId());

		utx.commit();
		given().cookie(getManagerCookie()).expect().statusCode(204)
		.when()
				.delete("http://localhost:8080/web/rest/ProjectTypeRest/delete/" + c.getId());	
		
		given().cookie(getManagerCookie()).expect().statusCode(204)
		.when()
				.get("http://localhost:8080/web/rest/ProjectTypeRest/getItem/" + c.getId());	
	}

	@Test
	public void testListAll() throws Exception {

		utx.begin();
		em.joinTransaction();


		assertEquals("database setup error-> database not empty", 0, projectTypeDao.listAll().size());
		int size = 10;
		Integer[] idArray = new Integer[size];
		for (int i = 0; i < size; i++) {
		 // one2one refs (lazy)
	
		// create Project
		ProjectType p = new ProjectType();
		p.setDescription(DESC);
	
		
		// persist

		em.persist(p);
		idArray[i] = p.getId().intValue();
		}
		utx.commit();
		ResponseSpecification r = given().cookie(getManagerCookie()).expect().statusCode(200);

		for (int i = 0; i < size; i++) {
		
			r.body("[" + i + "].id", isOneOf(idArray))
		     .body("[" + i + "].description", equalTo(DESC));
		}

		r.when().get("http://localhost:8080/web/rest/ProjectTypeRest/listAll/");
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
		
		given().cookie(getManagerCookie()).expect().statusCode(200).body("id", equalTo(p.getId().intValue()))
		.body("description", equalTo(DESC))
		.when()
				.get("http://localhost:8080/web/rest/ProjectTypeRest/getItem/" + p.getId());
	}
}