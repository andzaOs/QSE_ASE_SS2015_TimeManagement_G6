package at.tuwien.ase.rest.test;

import static com.jayway.restassured.RestAssured.expect;
import static com.jayway.restassured.RestAssured.given;
import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.Matchers.isOneOf;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

import java.util.ArrayList;

import javax.ejb.EJB;

import org.jboss.arquillian.junit.Arquillian;
import org.junit.Test;
import org.junit.runner.RunWith;

import at.tuwien.ase.dao.CompanyDaoInterface;
import at.tuwien.ase.model.Company;
import at.tuwien.ase.model.User;
import at.tuwien.ase.model.UserType;

import com.jayway.restassured.RestAssured;
import com.jayway.restassured.parsing.Parser;
import com.jayway.restassured.specification.ResponseSpecification;

@RunWith(Arquillian.class)
public class CompanyRestTest extends TestSuiteRest{
	
	private static final String COMPANY_NAME = "Test_company";

	@EJB
	CompanyDaoInterface companyDao;

	@Test
	public void getItemAccessTest() throws Exception{
		
		//testRolesAuth("http://localhost:8080/web/rest/CompanyRest/getItem/1", UserType.MANAGER, UserType.MANAGER);
	}

	@Test
	public void testListAll() throws Exception {

		utx.begin();
		em.joinTransaction();

		assertEquals("database setup error-> database not empty", 0, companyDao.listAll().size());
		int size = 10;
		Integer[] idArray = new Integer[size];
		for (int i = 0; i < size; i++) {
		// create company
		Company c = new Company();
		c.setName(COMPANY_NAME);
		c.setUserList(new ArrayList<User>());
	

		// persist
		
		em.persist(c);

		assertNotNull(c.getId());

		idArray[i] = c.getId().intValue();
		}
		
		utx.commit();
		// check rest
		RestAssured.defaultParser = Parser.JSON;

		ResponseSpecification r = given().cookie(getManagerCookie()).expect().statusCode(200);

		for (int i = 0; i < size; i++) {


			r.body("[" + i + "].id", isOneOf(idArray)).body("[" + i + "].name", equalTo(COMPANY_NAME));
		}

		r.when().get("http://localhost:8080/web/rest/CompanyRest/listAll/");

	}
	@Test
	public void testGetItem() throws Exception {

		utx.begin();
		em.joinTransaction();

		assertEquals("database setup error-> database not empty", 0, companyDao.listAll().size());

		// create company
		Company c = new Company();
		c.setName(COMPANY_NAME);
		c.setUserList(new ArrayList<User>());
	

		// persist
		
		em.persist(c);

		assertNotNull(c.getId());

		
		
		utx.commit();
		// check rest
		RestAssured.defaultParser = Parser.JSON;
		given().cookie(getManagerCookie()).expect().statusCode(200).body("id", equalTo(c.getId().intValue())).body("name", equalTo(COMPANY_NAME)).when()
				.get("http://localhost:8080/web/rest/CompanyRest/getItem/" + c.getId());

	}
}