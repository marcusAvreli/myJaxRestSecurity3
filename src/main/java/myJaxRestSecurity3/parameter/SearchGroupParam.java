package myJaxRestSecurity3.parameter;



public class SearchGroupParam 
{
	//private Key question = null;
	//private Key tournament = null;
	private int pageNumber = 1; 
	private int resultsByPage = 50;
	
	public SearchGroupParam()
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

/*	public Key getQuestion()
	{
		return question;
	}

	public void setQuestion(Key question)
	{
		this.question = question;
	}
	
	public Key getTournament() {
		return tournament;
	}*/

	/*public void setTournament(Key tournament) {
		this.tournament = tournament;
	}*/
	
}