package at.wberger.springredisdemo.events;

public class RequestUserByNameEvent {
	
	private String ws2_a;
	
	private String userName;

	public String getUserName() {
		return userName;
	}

	public void setUserName(String userName) {
		this.userName = userName;
	}

	public RequestUserByNameEvent(String userName) {
		super();
		this.userName = userName;
	}

}
