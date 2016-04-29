package at.tuwien.ase.rest.security;

import java.lang.reflect.Method;
import java.net.HttpURLConnection;
import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;

import javax.annotation.security.DenyAll;
import javax.annotation.security.PermitAll;
import javax.annotation.security.RolesAllowed;
import javax.interceptor.AroundInvoke;
import javax.interceptor.InvocationContext;
import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.ws.rs.WebApplicationException;

import org.jboss.resteasy.spi.ResteasyProviderFactory;

import at.tuwien.ase.model.UserType;
import at.tuwien.ase.rest.LoginRest;

public class SecurityInterceptor{
	public static final String SESSION_COOKIE = "SESSION";
	public static final int SESSION_VALIDITY = 10800;
	 @AroundInvoke
	   public Object intercept(InvocationContext ctx) throws Exception
	   {

		 Method method = ctx.getMethod();
		 HttpServletRequest request = ResteasyProviderFactory.getContextData(HttpServletRequest.class); 

		// Access allowed for all
		if (method.isAnnotationPresent(PermitAll.class)) {
			return ctx.proceed();
		}
		// Access denied for all
		if (method.isAnnotationPresent(DenyAll.class)) {
			
			  throw new WebApplicationException(HttpURLConnection.HTTP_FORBIDDEN);

		}
		if(request.getCookies()==null){
			  throw new WebApplicationException(HttpURLConnection.HTTP_FORBIDDEN);
		}
		
		Cookie cookie =null;
		for (Cookie c : request.getCookies()) {
			if(c.getName().equals(SESSION_COOKIE)){
				cookie=c;
				break;
			}
			
		}
		
		
		if(cookie==null){
			throw new WebApplicationException(HttpURLConnection.HTTP_FORBIDDEN);
		}
		SecurityToken token = LoginRest.getSecurityTokenOrNull(cookie.getValue());
		if (token == null) {
			throw new WebApplicationException(HttpURLConnection.HTTP_FORBIDDEN);
		}
		

		 UserType userRole = token.getUser().getUserType();
		if(userRole.equals(UserType.MANAGER)){///MANAGER IS THE SUPERUSER 
			return ctx.proceed();
		}
		
		if (method.isAnnotationPresent(RolesAllowed.class)) {
			
			RolesAllowed rolesAnnotation = method.getAnnotation(RolesAllowed.class);
			Set<String> rolesSet = new HashSet<String>(Arrays.asList(rolesAnnotation.value()));
	
			if(rolesSet.contains(userRole.name())){
				return ctx.proceed();
			}
		}
		throw new WebApplicationException(HttpURLConnection.HTTP_FORBIDDEN);
	}


}