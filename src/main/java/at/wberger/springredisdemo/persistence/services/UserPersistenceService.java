package at.wberger.springredisdemo.persistence.services;

import java.util.List;

import at.wberger.springredisdemo.core.domain.User;
import at.wberger.springredisdemo.events.AddUserEvent;
import at.wberger.springredisdemo.events.RequestGetUsersEvent;
import at.wberger.springredisdemo.events.RequestUserByIdEvent;
import at.wberger.springredisdemo.events.RequestUserByNameEvent;


public interface UserPersistenceService {
	
	String addUser(AddUserEvent addUserEvent);
	
	User getUserById(RequestUserByIdEvent requestUserByIdEvent);
	
	User getUserByUserName(RequestUserByNameEvent requestUserByNameEvent);
	
	List<User> getUsers(RequestGetUsersEvent requestGetUsersEvent);
}
