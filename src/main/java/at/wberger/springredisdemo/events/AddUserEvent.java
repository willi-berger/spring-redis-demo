package at.wberger.springredisdemo.events;

import at.wberger.springredisdemo.core.domain.User;

public class AddUserEvent {

	// for testing git conflics  -WS-1
	// WS-1 changed
	// WS-1 change again
	private int j;
	// for testing git conflics  -WS-2
	// WS-2 changed i -> j2
	private int j2;
	
	private User newUser;

	public User getNewUser() {
		return newUser;
	}

	public void setNewUser(User newUser) {
		this.newUser = newUser;
	}

	public AddUserEvent(User newUser) {
		super();
		this.newUser = newUser;
	}
		
}
