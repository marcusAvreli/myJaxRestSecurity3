package myJaxRestSecurity3.facade;

import java.util.HashMap;
import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.Persistence;
import javax.persistence.Query;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Root;
import javax.ws.rs.WebApplicationException;
import javax.ws.rs.core.Response;

import org.apache.log4j.LogManager;
import org.apache.log4j.Logger;

import myJaxRestSecurity3.facade.JpaResultHelper;
import myJaxRestSecurity3.exceptions.AuthenticationException;
import myJaxRestSecurity3.manager.security.SecurityManager;
import myJaxRestSecurity3.model.Session;
import myJaxRestSecurity3.model.User;
import myJaxRestSecurity3.parameter.SearchUserParam;

public class Facade {
	private static final Logger logger = LogManager.getLogger(Facade.class);
	private EntityManagerFactory emf = Persistence.createEntityManagerFactory("jbd-pu");

	public Facade(EntityManagerFactory emf) {
		this.emf = emf;
	}

	private EntityManager getEntityManager() {
		return emf.createEntityManager();
	}

	public User getUser(String userKey) {
		if (userKey == null)
			return null;

		EntityManager pm = emf.createEntityManager();
		User user = null;

		try {
			// pm.setDetachAllOnCommit(true);
			CriteriaBuilder cb = pm.getCriteriaBuilder();
			CriteriaQuery<User> cq = cb.createQuery(User.class);
			Root<User> userRoot = cq.from(User.class);
			cq.where(cb.equal(userRoot.get("firstName"), userKey));
			Query q = pm.createQuery(cq);
			
			List<User> users = (List<User>) q.getResultList();
			user = users.get(0);
		} catch (Exception e) {
			throw new WebApplicationException(Response.Status.NOT_FOUND);
		} finally {
			pm.close();
		}

		return user;
	}

	public User saveUser(User user) {
		logger.info("saving user");

		logger.info("IsAdmin" + user.isAdmin());
		logger.info("CreationDate:" + user.getCreationDate());
		logger.info("Email:" + user.getEmail());
		logger.info("password" + user.getPassword());
		logger.info("BirthDate" + user.getBirthDate());
		logger.info("Language:" + user.getLanguage());
		logger.info("LoginAttempt:" + user.getLoginAttempt());

		EntityManager pm = emf.createEntityManager();
		pm.getTransaction().begin();
		logger.info("saving user1");

		try {
			// pm.setDetachAllOnCommit(true);
			pm.persist(user);
			pm.getTransaction().commit();
			// pm.flush();
			logger.info("user save successfully");
		} catch (Exception e) {
			logger.info("exception");
			throw new WebApplicationException(Response.Status.NOT_FOUND);
		} finally {
			logger.info("connection closed");
			pm.close();
		}
		logger.info("saving finished");
		return user;
	}

	public Session saveSession(Session session) {
		logger.info("save session STARTED");
		/*
		 * logger.info("IsAdmin"+user.isAdmin());
		 * logger.info("CreationDate:"+user.getCreationDate());
		 * logger.info("Email:"+user.getEmail());
		 * logger.info("password"+user.getPassword());
		 * logger.info("BirthDate"+user.getBirthDate());
		 * logger.info("Language:"+user.getLanguage());
		 * logger.info("LoginAttempt:"+user.getLoginAttempt());
		 */
		EntityManager pm = emf.createEntityManager();
		pm.getTransaction().begin();
		logger.info("saving session");

		try {
			// pm.setDetachAllOnCommit(true);
			pm.persist(session);
			pm.getTransaction().commit();
			// pm.flush();
			logger.info("session save successfully");
		} catch (Exception e) {
			logger.info("exception");
			throw new WebApplicationException(Response.Status.NOT_FOUND);
		} finally {
			logger.info("connection closed");
			pm.close();
		}
		logger.info("saving finished");
		return session;

	}

	public User updateUser(User user) {
		logger.info("updateUser user");

		logger.info("IsAdmin" + user.isAdmin());
		logger.info("CreationDate:" + user.getCreationDate());
		logger.info("Email:" + user.getEmail());
		logger.info("password" + user.getPassword());
		logger.info("BirthDate" + user.getBirthDate());
		logger.info("Language:" + user.getLanguage());
		logger.info("LoginAttempt:" + user.getLoginAttempt());

		EntityManager pm = emf.createEntityManager();
		pm.getTransaction().begin();
		logger.info("saving user1");

		try {
			// pm.setDetachAllOnCommit(true);
			pm.merge(user);
			pm.getTransaction().commit();
			// pm.flush();
			logger.info("updateUser successfully");
		} catch (Exception e) {
			logger.info("updateUser exception");
			throw new WebApplicationException(Response.Status.NOT_FOUND);
		} finally {
			logger.info("connection closed");
			pm.close();
		}
		logger.info("updateUser finished");
		return user;
	}

	public void deleteSession(Session session) {
		logger.info("deleteSession START");
		EntityManager pm = emf.createEntityManager();

		try {
			pm.getTransaction().begin();
			logger.info("delete session 1");
			if(!pm.contains(session)) {
				logger.info("does not contains");
				session = pm.merge(session);
			}
			//pm.remove(pm.find(Session.class,session));
		//session = pm.merge(session);
			pm.remove(session);
			pm.getTransaction().commit();
		} catch (Exception e) {
			logger.info("delete session exception occured");
			logger.error("Error occured:",e);
		} finally {
			pm.close();
		}

	}

	@SuppressWarnings("unchecked")
	public List<Session> searchSessions(String userId) {
		logger.info("search session STARTED");
		EntityManager pm = emf.createEntityManager();

		List<Session> list = null;

		CriteriaBuilder cb = pm.getCriteriaBuilder();
		CriteriaQuery<Session> cq = cb.createQuery(Session.class);
		Root<Session> user = cq.from(Session.class);
		cq.where(cb.equal(user.get("userId"), userId));
		Query q = pm.createQuery(cq);
		List<Session> users = (List<Session>) q.getResultList();

		pm.close();

		logger.info("search session FINISHED");
		return users;
	}

	/*
	 * public List<RestaurantDTO> getAllRestaurants() { EntityManager em =
	 * getEntityManager(); try { em.getTransaction().begin();
	 * TypedQuery<RestaurantDTO> tq =
	 * em.createQuery("Select new dto.RestaurantDTO(r) from Restaurant r",
	 * RestaurantDTO.class); List<RestaurantDTO> restaurants = tq.getResultList();
	 * em.getTransaction().commit(); return restaurants; } finally { em.close(); } }
	 */
	@SuppressWarnings("unchecked")
	public Session getSession(String sessionKey) {
		logger.info("get session start");
		if (sessionKey == null || sessionKey.isEmpty())
			return null;

		EntityManager pm = emf.createEntityManager();
		List<Session> sessions = null;
		Session returnValue = null;

		try {
			CriteriaBuilder cb = pm.getCriteriaBuilder();
			CriteriaQuery<Session> cq = cb.createQuery(Session.class);
			Root<Session> session = cq.from(Session.class);
			cq.where(cb.equal(session.get("sessionKey"), sessionKey));
			Query q = pm.createQuery(cq);

			logger.info("=====FIND BY USERNAME=====");
			sessions = (List<Session>) q.getResultList();

			/*
			 * Query query = pm.newQuery(Session.class); pm.setDetachAllOnCommit(true);
			 * query.setFilter("sessionKey == sessionKeyParam");
			 * query.declareParameters("String sessionKeyParam");
			 * query.declareImports("import com.google.appengine.api.datastore.Key;");
			 * 
			 * sessions = (List<Session>) query.execute(sessionKey);
			 */
			if (sessions.size() == 1) {
				logger.info("=======session found===========");
				returnValue = sessions.get(0);
			}
		} catch (Exception e) {
			throw new WebApplicationException(Response.Status.NOT_FOUND);
		} finally {
			logger.info("get session close connection");
			pm.close();
		}
		logger.info("get session finish");
		return returnValue;
	}

	public User getVeryfiedUser(String username, String password) throws AuthenticationException {
		EntityManager em = emf.createEntityManager();
		User user;
		try {
			user = em.find(User.class, username);
			/*
			 * if (user == null || !user.verifyPassword(password)) { throw new
			 * AuthenticationException("Invalid user name or password"); }
			 */
		} finally {
			em.close();
		}
		return user;
	}
	/*
	 * public List<MenuItemDTO> getMenuItems(int id) { EntityManager em =
	 * getEntityManager(); try { Restaurant restaurant = em.find(Restaurant.class,
	 * id); TypedQuery<MenuItemDTO> tq = em.
	 * createQuery("Select new dto.MenuItemDTO(m) from MenuItem m where m.restaurant=:rest"
	 * , MenuItemDTO.class); tq.setParameter("rest", restaurant); List<MenuItemDTO>
	 * menu = tq.getResultList(); return menu; } finally { em.close(); } }
	 */
	/*
	 * public List<RestaurantDTO> getMyRestaurants(String owner) { EntityManager em
	 * = getEntityManager(); try { User ownerOfRestaurants = em.find(User.class,
	 * owner); TypedQuery<RestaurantDTO> tq = em.
	 * createQuery("Select new dto.RestaurantDTO(r) from Restaurant r where r.owner=:owner"
	 * , RestaurantDTO.class); tq.setParameter("owner", ownerOfRestaurants);
	 * List<RestaurantDTO> list = tq.getResultList(); return list; } finally {
	 * em.close(); } }
	 */

	/*
	 * public User addNewUser(User user) throws AuthenticationException {
	 * EntityManager em = emf.createEntityManager(); try {
	 * em.getTransaction().begin(); em.persist(user); em.getTransaction().commit();
	 * } finally { em.close(); } return user; }
	 * 
	 * public Long getNumberOfUsers() { EntityManager em = getEntityManager(); try {
	 * Query q = em.createQuery("select count(u) from User u"); Long count = (Long)
	 * q.getSingleResult(); return count; } finally { em.close(); } }
	 */

	@SuppressWarnings("unchecked")
	public List<User> searchUsers(SearchUserParam param) {
		EntityManager pm = emf.createEntityManager();
		pm.getTransaction().begin();

		logger.info("searchuser START");

		CriteriaBuilder cb = pm.getCriteriaBuilder();
		CriteriaQuery<User> cq = cb.createQuery(User.class);
		Root<User> user = cq.from(User.class);
		cq.where(cb.equal(user.get("email"), param.getEmail()));
		Query q = pm.createQuery(cq);
		List<User> users = (List<User>) q.getResultList();

		pm.close();
		logger.info("searchuser FINISH");

		return users;
	}

	public boolean validateEmail(String email) {

		SearchUserParam p = new SearchUserParam();
		p.setEmail(email);
		List<User> users = searchUsers(p);
		if (users == null) {
			return true;
		}

		return users.size() != 0 ? false : true;
	}

}
