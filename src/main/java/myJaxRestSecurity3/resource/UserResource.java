package myJaxRestSecurity3.resource;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Properties;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.EntityTransaction;
import javax.persistence.Persistence;
import javax.persistence.Query;
//import javax.mail.Message;
//import javax.mail.Transport;
//import javax.mail.internet.InternetAddress;
//import javax.mail.internet.MimeMessage;
import javax.ws.rs.Consumes;
import javax.ws.rs.DELETE;
import javax.ws.rs.GET;
import javax.ws.rs.HeaderParam;
import javax.ws.rs.POST;
import javax.ws.rs.PUT;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.QueryParam;
import javax.ws.rs.WebApplicationException;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.SecurityContext;

import org.apache.log4j.LogManager;
import org.apache.log4j.Logger;

import myJaxRestSecurity3.facade.Facade;
import myJaxRestSecurity3.manager.security.SecurityManager;
import myJaxRestSecurity3.manager.security.SessionManager;
import myJaxRestSecurity3.model.Session;
import myJaxRestSecurity3.model.User;
import myJaxRestSecurity3.parameter.CreateUserParam;
import myJaxRestSecurity3.response.KeyResponse;


@Path("/users")
public class UserResource {
	private static final Logger logger = LogManager.getLogger(UserResource.class);


	/**
	 * POST rest/users/login
	 * Generation a session for a user to make further calls faster
	 * @param sec Authentication of the user to log in
	 * @return Session of the user
	 */
	@POST
	@Path("/login")
	@Produces({ MediaType.APPLICATION_JSON, MediaType.APPLICATION_XML })
	public Session verifyUserCredentials(@Context SecurityContext sec) {
		logger.info("verifyUserCredentials START");
		Session response = null;
		
		if(sec.getUserPrincipal() != null) {
			logger.info("verifyUserCredentials 1");
			String userKey = (sec.getUserPrincipal().getName());
			logger.info("verifyUserCredentials userKey:"+userKey);
			response = SessionManager.createNewSession(userKey);
		}
		else {
			logger.info("verifyUserCredentials FORBIDDEN");
			throw new WebApplicationException(Response.Status.FORBIDDEN);
		}
		logger.info("verifyUserCredentials FINISH");
		return response;
	}

	/**
	 * POST rest/users/logout
	 * Close a user session
	 * @param sec Authentication of the user to log out
	 * @param sessionKey Session of the user to close
	 */
	@POST
	@Path("/logout")
	@Produces({ MediaType.APPLICATION_JSON, MediaType.APPLICATION_XML })
	public void logout(@Context SecurityContext sec, @HeaderParam("session-id") String sessionKey) {
		logger.info("logout START");
		//logger.info("sessionKey:"+sessionKey);
		if(sec.getUserPrincipal() != null) {
			logger.info("logout 1");
			// Close session
			if(!sessionKey.isEmpty()) {
				logger.info("logout 2");
				try{
					logger.info("logout 3");
					String userKey = (sec.getUserPrincipal().getName());
					logger.info("logout 4");
					SessionManager.closeUserSession(userKey, sessionKey);
					logger.info("logout 5");
				}
				catch(Exception e){
					logger.info("logout NOT_FOUND");
					throw new WebApplicationException(Response.Status.NOT_FOUND);
				}
			}
		}
		else {
			logger.info("logout FORBIDDEN");
			throw new WebApplicationException(Response.Status.FORBIDDEN);
		}
	}

	/**
	 * POST rest/users
	 * Adds a new user to the database
	 * @param user to be added (CreateUserParam)
	 * @return The key of the user created
	 */
	@POST
	@Produces({ MediaType.APPLICATION_JSON, MediaType.APPLICATION_XML })
	@Consumes({ MediaType.APPLICATION_JSON, MediaType.APPLICATION_XML })
	public KeyResponse addUser(CreateUserParam user)
	{
		KeyResponse resp = new KeyResponse();
		try
		{
			if(!user.isValid()) {
				resp.setError("This user cannot be created");
				return resp;
			}
			EntityManagerFactory emf = Persistence.createEntityManagerFactory("jbd-pu");
			Facade facade = new Facade(emf);
			if(!facade.validateEmail(user.getEmail())) {
				resp.setError("An account with this email is already registered");
				return resp;
			}

			Date d = new Date();
			User newUser = new User();
			newUser.setCreationDate(d);
			newUser.setBirthDate(user.getBirthDate());
			newUser.setEmail(user.getEmail());
			newUser.setFirstName(user.getFirstName());
			newUser.setLastName(user.getLastName());
			newUser.setLanguage(user.getLanguage());
			newUser.setLastLogin(null);
			newUser.setPassword(user.getPassword());
			newUser.setAdmin(false);

		//	EntityManagerFactory emf = Persistence.createEntityManagerFactory("jbd-pu");
		//	Facade facade = new Facade(emf);
			facade = new Facade(emf);
			newUser = facade.saveUser(newUser);
			//DBManager.save(newUser);
			resp.setKey(newUser.getId());
		}
		catch(Exception e)
		{
			resp.setError(e.getMessage());
		}

		return resp;
	}


}