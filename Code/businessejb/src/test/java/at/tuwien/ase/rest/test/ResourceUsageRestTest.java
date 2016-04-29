package at.tuwien.ase.rest.test;

import static com.jayway.restassured.RestAssured.expect;
import static com.jayway.restassured.RestAssured.given;
import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.Matchers.isOneOf;
import static org.hamcrest.Matchers.notNullValue;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

import java.util.Date;

import javax.ejb.EJB;

import org.jboss.arquillian.junit.Arquillian;
import org.junit.Test;
import org.junit.runner.RunWith;

import at.tuwien.ase.dao.ResourceUsageDaoInterface;
import at.tuwien.ase.model.Resource;
import at.tuwien.ase.model.ResourceUsage;
import at.tuwien.ase.model.TaskReport;
import at.tuwien.ase.model.UserType;

import com.jayway.restassured.RestAssured;
import com.jayway.restassured.parsing.Parser;
import com.jayway.restassured.response.Response;
import com.jayway.restassured.specification.ResponseSpecification;

@RunWith(Arquillian.class)
public class ResourceUsageRestTest extends TestSuiteRest {

	@EJB
	ResourceUsageDaoInterface resourceUsageDao;

//	ResourceUsageRest/persistResourceUsage - Worker, Supervisor
	@Test
	public void persistResourceUsageAccessTest() throws Exception{
		
		testRolesAuthPost("http://localhost:8080/web/rest/ResourceUsageRest/persist/",new ResourceUsage(),UserType.WORKER,UserType.SUPERVISOR);
	}
	@Test
	public void getItemAccessTest() throws Exception{
		
		testRolesAuth("http://localhost:8080/web/rest/ResourceUsageRest/getItem/1", UserType.WORKER, UserType.SUPERVISOR, UserType.MANAGER);
	}

	@Test
	public void testDel() throws Exception {

		utx.begin();
		em.joinTransaction();

		// create 
		ResourceUsage c = new ResourceUsage();

		// persist

		em.persist(c);

		assertNotNull(c.getId());

		utx.commit();
		given().cookie(getManagerCookie()).expect().statusCode(204)
		.when()
				.delete("http://localhost:8080/web/rest/ResourceUsageRest/delete/" + c.getId());	
		
		given().cookie(getManagerCookie()).expect().statusCode(204)
		.when()
				.get("http://localhost:8080/web/rest/ResourceUsageRest/getItem/" + c.getId());	
	}
	@Test
	public void testListAll() throws Exception {

		utx.begin();
		em.joinTransaction();

		assertEquals("database setup error-> database not empty", 0, resourceUsageDao.listAll().size());
		int size = 10;
		Integer[] idArray = new Integer[size];
		Date begin = new Date();
		Double cost = 10.0;

		Date end = new Date();
		Double quantity = 50.0;
		for (int i = 0; i < size; i++) {
			// one2one refs (lazy)
			Resource resource = new Resource();
			;
			em.persist(resource);
			TaskReport taskReport = new TaskReport();
			em.persist(taskReport);
			// create
			ResourceUsage r = new ResourceUsage();
		
			r.setBegin(begin);

			r.setCost(cost);

			r.setEnd(end);

			r.setQuantity(quantity);

			r.setResource(resource);

			r.setTaskReport(taskReport);

			// persist

			em.persist(r);

			idArray[i] = r.getId().intValue();
		}
		utx.commit();

		RestAssured.defaultParser = Parser.JSON;
		ResponseSpecification r = given().cookie(getManagerCookie()).expect().statusCode(200);

		for (int i = 0; i < size; i++) {
			r.body("[" + i + "].id", isOneOf(idArray))
		    .body("[" + i + "].begin", equalTo(begin.getTime()))
		    .body("[" + i + "].end", equalTo(end.getTime()))
		    
		    .body("[" + i + "].resource.id", notNullValue())
		    .body("[" + i + "].taskReport.id", notNullValue());
			
		}

		r.when().get("http://localhost:8080/web/rest/ResourceUsageRest/listAll/");
	}

	@Test
	public void testGetItem() throws Exception {

		utx.begin();
		em.joinTransaction();

		assertEquals("database setup error-> database not empty", 0, resourceUsageDao.listAll().size());
		// one2one refs (lazy)
		Resource resource = new Resource();
		;
		em.persist(resource);
		TaskReport taskReport = new TaskReport();
		em.persist(taskReport);
		// create
		ResourceUsage r = new ResourceUsage();
		Date begin = new Date();
		Double cost = 10.0;
		Date end = new Date();
		Double quantity = 50.0;
		r.setBegin(begin);

		r.setCost(cost);

		r.setEnd(end);

		r.setQuantity(quantity);

		r.setResource(resource);

		r.setTaskReport(taskReport);

		// persist

		em.persist(r);
		utx.commit();

		assertNotNull(r.getId());
		RestAssured.defaultParser = Parser.JSON;

		Response rest = given().cookie(getManagerCookie()).expect().statusCode(200).body("id", equalTo(r.getId().intValue())).body("begin", equalTo(begin.getTime())).body("end", equalTo(end.getTime()))

				.body("resource.id", equalTo(resource.getId().intValue())).body("taskReport.id", equalTo(taskReport.getId().intValue()))

				.when().get("http://localhost:8080/web/rest/ResourceUsageRest/getItem/" + r.getId()).peek();
		rest.getBody().peek();
	}
}