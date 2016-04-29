package at.tuwien.ase.rest.test;

import static com.jayway.restassured.RestAssured.expect;
import static com.jayway.restassured.RestAssured.given;
import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.Matchers.isOneOf;
import static org.hamcrest.Matchers.notNullValue;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.fail;

import java.util.ArrayList;
import java.util.List;

import javax.ejb.EJB;

import org.jboss.arquillian.junit.Arquillian;
import org.junit.Test;
import org.junit.runner.RunWith;

import at.tuwien.ase.dao.ResourceDaoInterface;
import at.tuwien.ase.model.Category;
import at.tuwien.ase.model.Resource;
import at.tuwien.ase.model.ResourceUsage;
import at.tuwien.ase.model.TaskType;
import at.tuwien.ase.model.UserType;

import com.jayway.restassured.specification.ResponseSpecification;
@RunWith(Arquillian.class)
public class ResourceRestTest extends TestSuiteRest {
	private static final String DESC = "DESC";

	@EJB
	ResourceDaoInterface resourceDao;
	//ResourceRest/listAllForTaskType/ - Worker, Supervisor
	@Test
	public void listAllForTaskTypeAccessTest() throws Exception{
		
		testRolesAuth("http://localhost:8080/web/rest/ResourceRest/listAllForTaskType/1",UserType.WORKER,UserType.SUPERVISOR);
	}

	@Test
	public void getItemAccessTest() throws Exception{
		
		testRolesAuth("http://localhost:8080/web/rest/ResourceRest/getItem/1");
	}

	@Test
	public void testDel() throws Exception {

		utx.begin();
		em.joinTransaction();

		// create 
		Resource c = new Resource();

		// persist

		em.persist(c);

		assertNotNull(c.getId());

		utx.commit();
		given().cookie(getManagerCookie()).expect().statusCode(204)
		.when()
				.delete("http://localhost:8080/web/rest/ResourceRest/delete/" + c.getId());	
		
		given().cookie(getManagerCookie()).given().cookie(getManagerCookie()).expect().statusCode(204)
		.when()
				.get("http://localhost:8080/web/rest/ResourceRest/getItem/" + c.getId());	
	}
	
	@Test
	public void testListAllForTaskType() throws Exception {

		utx.begin();
		em.joinTransaction();

		assertEquals("database setup error-> database not empty", 0, resourceDao.listAll().size());
		List<Resource> list = new ArrayList<Resource>();
		int size = 10;

		Integer[] idArray = new Integer[size];
		TaskType tt = new TaskType();
		tt.setResourceList(new ArrayList<Resource>());
		em.persist(tt);
		for (int i = 0; i < size; i++) {

			// one2one refs (lazy)
			Category c = new Category();
			em.persist(c);
			// create
			Resource r = new Resource();
			r.setTaskTypeList(new ArrayList<TaskType>());
			r.setCategory(c);
			r.setDescription(DESC);
			for (int j = 0; j < 4; j++) {
				TaskType ttj = new TaskType();
				ttj.setResourceList(new ArrayList<Resource>());
				em.persist(ttj);
				ttj.getResourceList().add(r);
				r.getTaskTypeList().add(ttj);
				
			}
			r.getTaskTypeList().add(tt);
			tt.getResourceList().add(r);
			// persist
			list.add(r);
			em.persist(r);
			idArray[i] = r.getId().intValue();
		}
		utx.commit();
		ResponseSpecification r = given().cookie(getManagerCookie()).expect().statusCode(200);

		for (int i = 0; i < size; i++) {
		r.body("[" + i + "].id", isOneOf(idArray))
		.body("[" + i + "].category.id", notNullValue())
		.body("[" + i + "].description", equalTo(DESC));
		
		}

		r.when().get("http://localhost:8080/web/rest/ResourceRest/listAllForTaskType/"+tt.getId());
		
	}
	@Test
	public void testListItem() throws Exception {

		utx.begin();
		em.joinTransaction();

		assertEquals("database setup error-> database not empty", 0, resourceDao.listAll().size());
		int size = 10;
		Integer[] idArray = new Integer[size];
		for (int i = 0; i < size; i++) {

		// one2one refs (lazy)
		Category c = new Category();
		em.persist(c);
		// create 
		Resource r = new Resource();
		r.setCategory(c);
		r.setDescription(DESC);
		
		// persist

		em.persist(r);
		idArray[i] = r.getId().intValue();
		}
		utx.commit();
		ResponseSpecification r = given().cookie(getManagerCookie()).expect().statusCode(200);

		for (int i = 0; i < size; i++) {
		r.body("[" + i + "].id", isOneOf(idArray))
		.body("[" + i + "].category.id", notNullValue())
		.body("[" + i + "].description", equalTo(DESC));
		
		}

		r.when().get("http://localhost:8080/web/rest/ResourceRest/listAll/");

		
	}
	
	
	@Test 
	public void testPersistResource2() throws Exception{
		String dest1="new Resource";
		String dest2="updated Resource";
		String name1="new Name";
		String name2="updated Name";
		
		//new 
		Resource  r=new Resource();
		Category c = new Category();
		c.setName(name1);
		r.setCategory(c);
		
		//save
		r.setDescription(dest1);
		given().cookie(getManagerCookie()).contentType("application/json")
		.body( r)
		.expect().statusCode(204).when().post("http://localhost:8080/web/rest/ResourceRest/persist/");
		//check	
		List<Resource> list = resourceDao.listAll();
		assertEquals(1, list.size());
		assertEquals(dest1, list.get(0).getDescription());
		assertEquals(name1, list.get(0).getCategory().getName());
		Long id = list.get(0).getId();
		assertNotNull(id);
		//update
		r.setId(id);
		r.setDescription(dest2);
		c.setName(name2);
		given().cookie(getManagerCookie()).contentType("application/json")
		.body( r)
		.expect().statusCode(204).when().post("http://localhost:8080/web/rest/ResourceRest/persist/");
		list = resourceDao.listAll();
		assertEquals(1, list.size());
		assertEquals(dest2, list.get(0).getDescription());
		assertEquals(name2, list.get(0).getCategory().getName());
		assertEquals( id ,list.get(0).getId());
		
		//remove category
		r.setCategory(null);
		given().cookie(getManagerCookie()).contentType("application/json")
		.body( r)
		.expect().statusCode(204).when().post("http://localhost:8080/web/rest/ResourceRest/persist/");
		list = resourceDao.listAll();

		assertNull(list.get(0).getCategory());
		
		
	}
	@Test 
	public void testPersistResource1() throws Exception{

		
		//new 
		Resource  r=new Resource();
		String dest1="new Resource";
		r.setDescription(dest1);
		given().cookie(getManagerCookie()).contentType("application/json")
		.body( r)
		.expect().statusCode(204).when().post("http://localhost:8080/web/rest/ResourceRest/persist/");
		List<Resource> list = resourceDao.listAll();
		assertEquals(1, list.size());
		assertEquals(dest1, list.get(0).getDescription());
		
		
		
	}
	@Test
	public void testGetItem() throws Exception {

		utx.begin();
		em.joinTransaction();

		assertEquals("database setup error-> database not empty", 0, resourceDao.listAll().size());
		// one2one refs (lazy)
		Category c = new Category();
		em.persist(c);
		// create 
		Resource r = new Resource();
		r.setCategory(c);
		r.setDescription(DESC);
		
		// persist

		em.persist(r);
		utx.commit();
		
		given().cookie(getManagerCookie()).expect().statusCode(200).body("id", equalTo(r.getId().intValue()))
		.body("category.id", equalTo(c.getId().intValue()))
		.body("description", equalTo(DESC))
		
		.when()
				.get("http://localhost:8080/web/rest/ResourceRest/getItem/" + r.getId());

		
	}
}