package com.sports.cricket.service;

import java.io.Serializable;
import java.util.List;

import com.sports.cricket.dao.UserDao;
import com.sports.cricket.model.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service("userService")
public class UserServiceImpl implements UserService, Serializable {

	UserDao userDao;

	@Autowired
	public void setUserDao(UserDao userDao) {
		this.userDao = userDao;
	}

	@Override
	public User findById(Integer id) {
		return userDao.findById(id);
	}

	@Override
	public List<User> findAll() {
		return userDao.findAll();
	}

	@Override
	public void saveOrUpdate(User user) {

		if (findById(user.getId())==null) {
			userDao.save(user);
		} else {
			userDao.update(user);
		}

	}

	@Override
	public void delete(int id) {
		userDao.delete(id);
	}

}