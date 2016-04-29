package at.tuwien.ase.rest.test;

import static com.jayway.restassured.RestAssured.*;
import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.Matchers.isOneOf;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

import javax.ejb.EJB;

import at.tuwien.ase.model.UserType;
import org.jboss.arquillian.junit.Arquillian;
import org.junit.Test;
import org.junit.runner.RunWith;

import at.tuwien.ase.dao.WorkingObjectDaoInterface;
import at.tuwien.ase.model.WorkingObject;

import com.jayway.restassured.specification.ResponseSpecification;

@RunWith(Arquillian.class)
public class WorkingObjectRestTest extends TestSuiteRest {

	private static final String DESC = "DESC";
	@Test
	public void getItemAccessTest() throws Exception{
		
		testRolesAuth("http://localhost:8080/web/rest/WorkingObjectRest/getItem/1", UserType.SUPERVISOR, UserType.MANAGER);
	}

	@EJB
	WorkingObjectDaoInterface workingObjectDao;
	@Test
	public void testDel() throws Exception {

		utx.begin();
		em.joinTransaction();

		// create 
		WorkingObject c = new WorkingObject();

		// persist

		em.persist(c);

		assertNotNull(c.getId());

		utx.commit();
		given().cookie(getManagerCookie()).expect().statusCode(204)
		.when()
				.delete("http://localhost:8080/web/rest/WorkingObjectRest/delete/" + c.getId());	
		
		given().cookie(getManagerCookie()).expect().statusCode(204)
		.when()
				.get("http://localhost:8080/web/rest/WorkingObjectRest/getItem/" + c.getId());	
	}
	@Test
	public void testListAll() throws Exception {
		utx.begin();
		em.joinTransaction();

		assertEquals("database setup error-> database not empty", 0, workingObjectDao.listAll().size());
		int size = 10;
		Integer[] idArray = new Integer[size];
		for (int i = 0; i < size; i++) {

			// create
			WorkingObject w = new WorkingObject();
			w.setDescription(DESC);

			// persist

			em.persist(w);

			assertNotNull(w.getId());
			idArray[i] = w.getId().intValue();
		}
		utx.commit();

		ResponseSpecification r = given().cookie(getManagerCookie()).expect().statusCode(200);

		for (int i = 0; i < size; i++) {

			r.body("[" + i + "].id", isOneOf(idArray))
			.body("[" + i + "].description", equalTo(DESC));
		}

		r.when().get("http://localhost:8080/web/rest/WorkingObjectRest/listAll/");
	}

	@Test
	public void testGetItem() throws Exception {

		utx.begin();
		em.joinTransaction();

		assertEquals("database setup error-> database not empty", 0, workingObjectDao.listAll().size());

		// create
		WorkingObject w = new WorkingObject();
		w.setDescription(DESC);

		// persist

		em.persist(w);

		assertNotNull(w.getId());

		utx.commit();
		given().cookie(getManagerCookie()).expect().statusCode(200).body("id", equalTo(w.getId().intValue()))

		.body("description", equalTo(DESC))

		.when().get("http://localhost:8080/web/rest/WorkingObjectRest/getItem/" + w.getId());
	}
}