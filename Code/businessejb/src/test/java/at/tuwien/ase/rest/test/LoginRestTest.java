package at.tuwien.ase.rest.test;

import static com.jayway.restassured.RestAssured.expect;
import static com.jayway.restassured.RestAssured.given;
import static org.hamcrest.Matchers.equalTo;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

import java.util.Date;
import java.util.HashMap;
import java.util.Map.Entry;

import javax.ejb.EJB;

import org.jboss.arquillian.junit.Arquillian;
import org.junit.Test;
import org.junit.runner.RunWith;

import at.tuwien.ase.dao.UserDaoInterface;
import at.tuwien.ase.model.User;
import at.tuwien.ase.model.UserType;
import at.tuwien.ase.rest.LoginRest;
import at.tuwien.ase.rest.security.SecurityInterceptor;
import at.tuwien.ase.rest.security.SecurityToken;

import com.jayway.restassured.response.Response;

@RunWith(Arquillian.class)
public class LoginRestTest extends TestSuiteRest {

	public static  class LoginRestHelper extends LoginRest{
		public static HashMap<Date, SecurityToken> getMap(){
			return sessionMap;
		}
	}

	@EJB
	UserDaoInterface userDao;
	@Test
	public void testGetSecurityTokenOrNull() throws Exception {
		
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

		assertNotNull(u.getId());
		

		utx.commit();
		// check rest
		Response r = given().expect().statusCode(200).body("id", equalTo(u.getId().intValue())).when()
				.get("http://localhost:8080/web/rest/LoginRest/login/" + u.getUsername() + "/" + u.getPassword());
		String cookie = r.getCookie(SecurityInterceptor.SESSION_COOKIE).replace("\"", "");
		assertEquals(null, LoginRest.getSecurityTokenOrNull("WRONG SESSION"));
		assertNotNull(LoginRest.getSecurityTokenOrNull(cookie));
		for ( Entry<Date, SecurityToken> s : LoginRestHelper.getMap().entrySet()) {
			System.out.println(s.getValue().getUser().getForname());
		}
		assertEquals(1, LoginRestHelper.getMap().entrySet().size());
		Date d = LoginRestHelper.getMap().keySet().iterator().next();
		SecurityToken token = LoginRestHelper.getMap().get(d);
		
		LoginRestHelper.getMap().clear();
		assertEquals(0, LoginRestHelper.getMap().entrySet().size());
		LoginRestHelper.getMap().put(new Date(System.currentTimeMillis()-(SecurityInterceptor.SESSION_VALIDITY*1000L)-1), token);
		assertEquals(1, LoginRestHelper.getMap().entrySet().size());
		assertEquals(null,LoginRest.getSecurityTokenOrNull(cookie));
		assertEquals(0, LoginRestHelper.getMap().entrySet().size());
		
	}

	@Test
	public void testSuccessfulLogin() throws Exception{
		
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

		assertNotNull(u.getId());
		

		utx.commit();
		// check rest
		given().cookie(getManagerCookie()).expect().statusCode(200).body("id", equalTo(u.getId().intValue())).when()
				.get("http://localhost:8080/web/rest/LoginRest/login/" + u.getUsername() + "/" + u.getPassword());
	
	}
	
	@Test
	public void testUnSuccessfulLogin() throws Exception{
		
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

		assertNotNull(u.getId());
		
		utx.commit();
		// check rest
		given().cookie(getManagerCookie()).expect().statusCode(403).when()
				.get("http://localhost:8080/web/rest/LoginRest/login/xxx/yyy");
	}
}
