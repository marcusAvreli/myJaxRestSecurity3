package myJaxRestSecurity3.response;

import javax.persistence.Transient;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;

@XmlRootElement
public class BaseResponse {

	@Transient
	private String error;

	@XmlElement
	public boolean isCompleted() {
		return error == null || error.isEmpty();
	}

	@XmlElement
	public String getError() {
		return error;
	}

	public void setError(String error) {
		this.error = error;
	}

}
