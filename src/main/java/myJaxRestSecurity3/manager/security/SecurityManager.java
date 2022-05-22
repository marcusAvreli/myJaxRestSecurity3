package myJaxRestSecurity3.manager.security;

import javax.ws.rs.core.Context;

import com.sun.jersey.core.util.Base64;

import com.sun.jersey.spi.container.ContainerRequest;
import com.sun.jersey.spi.container.ContainerRequestFilter;

import myJaxRestSecurity3.facade.Facade;
import myJaxRestSecurity3.model.Session;
import myJaxRestSecurity3.model.User;
import myJaxRestSecurity3.parameter.SearchUserParam;

import java.security.Principal;
import java.util.Date;
import java.util.List;

import javax.persistence.EntityManagerFactory;
import javax.persistence.Persistence;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.ws.rs.core.SecurityContext;
import javax.ws.rs.core.UriInfo;

import org.apache.log4j.LogManager;
import org.apache.log4j.Logger;

public class SecurityManager implements ContainerRequestFilter {
	private static final Logger logger = LogManager.getLogger(SecurityManager.class);
	@Context
	UriInfo uriInfo;

	@Context
	HttpServletRequest request;

	@Context
	HttpServletResponse response;

	public ContainerRequest filter(ContainerRequest request) {
		logger.info("filter START");
		User user = null;
		try {
			if(uriInfo.getRequestUri().getPath().contains("users/logout")){
				user = validateSession();
				if(user != null) {
					request.setSecurityContext(new Authorizer(user));
					return request;
				}
			}
			//user = new User();
			user = validateUserAuthentication();
			logger.info("filter");
			request.setSecurityContext(new Authorizer(user));
			return request;

		} catch (Exception e) {
			logger.info("filter EXCEPTION");
			request.setSecurityContext(new Authorizer(null));
		}
		logger.info("filter FINISH");
		return request;
	}

	private Session getSession(){
		logger.info("get session start");
		Session session = null;
		EntityManagerFactory emf = Persistence.createEntityManagerFactory("jbd-pu");
		Facade facade = new Facade(emf);
		String sessionId = request.getHeader("session-id");
		logger.info("get session:"+sessionId);
		if (sessionId != null && !sessionId.isEmpty()) {
			session = facade.getSession(sessionId);
		}
		logger.info("get session finish");
		return session;
	}

	private User validateSession(){
		logger.info("validate session start");
		User user = null;
		EntityManagerFactory emf = Persistence.createEntityManagerFactory("jbd-pu");
		Facade facade = new Facade(emf);
		logger.info("validate session 1");
		Session session = getSession();
		
		logger.info("validate session2");
		if(session != null && session.isActive()){
			logger.info("validate session3");
			response.addHeader("session", "active");
			//ExceptionLogger.getLogger().warning("Session : " + session.getSessionKey());
			if(!session.isExpired()) {
				logger.info("validate session4"+session.getUserId());
				user = facade.getUser(session.getUserId());
				logger.info(user);
			}
		}
		logger.info("validate session finish");
		return user;
	}

	private User validateUserAuthentication(){
		Date date = new Date();
		SearchUserParam p = new SearchUserParam();
		List<User> userList = null;
		User user = null;

		String header = request.getHeader("Authorization");
		if(header == null || header.isEmpty())
			return null;

		header = header.substring("Basic ".length());
		String[] creds = new String(Base64.base64Decode(header)).split(":");
		String username = creds[0];
		String password = creds[1];
		logger.info("Username: "+username);
		logger.info("password: "+password);
		if(!username.isEmpty() && !password.isEmpty()) {
			logger.info("validateUserAuthentication1");
			EntityManagerFactory emf = Persistence.createEntityManagerFactory("jbd-pu");
			Facade facade = new Facade(emf);
			p.setEmail(username.toLowerCase());

			userList = facade.searchUsers(p);
			if (userList == null || userList.isEmpty()) {
				return null;
			}
			user = userList.get(0);
			logger.info("validateUserAuthentication2"+user);
			Long minutesAgo = new Long(3);
			Date dateIn_X_MinAgo = new Date(date.getTime() - minutesAgo * 60 * 1000);

			if (user.getLastLoginAttempt() == null) {
				user.setLastLoginAttempt(date);
			}

			if (user.getLoginAttempt() >= 8 && user.getLastLoginAttempt().after(dateIn_X_MinAgo)) {
				return null;
			}
			logger.info("validateUserAuthentication3");
			logger.info("CuurentLoginAttempt:"+user.getLoginAttempt());
			user.setLastLoginAttempt(date);
			user.setLoginAttempt(user.getLoginAttempt() + 1);
			//facade = new Facade(emf);
			facade.updateUser(user);
			logger.info("validateUserAuthentication3.1");
			if (BCrypt.checkpw(password, user.getPassword())) {
				logger.info("validateUserAuthentication4");
				user.setLastLogin(date);
				user.setLoginAttempt(0);
				facade = new Facade(emf);
				facade.updateUser(user);

				Session userSession = getSession();
				if(userSession != null){
					//SessionManager.extendSessionEndTime(userSession);
				}
				logger.info("validateUserAuthentication5");
				return user;
			}
		}
		logger.info("validateUserAuthentication6");
		return null;
	}

	/**
	 * SecurityContext used to perform authorization checks.
	 */
	public class Authorizer implements SecurityContext {

		private Principal principal = null;

		public Authorizer(final User user) {
			if (user != null) {
				logger.info("security context user not null");
				logger.info("user:"+user.toString());
				//ExceptionLogger.getLogger().warning("User : " + user.getEmail());
				principal = new Principal() {
					public String getName() {
						return user.getFirstName();
					}
				};
			}else {
				logger.info("security context user is null");
			}
		}

		public Principal getUserPrincipal() {
			return principal;
		}

		public boolean isSecure() {
			return "https".equals(uriInfo.getRequestUri().getScheme());
		}

		public String getAuthenticationScheme() {
			if (principal == null) {
				return null;
			}
			return SecurityContext.FORM_AUTH;
		}

		@Override
		public boolean isUserInRole(String role$groupKey) {
			return false;
		}
	}

}
