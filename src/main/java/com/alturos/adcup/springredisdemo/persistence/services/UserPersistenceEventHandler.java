package com.alturos.adcup.springredisdemo.persistence.services;

import static com.alturos.adcup.springredisdemo.persistence.services.PersistenceMappingUtil.*;

import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;

import com.alturos.adcup.springredisdemo.core.domain.User;
import com.alturos.adcup.springredisdemo.events.AddUserEvent;
import com.alturos.adcup.springredisdemo.events.RequestUserByIdEvent;
import com.alturos.adcup.springredisdemo.persistence.RequestGetUsersEvent;
import com.alturos.adcup.springredisdemo.persistence.RequestUserByNameEvent;
import com.alturos.adcup.springredisdemo.persistence.repository.UserRedisRepository;

public class UserPersistenceEventHandler implements UserPersistenceService {

	private static final Logger LOG = LoggerFactory.getLogger(UserPersistenceEventHandler.class);
	
	@Autowired
	private UserRedisRepository repository;
	
	@Override
	public String addUser(AddUserEvent addUserEvent) {
		LOG.debug("add new user {}", addUserEvent.getNewUser());
		
		Long id = repository.addUser(
				fromDomain(addUserEvent.getNewUser()));
		
		return String.valueOf(id); 
	}

	@Override
	public com.alturos.adcup.springredisdemo.core.domain.User getUserById(
			RequestUserByIdEvent requestUserByIdEvent) {
		return toDomain(repository.getUserById(requestUserByIdEvent.getUserId()));
	}

	@Override
	public User getUserByUserName(RequestUserByNameEvent requestUserByNameEvent) {
		return toDomain(repository.getUserByName(requestUserByNameEvent.getUserName()));
	}

	@Override
	public List<User> getUsers(RequestGetUsersEvent requestGetUsersEvent) {
		LOG.debug("getUsers {}", requestGetUsersEvent);
		return toDomain(repository.getUsers(
				requestGetUsersEvent.getStart(), requestGetUsersEvent.getCount()));
	}

}
