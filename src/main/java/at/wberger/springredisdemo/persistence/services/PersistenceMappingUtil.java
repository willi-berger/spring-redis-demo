package at.wberger.springredisdemo.persistence.services;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import at.wberger.springredisdemo.persistence.domain.User;

public class PersistenceMappingUtil {
	
	public static User fromDomain(
			at.wberger.springredisdemo.core.domain.User domainUser) {
		return new User(domainUser.getId(), domainUser.getUserName(), domainUser.getPassword());
	}

	public static at.wberger.springredisdemo.core.domain.User toDomain(User user) {
		return new at.wberger.springredisdemo.core.domain.User(user.getId(), user.getUserName(), user.getPassword());
	}
	
	public static List<at.wberger.springredisdemo.core.domain.User> toDomain(Collection<User> users) {
		if (users == null)
			return java.util.Collections.EMPTY_LIST;
		
		ArrayList<at.wberger.springredisdemo.core.domain.User>domainUsers = new ArrayList<>();
		for (User u:users) {
			domainUsers.add(toDomain(u));
		}
		return domainUsers;
	}	
}
