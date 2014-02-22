package at.wberger.springredisdemo.persistence.services;

import static at.wberger.springredisdemo.persistence.services.PersistenceMappingUtil.*;

import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;

import at.wberger.springredisdemo.core.domain.User;
import at.wberger.springredisdemo.events.AddUserEvent;
import at.wberger.springredisdemo.events.RequestGetUsersEvent;
import at.wberger.springredisdemo.events.RequestUserByIdEvent;
import at.wberger.springredisdemo.events.RequestUserByNameEvent;
import at.wberger.springredisdemo.persistence.repository.UserRedisRepository;

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
	public at.wberger.springredisdemo.core.domain.User getUserById(
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
