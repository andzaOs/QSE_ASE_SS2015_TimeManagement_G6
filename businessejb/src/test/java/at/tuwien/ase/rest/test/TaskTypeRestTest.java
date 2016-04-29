package at.tuwien.ase.rest.test;

import static com.jayway.restassured.RestAssured.expect;
import static com.jayway.restassured.RestAssured.given;
import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.Matchers.isOneOf;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

import java.util.ArrayList;
import java.util.List;

import javax.ejb.EJB;

import at.tuwien.ase.model.UserType;
import org.jboss.arquillian.junit.Arquillian;
import org.junit.Test;
import org.junit.runner.RunWith;

import at.tuwien.ase.dao.ResourceDaoInterface;
import at.tuwien.ase.dao.TaskTypeDaoInterface;
import at.tuwien.ase.model.Resource;
import at.tuwien.ase.model.TaskType;
import at.tuwien.ase.model.User;

import com.jayway.restassured.RestAssured;
import com.jayway.restassured.parsing.Parser;
import com.jayway.restassured.specification.ResponseSpecification;

@RunWith(Arquillian.class)
public class TaskTypeRestTest extends TestSuiteRest {
	private static final String NAME = "NAME";

	private static final String TASKNR = "1.1.1.1";
	@EJB
	ResourceDaoInterface ruDao;
	@EJB
	TaskTypeDaoInterface taskTypeDao;
	@Test
	public void getItemAccessTest() throws Exception{
		
		testRolesAuth("http://localhost:8080/web/rest/TaskTypeRest/getItem/1", UserType.SUPERVISOR, UserType.MANAGER);
	}

	private String DESC = "desc";
	@Test
	public void testDel() throws Exception {

		utx.begin();
		em.joinTransaction();

		// create 
		TaskType c = new TaskType();

		// persist

		em.persist(c);

		assertNotNull(c.getId());

		utx.commit();
		given().cookie(getManagerCookie()).expect().statusCode(204)
		.when()
				.delete("http://localhost:8080/web/rest/TaskTypeRest/delete/" + c.getId());	
		
		given().cookie(getManagerCookie()).expect().statusCode(204)
		.when()
				.get("http://localhost:8080/web/rest/TaskTypeRest/getItem/" + c.getId());	
	}
	@Test
	public void testGetItemDel() throws Exception {
		String desc = "desc";

		utx.begin();
		em.joinTransaction();
		assertEquals("database setup error-> database not empty", 0, taskTypeDao.listAll().size());

		// create taskTypeDao
		TaskType t = new TaskType();
		t.setDescription(desc);
		t.setExpectedWorkHours(10.0);
		t.setName(NAME);
		t.setTaskNumber(TASKNR);

		em.persist(t);
		utx.commit();

		TaskType td = taskTypeDao.get(t.getId());

		assertEquals(t.getId(), td.getId());

		given().cookie(getManagerCookie()).expect().statusCode(204).when().delete("http://localhost:8080/web/rest/TaskTypeRest/delete/" + t.getId());

		assertEquals(null, taskTypeDao.get(t.getId()));
		

	}
	@Test
	public void testListAll() throws Exception {
		utx.begin();
		em.joinTransaction();
		assertEquals("database setup error-> database not empty", 0, taskTypeDao.listAll().size());
		int size = 10;
		Integer[] idArray = new Integer[size];
		for (int i = 0; i < size; i++) {

			// create taskTypeDao
			TaskType t = new TaskType();
			t.setDescription(DESC);
			t.setName(NAME);
			t.setTaskNumber(TASKNR);

			em.persist(t);
			idArray[i] = t.getId().intValue();
		}
		utx.commit();

		RestAssured.defaultParser = Parser.JSON;
		ResponseSpecification r = given().cookie(getManagerCookie()).expect().statusCode(200);

		for (int i = 0; i < size; i++) {
			r.body("[" + i + "].id", isOneOf(idArray))
		    .body("[" + i + "].description", equalTo(DESC))
		    .body("[" + i + "].taskNumber", equalTo(TASKNR))
			.body("[" + i + "].name", equalTo(NAME));
		}

		r.when().get("http://localhost:8080/web/rest/TaskTypeRest/listAll/");

	}

	@Test
	public void testGetItem() throws Exception {
		utx.begin();
		em.joinTransaction();
		assertEquals("database setup error-> database not empty", 0, taskTypeDao.listAll().size());

		// create taskTypeDao
		TaskType t = new TaskType();
		t.setDescription(DESC);
		t.setExpectedWorkHours(10.0);
		t.setName(NAME);
		t.setTaskNumber(TASKNR);

		em.persist(t);
		utx.commit();
		RestAssured.defaultParser = Parser.JSON;

		given().cookie(getManagerCookie()).expect().statusCode(200).body("id", equalTo(t.getId().intValue())).body("description", equalTo(DESC)).body("taskNumber", equalTo(TASKNR)).body("name", equalTo(NAME))

		.when().get("http://localhost:8080/web/rest/TaskTypeRest/getItem/" + t.getId());

	}
}