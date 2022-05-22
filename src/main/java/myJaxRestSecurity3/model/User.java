package myJaxRestSecurity3.model;

import java.util.ArrayList;
import java.util.Date;
import java.util.Iterator;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Table;

import javax.xml.bind.annotation.XmlTransient;
import javax.xml.bind.annotation.adapters.XmlJavaTypeAdapter;

import org.hibernate.annotations.GenericGenerator;

import myJaxRestSecurity3.enums.Language;
import myJaxRestSecurity3.manager.security.BCrypt;


@Entity
@Table(name = "security3_user")
public class User {

	
	@Id
    @GeneratedValue(generator = "UUID")
    @GenericGenerator(
        name = "UUID",
    strategy = "org.hibernate.id.UUIDGenerator"
    )
@Column(updatable = false, nullable = false)
private String id;
	private Date birthDate;

	
	private Date creationDate = new Date();

	
	private String email;

	
	private String firstName;

	
	private String lastName;

	
	private Language language;

	
	
	private Date lastLogin = new Date();

	
	private String password;

	
	private boolean isAdmin = false;

	
	private int loginAttempt = 0;

	
	private Date lastLoginAttempt = null;

	
	//private List<Key> teams = new ArrayList<Key>();

	public User() 
	{

	}
public String getId() {
	return id;
}
	
	public Date getBirthDate() {
		return birthDate;
	}

	public void setBirthDate(Date birthDate) {
		this.birthDate = birthDate;
	}

	
	public Date getCreationDate() {
		return creationDate;
	}

	public void setCreationDate(Date creationDate) {
		this.creationDate = creationDate;
	}

	
	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email.toLowerCase();
	}

	
	public String getFirstName() {
		return firstName;
	}

	public void setFirstName(String firstName) {
		this.firstName = firstName;
	}

	
	public Language getLanguage() {
		return language;
	}

	public void setLanguage(Language language) {
		this.language = language;
	}

	
	public Date getLastLogin() {
		return lastLogin;
	}

	public void setLastLogin(Date lastLogin) {
		this.lastLogin = lastLogin;
	}

	
	public String getLastName() {
		return lastName;
	}

	public void setLastName(String lastName) {
		this.lastName = lastName;
	}

	
	public String getPassword() {
		return password;
	}

	public void setPassword(String password) {
		this.password = BCrypt.hashpw(password, BCrypt.gensalt());
	}

	
	public boolean isAdmin() {
		return isAdmin;
	}

	public void setAdmin(boolean isAdmin) {
		this.isAdmin = isAdmin;
	}

	
	public int getLoginAttempt() {
		return loginAttempt;
	}

	public void setLoginAttempt(int loginAttempt) {
		this.loginAttempt = loginAttempt;
	}

	
	public Date getLastLoginAttempt() {
		return lastLoginAttempt;
	}

	public void setLastLoginAttempt(Date lastLoginAttempt) {
		this.lastLoginAttempt = lastLoginAttempt;
	}

	

	/*public boolean addTeam(Team team)
	{
		if (team.getKey() == null)
			DBManager.save(team);

		if (this.key == null)
			DBManager.save(this);

		if(getGroups().contains(team.getGroup()))
		{
			if(!this.teams.contains(team.getKey())){
				this.teams.add(team.getKey());
				DBManager.save(team);
			}
			if(!team.getUsers().contains(this.getKey())){
				team.getUsers().add(this.getKey());
				DBManager.save(this);
			}
			
			return true;
		}
		return false;
	}*/

/*	public void removeTeam(Team team)
	{
		if (this.teams.contains(team.getKey()))
		{
			this.teams.remove(team.getKey());
			team.getUsers().remove(this.getKey());

			DBManager.save(team);
			DBManager.save(this);
		}
	}*/

	/*public void addGroupRoles(GroupRole groupRole)
	{
		if (groupRole.getKey() == null)
			//DBManager.save(groupRole);

		if (this.key == null)
			DBManager.save(this);

		//		this.groupRoles.add(groupRole.getKey());
		groupRole.setUser(this.getKey());

		DBManager.save(groupRole);
		DBManager.save(this);
	}*/


/*	@XmlTransient
	public List<Key> getGroups()
	{
		List<Key> groupKey = new ArrayList<Key>();
		SearchGroupRoleParam groupRoleParam = new SearchGroupRoleParam();
		groupRoleParam.setUser(this.getKey());

		for(GroupRole groupRole: DBManager.searchGroupRole(groupRoleParam))
		{
			groupKey.add(groupRole.getGroup());
		}
		return groupKey;
	}
*/
/*	@Override
	public void delete()
	{
		SearchTeamParam searchParam = new SearchTeamParam();
		searchParam.setUser(this.getKey());

		for(Team t : DBManager.searchTeams(searchParam))
			t.removeUser(this);

		SearchGroupRoleParam param = new SearchGroupRoleParam();
		param.setUser(this.getKey());
		for(GroupRole gr : DBManager.searchGroupRole(param))
		{
			//this.getGroupRoles().remove(gr.getKey());
			gr.delete();
		}

		DBManager.save(this);

		DBManager.delete(this.getKey());
		key = null;
	}
*/
/*	@Override
	public boolean isValid() {
		if(this.key == null){
			//if(!DBManager.validateEmail(email))
			//	return false;
		}
		
		Pattern p = Pattern.compile(".+@.+\\.[a-z]+");
		Matcher m = p.matcher(this.email);

		if (this.birthDate != null 
				&& this.email != null && !this.email.isEmpty() && m.matches()
				&& !this.firstName.isEmpty() && this.firstName != null
				&& !this.lastName.isEmpty() && this.lastName != null
				&& this.language != null 
				&& creationDate != null
				&& this.password != null && this.password.length() >= 6) {
			return true;
		}
		return false;
		
	}
*/
	@Override 
	public String toString() {
		return "firstname: "+firstName+" lastname:"+lastName;
	}

}