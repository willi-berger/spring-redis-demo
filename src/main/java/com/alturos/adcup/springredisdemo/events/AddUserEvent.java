package com.alturos.adcup.springredisdemo.events;

import com.alturos.adcup.springredisdemo.core.domain.User;

public class AddUserEvent {

	// for testing git conflics  -WS-2
	private int i;
	
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
