package com.sports.cricket.service;

import java.util.List;

import com.sports.cricket.model.User;

public interface UserService {

	User findById(Integer id);
	
	List<User> findAll();

	void saveOrUpdate(User user);
	
	void delete(int id);
	
}