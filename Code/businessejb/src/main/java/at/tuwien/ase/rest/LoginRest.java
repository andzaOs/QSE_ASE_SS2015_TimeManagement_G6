package at.tuwien.ase.rest;

import java.security.SecureRandom;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;

import javax.annotation.security.DeclareRoles;
import javax.annotation.security.PermitAll;
import javax.ejb.EJB;
import javax.ejb.Singleton;
import javax.interceptor.Interceptors;
import javax.persistence.PersistenceContext;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.NewCookie;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.Response.ResponseBuilder;
import javax.ws.rs.core.Response.Status;

import org.hibernate.SQLQueryResultMappingBuilder.ReturnsHolder;
import org.hibernate.Session;
import org.jboss.resteasy.util.Base64;

import at.tuwien.ase.dao.UserDaoInterface;
import at.tuwien.ase.model.User;
import at.tuwien.ase.rest.security.SecurityInterceptor;
import at.tuwien.ase.rest.security.SecurityToken;

@Singleton
@Path("/LoginRest")
public class LoginRest implements LogingRestI {
	static SecureRandom  random = new SecureRandom();
	static protected HashMap<Date, SecurityToken> sessionMap = new HashMap<Date, SecurityToken>();


	ProxyRemover<User> proxyRemover = new ProxyRemover<User>();

	@PersistenceContext
	Session session;

	@EJB
	// inject
	UserDaoInterface userDao;

	
	@Override
	@GET
	@Path("login/{username}/{password}")
	@Produces("application/json")	
	public Response login(@PathParam("username") String username, @PathParam("password") String password) throws Exception {

		User u = null;
		u = userDao.getByCredentials(username, password);
		if (u != null) {
			ResponseBuilder r = Response.ok((User) ProxyRemover.cleanFromProxies(session,u), MediaType.APPLICATION_JSON);
			String sesId = generateSession();
			r.cookie(new NewCookie(SecurityInterceptor.SESSION_COOKIE, sesId, "/", null, "", SecurityInterceptor.SESSION_VALIDITY, false));
			addSession(new SecurityToken(u,sesId));
			return r.build();
		}

		return Response.status(Status.FORBIDDEN).build();

	}

	/**
	 * @param sessionId
	 * @return the session Token asociated to the session Id  this method lazily removes  old sessions from  sessionMap
	 */
	public synchronized static SecurityToken getSecurityTokenOrNull(String sessionId) {
		ArrayList<Date> remove = new ArrayList<Date>();
		SecurityToken sesToken = null;
		
		for ( Date d : sessionMap.keySet()) {
			if((System.currentTimeMillis()-d.getTime())>(SecurityInterceptor.SESSION_VALIDITY*1000L)){
				remove.add(d);
				
			}else if(sessionMap.get(d).getSesId().equals(sessionId)){
				sesToken=sessionMap.get(d);
				
			}
		}
		
		if(remove.size()>0){
				for (Date d  : remove) {
					sessionMap.remove(d);
				}
			
		}
		
		return sesToken;
	}

	private  static synchronized void addSession(SecurityToken token) {

		sessionMap.put(new Date(),token);

	}

	private static String generateSession() {
		byte[] s = new byte[512];
		random.nextBytes(s);

		return Base64.encodeBytes(s);
	}

}
