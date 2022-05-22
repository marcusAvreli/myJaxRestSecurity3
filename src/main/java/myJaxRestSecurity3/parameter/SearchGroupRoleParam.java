package myJaxRestSecurity3.parameter;

import myJaxRestSecurity3.enums.Role;



public class SearchGroupRoleParam 
{
	private String group = null;
	private String user = null;
	private Role role = null;
	private int pageNumber = 1;
	private int resultsByPage = 50;
	
	public SearchGroupRoleParam()
	{}

	public int getPageNumber() {
		return pageNumber;
	}

	public void setPageNumber(int pageNumber) {
		this.pageNumber = pageNumber;
	}

	public int getResultsByPage() {
		return resultsByPage;
	}

	public void setResultsByPage(int resultsByPage) {
		this.resultsByPage = resultsByPage;
	}

	public String getGroup()
	{
		return group;
	}

	public void setGroup(String group)
	{
		this.group = group;
	}

	public String getUser() {
		return user;
	}

	public void setUser(String user) {
		this.user = user;
	}
	
	public Role getRole() {
		return role;
	}

	public void setRole(Role role) {
		this.role = role;
	}
	
}