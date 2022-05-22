package myJaxRestSecurity3.parameter;

import java.util.Date;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlTransient;
import javax.xml.bind.annotation.adapters.XmlJavaTypeAdapter;

import myJaxRestSecurity3.enums.Language;



@XmlRootElement(name = "newUser")
public class CreateUserParam {

	@XmlTransient
	private Date birthDate;

	@XmlTransient
	private String email;

	@XmlTransient
	private String firstName;

	@XmlTransient
	private String lastName;

	@XmlTransient
	private Language language;

	@XmlTransient
	private String password;

	public CreateUserParam() {

	}

	@XmlElement
	//@XmlJavaTypeAdapter(DateConverter.class)
	public Date getBirthDate() {
		return birthDate;
	}

	public void setBirthDate(Date birthDate) {
		this.birthDate = birthDate;
	}

	@XmlElement
	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	@XmlElement
	public String getFirstName() {
		return firstName;
	}

	public void setFirstName(String firstName) {
		this.firstName = firstName;
	}

	@XmlElement
	public Language getLanguage() {
		return language;
	}

	public void setLanguage(Language language) {
		this.language = language;
	}

	@XmlElement
	public String getLastName() {
		return lastName;
	}

	public void setLastName(String lastName) {
		this.lastName = lastName;
	}

	@XmlElement
	public String getPassword() {
		return password;
	}

	public void setPassword(String password) {
		this.password = password;
	}

	public boolean isValid() {

	/*	if(!DBManager.validateEmail(email))
			return false;
*/
		Pattern p = Pattern.compile(".+@.+\\.[a-z]+");
		Matcher m = p.matcher(this.email);

		if (this.birthDate != null 
				&& this.email != null && !this.email.isEmpty() && m.matches()
				&& !this.firstName.isEmpty() && this.firstName != null
				&& !this.lastName.isEmpty() && this.lastName != null
				//&& this.language != null 
				&& this.password != null && this.password.length() >= 6) {
			return true;
		}
		return false;
	}
}