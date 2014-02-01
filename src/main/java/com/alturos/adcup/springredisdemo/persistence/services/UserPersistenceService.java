package com.alturos.adcup.springredisdemo.persistence.services;

import java.util.List;

import com.alturos.adcup.springredisdemo.core.domain.User;
import com.alturos.adcup.springredisdemo.events.AddUserEvent;
import com.alturos.adcup.springredisdemo.events.RequestGetUsersEvent;
import com.alturos.adcup.springredisdemo.events.RequestUserByIdEvent;
import com.alturos.adcup.springredisdemo.events.RequestUserByNameEvent;


public interface UserPersistenceService {
	
	String addUser(AddUserEvent addUserEvent);
	
	User getUserById(RequestUserByIdEvent requestUserByIdEvent);
	
	User getUserByUserName(RequestUserByNameEvent requestUserByNameEvent);
	
	List<User> getUsers(RequestGetUsersEvent requestGetUsersEvent);
}
