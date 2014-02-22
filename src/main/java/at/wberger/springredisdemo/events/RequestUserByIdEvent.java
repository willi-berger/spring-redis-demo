package at.wberger.springredisdemo.events;

public class RequestUserByIdEvent {
	
	private String userId;

	public String getUserId() {
		return userId;
	}

	public void setUserId(String userId) {
		this.userId = userId;
	}

	public RequestUserByIdEvent(String userId) {
		super();
		this.userId = userId;
	}
	

}
