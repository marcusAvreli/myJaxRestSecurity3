package myJaxRestSecurity3.response;

import javax.xml.bind.annotation.XmlRootElement;

@XmlRootElement
public class KeyResponse extends BaseResponse 
{
	private String key;

	public String getKey() {
		return key;
	}

	public void setKey(String key) {
		this.key = key;
	}
	
}