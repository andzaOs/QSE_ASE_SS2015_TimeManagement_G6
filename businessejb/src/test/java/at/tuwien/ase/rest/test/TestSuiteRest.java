package at.tuwien.ase.rest.test;

import static com.jayway.restassured.RestAssured.expect;
import static com.jayway.restassured.RestAssured.given;

import java.io.File;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import static org.junit.Assert.*;

import javax.inject.Inject;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;
import javax.transaction.UserTransaction;

import org.eu.ingwar.tools.arquillian.extension.suite.annotations.ArquilianSuiteDeployment;
import org.jboss.arquillian.container.test.api.Deployment;
import org.jboss.shrinkwrap.api.ShrinkWrap;
import org.jboss.shrinkwrap.api.asset.EmptyAsset;
import org.jboss.shrinkwrap.api.spec.EnterpriseArchive;
import org.jboss.shrinkwrap.api.spec.WebArchive;
import org.junit.After;
import org.junit.Before;
import org.junit.Rule;

import at.tuwien.ase.model.User;
import at.tuwien.ase.model.UserType;
import at.tuwien.ase.rest.security.SecurityInterceptor;
import at.tuwien.ase.rest.security.SecurityToken;
import at.tuwien.ase.rest.test.LoginRestTest.LoginRestHelper;

import com.jayway.restassured.RestAssured;
import com.jayway.restassured.parsing.Parser;
import com.jayway.restassured.response.Cookie;

@ArquilianSuiteDeployment
public class TestSuiteRest {
	@Rule public LogTestName logTestName = new LogTestName();
	@PersistenceContext(name = "AseDataSource")
	protected EntityManager em;
	@Inject
	
	protected UserTransaction utx;
	public TestSuiteRest() {
		RestAssured.defaultParser = Parser.JSON;
	}
	@Deployment
	public static EnterpriseArchive createDeployment() {
		EnterpriseArchive ear = ShrinkWrap.createFromZipFile(EnterpriseArchive.class, new File("build/ASE.ear"));
		WebArchive war = ear.getAsType(WebArchive.class, "/web.war");

		war.addPackage(TestSuiteRest.class.getPackage()).addAsManifestResource(EmptyAsset.INSTANCE, "beans.xml");
		return ear;
	}

	private void testStandardAuthPost(String string, Object o) throws Exception {
		
		
		expect().statusCode(403).given().cookie("bla","blo").contentType("application/json").body(o).when().post(string);//NO AUTH	 and wrong cookie
		expect().statusCode(403).given().contentType("application/json").body(o).when().post(string);//NO AUTH
		int c = expect().given().contentType("application/json").body(o).cookie(getManagerCookie()).when().post(string).peek().andReturn().getStatusCode();	//MANAGER IS SUPERUSER	
		
		assertTrue("wrong status code:"+c,c==202||c==200||c==204||c==405);
}
	protected void testStandardAuth(String string) throws Exception {
	
			expect().statusCode(403).given().cookie("bla","blo").when().get(string);//NO AUTH and wrong cookie
			expect().statusCode(403).when().get(string);//NO AUTH
			int c = expect().given().cookie(getManagerCookie()).when().get(string).peek().andReturn().getStatusCode();	//MANAGER IS SUPERUSER	
			assertTrue("wrong status code:"+c,c==202||c==200||c==204||c==405);
	}
	protected void testRolesAuthPost(String string,Object o,UserType... tt) throws Exception {
		testStandardAuthPost(string, o);
		ArrayList<UserType> l = new ArrayList<UserType>();
		
		for (UserType userType : tt) {
			l.add(userType);
			if(userType.equals(UserType.MANAGER)){
				int c = expect().given().contentType("application/json").body(o).cookie(getManagerCookie()).when().post(string).peek().andReturn().getStatusCode();	
				System.out.println(c+string);
				assertTrue("wrong status code:"+c,c==202||c==200||c==204||c==405);
			}
			
			if(userType.equals(UserType.SUPERVISOR)){
				int c = expect().given().contentType("application/json").body(o).cookie(getSuperVisorCookie()).when().post(string).peek().andReturn().getStatusCode();
				System.out.println(c+string);
				assertTrue("wrong status code:"+c,c==202||c==200||c==204||c==405);
			}
			if(userType.equals(UserType.WORKER)){
				int c =expect().given().contentType("application/json").body(o).cookie(getWorkerCookie()).when().post(string).peek().andReturn().getStatusCode();
				System.out.println(c+string);
				assertTrue("wrong status code:"+c,c==202||c==200||c==204||c==405);
			}
		}
		
			
		//MANAGER IS SUPERUSER
		if(!l.contains(UserType.SUPERVISOR)){
			expect().statusCode(403).given().contentType("application/json").body(o).cookie(getSuperVisorCookie()).when().post(string);	
		}
		if(!l.contains(UserType.WORKER)){
			expect().statusCode(403).given().contentType("application/json").body(o).cookie(getWorkerCookie()).when().post(string);	
		}
		
	}
	protected void testRolesAuth(String string,UserType... tt) throws Exception {
		testStandardAuth(string);
		ArrayList<UserType> l = new ArrayList<UserType>();
		
		for (UserType userType : tt) {
			l.add(userType);
			if(userType.equals(UserType.MANAGER)){
				int c = expect().given().cookie(getManagerCookie()).when().get(string).peek().andReturn().getStatusCode();	
				System.out.println(c+string);
				assertTrue(c==202||c==200||c==204||c==405);
			}
			
			if(userType.equals(UserType.SUPERVISOR)){
				int c = expect().given().cookie(getSuperVisorCookie()).when().get(string).peek().andReturn().getStatusCode();
				System.out.println(c+string);
				assertTrue(c==202||c==200||c==204||c==405);
			}
			if(userType.equals(UserType.WORKER)){
				int c =expect().given().cookie(getWorkerCookie()).when().get(string).peek().andReturn().getStatusCode();
				System.out.println(c+string);
				assertTrue(c==202||c==200||c==204||c==405);
			}
		}
		
			
		//MANAGER IS SUPERUSER
		if(!l.contains(UserType.SUPERVISOR)){
			expect().statusCode(403).given().cookie(getSuperVisorCookie()).when().get(string);	
		}
		if(!l.contains(UserType.WORKER)){
			expect().statusCode(403).given().cookie(getWorkerCookie()).when().get(string);	
		}
		
	}
	private Cookie getWorkerCookie() {
		
		User u=new User();
		u.setUserType(UserType.WORKER);
		SecurityToken t=new SecurityToken(u, "worker");
		LoginRestHelper.getMap().put(new Date(), t);
		
		return new Cookie.Builder(SecurityInterceptor.SESSION_COOKIE, "worker").build();
	}
	private Cookie getSuperVisorCookie() {
		
		User u=new User();
		u.setUserType(UserType.SUPERVISOR);
		SecurityToken t=new SecurityToken(u, "supervisor");
		LoginRestHelper.getMap().put(new Date(), t);
		
		return new Cookie.Builder(SecurityInterceptor.SESSION_COOKIE, "supervisor").build();
	}
	public Cookie getManagerCookie() throws Exception {
		
		User u=new User();
		u.setUserType(UserType.MANAGER);
		SecurityToken t=new SecurityToken(u, "manager");
		LoginRestHelper.getMap().put(new Date(), t);
		
		return new Cookie.Builder(SecurityInterceptor.SESSION_COOKIE, "manager").build();
	
	}
	@Before
	public void cleanLogins(){
		LoginRestHelper.getMap().clear();
	}
	@Before
	public void prepareDatabese() throws Exception {
		utx.begin();
		em.joinTransaction();
		try {

			//System.out.println("Dumping old records before test ...");
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
			e.printStackTrace();
			throw e;
		} finally {

			// Enable referential integrity using
			em.createNativeQuery("SET REFERENTIAL_INTEGRITY TRUE"); // lets do
																	// this
																	// allways
			utx.commit();
		}
	}
}
