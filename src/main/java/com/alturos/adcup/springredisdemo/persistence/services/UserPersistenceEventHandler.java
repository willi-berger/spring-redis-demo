package com.alturos.adcup.springredisdemo.persistence.services;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;

import com.alturos.adcup.springredisdemo.events.AddUserEvent;
import com.alturos.adcup.springredisdemo.persistence.domain.User;
import com.alturos.adcup.springredisdemo.persistence.repository.UserRedisRepository;

public class UserPersistenceEventHandler implements UserPersistenceService {

	private static final Logger LOG = LoggerFactory.getLogger(UserPersistenceEventHandler.class);
	
	@Autowired
	private UserRedisRepository repository;
	
	@Override
	public Long addUser(AddUserEvent addUserEvent) {
		LOG.debug("add new user {}", addUserEvent.getNewUser());
		
		
		return repository.addUser(
				fromDomain(addUserEvent.getNewUser()));

	}

	private User fromDomain(
			com.alturos.adcup.springredisdemo.core.domain.User domainUser) {
		return new User(domainUser.getUserName(), domainUser.getPassword());
	}

}
