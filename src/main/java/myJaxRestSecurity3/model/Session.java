package myJaxRestSecurity3.model;

import java.util.Date;
import java.util.UUID;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlTransient;
import javax.xml.bind.annotation.adapters.XmlJavaTypeAdapter;

import org.hibernate.annotations.GenericGenerator;


@Entity
@Table(name = "security3_session")
public class Session {
	
	
	@Id
    @GeneratedValue(generator = "UUID")
    @GenericGenerator(
        name = "UUID",
    strategy = "org.hibernate.id.UUIDGenerator"
    )
@Column(updatable = false, nullable = false)
	private String sessionKey;
	
	
	private String userId = null;
	
	
	private boolean active;
	
	
	private Date createTime;
	
	
	private Date endTime;
	
	public Session() {
		init();
	}
	
	public Session(String userId) {
		this.userId = userId;
		init();
	}
	
	private void init() {
		Date now = new Date();
		Date endTime = new Date(now.getTime() + 60 * 60 * 1000);
		
		this.createTime = now;
		this.active = true;
		this.endTime = endTime;
	}
	
	@XmlElement
	public String getSessionKey() {
		return sessionKey;
	}
	
	
	
	public String getUserId() {
		return userId;
	}
	
	public void setUserId(String userId) {
		this.userId = userId;
	}

	
	public boolean isActive() {
		return active;
	}

	public void setActive(boolean active) {
		this.active = active;
	}

	
	public Date getCreateTime() {
		return createTime;
	}

	public void setCreateTime(Date createTime) {
		this.createTime = createTime;
	}

	
	public Date getEndTime() {
		return endTime;
	}

	public void setEndTime(Date endTime) {
		this.endTime = endTime;
	}

	@XmlElement
	public boolean isExpired() {
		Date now = new Date();
		return false;
		//return this.endTime.before(now);
	}

/*	@Override
	public void delete() {
		DBManager.delete(this.getKey());
		key = null;
	}*/

	
	public boolean isValid() {
		return sessionKey != null && !sessionKey.isEmpty();
	}
}