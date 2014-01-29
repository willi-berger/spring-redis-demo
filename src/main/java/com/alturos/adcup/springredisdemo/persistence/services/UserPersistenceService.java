package com.alturos.adcup.springredisdemo.persistence.services;

import com.alturos.adcup.springredisdemo.events.AddUserEvent;

public interface UserPersistenceService {
	
	Long addUser(AddUserEvent addUserEvent);

}
