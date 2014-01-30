package com.alturos.adcup.springredisdemo.persistence.services;

import com.alturos.adcup.springredisdemo.core.domain.User;
import com.alturos.adcup.springredisdemo.events.AddUserEvent;
import com.alturos.adcup.springredisdemo.events.RequestUserByIdEvent;

public interface UserPersistenceService {
	
	String addUser(AddUserEvent addUserEvent);
	
	User getUserById(RequestUserByIdEvent requestUserByIdEvent);

}
