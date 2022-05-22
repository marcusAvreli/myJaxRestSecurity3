package myJaxRestSecurity3.manager.security;

import java.util.Collections;
import java.util.Comparator;
import java.util.Date;
import java.util.List;

import javax.persistence.EntityManagerFactory;
import javax.persistence.Persistence;

import org.apache.log4j.LogManager;
import org.apache.log4j.Logger;

import myJaxRestSecurity3.facade.Facade;
import myJaxRestSecurity3.model.Session;

public class SessionManager {

	private static final Logger logger = LogManager.getLogger(SessionManager.class);
	private static int maxSessionQtyByUser = 5;

	public static Session getLatestSession(String userId) {
		EntityManagerFactory emf = Persistence.createEntityManagerFactory("jbd-pu");
		Facade facade = new Facade(emf);
		Session latestSession = null;
		List<Session> sessionsList =  facade.searchSessions(userId);

		if (!sessionsList.isEmpty()){
			//clean old sessions
			for(Session s : sessionsList) {
				if (latestSession == null) {
					latestSession = s;
				}
				else if(latestSession.getCreateTime().before(s.getCreateTime())) {
					latestSession = s;
				}
			}
		}

		return latestSession;
	}

	public  static Session createNewSession(String userId){
		logger.info("Create New Session STARTED");
		Session currentSession = new Session(userId);
		EntityManagerFactory emf = Persistence.createEntityManagerFactory("jbd-pu");
		Facade facade = new Facade(emf);
		facade.saveSession(currentSession);
		cleanUserSessions(userId);
		logger.info("Create New Session FINISHED");
		return getLatestSession(userId);
	}

	public static void cleanUserSessions(String userId){
		EntityManagerFactory emf = Persistence.createEntityManagerFactory("jbd-pu");
		Facade facade = new Facade(emf);
		List<Session> sessionsList = facade.searchSessions(userId);

		if (!sessionsList.isEmpty()){
			//clean old sessions
			for(Session s : sessionsList) {
				if (!s.isActive()) {
					//s.delete();
				}
			}
		}

		sessionsList = facade.searchSessions(userId);

		if(sessionsList.size() >= maxSessionQtyByUser) {
			Collections.sort(sessionsList, new Comparator<Session>() {
				public int compare(Session s1, Session s2) {
					return s2.getCreateTime().compareTo(s1.getCreateTime());
				}
			});
			
			for(int i = 0; i < sessionsList.size(); i++){
				//if(i >= maxSessionQtyByUser)
				//sessionsList.get(i).delete();;
			}
		}
	}
	
	public static void extendSessionEndTime(Session s){
		//extend session endtime if required
		EntityManagerFactory emf = Persistence.createEntityManagerFactory("jbd-pu");
		Facade facade = new Facade(emf);
		Date now = new Date();
		if(s != null && s.isExpired()) {
			s.setEndTime(new Date(now.getTime() + 60*60*1000));
			facade.saveSession(s);
		}
	}

	public static void closeUserSession(String userKey, String sessionKey) {
		logger.info("closeUserSession START");
		EntityManagerFactory emf = Persistence.createEntityManagerFactory("jbd-pu");
		Facade facade = new Facade(emf);
		List<Session> sessionsList = facade.searchSessions(userKey);
		Session session = facade.getSession(sessionKey);
		if (!sessionsList.isEmpty() && session != null) {
			//clean all sessions
			logger.info("closeUserSession 1");
			for(Session s : sessionsList) {
				if(s.getSessionKey().equals(session.getSessionKey())) {
					facade = new Facade(emf);
					logger.info("going to delete"+s.getSessionKey());
					facade.deleteSession(s);
				}
			}
		}
		logger.info("closeUserSession FINISH");
	}
}