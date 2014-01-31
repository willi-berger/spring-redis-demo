package com.alturos.adcup.springredisdemo.persistence.services;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import com.alturos.adcup.springredisdemo.persistence.domain.User;

public class PersistenceMappingUtil {
	
	public static User fromDomain(
			com.alturos.adcup.springredisdemo.core.domain.User domainUser) {
		return new User(domainUser.getUserName(), domainUser.getPassword());
	}

	public static com.alturos.adcup.springredisdemo.core.domain.User toDomain(User user) {
		return new com.alturos.adcup.springredisdemo.core.domain.User(user.getUserName(), user.getPassword());
	}
	
	public static List<com.alturos.adcup.springredisdemo.core.domain.User> toDomain(Collection<User> users) {
		if (users == null)
			return java.util.Collections.EMPTY_LIST;
		
		ArrayList<com.alturos.adcup.springredisdemo.core.domain.User>domainUsers = new ArrayList<>();
		for (User u:users) {
			domainUsers.add(toDomain(u));
		}
		return domainUsers;
	}	
}
