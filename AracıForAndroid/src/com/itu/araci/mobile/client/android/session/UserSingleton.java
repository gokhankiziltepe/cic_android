package com.itu.araci.mobile.client.android.session;

import com.itu.araci.mobile.client.android.model.User;

public class UserSingleton {

	private User user;
	private static UserSingleton instance = null;

	private UserSingleton() {
	}

	public static UserSingleton getInstance() {
		if (instance == null) {
			instance = new UserSingleton();
		}
		return instance;
	}

	public User getUser() {
		return user;
	}

	public void setUser(User user) {
		this.user = user;
	}
}
