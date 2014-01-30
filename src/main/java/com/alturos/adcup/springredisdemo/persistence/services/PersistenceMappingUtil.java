package com.alturos.adcup.springredisdemo.persistence.services;

import com.alturos.adcup.springredisdemo.persistence.domain.User;

public class PersistenceMappingUtil {
	
	public static User fromDomain(
			com.alturos.adcup.springredisdemo.core.domain.User domainUser) {
		return new User(domainUser.getUserName(), domainUser.getPassword());
	}

	public static com.alturos.adcup.springredisdemo.core.domain.User toDomain(User user) {
		return new com.alturos.adcup.springredisdemo.core.domain.User(user.getUserName(), user.getPassword());
	}
	
}
