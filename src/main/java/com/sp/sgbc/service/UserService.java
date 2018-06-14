package com.sp.sgbc.service;

import com.sp.sgbc.model.User;

public interface UserService {
    public User findUserByName(String name);
	public User findUserByEmail(String email);
	public void saveUser(User user);
}
